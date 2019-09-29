#include "../../search/disperse_agent/evaluation.h"

#include <cassert>

namespace disperse_agent {

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
	double disperse_agent =
		DisperseAgent(game_data, turn_data, team_id);

	double not_my_team_mas =
		NotMyTeamMasu(game_data, turn_data, start_turn, team_id);

	double before_evaluation_bias =
		BeforeEvaluationBias(game_data, before_evaluation, team_id);

	double evaluations_sum = ally_tile_point_difference +
							 rival_tile_point_difference +
							 ally_area_point_difference +
							 rival_area_point_difference +
							 disperse_agent +
							 not_my_team_mas +
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

double DisperseAgent(const GameData &game_data, const TurnData &turn_data,
					 const int_fast32_t &team_id) {
	static string function_name = "DisperseAgent";
	static array<bool, 2> first_check = {true, true};
	static array<double, 2> bias = {};
	if (first_check[team_id]) {
		auto it = game_data.parameters.find(function_name + to_string(team_id));
		assert(it != game_data.parameters.end());
		bias[team_id] = it->second;
		first_check[team_id] = false;
	}

	static const int_fast32_t box_size_half =
		max(game_data.width, game_data.height) / 5;		// * (2 / 5) / 2
	static array<array<int_fast32_t, 20>, 20> agent_area = {};
	agent_area = {};

	for (int_fast32_t &&agent_id = 0; agent_id < turn_data.agent_num;
		 ++agent_id) {
		const Position &agent_pos =
			turn_data.agents_position[team_id][agent_id];
		const Position left_up =
			Position(max((int_fast32_t)0, agent_pos.h - box_size_half),
					 max((int_fast32_t)0, agent_pos.w - box_size_half));
		const Position right_up =
			Position(max((int_fast32_t)0, agent_pos.h - box_size_half),
					 agent_pos.w + box_size_half + 1);
		const Position left_down =
			Position(agent_pos.h + box_size_half + 1,
					 max((int_fast32_t)0, agent_pos.w - box_size_half));
		const Position right_down =
			Position(agent_pos.h + box_size_half + 1,
					 agent_pos.w + box_size_half + 1);

		if (game_data.IntoField(left_up))
			++agent_area[left_up.h][left_up.w];
		if (game_data.IntoField(right_up))
			--agent_area[right_up.h][right_up.w];
		if (game_data.IntoField(left_down))
			--agent_area[left_down.h][left_down.w];
		if (game_data.IntoField(right_down))
			++agent_area[right_down.h][right_down.w];
	}

	for (int_fast32_t &&h = 0; h < game_data.height; ++h) {
		for (int_fast32_t &&w = 0; w < game_data.width; ++w) {
			if (h > 0) agent_area[h][w] += agent_area[h-1][w];
			if (w > 0) agent_area[h][w] += agent_area[h][w-1];
			if (h > 0 && w > 0) agent_area[h][w] -= agent_area[h-1][w-1];
		}
	}

	int_fast32_t ret = 0;
	for (int_fast32_t &&agent_id = 0; agent_id < turn_data.agent_num;
		 ++agent_id) {
		const Position &agent_pos =
			turn_data.agents_position[team_id][agent_id];
		ret += turn_data.agent_num - agent_area[agent_pos.h][agent_pos.w];
	}

	ret *= bias[team_id];
	return ret;
}

double NotMyTeamMasu(const GameData &game_data, const TurnData &turn_data,
					 const int_fast32_t &start_turn,
					 const int_fast32_t &team_id) {
	static string function_name = "NotMyTeamMasu";
	static array<bool, 2> first_check = {true, true};
	static array<double, 2> bias = {};
	if (first_check[team_id]) {
		auto it = game_data.parameters.find(function_name + to_string(team_id));
		assert(it != game_data.parameters.end());
		bias[team_id] = it->second;
		first_check[team_id] = false;
	}

	if (turn_data.now_turn - start_turn != 2) return 0;

	static const int_fast32_t box_size_half =
		max(game_data.width, game_data.height) / 5;	// * (2 / 5) / 2
	static array<array<int_fast32_t, 20>, 20> agent_area = {};
	agent_area = {};

	for (int_fast32_t &&h = 0; h < game_data.height; ++h) {
		for (int_fast32_t &&w = 0; w < game_data.width; ++w) {
			const int_fast32_t &tile_color = turn_data.tile_data[h][w];
			const Position left_up =
				Position(max((int_fast32_t)0, h - box_size_half),
						 max((int_fast32_t)0, w - box_size_half));
			const Position right_up =
				Position(max((int_fast32_t)0, h - box_size_half),
						 w + box_size_half + 1);
			const Position left_down =
				Position(h + box_size_half + 1,
						 max((int_fast32_t)0, w - box_size_half));
			const Position right_down =
				Position(h + box_size_half + 1,
						 w + box_size_half + 1);

			const int_fast32_t sum =
				tile_color == team_id ? 0 :
				tile_color == kBrank ? 1 :
				2;
			if (game_data.IntoField(left_up))
				agent_area[left_up.h][left_up.w] += sum;
			if (game_data.IntoField(right_up))
				agent_area[right_up.h][right_up.w] -= sum;
			if (game_data.IntoField(left_down))
				agent_area[left_down.h][left_down.w] -= sum;
			if (game_data.IntoField(right_down))
				agent_area[right_down.h][right_down.w] += sum;
		}
	}

	for (int_fast32_t &&h = 0; h < game_data.height; ++h) {
		for (int_fast32_t &&w = 0; w < game_data.width; ++w) {
			if (h > 0) agent_area[h][w] += agent_area[h-1][w];
			if (w > 0) agent_area[h][w] += agent_area[h][w-1];
			if (h > 0 && w > 0) agent_area[h][w] -= agent_area[h-1][w-1];
		}
	}

	int_fast32_t ret = 0;
	for (int_fast32_t &&agent_id = 0; agent_id < turn_data.agent_num;
		 ++agent_id) {
		const Position &agent_pos =
			turn_data.agents_position[team_id][agent_id];
		ret += turn_data.agent_num - agent_area[agent_pos.h][agent_pos.w];
	}

	ret *= bias[team_id];
	return ret;
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