#include "../../base/base_system.h"
#include "../../action/change_action.h"

namespace maximize_point {

	// エージェント近くにあるマスのうち、最もポイントが高いマスへ動くsolverです
	array<Move, 8> greedy(const GameData & game_data,
		TurnData & turn_data,
		const int& team_id) {
		array<Move, 8> ret;
		vector<vector<Move>> all_agent_moves;

		all_agent_moves = GetAgentsAllMoves(game_data, turn_data, team_id, false, false);

		// 各エージェントの遷移候補のうち、最もポイントが高いマスを探します
		for (int&& agent_id = 0; agent_id < turn_data.agent_num; ++agent_id) {
			const Position& agent_pos = turn_data.GetPosition(team_id, agent_id);
			Position target_pos;
			int max_point = -17;	// マスのポイントの最低値は-16
			int max_point_index;

			for (int&& i = 0; i < all_agent_moves[agent_id].size(); ++i) {
				target_pos = agent_pos + kNextToNine[all_agent_moves[agent_id][i].direction];
				const int& target_pos_point = game_data.GetTilePoint(target_pos);

				if (target_pos_point > max_point && target_pos_state != kAlly) {
					max_point = target_pos_point;
					max_point_index = i;
				}
			}
			ret[agent_id] = all_agent_moves[agent_id][max_point_index];
		}
		return ret;
	}
}
