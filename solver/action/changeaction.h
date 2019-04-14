#ifndef CHANGEACTION_H
#define CHANGEACTION_H

#include "../base/basesystem.h"

#include <cstdint>
#include <vector>

namespace action {

enum AgentActions {
	kNone = 0,
	kWalk = 1,
	kErase = 2,
};
constexpr std::array<char, 3> kToCharactor = {'n', 'w', 'e'};
// 要は
// 0 1 2
// 3 4 5
// 6 7 8
// です
constexpr std::array<base::Position, 9> kNextToNine = {base::Position(-1,-1),
													   base::Position(-1,0),
													   base::Position(-1,1),
													   base::Position(0,-1),
													   base::Position(0,0),
													   base::Position(0,1),
													   base::Position(1,-1),
													   base::Position(1,0),
													   base::Position(1,1)};

// team_id_、agent_id_はbase::TurnDataを参考にしてください
// agent_action_は上記の定数三種を選択してください
// target_position_は現在位置からの行動先の相対位置(kNextToNine)を指定してください
class Move {
public :
	int32_t team_id_;
	int32_t agent_id_;
	int32_t target_id_;
	int32_t agent_action_;

	Move() {};
	Move(const int32_t &team_id, const int32_t &agent_id,
		 const int32_t &target_id, const int32_t &agent_action) :
		team_id_(team_id), agent_id_(agent_id),
		target_id_(target_id), agent_action_(agent_action)
	{};

	bool operator<(const Move &another) const {
		return team_id_ == another.team_id_ ?
			agent_id_ < another.agent_id_ :
			team_id_ < another.team_id_;
	}

protected :
private :
};

base::Position& GetNowPositionFromMove(base::TurnData&, const Move&);

const base::Position& GetNowPositionFromMove(const base::TurnData&,
											 const Move&);

bool IsSameTargetPosition(const base::TurnData&, const Move&, const Move&);

bool IsAbleAction(const base::GameData&, const base::TurnData&, const Move&);

bool CheckConflict(const base::GameData&, const std::vector<Move>&,
				   std::vector<bool>&, base::TurnData&);

bool CheckCanWalk(const base::GameData&, const std::vector<Move>&,
				  std::vector<bool>&, base::TurnData&);

bool CheckWalkAction(const base::GameData&, const std::vector<Move>&,
					 std::vector<bool>&, base::TurnData&);

bool CheckEraseAction(const base::GameData&, const std::vector<Move>&,
					  std::vector<bool>&, base::TurnData&);

base::TurnData NextTurnData(const base::GameData&, const base::TurnData&,
							const std::vector<Move>&);

}

#endif