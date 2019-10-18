#include "../base/base_system.cpp"
#include "../action/change_action.cpp"
#include "../calculation/calculation.cpp"

// #include "./split_agent/split_agent.cpp"
// #include "./split_agent/evaluation.cpp"

#include "./disperse_agent/disperse_agent.cpp"
#include "./disperse_agent/evaluation.cpp"

// #include "./improve_disperse/improve_disperse.cpp"
// #include "./improve_disperse/evaluation.cpp"

// #include "./neo_beamsearch/neo_beamsearch.cpp"
// #include "./neo_beamsearch/evaluation.cpp"

// #include "./none/none.cpp"

#include <fstream>
#include <sstream>

void InputDefaultParameter(GameData &game_data) {
	// string file_path = "./neo_beamsearch/default_evaluation.txt";
	string file_path = "./disperse_agent/default_evaluation.txt";
	// string file_path = "./improve_disperse/default_evaluation.txt";

	ifstream ifs;
	istringstream iss;
	string str, key, value;

	ifs = ifstream(file_path);
	while (getline(ifs, str)) {
		iss = istringstream(str);
		getline(iss, key, '=');
		getline(iss, value, '=');
		for (int_fast32_t &&i = 0; i < 2; ++i)
			game_data.parameters[key + to_string(i)] = stod(value);
	}

	return;
}

void InputCustomParameter(int_fast32_t &argc, char** argv, GameData &game_data) {
	istringstream iss;
	string key, value;

	for (int_fast32_t &&i = 1; i < argc; ++i) {
		iss = istringstream(argv[i]);
		getline(iss, key, '=');
		getline(iss, value, '=');
		for (int_fast32_t &&j = 0; j < 2; ++j)
			game_data.parameters[key + to_string(i)] = stod(value);
	}

	return;
}

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
				cout << "n" << endl;
				break;
			}
		}
	}

	return;
}

int main (int argc, char** argv) {
	GameData game_data;
	game_data.turn_time_ms = 10000;

	game_data.Input();


	if (argc == 1) {
		InputDefaultParameter(game_data);
	} else {
		InputCustomParameter(argc, argv, game_data);
	}

	TurnData turn_data, before_turn_data, bbefore_turn_data;
	array<Move, 8> ret_move;
	while (turn_data.Input(game_data)) {
		cerr << "turn : " << turn_data.now_turn << endl;
		ret_move = disperse_agent::SplitSearch(game_data, turn_data, kAlly,
											   turn_data == before_turn_data ||
											   turn_data == bbefore_turn_data);
		// ret_move = none::AllNone();

		OutputAnswer(game_data, ret_move);

		bbefore_turn_data = before_turn_data;
		before_turn_data = turn_data;
	}

	return 0;
}