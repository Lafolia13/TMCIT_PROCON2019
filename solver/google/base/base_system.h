#ifndef BASE_SYSTEM_H
#define BASE_SYSTEM_H

#include <cstdint>
#include <array>

using namespace std;

struct Position {
	int8_t h = {};
	int8_t w = {};

	constexpr Position() {};
	constexpr Position(const &h, const &w) :
		h(h),
		w(w)
	{};

	Position operator+(const Position &another) const {
		return Position(h + another.h, w + another.w);
	}
	bool operator==(const Position &another) const {
		return h == another.h && w == another.w;
	}
};

struct GameData {
	int32_t max_turn = {};
	int32_t height = {};
	int32_t width = {};
	int32_t agent_num = {};
	array<array<int8_t, 20>, 20> field_data = {};

	constexpr GameData() {};

	bool Input();
	const bool IntoField(const int16_t&, const int8_t&) const;
	const bool IntoField(const Position&) const;
};

struct TurnData {
	int8_t now_turn = {};
	int8_t agent_num = {};
	int32_t tile_point = {};
	int32_t area_point = {};
	array<array<int16_t, 8>, 2> agents_position = { 9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999,
													9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999};
	array<int64_t, 20> tile_data = {};
	array<int32_t, 20> is_area = {};					// あるマスが領域であるか

	constexpr TurnData() {};

	bool Input(const GameData&);
	const int8_t GetTileState(int8_t &h, int8_t &w) const;
	const int8_t GetTileState(const Position&) const;
	const Position GetPosition(const int8_t&, const int8_t&) const;
	void CalculationAllTilePoint(const GameData&, const int8_t&);
	void CalculationTilePoint(const GameData&, const TurnData&, const int8_t&);
	void CalculationAllAreaPoint(const GameData&, const int8_t&);
	void CalculationAreaPoint(const GameData&, const int8_t&);
};


enum TileColors {
	kAlly = 0,
	kRival = 1,
	kBrank = 2
};

// nビットの値を取る用。 target&kNumで下位nビットの値を取れます
enum BitAnds {
	kOne = (1<<1) - 1,
	kTwo = (1<<2) - 1,
	kThree = (1<<3) - 1,
	kFour = (1<<4) - 1,
	kFive = (1<<5) - 1
};

// 四近傍
constexpr array<Position, 4> kNextToFour = {
											Position(-1,0),
											Position(0,-1),
											Position(0,1),
											Position(1,0)
										  };

// 八近傍
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

// 九近傍
constexpr array<Position, 9> kNextToNine = {
											Position(-1,-1),
											Position(-1,0),
											Position(-1,1),
											Position(0,-1),
											Position(0,0),
											Position(0,1),
											Position(1,-1),
											Position(1,0),
											Position(1,1)
										  };

#endif