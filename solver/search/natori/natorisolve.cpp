#include "../../search/Natori/natorisolve.h"

#include <queue>

namespace search {
namespace natori {

constexpr int32_t beam_width = 1000;
constexpr int32_t beam_depth = 1;

bool NextPermutation(const std::vector<std::vector<action::Move> >
					 	&agents_moves,
					 int32_t agent_id,
					 std::vector<int32_t> &moves_id,
					 std::vector<action::Move> &ret_move) {
	if (agent_id == agents_moves.size())
		return false;

	int32_t &target_id = moves_id[agent_id];
	const std::vector<action::Move> target_moves = agents_moves[agent_id];

	if (target_id == target_moves.size()) {
		target_id = 0;
		ret_move[agent_id] = target_moves[target_id++];

		return NextPermutation(agents_moves, agent_id+1,
							   moves_id, ret_move);
	} else {
		ret_move[agent_id] = target_moves[target_id];
		target_id++;

		return true;
	}
}

std::vector<action::Move> GetAgentActions(const base::GameData &game_data,
										  const base::TurnData &turn_data,
										  const int32_t &team_id,
										  const int32_t &agent_id) {
	std::vector<action::Move> ret;
	const base::Position &agent_position = turn_data.GetNowPosition(team_id,
																	agent_id);
	base::Position target_position;
	action::Move agent_move(team_id, agent_id, 0,0);

	for (int32_t i = 0; i < action::kNextToNine.size(); ++i) {
		const base::Position &next_to = action::kNextToNine[i];
		target_position = agent_position + next_to;
		if (IntoField(game_data, target_position) == false)
			continue;

		const int32_t &target_tile_data =
			turn_data.GetTileData(target_position);
		agent_move.target_id_ = i;
		if (target_tile_data == base::kBrank || target_tile_data == team_id) {
			agent_move.agent_action_ = action::kWalk;
		} else {
			agent_move.agent_action_ = action::kErase;
		}
		ret.push_back(agent_move);
	}

	return ret;
}

std::vector<action::Move> BeamSearch(const base::GameData &game_data,
									   const base::TurnData &turn_data) {
	std::priority_queue<Node, std::vector<Node>,
						std::greater<Node> > p_queue, next_queue;

	std::vector<std::vector<action::Move> > agents_moves(game_data.agent_num_);
	std::vector<action::Move> check_moves(game_data.agent_num_);
	std::vector<int32_t> moves_id(game_data.agent_num_);
	Node next_node;
	p_queue.push(Node(turn_data, check_moves, 0));
	for (int i = 0; i < beam_depth; ++i) {
		while (p_queue.size() > 0) {
			Node now_node = p_queue.top();
			p_queue.pop();
			for (int agent_id = 0; agent_id < game_data.agent_num_;
				 ++agent_id) {
				agents_moves[agent_id] =
					GetAgentActions(game_data, turn_data,
									base::kAlly, agent_id);
			}

			std::fill(moves_id.begin(), moves_id.end(), 0);
			for (int agent_id = 0; agent_id < game_data.agent_num_; ++agent_id)
				check_moves[agent_id] = agents_moves[agent_id][0];

			while (NextPermutation(agents_moves, 0, moves_id,
								  check_moves) == true) {
				base::TurnData next_turn_data =
					NextTurnData(game_data, now_node.turn_data_, check_moves);

				auto all_point =
					calculation::CalculationAllPoint(game_data,
													 next_turn_data);
				const calculation::Point &ally_point = all_point.first;
				const calculation::Point &rival_point = all_point.second;
				const int32_t evaluation =
					ally_point.all_point_ - rival_point.all_point_;

				next_node = Node(next_turn_data, check_moves, evaluation);
				if (i == 0) {
					next_node.root_move_ = check_moves;
				} else {
					next_node.root_move_ = now_node.root_move_;
				}

				if (next_queue.size() == 0 ||
					next_queue.top().evaluation_ < evaluation)
					next_queue.push(next_node);
				if (next_queue.size() > beam_width)
					next_queue.pop();
			}
		}

		swap(p_queue, next_queue);
	}

	std::vector<action::Move> ret;
	while (p_queue.size() > 0) {
		ret = p_queue.top().root_move_;
		p_queue.pop();
	}
	return ret;
}

}
}