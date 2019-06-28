#ifndef BASE_SYSTEM_H
#define BASE_SYSTEM_H

#include <cstdint>
#include <array>

using namespace std;

struct Position {
	int_fast32_t h = {};
	int_fast32_t w = {};

	constexpr Position() {};
	constexpr Position(const int_fast32_t &h, const int_fast32_t &w) :
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
	int_fast32_t max_turn = {};
	int_fast32_t height = {};
	int_fast32_t width = {};
	int_fast32_t agent_num = {};
	array<array<int_fast32_t, 20>, 20> field_data = {};

	constexpr GameData() noexcept {};

	bool Input();
	bool IntoField(const Position&);
};

struct TurnData {
	int_fast32_t now_turn = {};
	int_fast32_t agent_num = {};
	int_fast32_t tile_point = {};
	int_fast32_t area_point = {};
	array<array<Position, 8>, 2> agents_position = {};
	array<array<int_fast32_t, 20>, 20> tile_data = {};
	array<array<int_fast32_t, 20>, 20> is_area = {};					// あるマスが領域であるか

	constexpr TurnData() {};

	bool Input(const GameData&);
	int_fast32_t& GetTileState(const int_fast32_t &h, const int_fast32_t &w);
	int_fast32_t& GetTileState(const Position&);
	Position& GetPosition(const int_fast32_t&, const int_fast32_t&);
	void CalculationTilePoint(const GameData&, const TurnData&, const int_fast32_t&);
	void CalculationAllTilePoint(const GameData&, const int_fast32_t&);
	void CalculationAreaPoint(const GameData&, const int_fast32_t&);
	void CalculationAllAreaPoint(const GameData&, const int_fast32_t&);
	void Transition(const int_fast32_t&);
};

enum TileColors {
	kAlly = 0,
	kRival = 1,
	kBrank = 2
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