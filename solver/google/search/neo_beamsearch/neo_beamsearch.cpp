#include "../../search/neo_beamsearch/neo_beamsearch.h"
#include "../../search/neo_beamsearch/evaluation.h"

#include <queue>
#include <algorithm>
#include <set>
#include <numeric>
#include <random>
#include <ctime>
#include <iostream>

namespace neo_beamsearch {

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
	const int_fast32_t can_simulate_ps = 800000;
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
	turn_data.agent_exist = {};
	for (int_fast32_t &&agent_id = 0; agent_id < turn_data.agent_num;
		 ++agent_id) {
		const Position &agent_pos = turn_data.GetPosition(team_id^1, agent_id);
		turn_data.agent_exist.set(GetBitsetNumber(agent_pos));
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

template<typename T>
void InputVector(vector<T> &in_vec, int_fast32_t &vec_size, T in) {
	if (in_vec.size() == vec_size) {
		in_vec.push_back(in);
		++vec_size;
	} else {
		in_vec[vec_size++] = in;
	}

	return;
}

// [candidate][turn][agent]
vector<vector<array<Move, 8>>> BeamSearch(
		const GameData &game_data, const TurnData &turn_data,
		const int_fast32_t &team_id, const int_fast32_t &ally_team,
		const int_fast32_t &search_id, const bool &first_search) {
	const int_fast32_t beam_depth =
		min(game_data.max_turn - turn_data.now_turn + 1,
			(int_fast32_t)(turn_data.agent_num == 3 ? 4 : 5));
	const int_fast32_t beam_width = GetBeamWidth(game_data, beam_depth,
												 first_search,
												 team_id != ally_team);
	cerr << beam_width << " : ";
	static vector<Node*> now_all_nodes(1<<(3*9), nullptr), next_all_nodes(1<<(3*9), nullptr);
	vector<pair<double ,int_fast32_t>> now_vec, next_vec;
	int_fast32_t now_vec_size = 0, next_vec_size = 0;

	Node root(turn_data, 0);
	root.node_id = NodeID(turn_data.now_turn, turn_data.now_turn, search_id);
	root.GetKey(team_id);
	EraseAgent(team_id^1, root.turn_data);
	if (now_all_nodes[root.key] == nullptr) {
		now_all_nodes[root.key] = new Node();
	}
	*now_all_nodes[root.key] = root;
	InputVector(now_vec, now_vec_size, make_pair(0.0, root.key));

	for (int_fast32_t turn = turn_data.now_turn;
		 turn < min(turn_data.now_turn + beam_depth,
		 			 game_data.max_turn);
		 ++turn) {

		double evaluation_mini = 1e10;
		for (int_fast32_t &&vec_id = 0; vec_id < now_vec_size; ++vec_id) {
			const double now_evaluation = now_vec[vec_id].first;
			const int_fast32_t now_key = now_vec[vec_id].second;
			const Node &now_node = *now_all_nodes[now_key];
			if (now_node.evaluation > now_evaluation) continue;

			static TurnData now_turn_data;
			static vector<vector<Move>> all_moves;
			now_turn_data = now_node.turn_data;
			vector<int_fast32_t> move_ids(turn_data.agent_num);
			vector<Move> check_moves(turn_data.agent_num);

			all_moves = GetAgentsAllMoves(game_data, now_turn_data,
										  team_id, false, true);
			if (game_data.agent_num >= 6 && turn_data.agent_num == 3) {
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

				next_node.all_turn_moves = now_node.all_turn_moves;
				next_node.all_turn_moves.push_back(array<Move, 8>());
				for (int_fast32_t &&i = 0; i < turn_data.agent_num; ++i) {
					next_node.all_turn_moves.back()[i] = check_moves[i];
				}

				if (next_all_nodes[next_node.key] == nullptr) {
						next_all_nodes[next_node.key] = new Node();
				}
				Node &check_node = *next_all_nodes[next_node.key];
				if (next_vec_size < beam_width ||
					evaluation_mini < check_node.evaluation) {
					evaluation_mini = min(evaluation_mini,
										  check_node.evaluation);
					if (check_node.node_id == next_node.node_id) {
						if (check_node.evaluation < next_node.evaluation) {
							check_node = next_node;
						}
					} else {
						check_node = next_node;
					}
					InputVector(next_vec, next_vec_size,
							    make_pair(next_node.evaluation,next_node.key));
				}
			} while (NextPermutation(all_moves, 0, move_ids, check_moves));
		}

		sort(next_vec.begin(), next_vec.begin() + next_vec_size, greater<>());
		map<array<Move, 8>, int_fast32_t> first_move_count;

		now_vec_size = 0;
		const double eps = 1e-9;
		for (int_fast32_t &&vec_id = 0;
			 vec_id < next_vec_size && now_vec_size < beam_width; ++vec_id) {
			const auto &check_pair = next_vec[vec_id];
			const Node &check_node = *next_all_nodes[check_pair.second];
			if (!(check_pair.first - eps <= check_node.evaluation &&
				  check_node.evaluation <= check_pair.first + eps))
				continue;
			if (first_move_count[check_node.first_move] > beam_width * 0.60)
				continue;

			InputVector(now_vec, now_vec_size, check_pair);
			++first_move_count[check_node.first_move];
		}
		next_vec_size = 0;

		swap(now_all_nodes, next_all_nodes);
	}

	const int_fast32_t &ret_size = game_data.agent_num <= 6 ? 50 : 13;
	vector<vector<array<Move, 8>>> ret;
	map<array<Move, 8>, int_fast32_t> first_move_count;
	for (int_fast32_t &&vec_id = 0;
		 vec_id < now_vec_size && ret.size() < ret_size; ++vec_id) {
		const Node &check_node = *now_all_nodes[now_vec[vec_id].second];

		if (first_move_count[check_node.first_move] > ret_size * 0.34) continue;

		ret.push_back(check_node.all_turn_moves);
		++first_move_count[check_node.first_move];
	}
	cerr << first_move_count.size() << " ";

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

// [team_id][agent_id]
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

// [split_id]
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

// [team_id][split_id][agent_id]
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

// [team_id][split_id][candidate_id][turn][agent_id]
array<vector<vector<vector<array<Move, 8>>>>, 2> GetCandidateSplitMoves(
		const GameData &game_data, const vector<TurnData> &split_turn_data,
		const int_fast32_t &ally_team) {
	const int_fast32_t &split_size = split_table[game_data.agent_num].size();
	array<vector<vector< vector<array<Move, 8>> >>, 2> ret;

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

void MakeTrasitionMoves(const GameData &game_data, const int_fast32_t &turn,
						const vector<vector<array<Move, 8>>> &check_split_moves,
						vector<Move> &check_all_moves) {
	const auto &target_split = split_table[game_data.agent_num];
	const int_fast32_t &split_size = target_split.size();
	int_fast32_t &&before_count = 0;
	for (int_fast32_t &&split_id = 0; split_id < split_size; ++split_id) {
		const int_fast32_t &one_size = target_split[split_id];
		for (int_fast32_t &&agent_id = 0; agent_id < one_size; ++agent_id) {
			int_fast32_t truth_id = before_count + agent_id;
			check_all_moves[truth_id] =
				check_split_moves[split_id][turn][agent_id];
		}
		before_count += one_size;
	}

	return;
}

void CheckInvalidAction(const GameData &game_data, const TurnData &turn_data,
						vector<Move> &moves) {
	for (auto &agent_move: moves) {
		const Position &now_pos =
			turn_data.agents_position[agent_move.team_id][agent_move.agent_id];
		const Position next_pos =
			now_pos + kNextToNine[agent_move.direction];

		if (game_data.IntoField(next_pos) == false) {
			agent_move.action = kNone;
		} else {
			const int_fast32_t &tile_color =
				turn_data.tile_data[next_pos.h][next_pos.w];
			if (agent_move.action == kWalk) {
				if (tile_color == (agent_move.team_id^1)) {
					agent_move.action = kNone;
				}
			} else {
				if (tile_color == kBrank) {
					agent_move.action = kNone;
				}
			}
		}
	}

	return;
}

vector<vector<vector<Move>>> RivalAllSearch(
		const GameData &game_data, const TurnData &turn_data,
		const vector<vector<vector<array<Move, 8>>>> &split_moves,
		const int_fast32_t &rival_team) {
	const int_fast32_t check_turn =
		min((int_fast32_t)3, (int_fast32_t)split_moves[0][0].size());
	TurnData now_turn_data = turn_data;
	EraseAgent(rival_team^1, now_turn_data);
	vector<int_fast32_t> move_ids(split_moves.size(), 0);
	vector<vector<array<Move, 8>>> check_split_moves(split_moves.size());
	vector<Node> all_nodes;

	for (int_fast32_t &&split_id = 0; split_id < split_moves.size();
		 ++split_id) {
		check_split_moves[split_id] = split_moves[split_id][0];
	}
	do {
		static vector<Move> check_all_moves(game_data.agent_num);
		static TurnData before_turn_data;
		Node next_node(now_turn_data, 0.0);
		// for (int_fast32_t &&turn = 0; turn < check_split_moves[0].size();
		// 	 ++turn) {
		for (int_fast32_t &&turn = 0; turn < check_turn; ++turn) {
			MakeTrasitionMoves(game_data, turn, check_split_moves,
							   check_all_moves);

			next_node.all_turn_moves.push_back(array<Move, 8>());
			for (int_fast32_t &&agent_id = 0; agent_id < game_data.agent_num;
				++agent_id) {
				next_node.all_turn_moves.back()[agent_id] =
					check_all_moves[agent_id];
			}

			before_turn_data = next_node.turn_data;
			CheckInvalidAction(game_data, next_node.turn_data, check_all_moves);
			next_node.turn_data.Transition(game_data, check_all_moves);
			++next_node.turn_data.now_turn;

			const auto &target_split = split_table[game_data.agent_num];
			const int_fast32_t &split_size = target_split.size();
			int_fast32_t &&before_count = 0;
			for (int_fast32_t &&split_id = 0; split_id < split_size;
				 ++split_id) {
				const int_fast32_t &one_size = target_split[split_id];
				for (int_fast32_t &&agent_id = 0; agent_id < one_size-1;
					 ++agent_id) {
					const int_fast32_t truth_id = before_count + agent_id;
					const int_fast32_t &target_id =
						check_all_moves[truth_id].agent_id;
					const int_fast32_t &next_id =
						check_all_moves[truth_id+1].agent_id;
					Position &target_agent_pos =
						next_node.turn_data.GetPosition(rival_team, target_id);
					Position &next_agent_pos =
						next_node.turn_data.GetPosition(rival_team, next_id);

					if (target_agent_pos > next_agent_pos) {
						swap(target_agent_pos, next_agent_pos);
						--agent_id;
					}
				}
				before_count += one_size;
			}

			if (turn == 0) {
				for (int_fast32_t &&agent_id = 0; agent_id < game_data.agent_num;
					 ++agent_id) {
					next_node.first_move[agent_id] = check_all_moves[agent_id];
				}
			}
			next_node.evaluation = GetEvaluation(game_data,
												 next_node.turn_data,
												 before_turn_data,
												 check_all_moves,
												 rival_team,
												 turn_data.now_turn,
												 next_node.evaluation,
												 false);
		}
		all_nodes.push_back(next_node);
	} while (NextPermutation(split_moves, 0, move_ids, check_split_moves));
	sort(all_nodes.begin(), all_nodes.end(), greater<>());
	vector<vector<vector<Move>>> ret;
	for (int_fast32_t &&i = 0; i < all_nodes.size() && ret.size() < 10; ++i) {
		const Node &check_node = all_nodes[i];
		vector<vector<Move>> in_move(check_node.all_turn_moves.size());
		for (int_fast32_t &&turn = 0; turn < check_node.all_turn_moves.size();
			 ++turn) {
			in_move[turn].resize(game_data.agent_num);
			for (int_fast32_t &&agent_id = 0; agent_id < game_data.agent_num;
				 ++agent_id) {
				in_move[turn][agent_id] =
					check_node.all_turn_moves[turn][agent_id];
			}
		}
		ret.push_back(in_move);
	}

	return ret;
}

array<Move, 8> AllyAllSearch(
		const GameData &game_data, const TurnData &turn_data,
		const vector<vector<vector<array<Move, 8>>>> &ally_split_moves,
		const vector<vector<vector<Move>>> &rival_all_moves,
		const int_fast32_t &ally_team,
		const bool &conflicted) {
	const int_fast32_t check_turn =
		min((int_fast32_t)3, (int_fast32_t)rival_all_moves[0].size());
	vector<int_fast32_t> move_ids(ally_split_moves.size(), 0);
	vector<vector<array<Move, 8>>> check_split_moves(ally_split_moves.size());
	vector<Node> all_nodes;
	for (int_fast32_t &&split_id = 0; split_id < ally_split_moves.size();
		 ++split_id) {
		check_split_moves[split_id] = ally_split_moves[split_id][0];
	}
	do {
		static vector<Move> check_ally_moves(game_data.agent_num);
		static vector<Move> check_all_agent_moves(game_data.agent_num * 2);
		static TurnData before_turn_data;
		double evaluation_mini = 1e10;

		Node next_node;
		for (auto check_rival_moves: rival_all_moves) {
			next_node.turn_data = turn_data;
			next_node.evaluation = 0.0;
			// for (int_fast32_t &&turn = 0; turn < check_rival_moves.size();
			// 	 ++turn) {
			for (int_fast32_t &&turn = 0; turn < check_turn; ++turn) {
				MakeTrasitionMoves(game_data, turn, check_split_moves,
								   check_ally_moves);
				for (int_fast32_t &&agent_id = 0;
					 agent_id < game_data.agent_num; ++agent_id) {
					check_all_agent_moves[agent_id] =
						check_ally_moves[agent_id];
					check_all_agent_moves[agent_id + game_data.agent_num] =
						check_rival_moves[turn][agent_id];
				}

				before_turn_data = next_node.turn_data;
				CheckInvalidAction(game_data, next_node.turn_data,
								   check_all_agent_moves);
				next_node.turn_data.Transition(game_data, check_all_agent_moves);
				++next_node.turn_data.now_turn;
// cerr << next_node.turn_data.tile_point[0] + next_node.turn_data.area_point[0] << " "
// 	 << next_node.turn_data.tile_point[1] + next_node.turn_data.area_point[1] << endl;
				const auto &target_split = split_table[game_data.agent_num];
				const int_fast32_t &split_size = target_split.size();
				int_fast32_t &&before_count = 0;
				for (int_fast32_t &&split_id = 0; split_id < split_size;
					 ++split_id) {
					const int_fast32_t &one_size = target_split[split_id];
					for (int_fast32_t &&agent_id = 0; agent_id < one_size-1;
						 ++agent_id) {
						bool swapped = false;
						const int_fast32_t truth_id = before_count + agent_id;

						const int_fast32_t &ally_target_id =
							check_ally_moves[truth_id].agent_id;
						const int_fast32_t &ally_next_id =
							check_ally_moves[truth_id+1].agent_id;
						Position &ally_target_agent_pos =
							next_node.turn_data.GetPosition(ally_team,
															ally_target_id);
						Position &ally_next_agent_pos =
							next_node.turn_data.GetPosition(ally_team,
															ally_next_id);
						if (ally_target_agent_pos > ally_next_agent_pos) {
							swap(ally_target_agent_pos, ally_next_agent_pos);
							swapped = true;
						}

						const int_fast32_t &rival_target_id =
							check_rival_moves[turn][truth_id].agent_id;
						const int_fast32_t &rival_next_id =
							check_rival_moves[turn][truth_id+1].agent_id;
						Position &rival_target_agent_pos =
							next_node.turn_data.GetPosition(ally_team^1,
															rival_target_id);
						Position &rival_next_agent_pos =
							next_node.turn_data.GetPosition(ally_team^1,
															rival_next_id);
						if (rival_target_agent_pos > rival_next_agent_pos) {
							swap(rival_target_agent_pos, rival_next_agent_pos);
							swapped = true;
						}

						if (swapped)
							--agent_id;
					}
					before_count += one_size;
				}

				if (turn == 0) {
					for (int_fast32_t &&agent_id = 0; agent_id < game_data.agent_num;
						 ++agent_id) {
						next_node.first_move[agent_id] = check_all_agent_moves[agent_id];
					}
				}

				next_node.evaluation = GetEvaluation(game_data,
													 next_node.turn_data,
													 before_turn_data,
													 check_all_agent_moves,
													 ally_team,
													 turn_data.now_turn,
													 next_node.evaluation,
													 false);
			}
			evaluation_mini = min(evaluation_mini, next_node.evaluation);
		}
		next_node.evaluation = evaluation_mini;
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
			for (auto &restore_moves : candidate_split_moves[team_id][split_id]) {
				RestoreTruthIndex(truth_id[team_id][split_id],
								  restore_moves);
			}
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