#include "../../search/toriaezu/evaluation.h"

namespace toriaezu {

double GetEvaluation(const GameData &game_data, const TurnData &turn_data,
					 const int_fast32_t &team_id) {

	return (turn_data.tile_point[team_id] + turn_data.area_point[team_id]) -
		   (turn_data.tile_point[team_id^1] + turn_data.area_point[team_id^1]);
}

}