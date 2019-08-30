#include "../../search/toriaezu/toriaezu.h"

#include <queue>

namespace toriaezu {

template<typename T>
using greater_priority_queue<T> = priority_queue<T, vector<T>, greater<T>>;

// turn_data.agent_num <= 3
void Node::GetKey() {
	key = 0;
	for (int_fast32_t &&agent_id = 0; agent_id < turn_data.agent_num; ++agent_id) {
		const Position &agent_pos = turn_data.agents_position
		key = (key << 10) + (agent_pos.h << 5) + (agent_pos.w);
	}

	return;
}

array<Move, 8> BeamSearch(const GameData &game_data, const TurnData &turn_data) {
	const int_fast32_t kBeamWidth = 200, kBeamDepth = 3;

	static array<Node, 1<<30> all_nodes = {};
	array<Move, 8> ret = {};
	greater_priority_queue<pair<double, int_fast32_t>> now_que, next_que;
	Node root(turn_data, 0);
	root.GetKey();
	all_nodes[root.key] = root;
	now_que.push(make_pair(0, root.key));

	for (int_fast32_t &&i = 0; i < kBeamDepth; ++i) {
		while (now_que.size()) {
			const double now_evaluation = now_que.top().first;
			const int_fast32_t now_index = now_que.top().second;
			now_que.pop();
			if (all_nodes[now_index].evaluation > now_evaluation) continue;
		}

		swap(now_que, next_que);
	}

	return ret;
}

}
