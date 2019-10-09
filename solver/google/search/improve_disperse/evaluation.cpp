#include "../../search/improve_disperse/evaluation.h"

#include <cassert>

namespace improve_disperse {

double GetEvaluation(const GameData &game_data, TurnData &turn_data,
					 const TurnData &before_turn_data,
					 const vector<Move> &moves,
					 const int_fast32_t &team_id,
					 const int_fast32_t &start_turn,
					 const double &before_evaluation,
					 const bool &in_beam) {
	double ally_tile_point_difference = 0.0;
	double rival_tile_point_difference = 0.0;
	double ally_area_point_difference = 0.0;
	double rival_area_point_difference = 0.0;

	double ally_area_num = 0.0;
	double rival_area_num = 0.0;
	double stay_minus_masu = 0.0;
	double action_to_rival_location = 0.0;
	double disperse_agent = 0.0;
	double not_my_team_mas = 0.0;

	double before_evaluation_bias = 0.0;
	double evaluations_sum = 0.0;

	turn_data.CalculationAllAreaPoint(game_data);

	ally_tile_point_difference =
		AllyTilePointDifference(game_data, turn_data, before_turn_data,
								team_id);
	rival_tile_point_difference =
		RivalTilePointDifference(game_data, turn_data, before_turn_data,
								 team_id);

	ally_area_point_difference =
		AllyAreaPointDifference(game_data, turn_data, before_turn_data,
								team_id);
	rival_area_point_difference =
		RivalAreaPointDifference(game_data, turn_data, before_turn_data,
								 team_id);

	if (!in_beam) {
		action_to_rival_location =
			ActionToRivalLocation(game_data, before_turn_data, moves, team_id);
	}

	if (in_beam) {
		ally_area_num =
			AllyAreaNum(game_data, turn_data, team_id);

		rival_area_num =
			RivalAreaNum(game_data, turn_data, team_id);

		stay_minus_masu =
			StayMinusMasu(game_data, turn_data, team_id);


		disperse_agent =
			DisperseAgent(game_data, turn_data, team_id);

		not_my_team_mas =
			NotMyTeamMasu(game_data, turn_data, start_turn, team_id);
	}

	before_evaluation_bias =
		BeforeEvaluationBias(game_data, before_evaluation, team_id);

	evaluations_sum = ally_tile_point_difference +
					  rival_tile_point_difference +
					  ally_area_point_difference +
					  rival_area_point_difference +
					  ally_area_num +
					  rival_area_num +
					  stay_minus_masu +
					  action_to_rival_location +
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

double AllyAreaNum(const GameData &game_data,const TurnData &turn_data,
				   const int_fast32_t &team_id) {
	static string function_name = "AllyAreaNum";
	static array<bool, 2> first_check = {true, true};
	static array<double, 2> bias = {};
	if (first_check[team_id]) {
		auto it = game_data.parameters.find(function_name + to_string(team_id));
		assert(it != game_data.parameters.end());
		bias[team_id] = it->second;
		first_check[team_id] = false;
	}

	double ret = turn_data.area_num[team_id];

	ret *= bias[team_id];
	return ret;
}

double RivalAreaNum(const GameData &game_data, const TurnData &turn_data,
					const int_fast32_t &team_id) {
	static string function_name = "RivalAreaNum";
	static array<bool, 2> first_check = {true, true};
	static array<double, 2> bias = {};
	if (first_check[team_id]) {
		auto it = game_data.parameters.find(function_name + to_string(team_id));
		assert(it != game_data.parameters.end());
		bias[team_id] = it->second;
		first_check[team_id] = false;
	}

	double ret = turn_data.area_num[team_id^1];

	ret *= bias[team_id^1];
	return -ret;
}

double StayMinusMasu(const GameData &game_data, const TurnData &turn_data,
					 const int_fast32_t &team_id) {
	static string function_name = "StayMinusMasu";
	static array<bool, 2> first_check = {true, true};
	static array<double, 2> bias = {};
	if (first_check[team_id]) {
		auto it = game_data.parameters.find(function_name + to_string(team_id));
		assert(it != game_data.parameters.end());
		bias[team_id] = it->second;
		first_check[team_id] = false;
	}

	double ret = 0;
	for (int_fast32_t &&agent_id = 0; agent_id < turn_data.agent_num;
		 ++agent_id) {
		const Position &agent_pos =
			turn_data.agents_position[team_id][agent_id];
		ret += min((int_fast32_t)0, game_data.GetTilePoint(agent_pos));
	}

	ret *= bias[team_id];
	return ret;
}

double ActionToRivalLocation(const GameData &game_data,
							 const TurnData &before_turn_data,
							 const vector<Move> &moves,
							 const int_fast32_t &team_id) {
	static string function_name = "ActionToRivalLocation";
	static array<bool, 2> first_check = {true, true};
	static array<double, 2> bias = {};
	if (first_check[team_id]) {
		auto it = game_data.parameters.find(function_name + to_string(team_id));
		assert(it != game_data.parameters.end());
		bias[team_id] = it->second;
		first_check[team_id] = false;
	}

	double ret = 0;
	for (const auto &move : moves) {
		if (move.team_id != team_id) continue;
		if (before_turn_data.agent_exist[GetBitsetNumber(
				move.target_position)] == true) {
			ret += game_data.GetTilePoint(move.target_position);
		}
	}

	ret *= bias[team_id];
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

	static const int_fast32_t box_size_half = 2;		// * (2 / 5) / 2
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

	// 重いので深さ2のときだけやります
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

			const int_fast32_t sum = tile_color == (team_id^1);
			// const int_fast32_t sum =
			// 	tile_color == team_id ? 0 :
			// 	tile_color == kBrank ? 1 :
			// 	2;
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