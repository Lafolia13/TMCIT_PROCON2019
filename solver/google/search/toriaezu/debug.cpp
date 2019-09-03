#include "../../base/base_system.cpp"
#include "../../action/change_action.cpp"
#include "../../calculation/calculation.cpp"
#include "../../search/toriaezu/toriaezu.cpp"
#include "../../search/toriaezu/evaluation.cpp"

void DebugGameData(GameData &game_data) {
	fprintf(stderr, "max_turn : %d\n", game_data.max_turn);
	fprintf(stderr, "height : %d\n", game_data.height);
	fprintf(stderr, "width : %d\n", game_data.width);
	fprintf(stderr, "agent_num : %d\n", game_data.agent_num);
	fprintf(stderr, "field_data\n");
	for (int_fast32_t &&h = 0; h < game_data.height; ++h) {
		for (int_fast32_t &&w = 0; w < game_data.width; ++w) {
			cerr << game_data.field_data[h][w] << " ";
		}
		cerr << endl;
	}

	cerr << endl;
	return;
}

void DebugTurnData(GameData &game_data, TurnData &turn_data) {
	fprintf(stderr, "now_turn : %d\n", turn_data.now_turn);
	fprintf(stderr, "agent_num : %d\n", turn_data.agent_num);
	fprintf(stderr, "point\n");
	fprintf(stderr, "allly : tile_point : %d, area_point : %d\n", turn_data.tile_point[kAlly], turn_data.area_point[kAlly]);
	fprintf(stderr, "rival : tile_point : %d, area_point : %d\n", turn_data.tile_point[kRival], turn_data.area_point[kRival]);
	fprintf(stderr, "agents_position\n");
	fprintf(stderr, "ally\n");
	for (int_fast32_t &&i = 0; i < turn_data.agent_num; ++i) {
		fprintf(stderr, "%d : %d %d\n", i, turn_data.agents_position[kAlly][i].h, turn_data.agents_position[kAlly][i].w);
	}
	fprintf(stderr, "rival\n");
	for (int_fast32_t &&i = 0; i < turn_data.agent_num; ++i) {
		fprintf(stderr, "%d : %d %d\n", i, turn_data.agents_position[kRival][i].h, turn_data.agents_position[kRival][i].w);
	}
	fprintf(stderr, "tile_data\n");
	for (int_fast32_t &&h = 0; h < game_data.height; ++h) {
		for (int_fast32_t &&w = 0; w < game_data.width; ++w) {
			cerr << turn_data.tile_data[h][w] << " ";
		}
		cerr << endl;
	}
	fprintf(stderr, "agent_exist\n");
	for (int_fast32_t &&i = 0; i < turn_data.agent_num; ++i) {
		for (int_fast32_t &&j = 0; j < 2; ++j)
			cerr << turn_data.agent_exist[GetBitsetNumber(turn_data.agents_position[j][i])];
	}

	cerr << endl << endl;
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
		DebugTurnData(game_data, turn_data);
		cerr << "turn : " << turn_data.now_turn << endl;
		ret_move = toriaezu::BeamSearch(game_data, turn_data);

		OutputAnswer(game_data, ret_move);
	}

	return 0;
}


/*

50
8 8
2
2 1 0 11 11 0 1 2
14 -8 4 7 7 4 -8 14
3 -15 8 9 9 8 -15 3
0 13 14 11 11 14 13 0
11 12 13 7 7 13 12 11
11 7 0 13 13 0 7 11
-15 3 6 1 1 6 3 -15
5 3 0 -10 -10 0 3 5
0
2 2 2 2 2 2 2 2
2 2 2 2 2 2 2 2
2 2 2 2 2 2 2 2
2 2 0 2 2 1 2 2
2 2 2 0 1 2 2 2
2 2 2 2 2 2 2 2
2 2 2 2 2 2 2 2
2 2 2 2 2 2 2 2
3 4
2 3
4 4
5 3

50
10 10
2
10 14 3 -9 0 0 -9 3 14 10
9 6 14 -10 1 1 -10 14 6 9
4 10 -2 -1 0 0 -1 -2 10 4
3 7 7 4 2 2 4 7 7 3
-10 8 10 4 2 2 4 10 8 -10
10 -3 -11 8 8 8 8 -11 -3 10
4 11 11 2 4 4 2 11 11 4
13 13 11 0 0 0 0 11 13 13
5 9 7 3 11 11 3 7 9 5
8 14 11 15 7 7 15 11 14 8
0
2 2 2 2 2 2 2 2 2 2
2 2 1 2 2 2 2 0 2 2
2 2 2 2 2 2 2 2 2 2
2 2 2 2 2 2 2 2 2 2
2 0 2 2 2 2 2 2 1 2
2 2 2 2 2 2 2 2 2 2
2 2 2 2 2 2 2 2 2 2
2 2 2 2 2 2 2 2 2 2
2 2 2 2 2 2 2 2 2 2
2 2 2 2 2 2 2 2 2 2
1 4
7 1
8 4
2 1


*/