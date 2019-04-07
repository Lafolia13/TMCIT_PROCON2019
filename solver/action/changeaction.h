#ifndef CHANGEACTION_H
#define CHANGEACTION_H

#include "../base/basesystem.cpp"

#include <cstdint>
#include <vector>

namespace action {

constexpr int32_t kNone = 0;
constexpr int32_t kWalk = 1;
constexpr int32_t kErase = 2;

class Move {
public :
	int32_t team_id_;
	int32_t agent_id_;
	int32_t agent_action_;
	base::Position target_position_;

	Move() {};
	Move(const int32_t &team_id_, const int32_t &agent_id_,
		 const int32_t &agent_action_, const base::Position &target_position_) :
		team_id_(team_id_), agent_id_(agent_id_),
		agent_action_(agent_action_), target_position_(target_position_)
	{};

	inline bool operator<(const Move &another) {
		return this->target_position_ < another.target_position_;
	}

protected :
private :
};

inline bool IsSameTargetPosition(const Move&, const Move&);

inline bool IsAbleAction(const Move&, const base::GameData&,
							const base::TurnData&);

bool CheckConflict(const base::GameData&, const std::vector<Move>&,
				   std::vector<bool>&, base::TurnData&);

bool CheckWalkAction(const base::GameData&, const std::vector<Move>&,
					 std::vector<bool>&, base::TurnData&);

bool CheckEraseAction(const base::GameData&, const std::vector<Move>&,
					  std::vector<bool>&, base::TurnData&);

inline base::TurnData NextTurnData(const std::vector<Move>&, const base::GameData&,
							const base::TurnData&);

}

#endif