#include "../../search/natori/evaluation.h"

namespace search {
namespace natori {

int32_t GetPointDefference(const base::GameData &game_data,
						   const base::TurnData &turn_data) {
	auto ally_and_rival_points =
		calculation::CalculationAllPoint(game_data, turn_data);

	calculation::Point ally_point = ally_and_rival_points.first;
	calculation::Point rival_point = ally_and_rival_points.second;

	return ally_point.all_point_ - rival_point.all_point_;
}

}
}