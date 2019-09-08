<<<<<<< HEAD
#include "../../search/split_agent/evaluation.h"

#include <cassert>

namespace split_agent {
=======
#include "../../search/toriaezu/evaluation.h"

#include <cassert>

namespace toriaezu {
>>>>>>> new branch extends toriaezu solver

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
	static array<bool, 2> first_check = {true, true};
	static array<double, 2> bias = {};
	if (first_check[team_id]) {
		auto it = game_data.parameters.find(function_name + to_string(team_id));
		assert(it != game_data.parameters.end());
		bias[team_id] = it->second;
		first_check[team_id] = false;
	}

	double ret = (turn_data.tile_point[team_id] -
				  before_turn_data.tile_point[team_id]) * bias[team_id];

	return ret;
}

double RivalTilePointDifference(const GameData &game_data,
								const TurnData &turn_data,
								const TurnData &before_turn_data,
								const int_fast32_t &team_id) {
	static string function_name = "RivalTilePointDifference";
	static array<bool, 2> first_check = {true, true};
	static array<double, 2> bias = {};
	if (first_check[team_id]) {
		auto it = game_data.parameters.find(function_name + to_string(team_id));
		assert(it != game_data.parameters.end());
		bias[team_id] = it->second;
		first_check[team_id] = false;
	}

	double ret = (turn_data.tile_point[team_id^1] -
				  before_turn_data.tile_point[team_id^1]) * bias[team_id];

	return -ret;
}

double AllyAreaPointDifference(const GameData &game_data,
							   const TurnData &turn_data,
							   const TurnData &before_turn_data,
							   const int_fast32_t &team_id) {
	static string function_name = "AllyAreaPointDifference";
	static array<bool, 2> first_check = {true, true};
	static array<double, 2> bias = {};
	if (first_check[team_id]) {
		auto it = game_data.parameters.find(function_name + to_string(team_id));
		assert(it != game_data.parameters.end());
		bias[team_id] = it->second;
		first_check[team_id] = false;
	}

	double ret = (turn_data.area_point[team_id] -
				  before_turn_data.area_point[team_id]) * bias[team_id];

	return ret;

}

double RivalAreaPointDifference(const GameData &game_data,
								const TurnData &turn_data,
								const TurnData &before_turn_data,
								const int_fast32_t &team_id) {
	static string function_name = "RivalAreaPointDifference";
	static array<bool, 2> first_check = {true, true};
	static array<double, 2> bias = {};
	if (first_check[team_id]) {
		auto it = game_data.parameters.find(function_name + to_string(team_id));
		assert(it != game_data.parameters.end());
		bias[team_id] = it->second;
		first_check[team_id] = false;
	}

	double ret = (turn_data.area_point[team_id^1] -
				  before_turn_data.area_point[team_id^1]) * bias[team_id];

	return -ret;

}

double BeforeEvaluationBias(const GameData &game_data,
							const double &before_evaluation,
							const int_fast32_t &team_id) {
	static string function_name = "BeforeEvaluationBias";
	static array<bool, 2> first_check = {true, true};
	static array<double, 2> bias = {};
	if (first_check[team_id]) {
		auto it = game_data.parameters.find(function_name + to_string(team_id));
		assert(it != game_data.parameters.end());
		bias[team_id] = it->second;
		first_check[team_id] = false;
	}

	double ret = before_evaluation * bias[team_id];

	return ret;
}

double FirstEvaluation(const GameData &game_data,
					   const double &evaluation,
					   const int_fast32_t &team_id) {
	static string function_name = "FirstEvaluation";
	static array<bool, 2> first_check = {true, true};
	static array<double, 2> bias = {};
	if (first_check[team_id]) {
		auto it = game_data.parameters.find(function_name + to_string(team_id));
		assert(it != game_data.parameters.end());
		bias[team_id] = it->second;
		first_check[team_id] = false;
	}

	double ret = evaluation * bias[team_id];

	return ret;
}

}