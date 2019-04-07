#include "../action/changeaction.h"

#include <algorithm>
#include <array>

namespace action {

inline bool IsSameTargetPosition(const Move &check_move,
								 const Move &compare_move) {
	return check_move.target_position_ == compare_move.target_position_;
}

// 行動不可(場外、行動先にエージェント、kBrankなマスにErase、敵チームのマスにWalk) : false, 行動可能 : true
inline bool IsAbleAction(const Move &check_move,
					 const base::GameData &game_data,
					 const base::TurnData &turn_data) {
	const base::Position &target_position = check_move.target_position_;
	int32_t tile_color = turn_data.tile_data_[target_position.h_]
											 [target_position.w_];
	if (base::IntoField(target_position, game_data))
		return false;
	if (turn_data.stay_agent_[target_position.h_][target_position.w_] == true)
		return false;
	if (check_move.agent_action_ == kWalk) {
		int32_t rival_color = 1^check_move.agent_action_;	// Magic
															// base::kArryTeam : 0, base::kRivalTeam : 1のため、
															// ビット反転すると敵チームのIDとなります
		if (tile_color == rival_color)
			return false;
	} else if (check_move.agent_action_ == kErase) {
		if (tile_color == base::kBrank)
			return false;
	}

	return true;
}

// 衝突判定とErase、Noneなエージェントの次の位置を確定します
// agents_moveはソート済みなvectorにしてください
bool CheckConflict(const base::GameData &game_data,
				   const std::vector<Move> &agents_move,
				   std::vector<bool> &moved_agent,
				   base::TurnData &turn_data) {
	for (int32_t move_id = 0; move_id < agents_move.size(); ++move_id) {
		const Move &target_move = agents_move[move_id];
		base::Position &agent_position =
			turn_data.agents_position_[target_move.team_id_]
									  [target_move.agent_id_];
		// 前回位置のフラグを折ります
		turn_data.stay_agent_[agent_position.h_]
							 [agent_position.w_] = false;

		// NoneかEraseのとき、エージェントの次の位置が確定します
		// Noneのとき、行動は即座に完了します
		if (target_move.agent_action_ == kNone ||
			target_move.agent_action_ == kErase) {
			if (target_move.agent_action_ == kNone)
				moved_agent[move_id] = true;
			turn_data.stay_agent_[agent_position.h_]
								 [agent_position.w_] = true;
		}
		// ソート済みのため、隣接するMoveはtarget_position_が同一な場合があります
		// この場合Conflictとなるため、行動は完了し、エージェントの次の位置も確定します
		bool same_target = false;
		if (move_id == 0 && agents_move.size() > 1) {
			same_target |= IsSameTargetPosition(agents_move[move_id],
											   agents_move[move_id + 1]);
		}
		if(move_id == agents_move.size() -1 && agents_move.size() > 1) {
			same_target |= IsSameTargetPosition(agents_move[move_id],
											   agents_move[move_id - 1]);
		}
		if(same_target == true) {
			turn_data.stay_agent_[agent_position.h_]
								 [agent_position.w_] = true;
			moved_agent[move_id] = true;
		}
	}

	return true;
}

// Walkアクションを確定します
// Conflict判定を抜けたことは、少なくとも他のエージェントのWalkアクションによって行動が阻害されることがないことを示します
// すなわち、行動先にエージェントが存在するか、そもそも行動可能かを調べることで結果は確定します
bool CheckWalkAction(const base::GameData &game_data,
					 const std::vector<Move> &agents_move,
					 std::vector<bool> &moved_agent,
					 base::TurnData &turn_data) {
	for (int move_id = 0; move_id < agents_move.size(); ++move_id) {
		if (moved_agent[move_id] == true ||
			agents_move[move_id].agent_action_ == kErase)
			continue;
		moved_agent[move_id] = true;

		const Move &target_move = agents_move[move_id];
		const base::Position &target_position = target_move.target_position_;
		base::Position &agent_position = turn_data.agents_position_
												[target_move.team_id_]
												[target_move.agent_id_];
		if (IsAbleAction(target_move, game_data, turn_data) == true) {
			turn_data.stay_agent_[target_position.h_][target_position.w_] = true;
			agent_position = target_position;
		} else {
			turn_data.stay_agent_[agent_position.h_][agent_position.w_] = true;
		}
	}

	return true;
}

// Eraseアクションを確定します
// 同上です。Walkアクションを優先するのは、エージェントの移動前の位置のタイルをEraseすることが可能なため、
// 先にWalkなエージェントの位置を確定する必要があるからです
bool CheckEraseAction(const base::GameData &game_data,
					  const std::vector<Move> &agents_move,
					  std::vector<bool> &moved_agent,
					  base::TurnData &turn_data) {
	for (int move_id = 0; move_id < agents_move.size(); ++move_id) {
		if (moved_agent[move_id] == true ||
			agents_move[move_id].agent_action_ == kErase)
			continue;
		moved_agent[move_id] = true;

		const Move &target_move = agents_move[move_id];
		const base::Position &target_position = target_move.target_position_;
		if (IsAbleAction(target_move, game_data, turn_data) == true) {
			turn_data.tile_data_[target_position.h_][target_position.w_] = false;
		}
	}

	return true;
}

// base::turn_dataとagents_moveから、次のターンのbase::TurnDataオブジェクトを作成します
// 生成する方法として、Conflictを調べる→Walkアクションを確定する→Eraseアクションを確定するをしています
inline base::TurnData NextTurnData(const std::vector<Move> &agents_move,
							const base::GameData &game_data,
							const base::TurnData &turn_data) {
	base::TurnData ret = turn_data;
	std::vector<Move> agents_move_cp = agents_move;
	std::vector<bool> moved_agent(agents_move_cp.size());
	sort(agents_move_cp.begin(), agents_move_cp.end());

	CheckConflict(game_data, agents_move_cp, moved_agent, ret);
	CheckWalkAction(game_data, agents_move_cp, moved_agent, ret);
	CheckEraseAction(game_data, agents_move_cp, moved_agent, ret);

	return ret;
}

}