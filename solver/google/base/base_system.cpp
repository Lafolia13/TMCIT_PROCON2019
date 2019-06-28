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

bool GameData::IntoField(const Position &pos) {
	return (0 <= pos.h && pos.h < height && 0 <= pos.w && pos.w < width);
}

bool TurnData::Input(const GameData &game_data) {
	cin >> now_turn;
	if (this->now_turn > game_data.max_turn)
		return false;

	for (int_fast32_t &&h = 0; h < game_data.height; ++h) {
		for (int_fast32_t &&w = 0; w < game_data.width; ++w) {
			cin >> tile_data[h][w];
		}
	}

	for (int8_t &&team_id = 0; team_id < 2; ++team_id) {
		for (int8_t agent_id = 0; agent_id < game_data.agent_num; ++agent_id) {
			cin >> agents_position[team_id][agent_id].w
				>> agents_position[team_id][agent_id].h;
		}
	}

	return true;
}

int_fast32_t& TurnData::GetTileState(const int_fast32_t &h, const int_fast32_t &w) {
	return tile_data[h][w];
}

int_fast32_t& TurnData::GetTileState(const Position &pos) {
	return GetTileState(pos.h, pos.w);
}

Position& TurnData::GetPosition(const int_fast32_t &team_id, const int_fast32_t &agent_id) {
	return agents_position[team_id][agent_id];
}