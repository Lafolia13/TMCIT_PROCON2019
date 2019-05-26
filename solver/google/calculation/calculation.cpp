#include "../base/base_system.h"

void TurnData::CalculationAllTilePoint(const GameData &game_data,
									   const int8_t &team_id) {
	tile_point = 0;
	for (int8_t &&h = 0; h < game_data.height; ++h) {
		for (int8_t &&w = 0; w < game_data.width; ++w) {
			const int8_t &&tile_state = GetTileState(h, w);
			if (tile_state == team_id)
				tile_point += game_data.field_data[h][w];
			else if (tile_state == (team_id^1))		// rival = team_id^1
				tile_point -= game_data.field_data[h][w];
		}
	}

	return;
}

void TurnData::CalculationTilePoint(const GameData &game_data,
									const TurnData &before,
									const int8_t &team_id) {
	for (int8_t &&agent_id = 0; agent_id < agent_num; ++agent_id) {
		const Position &now_pos = GetPosition(team_id, agent_id);
		for (const Position &next_to : kNextToNine) {
			const Position &&check = now_pos + next_to;
			if (!game_data.IntoField(check)) continue;

			const int8_t &&now_tile = GetTileState(check);
			const int8_t &&before_tile = before.GetTileState(check);
			if (now_tile == before_tile) continue;

			if (before_tile == team_id)
				tile_point -= game_data.field_data[check.h][check.w];
			else (before_tile == (team_id^1))
				tile_point += game_data.field_data[check.h][check.w];
		}
	}

	return;
}

void TurnData::CalculationAllAreaPoint(const GameData &game_data,
									   const int8_t &team_id) {
	area_point = 0;
	fill(is_area.begin(), is_area.end(), 0);

}

void TurnData::CalculationAreaPoint(const GameData &game_data,
									const int8_t &team_id) {
	for (int8_t agent_id = 0; agent_id < agent_num; ++agent_id) {
		Position position = GetPosition(team_id, agent_id);
	}
}