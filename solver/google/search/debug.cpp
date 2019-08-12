#include "../base/base_system.cpp"
#include "../calculation/calculation.cpp"
#include "../action/change_action.cpp"

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

int main() {
	GameData game_data = {};
	game_data.Input();

	vector<Move> moves = {
		Move(kAlly, 0, 1, kErase),
		// Move(kAlly, 1, 5, kWalk),
		// Move(kRival, 0, 3, kWalk),
		// Move(kRival, 1, 3, kWalk)
	};

	TurnData turn_data = {};
	turn_data.Input(game_data);
	for (int_fast32_t &&turn = 0; turn < 4/*game_data.max_turn*/; ++turn) {
		// DebugGameData(game_data);
		DebugTurnData(game_data, turn_data);
		turn_data.Transition(game_data, moves);
		turn_data.CalculationAllTilePoint(game_data);
		turn_data.CalculationAllAreaPoint(game_data);
	}


	return 0;
}


/*

50
11 11
2
7 0 5 13 3 13 3 13 5 0 7
12 3 12 11 11 11 11 11 12 3 12
10 -7 11 5 6 0 6 5 11 -7 10
9 0 0 10 6 4 6 10 0 0 9
-10 12 5 9 10 2 10 9 5 12 -10
15 14 12 12 2 -4 2 12 12 14 15
-13 13 13 0 2 12 2 0 13 13 -13
6 12 15 6 4 6 4 6 15 12 6
0 10 5 15 -4 -12 -4 15 5 10 0
10 13 12 3 6 -10 6 3 12 13 10
0 -11 3 -3 11 4 11 -3 3 -11 0
0
2 2 0 2 2 2 2 2 1 2 2
2 2 2 2 2 2 2 2 2 2 2
2 2 2 2 2 2 2 2 2 2 2
2 2 2 2 2 2 2 2 2 2 2
2 2 2 2 2 2 2 2 2 2 2
2 2 2 2 2 2 2 2 2 2 2
2 2 2 2 2 2 2 2 2 2 2
2 2 2 2 2 2 2 2 2 2 2
2 2 0 2 2 2 2 2 1 2 2
2 2 2 2 2 2 2 2 2 2 2
2 2 2 2 2 2 2 2 2 2 2
2 8
2 0
8 8
8 0

1
5 5
2
1 1 1 1 1
1 1 1 1 1
1 1 1 1 1
1 1 1 1 1
1 1 1 1 1
0
1 1 1 1 1
1 0 2 2 1
1 2 2 2 1
1 2 2 2 1
1 1 1 1 1
1 1
4 0
0 4
4 4

*/