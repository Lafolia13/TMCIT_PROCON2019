#include "../base/base_system.h"

#include <iostream>

bool GameData::Input() {
	cin >> max_turn;
	cin >> width >> height;
	cin >> agent_num;

	for (int_fast32_t &&h = 0; h < height; ++h) {
		for (int_fast32_t &&w = 0; w < width; ++w) {
			cin >> field_data[h][w];
		}
	}

	return true;
}

int_fast32_t GameData::GetTilePoint(const Position &pos) const {
	return field_data[pos.h][pos.w];
}

void TurnData::reset() {
	now_turn = {};
	agent_num = {};
	tile_point = {};
	area_point = {};
	agents_position = {};
	tile_data = {};
	// is_area = {};
	bitset<400> agent_exist = {};
}

bool TurnData::Input(const GameData &game_data) {
	reset();

	cin >> now_turn;
	if (now_turn > game_data.max_turn) return false;

	for (int_fast32_t &&h = 0; h < game_data.height; ++h) {
		for (int_fast32_t &&w = 0; w < game_data.width; ++w) {
			cin >> tile_data[h][w];
		}
	}

	agent_num = game_data.agent_num;
	for (int_fast32_t &&team_id = 0; team_id < 2; ++team_id) {
		for (int_fast32_t &&agent_id = 0; agent_id < agent_num; ++agent_id) {
			cin >> agents_position[team_id][agent_id].w
				>> agents_position[team_id][agent_id].h;

			agent_exist.set(GetBitsetNumber(agents_position[team_id][agent_id]));
		}
	}

	CalculationAllTilePoint(game_data);
	CalculationAllAreaPoint(game_data);

	return true;
}

int_fast32_t& TurnData::GetTileState(const int_fast32_t &h, const int_fast32_t &w) {
	return tile_data[h][w];
}

int_fast32_t& TurnData::GetTileState(const Position &pos) {
	return GetTileState(pos.h, pos.w);
}

int_fast32_t GetBitsetNumber(const int_fast32_t &h, const int_fast32_t &w) {
	return h*20 + w;
}

int_fast32_t GetBitsetNumber(const Position &pos) {
	return GetBitsetNumber(pos.h, pos.w);
}