#include "../../search/toriaezu/evaluation.h"

#include <cassert>

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
	auto it = game_data.parameters.find(function_name + to_string(team_id));
	assert(it != game_data.parameters.end());

	double ret = (turn_data.tile_point[team_id] -
				  before_turn_data.tile_point[team_id]) * it->second;

	return ret;
}

double RivalTilePointDifference(const GameData &game_data,
								const TurnData &turn_data,
								const TurnData &before_turn_data,
								const int_fast32_t &team_id) {
	static string function_name = "RivalTilePointDifference";
	auto it = game_data.parameters.find(function_name + to_string(team_id));
	assert(it != game_data.parameters.end());

	double ret = (turn_data.tile_point[team_id^1] -
				  before_turn_data.tile_point[team_id^1]) * it->second;

	return -ret;
}

double AllyAreaPointDifference(const GameData &game_data,
							   const TurnData &turn_data,
							   const TurnData &before_turn_data,
							   const int_fast32_t &team_id) {
	static string function_name = "AllyAreaPointDifference";
	auto it = game_data.parameters.find(function_name + to_string(team_id));
	assert(it != game_data.parameters.end());

	double ret = (turn_data.area_point[team_id] -
				  before_turn_data.area_point[team_id]) * it->second;

	return ret;

}

double RivalAreaPointDifference(const GameData &game_data,
								const TurnData &turn_data,
								const TurnData &before_turn_data,
								const int_fast32_t &team_id) {
	static string function_name = "RivalAreaPointDifference";
	auto it = game_data.parameters.find(function_name + to_string(team_id));
	assert(it != game_data.parameters.end());

	double ret = (turn_data.area_point[team_id^1] -
				  before_turn_data.area_point[team_id^1]) * it->second;

	return -ret;

}

double BeforeEvaluationBias(const GameData &game_data,
							const double &before_evaluation,
							const int_fast32_t &team_id) {
	static string function_name = "BeforeEvaluationBias";
	auto it = game_data.parameters.find(function_name + to_string(team_id));
	assert(it != game_data.parameters.end());

	double ret = before_evaluation * it->second;

	return ret;
}

double FirstEvaluation(const GameData &game_data,
					   const double &evaluation,
					   const int_fast32_t &team_id) {
	static string function_name = "FirstEvaluation";
	auto it = game_data.parameters.find(function_name + to_string(team_id));
	assert(it != game_data.parameters.end());

	double ret = evaluation * it->second;

	return ret;
}

}