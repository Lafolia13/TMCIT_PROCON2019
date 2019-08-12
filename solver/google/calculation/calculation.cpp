#include "../calculation/calculation.h"

bool GameData::IntoField(const Position &pos) const {
	return (0 <= pos.h && pos.h < height && 0 <= pos.w && pos.w < width);
}

bool GameData::IsFieldEdge(const Position &pos) const {
	return pos.h == 0 || pos.w == 0 ||
		   pos.h == height - 1 || pos.w == width - 1;
}

void TurnData::CalculationAllTilePoint(const GameData &game_data) {
	tile_point[kAlly] = 0;
	tile_point[kRival] = 0;

	for (int_fast32_t &&h = 0; h < game_data.height; ++h) {
		for (int_fast32_t &&w = 0; w < game_data.width; ++w) {
			const int_fast32_t &tile_state = GetTileState(h, w);
			if (tile_state != kBrank)
				tile_point[tile_state] += game_data.field_data[h][w];
		}
	}

	return;
}

int_fast32_t CheckSurrounded(const GameData &game_data,
							 const int_fast32_t &team_id,
							 const Position &start,
							 TurnData *p_turn_data,
							 array<array<int_fast32_t, 20>, 20> &checked) {
	static array<Position, 400> que = {};
	static int que_size = 0;

	TurnData &turn_data = *p_turn_data;

	int_fast32_t ret = 0;

	que_size = 0;
	que[que_size++] = start;
	static Position next;
	for (int_fast32_t &&i = 0; i < que_size; ++i) {
		const Position &now = que[i];
		if (game_data.IsFieldEdge(now) == true) {
			ret = 0;
			break;
		}

		ret += abs(game_data.GetTilePoint(now));

		for (const auto next_to : kNextToFour) {
			next = now + next_to;
			if (game_data.IntoField(next) == false) continue;
			if (turn_data.GetTileState(next) == team_id) continue;
			if (checked[next.h][next.w] == 1) continue;

			checked[next.h][next.w] = 1;
			que[que_size++] = next;
		}
	}

	return ret;
}

void TurnData::CalculationAreaPoint(const GameData &game_data,
									const int_fast32_t &team_id) {
	static array<array<int_fast32_t, 20>, 20> checked = {};

	area_point[team_id] = 0;

	checked = {};
	for (int_fast32_t &&h = 1; h < game_data.height - 1; ++h) {
		for (int_fast32_t &&w = 1; w < game_data.width - 1; ++w) {
			if (checked[h][w] == 1) continue;
			checked[h][w] = 1;
			if (GetTileState(h, w) != team_id &&
				GetTileState(h-1, w) == team_id &&
				GetTileState(h, w-1) == team_id)
				area_point[team_id] +=
						CheckSurrounded(game_data, team_id, Position(h, w),
										this, checked);
		}
	}
}

void TurnData::CalculationAllAreaPoint(const GameData &game_data) {
	CalculationAreaPoint(game_data, kAlly);
	CalculationAreaPoint(game_data, kRival);

	return;
}