#ifndef MAXIMIZE_POINT_H
#define MAXIMIZE_POINT_H

#include "../../base/base_system.h"
#include "../../action/change_action.h"

namespace maximize_point {

	array<Move, 8> greedy(const GameData & game_data,
		TurnData & turn_data,
		const int& team_id);
}
#endif
