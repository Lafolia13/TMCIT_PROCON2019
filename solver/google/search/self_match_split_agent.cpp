#include "../base/base_system.cpp"
#include "../action/change_action.cpp"
#include "../calculation/calculation.cpp"
#include "../for_optuna/for_optuna.cpp"

#include "../search/split_agent/split_agent.cpp"
#include "../search/split_agent/evaluation.cpp"

int_fast32_t SelfMatch(string path,
					   int_fast32_t turn_time_ms,
					   double ally_tile_point_difference_kally,
					   double rival_tile_point_difference_kally,
					   double ally_area_point_difference_kally,
					   double rival_area_point_difference_kally,
					   double before_evaluation_bias_kally,
					   double first_evaluation_kally,
					   double ally_tile_point_difference_krival,
					   double rival_tile_point_difference_krival,
					   double ally_area_point_difference_krival,
					   double rival_area_point_difference_krival,
					   double before_evaluation_bias_krival,
					   double first_evaluation_krival) {
	FileData file_data;
	GameData game_data;
	TurnData turn_data;

	file_data.Input(path);
	file_data.InputGameData(game_data);
	file_data.InputTurnData(game_data, turn_data);
	game_data.turn_time_ms = turn_time_ms;

	game_data.parameters["AllyTilePointDifference0"] = ally_tile_point_difference_kally;
	game_data.parameters["RivalTilePointDifference0"] = rival_tile_point_difference_kally;
	game_data.parameters["AllyAreaPointDifference0"] = ally_area_point_difference_kally;
	game_data.parameters["RivalAreaPointDifference0"] = rival_area_point_difference_kally;
	game_data.parameters["BeforeEvaluationBias0"] = before_evaluation_bias_kally;
	game_data.parameters["FirstEvaluation0"] = first_evaluation_kally;

	game_data.parameters["AllyTilePointDifference1"] = ally_tile_point_difference_krival;
	game_data.parameters["RivalTilePointDifference1"] = rival_tile_point_difference_krival;
	game_data.parameters["AllyAreaPointDifference1"] = ally_area_point_difference_krival;
	game_data.parameters["RivalAreaPointDifference1"] = rival_area_point_difference_krival;
	game_data.parameters["BeforeEvaluationBias1"] = before_evaluation_bias_krival;
	game_data.parameters["FirstEvaluation1"] = first_evaluation_krival;


	for (int_fast32_t &&turn = 0; turn < game_data.max_turn; ++turn) {
		cerr << turn << " : ";
		turn_data.now_turn = turn;
		auto ally_moves = split_agent::SplitSearch(game_data, turn_data, kAlly);
		auto rival_moves = split_agent::SplitSearch(game_data, turn_data, kRival);
		vector<Move> all_moves;
		for (int_fast32_t &&i = 0; i < game_data.agent_num; ++i) {
			all_moves.push_back(ally_moves[i]);
			all_moves.push_back(rival_moves[i]);
		}
		turn_data.Transition(game_data, all_moves);
		turn_data.CalculationAllTilePoint(game_data);
		turn_data.CalculationAllAreaPoint(game_data);

		cerr << turn_data.tile_point[kAlly] + turn_data.area_point[kAlly]
			 << " " << turn_data.tile_point[kRival] + turn_data.area_point[kRival]
			 << endl;
	}

	turn_data.CalculationAllTilePoint(game_data);
	turn_data.CalculationAllAreaPoint(game_data);
	int_fast32_t ally_point = turn_data.tile_point[kAlly] +
							  turn_data.area_point[kAlly];
	int_fast32_t rival_point = turn_data.tile_point[kRival] +
							   turn_data.area_point[kRival];

	if (ally_point > rival_point) {
		return 1;
	} else if (ally_point == rival_point) {
		return 0;
	} else {
		return -1;
	}
}

#include <iostream>
int main() {
	cout << SelfMatch("../../../../test/sample/map_creater/sample3.txt", 5000,
					  1,1,1,1,1,1,
					  1,1,1,1,1,1) << endl;

	return 0;
}