#include "../../search/split_agent/split_agent.h"
#include "../../search/split_agent/evaluation.h"

#include <queue>
#include <algorithm>
#include <set>

namespace split_agent {

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
	const int_fast32_t can_simulate_ps = 1400000;
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

void EraseRivalAgent(const int_fast32_t &rival_team,
					 TurnData &turn_data) {
	for (auto &agent_pos : turn_data.agents_position[rival_team]) {
		turn_data.agent_exist.reset(GetBitsetNumber(agent_pos));
	}

	return;
}

vector<array<Move, 8>> BeamSearch(const GameData &game_data,
						  const TurnData &turn_data,
						  const int_fast32_t &team_id,
						  const int_fast32_t &ally_team,
						  const bool &first_search) {
	const int_fast32_t beam_depth = 5;
	const int_fast32_t beam_width = GetBeamWidth(game_data, beam_depth,
												 first_search,
												 team_id != ally_team);
	using Node_ptr = Node*;
	static bool need_create = true;
	static Node_ptr *now_all_nodes, *next_all_nodes;
	if (need_create) {
		need_create = false;
		int_fast32_t node_size = 1<<(9*3);
		now_all_nodes = new Node_ptr[node_size];
		next_all_nodes = new Node_ptr[node_size];
	}
	greater_priority_queue<pair<double, int_fast32_t>> now_que, next_que;
	Node root(turn_data, 0);
	root.GetKey(team_id);
	EraseRivalAgent(team_id^1, root.turn_data);
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
			if (now_node.turn_data.now_turn == turn &&
				now_node.evaluation > now_evaluation) continue;

			static TurnData now_turn_data;
			static vector<vector<Move>> all_moves;
			now_turn_data = now_node.turn_data;
			vector<int_fast32_t> move_ids(turn_data.agent_num);
			vector<Move> check_moves(turn_data.agent_num);

			all_moves = GetAgentsAllMoves(game_data, now_turn_data,
										  team_id, false, true);
			move_ids = {};
			for (int &&i = 0; i < turn_data.agent_num; ++i)
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
								 			   team_id,
								 			   turn,
								 			   now_node.evaluation));
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

				if (check_node.turn_data.now_turn ==
					next_turn_data.now_turn) {
					if (check_node.evaluation < next_node.evaluation) {
						check_node = next_node;
						next_que.push(make_pair(next_node.evaluation,
												next_node.key));
					}
				} else {
					if (next_que.size() < beam_width) {
						check_node = next_node;
						next_que.push(make_pair(next_node.evaluation,
												next_node.key));
					} else {
						while (next_que.size() > beam_width) {
							next_que.pop();
						}
						const Node &top_node =
							*next_all_nodes[next_que.top().second];
						if (top_node.evaluation > next_que.top().first ||
							top_node.evaluation < next_node.evaluation) {
							check_node = next_node;
							next_que.pop();
							next_que.push(make_pair(next_node.evaluation,
													next_node.key));
						}
					}
				}
			} while (NextPermutation(all_moves, 0, move_ids, check_moves));
		}
		while (next_que.size() > beam_width) {
			next_que.pop();
		}
		swap(now_que, next_que);
		swap(now_all_nodes, next_all_nodes);
	}
	const int_fast32_t &ret_size = game_data.agent_num <= 6 ? 100 : 21;
	vector<array<Move, 8>> ret;
	set<array<Move, 8>> added;
	while (now_que.size()) {
		if (added.find(now_all_nodes[now_que.top().second]->first_move) ==
			added.end()) {
			ret.push_back(now_all_nodes[now_que.top().second]->first_move);
			added.insert(now_all_nodes[now_que.top().second]->first_move);
		}
		now_que.pop();
	}
	if (ret.size() > ret_size) {
		reverse(ret.begin(), ret.end());
		while (ret.size() > ret_size) {
			ret.pop_back();
		}
	}

	return ret;
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
		sort(ret[team_id].begin(), ret[team_id].begin() + game_data.agent_num);
	}

	return ret;
}

vector<TurnData> GetSplitTurnData(
		const GameData &game_data, const TurnData &turn_data,
		const array<array<pair<Position, int_fast32_t>, 8>, 2> &agents_with_id) {
	const int_fast32_t &split_size = split_table[game_data.agent_num].size();
	vector<TurnData> ret(split_size, turn_data);
	int_fast32_t before_count = 0;
	for (int_fast32_t &&split_id = 0; split_id < split_size; ++split_id) {
		ret[split_id].agent_num = split_table[game_data.agent_num][split_id];
		for (int_fast32_t &&team_id = 0; team_id < 2; ++team_id) {
			for (int_fast32_t &&agent_id = 0;
				 agent_id < ret[split_id].agent_num; ++agent_id) {
				const int_fast32_t truth_id = before_count + agent_id;
				ret[split_id].agents_position[team_id][agent_id] =
					agents_with_id[team_id][truth_id].first;
			}
		}
		before_count += split_table[game_data.agent_num][split_id];
	}

	return ret;
}

array<vector<vector<int_fast32_t>>, 2> GetTruthIndex(
		const GameData &game_data, const vector<TurnData> &split_turn_data,
		const array<array<pair<Position, int_fast32_t>, 8>, 2> &agents_with_id) {
	const int_fast32_t &all_agent_num = game_data.agent_num;
	const int_fast32_t &split_size = split_table[all_agent_num].size();
	array<vector<vector<int_fast32_t>>, 2> ret;
	for (int_fast32_t &&team_id = 0; team_id < 2; ++team_id) {
		ret[team_id].resize(split_size);
		int_fast32_t before_count = 0;
		for (int_fast32_t &&split_id = 0; split_id < split_size; ++split_id) {
			const int_fast32_t &one_size =
				split_table[all_agent_num][split_id];
			ret[team_id][split_id].resize(one_size);
			for (int_fast32_t &&agent_id = 0;
				 agent_id < split_table[all_agent_num][split_id]; ++agent_id) {
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
	for (int_fast32_t &&team_id = 0; team_id < 2; ++team_id) {
		const int_fast32_t &check_team = ally_team^1^team_id;
		ret[check_team].resize(split_size);
		for (int_fast32_t &&split_id = 0; split_id < split_size; ++split_id) {
			// 探索は敵から
			ret[check_team][split_id] = BeamSearch(game_data,
												   split_turn_data[split_id],
												   check_team, ally_team,
												   first_search);
			first_search = false;
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
	const int_fast32_t &all_agent_num = game_data.agent_num;
	const int_fast32_t &split_size = split_table[all_agent_num].size();
	int_fast32_t &&before_count = 0;
	for (int_fast32_t &&split_id = 0; split_id < split_size; ++split_id) {
		const int_fast32_t &one_size = split_table[all_agent_num][split_id];
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
	EraseRivalAgent(team_id^1, now_turn_data);
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
													 team_id,
													 turn_data.now_turn,
													 0));
		for (int_fast32_t &&agent_id = 0; agent_id < game_data.agent_num;
			 ++agent_id) {
			next_node.first_move[agent_id] = check_all_moves[agent_id];
		}
		all_nodes.push_back(next_node);
	} while (NextPermutation(split_moves, 0, move_ids, check_split_moves));
	sort(all_nodes.begin(), all_nodes.end(), greater<>());
	vector<vector<Move>> ret_moves(10);
	for (int_fast32_t &&i = 0; i < 10; ++i) {
		ret_moves[i].resize(game_data.agent_num);
		for (int_fast32_t &&agent_id = 0; agent_id < game_data.agent_num;
			 ++agent_id) {
			ret_moves[i][agent_id] = all_nodes[i].first_move[agent_id];
		}
	}

	if (all_nodes.size() < 10) {
		while (ret_moves.size() > all_nodes.size())
			ret_moves.pop_back();
	}

	return ret_moves;
}

array<Move, 8> AllyAllSearch(
		const GameData &game_data, const TurnData &turn_data,
		const vector<vector<array<Move, 8>>> &ally_split_moves,
		const vector<vector<Move>> &rival_all_moves,
		const int_fast32_t &ally_team) {
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
		double evaluation_sum = {};
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
			evaluation_sum += GetEvaluation(game_data,
												next_turn_data,
												turn_data,
												ally_team,
												turn_data.now_turn,
												0);
		}
		Node next_node(next_turn_data, evaluation_sum);
		for (int_fast32_t &&agent_id = 0; agent_id < game_data.agent_num;
			 ++agent_id) {
			next_node.first_move[agent_id] = check_ally_moves[agent_id];
		}
		all_nodes.push_back(next_node);
	} while (NextPermutation(ally_split_moves, 0, move_ids,
							 check_split_moves));

	sort(all_nodes.begin(), all_nodes.end(), greater<>());
	return all_nodes[0].first_move;
}

array<Move, 8> SplitSearch(const GameData &game_data,
						   const TurnData &turn_data,
						   const int_fast32_t &ally_team) {
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
										rival_all_moves, ally_team);

	sort(ally_all_moves.begin(), ally_all_moves.begin() + game_data.agent_num);

	return ally_all_moves;
}

}
