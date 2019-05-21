#ifndef BASE_SYSTEM_H
#define BASE_SYSTEM_H

#include <cstdint>
#include <array>

using namespace std;

enum TileColors {
	kAlly = 0,
	kRival = 1,
	kBrank = 2
};

enum BitAnds {
	kOne = 1,
	kTwo = 3,
	kThree = 7,
	kFour = 15,
	kFive = 31
};

struct Position {
	int8_t h = {};
	int8_t w = {};

	constexpr Position() {};
	constexpr Position(const &h, const &w) :
		h(h),
		w(w)
	{};
};

constexpr array<Position, 4> kNextToFou = {
											Position(-1,0),
											Position(0,-1),
											Position(0,1),
											Position(1,0)
										  };

constexpr array<Position, 8> kNextToEit = {
											Position(-1,-1),
											Position(-1,0),
											Position(-1,1),
											Position(0,-1),
											Position(0,1),
											Position(1,-1),
											Position(1,0),
											Position(1,1)
										  };

struct GameData {
	int32_t max_turn = {};
	int32_t height = {};
	int32_t width = {};
	int32_t agent_num = {};
	array<array<int32_t, 20>, 20> field_data = {};

	constexpr GameData() {};

	bool Input();
	bool IntoField(const int16_t&, const int8_t&);
};

struct TurnData {
	int8_t now_turn = {};
	int8_t agent_num = {};
	int32_t tile_point_ = {};
	int32_t area_point_ = {};
	array<array<int16_t, 8>, 2> agents_position = {};
	array<int64_t, 20> tile_data = {};
	array<int32_t, 20> is_area = {};					// あるマスが領域であるか

	constexpr TurnData() {};

	bool Input(const GameData&);
	const Position GetNowPosition(const int8_t&, const int8_t&);
	void CalculationTilePoint(const TurnData&, const bool&);
	void CalculationAreaPoint(const bool&);
};



#endif