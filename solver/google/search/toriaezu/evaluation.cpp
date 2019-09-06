#include "../../search/toriaezu/evaluation.h"

namespace toriaezu {

double GetEvaluation(const GameData &game_data, TurnData &turn_data,
					 const TurnData &before_turn_data,
					 const int_fast32_t &team_id,
					 const int_fast32_t &start_turn,
					 const double &before_evaluation) {
	double ally_tile_point_difference =
			AllyTilePointDifference(game_data, turn_data, before_turn_data,
									team_id);
	double rival_tile_point_difference =
			RivalTilePointDifference(game_data, turn_data, before_turn_data,
									 team_id);

	turn_data.CalculationAllAreaPoint(game_data);
	double ally_area_point_difference =
			AllyAreaPointDifference(game_data, turn_data, before_turn_data,
									team_id);
	double rival_area_point_difference =
			RivalAreaPointDifference(game_data, turn_data, before_turn_data,
									 team_id);
	double before_evaluation_bias =
			BeforeEvaluationBias(game_data, before_evaluation, team_id);

	double evaluations_sum = ally_tile_point_difference +
							 rival_tile_point_difference +
							 ally_area_point_difference +
							 rival_area_point_difference +
							 before_evaluation_bias;
	if (before_turn_data.now_turn == start_turn) {
		evaluations_sum = FirstEvaluation(game_data, evaluations_sum, team_id);
	}

	return evaluations_sum;
}

double AllyTilePointDifference(const GameData &game_data,
							   const TurnData &turn_data,
							   const TurnData &before_turn_data,
							   const int_fast32_t &team_id) {
	static string function_name = "AllyTilePointDifference";

	double ret = (turn_data.tile_point[team_id] -
				  before_turn_data.tile_point[team_id]) *
				 game_data.parameters[function_name + to_string(team_id)];

	return ret;
}

double RivalTilePointDifference(const GameData &game_data,
								const TurnData &turn_data,
								const TurnData &before_turn_data,
								const int_fast32_t &team_id) {
	static string function_name = "RivalTilePointDifference";

	double ret = (turn_data.tile_point[team_id^1] -
				  before_turn_data.tile_point[team_id^1]) *
				 game_data.parameters[function_name + to_string(team_id)];

	return -ret;
}

double AllyAreaPointDifference(const GameData &game_data,
							   const TurnData &turn_data,
							   const TurnData &before_turn_data,
							   const int_fast32_t &team_id) {
	static string function_name = "AllyAreaPointDifference";

	double ret = (turn_data.area_point[team_id] -
				  before_turn_data.area_point[team_id]) *
				 game_data.parameters[function_name + to_string(team_id)];

	return ret;

}

double RivalAreaPointDifference(const GameData &game_data,
								const TurnData &turn_data,
								const TurnData &before_turn_data,
								const int_fast32_t &team_id) {
	static string function_name = "RivalAreaPointDifference";

	double ret = (turn_data.area_point[team_id^1] -
				  before_turn_data.area_point[team_id^1]) *
				 game_data.parameters[function_name + to_string(team_id)];

	return -ret;

}

double BeforeEvaluationBias(const GameData &game_data,
							const double &before_evaluation,
							const int_fast32_t &team_id) {
	static string function_name = "BeforeEvaluationBias";

	double ret = before_evaluation *
				 game_data.parameters[function_name + to_string(team_id)];

	return ret;
}

double FirstEvaluation(const GameData &game_data,
					   const double &evaluation,
					   const int_fast32_t &team_id) {
	static string function_name = "FirstEvaluation";

	double ret = evaluation *
				 game_data.parameters[function_name + to_string(team_id)];

	return ret;
}

}