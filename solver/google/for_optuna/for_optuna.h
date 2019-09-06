#ifndef FOR_OPTUNA_H
#define FOR_OPTUNA_H

#include "../base/base_system.h"
#include "../action/change_action.h"
#include "../calculation/calculation.h"

#include <string>

struct FileData {
	int_fast32_t max_turn = {};
	int_fast32_t width = {};
	int_fast32_t height = {};
	int_fast32_t agent_num = {};
	array<array<int_fast32_t, 20>, 20> field_data = {};
	array<array<Position, 8>, 2> agents_position = {};


	constexpr FileData() {};

	void Input(const string&);
	void InputGameData(GameData&);
	void InputTurnData(const GameData&, TurnData&);
};

#endif
