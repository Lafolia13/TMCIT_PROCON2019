#include "../base/base_system.h"

#include <iostream>

bool GameData::Input() {
	cin >> max_turn;
	cin >> width >> height;
	cin >> agent_num;

	for (int8_t &&h = 0; h < height; ++h) {
		for (int8_t &&w = 0; w < width; ++w) {
			cin >> field_data[h][w];
		}
	}

	return true;
}

const bool GameData::IntoField(const int16_t &pos, const int8_t &way) const {
	const int8_t &&nh = ((pos >> 5)&kFive) + kNextToEit[way].h;
	const int8_t &&nw = (pos&kFive) + kNextToEit[way].w;
	return (0 <= nh && nh < height && 0 <= nw && nw < width);
}

const bool GameData::IntoField(const Position &pos) const {
	return (0 <= pos.h && pos.h < height && 0 <= pos.w && pos.w < width);
}

bool TurnData::Input(const GameData &game_data) {
	cin >> now_turn;
	if (this->now_turn > game_data.max_turn)
		return false;

	int8_t &&tile = 0;
	for (int8_t &&h = 0; h < game_data.height; ++h) {
		for (int8_t &&w = 0; w < game_data.width; ++w) {
			cin >> tile;
			tile_data[h] -= (tile_data[h] >> (w*2)) & kTwo;
			tile_data[h] += tile<<(w*2);
		}
	}

	int8_t &&hpos = 0, &&wpos = 0;
	for (int8_t &&team_id = 0; team_id < 2; ++team_id) {
		for (int8_t agent_id = 0; agent_id < game_data.agent_num; ++agent_id) {
			cin >> wpos >> hpos;
			agents_position[team_id][agent_id] = (hpos << 5) + wpos;
		}
	}

	return true;
}

const int8_t TurnData::GetTileState(const int8_t &h, const int8_t &w) const {
	return (tile_data[h]>>(w*2))&kTwo;
}

const int8_t TurnData::GetTileState(const Position &pos) const {
	return GetTileState(pos.h, pos.w);
}

const Position TurnData::GetPosition(const int8_t &team_id, const int8_t &agent_id) const {
	const int16_t &&num = static_cast<int16_t&&>(agents_position[team_id][agent_id]);
	const int8_t &&hpos = (num >> 5)&kFive;
	const int8_t &&wpos = num&kFive;

	return Position(hpos, wpos);
}