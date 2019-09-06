#include "../base/base_system.cpp"
#include "../action/change_action.cpp"
#include "../calculation/calculation.cpp"
#include "../for_optuna/for_optuna.cpp"

#include "../search/toriaezu/toriaezu.cpp"

int_fast32_t SelfMatch(string &path /* some parametor<double> */) {
	FileData file_data;
	GameData game_data;
	TurnData turn_data;

	file_data.Input(path);
	file_data.InputGameData(game_data);
	file_data.InputTurnData(game_data, turn_data);

	/*
		input some parameter
	*/

	for (int_fast32_t &&turn = 0; turn < game_data.max_turn; ++turn) {
		turn_data.now_turn = turn;
		auto ally_moves = /* something BeamSearch(game_data, turn_data, kAlly) */
		auto rival_moves = /* something BeamSearch(game_data, turn_data, kRival) */
		auto all_moves = ally_moves + rival_moves;
		turn_data.Transition(game_data, all_moves);
		turn_data.CalculationAllTilePoint(game_data);
		turn_data.CalculationAllAreaPoint(game_data);
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