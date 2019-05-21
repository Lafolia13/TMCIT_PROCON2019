#include "../base/base_system.h"

#include <iostream>

bool GameData::Input() {
	cin >> max_turn;
	cin >> width >> height;
	cin >> agent_num;

	for (int8_t h = 0; h < height; ++h) {
		for (int8_t w = 0; w < width; ++w) {
			cin >> field_data[h][w];
		}
	}

	return true;
}

bool GameData::IntoField(const int16_t &position, const int8_t &way) {
	int h = (position >> 5)&kFive + kNextToEit[way].h;
	int w = position&kFive + kNextToEit[way].w;
	return (0 <= h && h < height &&
			0 <= w && w < width);
}

bool TurnData::Input(const GameData &game_data) {
	cin >> now_turn;
	if (this->now_turn > game_data.max_turn)
		return false;

	int8_t tile;
	for (int8_t h = 0; h < game_data.height; ++h) {
		for (int8_t w = 0; w < game_data.width; ++w) {
			cin >> tile;
			tile_data[h] -= (tile_data[h] >> (w*2)) & kTwo;
			tile_data[h] += tile<<(w*2);
		}
	}

	int8_t hpos, wpos;
	for (int8_t team_id = 0; team_id < 2; ++team_id) {
		for (int8_t agent_id = 0; agent_id < game_data.agent_num; ++agent_id) {
			cin >> wpos >> hpos;
			agents_position[team_id][agent_id] = (hpos << 5) + wpos;
		}
	}

	return true;
}

const Position TurnData::GetNowPosition(const int8_t &team_id, const int8_t &agent_id) {
	const int16_t &num = agents_position[team_id][agent_id];
	const int hpos = (num >> 5)&kFive;
	const int wpos = num&kFive;
	return Position(hpos, wpos);
}