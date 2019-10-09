#include "../../search/improve_disperse/improve_disperse.h"
#include "../../search/improve_disperse/evaluation.h"

#include <queue>
#include <algorithm>
#include <set>
#include <numeric>
#include <random>
#include <ctime>
#include <iostream>

namespace improve_disperse {

template<typename T>
using greater_priority_queue = priority_queue<T, vector<T>, greater<T>>;

// turn_data.agent_num <= 3
void Node::GetKey(const int_fast32_t &team_id) {
	key = 0;
	for (int_fast32_t &&agent_id = 0; agent_id < turn_data.agent_num;
		 ++agent_id) {
		const Position &agent_pos =
			turn_data.agents_position[team_id][agent_id];
		key = (key << 9) + (agent_pos.h*20 + agent_pos.w + 1);
	}

	return;
}

int_fast32_t GetBeamWidth(const GameData &game_data,
						  const int_fast32_t &beam_depth,
						  const bool &first_search,
						  const bool &rival_team) {
	const int_fast32_t can_simulate_ps = 400000;
	const int_fast32_t one_transition =
		one_transition_table[game_data.agent_num];
	const int_fast32_t make_new_node = first_search ? 500 : 0;
	const double turn_time = (game_data.turn_time_ms - 2000 - make_new_node) / 1000.0;
	const double rival_width = 2.0/3.0;

	double ret_width =
		(can_simulate_ps * turn_time) /
		((beam_depth - 1) * one_transition * (1.0 + rival_width));
	if (rival_team)
		ret_width *= rival_width;
	return ret_width;
}

void EraseAgent(const int_fast32_t &team_id, TurnData &turn_data) {
	for (auto &agent_pos : turn_data.agents_position[team_id]) {
		turn_data.agent_exist.reset(GetBitsetNumber(agent_pos));
	}

	return;
}

void ReduceDirection(const GameData &game_data, const TurnData &turn_data,
					 vector<vector<Move>> &all_moves) {
	static array<int_fast32_t, 9> direction_kind;
	for (auto &check_moves : all_moves) {
		direction_kind = {};
		int_fast32_t mini_point = 1000, mini_id = 0;
		int_fast32_t nmini_point = 1000, nmini_id = 0;
		int_fast32_t kind_num = 0;
		for (auto &check_move : check_moves) {
			const int_fast32_t &check_direction =
				check_move.direction;
			const Position &check_position =
				check_move.target_position;
			++direction_kind[check_direction];
			if (direction_kind[check_direction] == 1) {
				++kind_num;
				const int_fast32_t &point =
					game_data.GetTilePoint(check_position);
				if (point <= mini_point) {
					nmini_point = mini_point;
					nmini_id = mini_id;
					mini_point = point;
					mini_id = check_direction;
				} else if (point <= nmini_point) {
					nmini_point = point;
					nmini_id = check_direction;
				}
			}
		}

		if (kind_num <= 6) continue;
		int_fast32_t ret_size = check_moves.size();
		for (int_fast32_t &&move_id = 0; move_id < ret_size; ++move_id) {
			const int_fast32_t &check_direction =
				check_moves[move_id].direction;
			if ((check_direction == mini_id ||
				 check_direction == nmini_id) &&
				check_moves[move_id].action == kWalk) {
				swap(check_moves[move_id--], check_moves[--ret_size]);
			}
		}

		while (check_moves.size() > ret_size)
			check_moves.pop_back();
	}

	return;
}

vector<array<Move, 8>> BeamSearch(const GameData &game_data,
						  const TurnData &turn_data,
						  const int_fast32_t &team_id,
						  const int_fast32_t &ally_team,
						  const int_fast32_t &search_id,
						  const bool &first_search) {
	const int_fast32_t beam_depth =
		min(game_data.max_turn - turn_data.now_turn + 1,
			(int_fast32_t)(turn_data.agent_num == 3 ? 4 : 5));
	int_fast32_t beam_width = GetBeamWidth(game_data, beam_depth,
										   first_search,
										   team_id != ally_team);
	cerr << beam_width << " : ";
	static vector<Node*> now_all_nodes(1<<(3*9), nullptr), next_all_nodes(1<<(3*9), nullptr);
	greater_priority_queue<pair<double, int_fast32_t>> now_que, next_que;
	Node root(turn_data, 0);
	root.node_id = NodeID(turn_data.now_turn, turn_data.now_turn, search_id);
	root.GetKey(team_id);
	EraseAgent(team_id^1, root.turn_data);
	if (now_all_nodes[root.key] == nullptr) {
		now_all_nodes[root.key] = new Node();
	}
	*now_all_nodes[root.key] = root;
	now_que.push(make_pair(0, root.key));
	for (int_fast32_t turn = turn_data.now_turn;
		 turn < min(turn_data.now_turn + beam_depth,
		 			 game_data.max_turn);
		 ++turn) {

		while (now_que.size()) {
			const double now_evaluation = now_que.top().first;
			const int_fast32_t now_key = now_que.top().second;
			now_que.pop();
			const Node &now_node = *now_all_nodes[now_key];

			static TurnData now_turn_data;
			static vector<vector<Move>> all_moves;
			now_turn_data = now_node.turn_data;
			vector<int_fast32_t> move_ids(turn_data.agent_num);
			vector<Move> check_moves(turn_data.agent_num);

			all_moves = GetAgentsAllMoves(game_data, now_turn_data,
										  team_id, false, true);
			if (turn > turn_data.now_turn &&
				game_data.agent_num >= 6 && turn_data.agent_num == 3) {
				ReduceDirection(game_data, now_turn_data, all_moves);
			}

			move_ids = {};
			for (int_fast32_t &&i = 0; i < turn_data.agent_num; ++i)
				check_moves[i] = all_moves[i].front();

			static TurnData next_turn_data;
			static Node next_node;
			do {
				TurnData next_turn_data = now_turn_data;
				next_turn_data.Transition(game_data, check_moves);
				++next_turn_data.now_turn;
				sort(next_turn_data.agents_position[team_id].begin(),
					 next_turn_data.agents_position[team_id].begin() +
					 turn_data.agent_num);
				next_node = Node(next_turn_data,
								 GetEvaluation(game_data,
								 			   next_turn_data,
								 			   now_turn_data,
								 			   check_moves,
								 			   team_id,
								 			   turn,
								 			   now_node.evaluation,
								 			   true));
				next_node.node_id = NodeID(turn_data.now_turn,
										   next_turn_data.now_turn,
										   search_id);
				next_node.GetKey(team_id);
				if (now_turn_data.now_turn == turn_data.now_turn) {
					for (int_fast32_t &&i = 0; i < turn_data.agent_num; ++i) {
						next_node.first_move[i] = check_moves[i];
					}
				} else {
					next_node.first_move = now_node.first_move;
				}

				if (next_all_nodes[next_node.key] == nullptr) {
						next_all_nodes[next_node.key] = new Node();
				}
				Node &check_node = *next_all_nodes[next_node.key];

				if (check_node.node_id == next_node.node_id) {
					static const double eps = 1e-9;
					if (check_node.evaluation + eps < next_node.evaluation) {
						check_node = next_node;
						next_que.push(make_pair(next_node.evaluation,
												next_node.key));
						++beam_width;
					}
				} else {
					while (next_que.size()) {
						static const double eps = 1e-9;
						const Node &top_node =
							*next_all_nodes[next_que.top().second];
						const double &top_evaluation =
							next_que.top().first;
						if (top_node.evaluation - eps < top_evaluation &&
							top_evaluation < top_node.evaluation + eps) {
							break;
						} else {
							next_que.pop();
							--beam_width;
						}
					}

					if (next_que.size() < beam_width) {
						check_node = next_node;
						next_que.push(make_pair(next_node.evaluation,
												next_node.key));
					} else if (next_que.top().first < next_node.evaluation) {
						check_node = next_node;
						next_que.pop();
						next_que.push(make_pair(next_node.evaluation,
												next_node.key));
					}
				}
			} while (NextPermutation(all_moves, 0, move_ids, check_moves));
		}

		map<array<Move, 8>, int_fast32_t> first_move_count;
		while (next_que.size()) {
			static const double eps = 1e-9;
			const Node &top_node =
				*next_all_nodes[next_que.top().second];
			const double &top_evaluation =
				next_que.top().first;
			if (top_node.evaluation - eps < top_evaluation &&
				top_evaluation < top_node.evaluation + eps) {
				now_que.push(next_que.top());
				next_que.pop();
				++first_move_count[top_node.first_move];
			} else {
				next_que.pop();
				--beam_width;
			}
		}
		cerr << first_move_count.size() << endl;
		// vector<pair<double, int_fast32_t>> reverse_que;
		// map<array<Move, 8>, int_fast32_t> first_move_count;
		// while (next_que.size()) {
		// 	reverse_que.push_back(next_que.top());
		// 	next_que.pop();
		// }
		// for (int_fast32_t &&i = reverse_que.size()-1;
		// 	 i >= 0 && now_que.size() < beam_width; --i) {
		// 	const auto &check_first_move =
		// 		next_all_nodes[reverse_que[i].second]->first_move;
		// 	if (first_move_count[check_first_move] > beam_width * 0.95)
		// 		continue;

		// 	now_que.push(reverse_que[i]);
		// 	first_move_count[check_first_move]++;
		// }
		swap(now_all_nodes, next_all_nodes);
	}
	// const int_fast32_t &ret_size = game_data.agent_num <= 6 ? 100 : 21;
	const int_fast32_t ret_size = 10;
	vector<array<Move, 8>> ret;
	set<array<Move, 8>> added;
	while (now_que.size()) {
		const auto &check_first_moves =
			now_all_nodes[now_que.top().second]->first_move;
		if (added.find(check_first_moves) == added.end()) {
			ret.push_back(check_first_moves);
			added.insert(check_first_moves);
		}
		now_que.pop();
	}
	reverse(ret.begin(), ret.end());
	while (ret.size() > ret_size) {
		ret.pop_back();
	}

	return ret;
}

int_fast32_t GetBoxSize(
		const GameData &game_data, const TurnData &turn_data,
		const array<array<pair<Position, int_fast32_t>, 8>, 2> &default_state,
		const vector<int_fast32_t> &check_permutation,
		const int_fast32_t &team_id,
		const int_fast32_t &id) {
	const auto &target_split = split_table[game_data.agent_num];
	const int_fast32_t &split_size = target_split.size();
	int_fast32_t ret = 0;
	int_fast32_t before_count = 0;
	for (int_fast32_t &&split_id = 0; split_id < split_size; ++split_id) {
		const int_fast32_t &one_size = target_split[split_id];
		int_fast32_t left = 999, right = -1;
		int_fast32_t up = 999, down = -1;
		for (int_fast32_t &&i = 0; i < one_size; ++i) {

			const int_fast32_t &check_id = check_permutation[i + before_count];
			const Position &check_pos =
				turn_data.agents_position[team_id][check_id];
			left = min(left, check_pos.w);
			right = max(right, check_pos.w);
			up = min(up, check_pos.h);
			down = max(down, check_pos.h);
		}

		before_count += one_size;

		ret += (right - left + 1) * (down - up + 1);
	}

	return ret;
}

void SortNearAgent(const GameData &game_data, const TurnData &turn_data,
				   array<array<pair<Position, int_fast32_t>, 8>, 2> &ret) {
	const array<array<pair<Position, int_fast32_t>, 8>, 2> cp = ret;

	vector<int_fast32_t> check_permutation(game_data.agent_num);
	for (int_fast32_t &&team_id = 0; team_id < 2; ++team_id) {
		iota(check_permutation.begin(), check_permutation.end(), 0);
		int_fast32_t mini_box = 9999999, mini_id = 0;
		int_fast32_t id = 0;
		do {
			int_fast32_t check_box = GetBoxSize(game_data, turn_data,
												cp, check_permutation,
												team_id, id);
			if (mini_box > check_box) {
				mini_box = check_box;
				mini_id = id;
			}
			++id;
		} while (next_permutation(check_permutation.begin(),
								  check_permutation.end()));

		iota(check_permutation.begin(), check_permutation.end(), 0);
		for (int_fast32_t &&i = 0; i < mini_id; ++i)
			next_permutation(check_permutation.begin(),
							 check_permutation.end());
		for (int_fast32_t &&agent_id = 0; agent_id < game_data.agent_num;
			 ++agent_id) {
			ret[team_id][agent_id] = cp[team_id][check_permutation[agent_id]];
		}
	}

	return;
}

array<array<pair<Position, int_fast32_t>, 8>, 2> GetAgentsPositionWidthID(
		const GameData &game_data, const TurnData &turn_data) {
	static array<array<pair<Position, int_fast32_t>, 8>, 2> ret = {};
	for (int_fast32_t &&team_id = 0; team_id < 2; ++team_id) {
		for (int_fast32_t &&agent_id = 0; agent_id < game_data.agent_num;
			 ++agent_id) {
			ret[team_id][agent_id].first =
				turn_data.agents_position[team_id][agent_id];
			ret[team_id][agent_id].second = agent_id;
		}
	}
	SortNearAgent(game_data, turn_data, ret);

	return ret;
}

vector<TurnData> GetSplitTurnData(
		const GameData &game_data, const TurnData &turn_data,
		const array<array<pair<Position, int_fast32_t>, 8>, 2> &agents_with_id) {
	const auto &target_split = split_table[game_data.agent_num];
	const int_fast32_t &split_size = target_split.size();
	vector<TurnData> ret(split_size, turn_data);
	int_fast32_t before_count = 0;
	for (int_fast32_t &&split_id = 0; split_id < split_size; ++split_id) {
		const int_fast32_t &one_size = target_split[split_id];
		ret[split_id].agent_num = one_size;
		for (int_fast32_t &&team_id = 0; team_id < 2; ++team_id) {
			for (int_fast32_t &&agent_id = 0; agent_id < one_size;
				 ++agent_id) {
				const int_fast32_t truth_id = before_count + agent_id;
				ret[split_id].agents_position[team_id][agent_id] =
					agents_with_id[team_id][truth_id].first;
			}
		}
		before_count += one_size;
	}

	return ret;
}

array<vector<vector<int_fast32_t>>, 2> GetTruthIndex(
		const GameData &game_data, const vector<TurnData> &split_turn_data,
		const array<array<pair<Position, int_fast32_t>, 8>, 2> &agents_with_id) {
	const auto &target_split = split_table[game_data.agent_num];
	const int_fast32_t &split_size = target_split.size();
	array<vector<vector<int_fast32_t>>, 2> ret;
	for (int_fast32_t &&team_id = 0; team_id < 2; ++team_id) {
		ret[team_id].resize(split_size);
		int_fast32_t before_count = 0;
		for (int_fast32_t &&split_id = 0; split_id < split_size; ++split_id) {
			const int_fast32_t &one_size = target_split[split_id];
			ret[team_id][split_id].resize(one_size);
			for (int_fast32_t &&agent_id = 0; agent_id < one_size;
				 ++agent_id) {
				const int_fast32_t truth_id = before_count + agent_id;
				ret[team_id][split_id][agent_id] =
					agents_with_id[team_id][truth_id].second;
			}
			before_count += one_size;
		}
	}

	return ret;
}

array<vector<vector<array<Move, 8>>>, 2> GetCandidateSplitMoves(
		const GameData &game_data, const vector<TurnData> &split_turn_data,
		const int_fast32_t &ally_team) {
	const int_fast32_t &split_size = split_table[game_data.agent_num].size();
	array<vector<vector<array<Move, 8>>>, 2> ret;

	bool first_search = true;
	int_fast32_t search_id = 0;
	for (int_fast32_t &&team_id = 0; team_id < 2; ++team_id) {
		const int_fast32_t &check_team = ally_team^1^team_id;
		ret[check_team].resize(split_size);
		for (int_fast32_t &&split_id = 0; split_id < split_size; ++split_id) {
			// 探索は敵から
			ret[check_team][split_id] = BeamSearch(game_data,
												   split_turn_data[split_id],
												   check_team, ally_team,
												   search_id++,
												   first_search);
			first_search = false;
			cerr << "Candidate : " << check_team << split_id << " : "
				 << ret[check_team][split_id].size() << endl;
		}
	}

	return ret;
}

void RestoreTruthIndex(const vector<int_fast32_t> &truth_id,
					   vector<array<Move, 8>> &restore_moves) {
	for (auto &moves : restore_moves) {
		for (int_fast32_t &&agent_id = 0; agent_id < truth_id.size();
			 ++agent_id) {
			moves[agent_id].agent_id = truth_id[agent_id];
		}
	}

	return;
}

void MakeTrasitionMoves(const GameData &game_data,
						const vector<array<Move, 8>> &check_split_moves,
						vector<Move> &check_all_moves) {
	const auto &target_split = split_table[game_data.agent_num];
	const int_fast32_t &split_size = target_split.size();
	int_fast32_t &&before_count = 0;
	for (int_fast32_t &&split_id = 0; split_id < split_size; ++split_id) {
		const int_fast32_t &one_size = target_split[split_id];
		for (int_fast32_t &&agent_id = 0; agent_id < one_size; ++agent_id) {
			int_fast32_t truth_id = before_count + agent_id;
			check_all_moves[truth_id] = check_split_moves[split_id][agent_id];
		}
		before_count += one_size;
	}

	return;
}

vector<vector<Move>> RivalAllSearch(
		const GameData &game_data, const TurnData &turn_data,
		const vector<vector<array<Move, 8>>> &split_moves,
		const int_fast32_t &team_id) {
	TurnData now_turn_data = turn_data;
	EraseAgent(team_id^1, now_turn_data);
	vector<int_fast32_t> move_ids(split_moves.size(), 0);
	vector<array<Move, 8>> check_split_moves(split_moves.size());
	vector<Node> all_nodes;
	for (int_fast32_t &&split_id = 0; split_id < split_moves.size();
		 ++split_id) {
		check_split_moves[split_id] = split_moves[split_id][0];
	}
	do {
		static vector<Move> check_all_moves(game_data.agent_num);
		static TurnData next_turn_data;
		MakeTrasitionMoves(game_data, check_split_moves, check_all_moves);
		next_turn_data = now_turn_data;
		next_turn_data.Transition(game_data, check_all_moves);
		++next_turn_data.now_turn;
		Node next_node(next_turn_data, GetEvaluation(game_data,
													 next_turn_data,
													 now_turn_data,
													 check_all_moves,
													 team_id,
													 turn_data.now_turn,
													 0,
													 false));
		for (int_fast32_t &&agent_id = 0; agent_id < game_data.agent_num;
			 ++agent_id) {
			next_node.first_move[agent_id] = check_all_moves[agent_id];
		}
		all_nodes.push_back(next_node);
	} while (NextPermutation(split_moves, 0, move_ids, check_split_moves));
	sort(all_nodes.begin(), all_nodes.end(), greater<>());
	vector<vector<Move>> ret_moves(20);
	for (int_fast32_t &&i = 0;
		 i < min((int_fast32_t)20, (int_fast32_t)all_nodes.size()); ++i) {
		ret_moves[i].resize(game_data.agent_num);
		for (int_fast32_t &&agent_id = 0; agent_id < game_data.agent_num;
			 ++agent_id) {
			ret_moves[i][agent_id] = all_nodes[i].first_move[agent_id];
		}
	}
	if (all_nodes.size() < 20) {
		while (ret_moves.size() > all_nodes.size())
			ret_moves.pop_back();
	}

	return ret_moves;
}

array<Move, 8> AllyAllSearch(
		const GameData &game_data, const TurnData &turn_data,
		const vector<vector<array<Move, 8>>> &ally_split_moves,
		const vector<vector<Move>> &rival_all_moves,
		const int_fast32_t &ally_team,
		const bool &conflicted) {
	vector<int_fast32_t> move_ids(ally_split_moves.size(), 0);
	vector<array<Move, 8>> check_split_moves(ally_split_moves.size());
	vector<Node> all_nodes;
	for (int_fast32_t &&split_id = 0; split_id < ally_split_moves.size();
		 ++split_id) {
		check_split_moves[split_id] = ally_split_moves[split_id][0];
	}
	do {
		static vector<Move> check_ally_moves(game_data.agent_num);
		static vector<Move> check_all_agent_moves(game_data.agent_num * 2);
		static TurnData next_turn_data;
		MakeTrasitionMoves(game_data, check_split_moves, check_ally_moves);
		double evaluation_mini = 1e10;
		for (auto check_rival_moves : rival_all_moves) {
			for (int_fast32_t &&agent_id = 0; agent_id < game_data.agent_num;
				 ++agent_id) {
				check_all_agent_moves[agent_id] = check_ally_moves[agent_id];
				check_all_agent_moves[game_data.agent_num + agent_id] =
					check_rival_moves[agent_id];
			}

			next_turn_data = turn_data;
			++next_turn_data.now_turn;
			next_turn_data.Transition(game_data, check_all_agent_moves);
			evaluation_mini = min(evaluation_mini,
								  GetEvaluation(game_data,
												next_turn_data,
												turn_data,
												check_all_agent_moves,
												ally_team,
												turn_data.now_turn,
												0,
												false));
		}
		Node next_node(next_turn_data, evaluation_mini);
		for (int_fast32_t &&agent_id = 0; agent_id < game_data.agent_num;
			 ++agent_id) {
			next_node.first_move[agent_id] = check_ally_moves[agent_id];
		}
		all_nodes.push_back(next_node);
	} while (NextPermutation(ally_split_moves, 0, move_ids,
							 check_split_moves));

	sort(all_nodes.begin(), all_nodes.end(), greater<>());
	const int_fast32_t &ally_point_sum = turn_data.tile_point[ally_team] +
										 turn_data.area_point[ally_team];
	const int_fast32_t &rival_point_sum = turn_data.tile_point[ally_team^1] +
										  turn_data.area_point[ally_team^1];

	if (conflicted && rival_point_sum > ally_point_sum) {
		const int_fast32_t &&ret_id =
			rand()%min((int_fast32_t)3, (int_fast32_t)all_nodes.size()-1) + 1;
		cerr << "random : " << ret_id << endl;
		return all_nodes[ret_id].first_move;
	} else {
		return all_nodes[0].first_move;
	}
}

array<Move, 8> SplitSearch(const GameData &game_data,
						   const TurnData &turn_data,
						   const int_fast32_t &ally_team,
						   const bool &conflicted) {
	srand((unsigned)time(NULL));
	auto agents_with_id = GetAgentsPositionWidthID(game_data, turn_data);
	auto split_turn_data = GetSplitTurnData(game_data, turn_data,
											agents_with_id);
	auto truth_id = GetTruthIndex(game_data, split_turn_data, agents_with_id);

	auto candidate_split_moves = GetCandidateSplitMoves(game_data,
														split_turn_data,
														ally_team);
	const int_fast32_t &split_size = split_table[game_data.agent_num].size();
	for (int_fast32_t &&team_id = 0; team_id < 2; ++team_id) {
		for (int_fast32_t &&split_id = 0; split_id < split_size; ++split_id) {
			RestoreTruthIndex(truth_id[team_id][split_id],
							  candidate_split_moves[team_id][split_id]);
		}
	}
	auto &ally_split_moves = candidate_split_moves[ally_team];
	auto &rival_split_moves = candidate_split_moves[ally_team^1];
	auto rival_all_moves = RivalAllSearch(game_data, turn_data,
										  rival_split_moves, ally_team^1);
	auto ally_all_moves = AllyAllSearch(game_data, turn_data, ally_split_moves,
										rival_all_moves, ally_team,
										conflicted);

	sort(ally_all_moves.begin(), ally_all_moves.begin() + game_data.agent_num);

	return ally_all_moves;
}

}