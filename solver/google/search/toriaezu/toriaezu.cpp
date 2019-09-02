#include "../../search/toriaezu/toriaezu.h"
#include "../../search/toriaezu/evaluation.h"

#include <queue>
#include <algorithm>

namespace toriaezu {

template<typename T>
using greater_priority_queue = priority_queue<T, vector<T>, greater<T>>;

// turn_data.agent_num <= 3
void Node::GetKey() {
	key = 0;
	for (int_fast32_t &&agent_id = 0; agent_id < turn_data.agent_num;
		 ++agent_id) {
		const Position &agent_pos = turn_data.agents_position[kAlly][agent_id];
		key = (key << 9) + (agent_pos.h*20 + agent_pos.w + 1);
	}

	return;
}

array<Move, 8> BeamSearch(const GameData &game_data,
						  const TurnData &turn_data) {
	const int_fast32_t kBeamWidth = 200, kBeamDepth = 3;

	static vector<Node*> all_nodes(1<<(9*3), nullptr);
	array<Move, 8> ret = {};
	greater_priority_queue<pair<double, int_fast32_t>> now_que, next_que;
	Node root(turn_data, 0);
	root.GetKey();
	if (all_nodes[root.key] == nullptr)
		all_nodes[root.key] = new Node();
	*all_nodes[root.key] = root;
	now_que.push(make_pair(0, root.key));
	for (int_fast32_t turn = turn_data.now_turn;
		 turn <= min(turn_data.now_turn + kBeamDepth,
		 			 game_data.max_turn);
		 ++turn) {
		while (now_que.size()) {
			const double now_evaluation = now_que.top().first;
			const int_fast32_t now_key = now_que.top().second;
			now_que.pop();
			const Node &now_node = *all_nodes[now_key];
			if (now_node.turn_data.now_turn == turn &&
				now_node.evaluation > now_evaluation) continue;

			TurnData &now_turn_data = all_nodes[now_key]->turn_data;
			static vector<vector<Move>> all_moves;
			vector<int_fast32_t> move_ids(turn_data.agent_num);
			vector<Move> check_moves(turn_data.agent_num);

			all_moves = GetAgentsAllMoves(game_data, now_turn_data, kAlly,
										  false, true);
			move_ids = {};
			for (int &&i = 0; i < turn_data.agent_num; ++i)
				check_moves[i] = all_moves[i][0];

			static TurnData next_turn_data;
			static Node next_node;
			while (NextPermutation(all_moves, 0, move_ids, check_moves)) {
				next_turn_data = now_turn_data;
				next_turn_data.Transition(game_data, check_moves);
				++next_turn_data.now_turn;
				sort(next_turn_data.agents_position[kAlly].begin(),
					 next_turn_data.agents_position[kAlly].end());
				next_node = Node(next_turn_data,
								 GetEvaluation(game_data, next_turn_data,
								 			   kAlly));
				next_node.GetKey();
				if (all_nodes[next_node.key] != nullptr &&
					all_nodes[next_node.key]->turn_data.now_turn ==
							next_turn_data.now_turn) {
					Node &before_node = *all_nodes[next_node.key];
					if (before_node.evaluation < next_node.evaluation) {
						before_node = next_node;
						next_que.push(make_pair(next_node.evaluation,
												next_node.key));
					}
				} else {
					if (all_nodes[next_node.key] == nullptr)
						all_nodes[next_node.key] = new Node();
					if (next_que.size() < kBeamWidth) {
						*all_nodes[next_node.key] = next_node;
						next_que.push(make_pair(next_node.evaluation,
												next_node.key));
					} else {
						static double top_evaluation;
						static int_fast32_t top_key;
						top_evaluation = next_que.top().first;
						top_key = next_que.top().second;
						if (all_nodes[top_key]->evaluation > top_evaluation ||
							top_evaluation < next_node.evaluation) {
							*all_nodes[next_node.key] = next_node;
							next_que.pop();
							next_que.push(make_pair(next_node.evaluation, next_node.key));
						}
					}
				}
			}
		}

		swap(now_que, next_que);
	}

	return ret;
}

}