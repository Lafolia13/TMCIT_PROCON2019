#include "../base/base_system.cpp"
#include "../action/change_action.cpp"
#include "../calculation/calculation.cpp"


int main () {
	GameData game_data;
	game_data.Input();

	TurnData turn_data;

	while (turn_data.Input(game_data)) {

	}

	return 0;
}