#include "../../search/toriaezu/toriaezu.h"
#include "../../search/toriaezu/evaluation.h"

#include <queue>
#include <algorithm>

namespace toriaezu {

template<typename T>
using greater_priority_queue = priority_queue<T, vector<T>, greater<T>>;

// turn_data.agent_num <= 3
void Node::GetKey(const int_fast32_t &team_id) {
	key = 0;
	for (int_fast32_t &&agent_id = 0; agent_id < turn_data.agent_num;
		 ++agent_id) {
		const Position &agent_pos = turn_data.agents_position[team_id][agent_id];
		key = (key << 9) + (agent_pos.h*20 + agent_pos.w + 1);
	}

	return;
}

array<Move, 8> BeamSearch(const GameData &game_data,
						  const TurnData &turn_data,
						  const int_fast32_t &team_id) {
	const int_fast32_t kBeamWidth = 2000, kBeamDepth = 5;

	static vector<Node*> now_all_nodes(1<<(9*3), nullptr), next_all_nodes(1<<(9*3), nullptr);
	greater_priority_queue<pair<double, int_fast32_t>> now_que, next_que;
	Node root(turn_data, 0);
	root.GetKey(team_id);
	if (now_all_nodes[root.key] == nullptr)
		now_all_nodes[root.key] = new Node();
	*now_all_nodes[root.key] = root;
	now_que.push(make_pair(0, root.key));
	for (int_fast32_t turn = turn_data.now_turn;
		 turn < min(turn_data.now_turn + kBeamDepth,
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

			all_moves = GetAgentsAllMoves(game_data, now_turn_data, team_id,
										  false, true);
			move_ids = {};
			for (int &&i = 0; i < turn_data.agent_num; ++i)
				check_moves[i] = all_moves[i].front();

			static TurnData next_turn_data;
			static Node next_node;
			while (NextPermutation(all_moves, 0, move_ids, check_moves)) {
				TurnData next_turn_data = now_turn_data;
				next_turn_data.Transition(game_data, check_moves);
				++next_turn_data.now_turn;
				next_turn_data.CalculationAllAreaPoint(game_data);
				sort(next_turn_data.agents_position[team_id].begin(),
					 next_turn_data.agents_position[team_id].end());
				next_node = Node(next_turn_data,
								 GetEvaluation(game_data, next_turn_data,
								 			   team_id));
				next_node.GetKey(team_id);
				if (now_turn_data.now_turn == turn_data.now_turn) {
					for (int_fast32_t &&i = 0; i < turn_data.agent_num; ++i) {
						next_node.first_move[i] = check_moves[i];
					}
				} else {
					next_node.first_move = now_node.first_move;
				}

				if (next_all_nodes[next_node.key] == nullptr)
					next_all_nodes[next_node.key] = new Node();
				Node &check_node = *next_all_nodes[next_node.key];

				if (check_node.turn_data.now_turn ==
					next_turn_data.now_turn) {
					if (check_node.evaluation < next_node.evaluation) {
						check_node = next_node;
						next_que.push(make_pair(next_node.evaluation,
												next_node.key));
					}
				} else {
					if (next_que.size() < kBeamWidth) {
						check_node = next_node;
						next_que.push(make_pair(next_node.evaluation,
												next_node.key));
					} else {
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
			}
		}

		swap(now_que, next_que);
		swap(now_all_nodes, next_all_nodes);
	}

	array<Move, 8> ret;
	while (now_que.size()) {
		if (now_que.size() == 1)
			ret = now_all_nodes[now_que.top().second]->first_move;
		now_que.pop();
	}

	return ret;
}

}