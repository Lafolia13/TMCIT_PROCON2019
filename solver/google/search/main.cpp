
#include "../base/base_system.cpp"
#include "../action/change_action.cpp"
#include "../calculation/calculation.cpp"

void OutputAnswer(const GameData &game_data, const array<Move, 8> &ret) {
	for (int_fast32_t &&agent_id = 0; agent_id < game_data.agent_num; ++agent_id) {
		int_fast32_t action = ret[agent_id].action;
		int_fast32_t direction = ret[agent_id].direction;
		switch(action) {
			case kWalk : {
				cout << "w" << direction << endl;
				break;
			}
			case kErase : {
				cout << "e" << direction << endl;
				break;
			}
			case kNone : {
				cout << "n" << direction << endl;
				break;
			}
		}
	}

	return;
}

int main () {
	GameData game_data;
	game_data.Input();

	TurnData turn_data;
	array<Move, 8> ret_move;
	while (turn_data.Input(game_data)) {
		cerr << "turn : " << turn_data.now_turn << endl;
		// ret_move = something::BeamSearch(game_data, turn_data);

		OutputAnswer(game_data, ret_move);
	}

	return 0;
}