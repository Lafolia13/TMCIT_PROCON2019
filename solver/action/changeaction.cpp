#include "../action/changeaction.h"

#include <algorithm>
#include <array>
namespace action {

base::Position& GetNowPositionFromMove(base::TurnData &turn_data,
									   const Move &check_move) {
	return turn_data.GetNowPosition(check_move.team_id_, check_move.agent_id_);
}

const base::Position& GetNowPositionFromMove(const base::TurnData &turn_data,
											 const Move &check_move) {
	return turn_data.GetNowPosition(check_move.team_id_, check_move.agent_id_);
}

bool IsSameTargetPosition(const base::TurnData &turn_data,
						  const Move &check_move, const Move &compare_move) {
	return GetNowPositionFromMove(turn_data, check_move) ==
		   GetNowPositionFromMove(turn_data, compare_move);
}

// 行動不可(場外、kBrankなマスにErase、敵チームのマスにWalk) : false, 行動可能 : true
bool IsAbleAction(const base::GameData &game_data,
				  const base::TurnData &turn_data,
				  const Move &check_move) {
	const base::Position &agent_position =
		GetNowPositionFromMove(turn_data, check_move);
	const base::Position &target_position =
		agent_position + action::kNextToNine[check_move.target_id_];
	const int32_t &target_tile_data =
		turn_data.GetTileData(target_position);
	if (base::IntoField(game_data, target_position) == false)
		return false;
	if (check_move.agent_action_ == kWalk) {
		const int32_t &rival_color = 1^check_move.agent_action_;	// Magic
															// base::kArry : 0, base::kRival : 1のため、
															// ビット反転すると敵チームのIDとなります
		if (target_tile_data == rival_color)
			return false;
	} else if (check_move.agent_action_ == kErase) {
		if (target_tile_data == base::kBrank)
			return false;
	}

	return true;
}

// 衝突判定とErase、Noneなエージェントの次の位置を確定します
bool CheckConflict(const base::GameData &game_data,
				   const std::vector<Move> &agents_move,
				   std::vector<bool> &moved_agent,
				   base::TurnData &turn_data) {
	std::vector<std::vector<int32_t> > target_area(game_data.height_,
												   std::vector<int32_t>(
												   game_data.width_, 0));
	// 行動位置をインクリメントします。1を超えたらコンフリクトしてます
	for (int move_id = 0; move_id < agents_move.size(); ++move_id){
		if (moved_agent[move_id] == true) continue;					// 仕様未定義のためアですが、行動不可領域に行動しようとしたエージェントは無視できると信じてます
		const Move &target_move = agents_move[move_id];
		const base::Position &target_position =
			GetNowPositionFromMove(turn_data, target_move) +
			kNextToNine[target_move.target_id_];
		++target_area[target_position.h_][target_position.w_];
	}

	for (int move_id = 0; move_id < agents_move.size(); ++move_id) {
		if (moved_agent[move_id] == true) continue;
		const Move &target_move = agents_move[move_id];
		const base::Position &agent_position =
			GetNowPositionFromMove(turn_data, target_move);
		const base::Position &target_position =
			agent_position + kNextToNine[target_move.target_id_];
		if (target_area[target_position.h_][target_position.w_] > 1 ||
			target_move.agent_action_ == kNone) {
			moved_agent[move_id] = true;
		} else if(target_move.agent_action_ == kWalk) {
			turn_data.stay_agent_[agent_position.h_]				// 移動できる場合があるので一度フラグを折ります
								 [agent_position.w_] = false;
		}
	}

	return true;
}

// この判定を抜けると行動可能が確定します
// Conflict判定を抜けたことは、少なくとも他のエージェントのWalkアクションによって行動が阻害されることがないことを示します
// 存在←移動1←移動2←移動3のような場合、1,2,3の順で停留が確定するため、最大n^2回確認を行う必要があります
bool CheckCanWalk(const base::GameData &game_data,
				  const std::vector<Move> &agents_move,
				  std::vector<bool> &moved_agent,
				  base::TurnData &turn_data) {
	bool stay_flag = true;
	for (int i = 0; i < agents_move.size() && stay_flag == true; i++) {
		stay_flag = false;
		for (int move_id = 0; move_id < agents_move.size(); ++move_id) {
			if (moved_agent[move_id] == true ||
				agents_move[move_id].agent_action_ != kWalk)
				continue;
			const Move &target_move = agents_move[move_id];
			const base::Position &agent_position =
				GetNowPositionFromMove(turn_data, target_move);
			const base::Position &target_position =
				agent_position + action::kNextToNine[target_move.target_id_];
			if (turn_data.IsExistAgent(target_position) == true) {
				moved_agent[move_id] = true;
				turn_data.stay_agent_[agent_position.h_]
									 [agent_position.w_] = true;
				stay_flag = true;
			}
		}
	}

	return true;
}

// Walkアクションを確定します
// すなわち、行動先にエージェントが存在するかを調べることで結果は確定します
// CHeckCanWalkでこれを行っているためいらないですが念の為
bool CheckWalkAction(const base::GameData &game_data,
					 const std::vector<Move> &agents_move,
					 std::vector<bool> &moved_agent,
					 base::TurnData &turn_data) {
	for (int move_id = 0; move_id < agents_move.size(); ++move_id) {
		if (moved_agent[move_id] == true ||
			agents_move[move_id].agent_action_ != kWalk)
			continue;
		moved_agent[move_id] = true;

		const Move &target_move = agents_move[move_id];
		base::Position &agent_position =
			GetNowPositionFromMove(turn_data, target_move);
		const base::Position &target_position =
			agent_position + action::kNextToNine[target_move.target_id_];
		if (turn_data.IsExistAgent(target_position) == false) {
			turn_data.stay_agent_[target_position.h_]
								 [target_position.w_] = true;
			turn_data.GetTileData(target_position) = target_move.team_id_;
			agent_position = target_position;
		} else {
			turn_data.stay_agent_[agent_position.h_]
								 [agent_position.w_] = true;
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
			agents_move[move_id].agent_action_ != kErase)
			continue;
		moved_agent[move_id] = true;

		const Move &target_move = agents_move[move_id];
		const base::Position &agent_position =
			GetNowPositionFromMove(turn_data, target_move);
		const base::Position &target_position =
			agent_position + action::kNextToNine[target_move.target_id_];
		if (turn_data.IsExistAgent(target_position) == false) {
			turn_data.GetTileData(target_position) = base::kBrank;
		}
	}

	return true;
}

// base::turn_dataとagents_moveから、次のターンのbase::TurnDataオブジェクトを作成します
// 生成する方法として、Conflictを調べる→Walkアクションを確定する→Eraseアクションを確定するをしています
base::TurnData NextTurnData(const base::GameData &game_data,
							const base::TurnData &turn_data,
							const std::vector<Move> &agents_move) {
	base::TurnData ret = turn_data;
	std::vector<bool> moved_agent(agents_move.size());
	for (int move_id = 0; move_id < agents_move.size(); ++move_id){
		moved_agent[move_id] = IsAbleAction(game_data, turn_data,
											agents_move[move_id]) == false;
	}

	CheckConflict(game_data, agents_move, moved_agent, ret);
	CheckCanWalk(game_data, agents_move, moved_agent, ret);
	CheckWalkAction(game_data, agents_move, moved_agent, ret);
	CheckEraseAction(game_data, agents_move, moved_agent, ret);

	return ret;
}

}