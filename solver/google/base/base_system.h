#ifndef BASE_SYSTEM_H
#define BASE_SYSTEM_H

#include <cstdint>
#include <array>
#include <vector>
#include <bitset>

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
	bool operator>(const Position &right) const {
		return h == h ?
			w > w :
			h > h;
	}
	bool operator<(const Position &right) const {
		return h == h ?
			w < w :
			h < h;
	}
};

struct GameData {
	int_fast32_t max_turn = {};
	int_fast32_t height = {};
	int_fast32_t width = {};
	int_fast32_t agent_num = {};
	array<array<int_fast32_t, 20>, 20> field_data = {};					// 配点情報

	constexpr GameData() {};

	// base_system
	bool Input();
	int_fast32_t GetTilePoint(const Position&) const;

	// calculation
	bool IntoField(const Position&) const;
	bool IsFieldEdge(const Position&) const;
};

struct Move {
	int_fast32_t team_id = {};
	int_fast32_t agent_id = {};
	int_fast32_t direction = {};										// kNextToNine
	int_fast32_t action = {};
	Position target_position = {};

	constexpr Move() {};
	constexpr Move(const int_fast32_t &team_id, const int_fast32_t &agent_id,
				   const int_fast32_t &direction, const int_fast32_t &action) :
		team_id(team_id),
		agent_id(agent_id),
		direction(direction),
		action(action)
	{};
};

struct TurnData {
	int_fast32_t now_turn = {};
	int_fast32_t agent_num = {};
	array<int_fast32_t, 2> tile_point = {};
	array<int_fast32_t, 2> area_point = {};
	array<array<Position, 8>, 2> agents_position = {};
	array<array<int_fast32_t, 20>, 20> tile_data = {};					// TileColors参照
	// array<array<int_fast32_t, 20>, 20> is_area = {};					// あるマスが領域であるか
	bitset<400> agent_exist = {};

	constexpr TurnData() {};

	// base_system
	void reset();
	bool Input(const GameData&);
	int_fast32_t& GetTileState(const int_fast32_t &h, const int_fast32_t &w);
	int_fast32_t& GetTileState(const Position&);

	// change_action
	Position& GetPosition(const int_fast32_t&, const int_fast32_t&);
	void Transition(const GameData&, const vector<Move>&);

	// calculation
	void CalculationAllTilePoint(const GameData&);
	void CalculationAreaPoint(const GameData&, const int_fast32_t&);
	void CalculationAllAreaPoint(const GameData&);
};

int_fast32_t GetBitsetNumber(const int_fast32_t&, const int_fast32_t&);
int_fast32_t GetBitsetNumber(const Position&);

enum TileColors {
	kAlly = 0,
	kRival = 1,
	kBrank = 2
};

enum AgentAction {
	kWalk = 0,
	kErase = 1
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