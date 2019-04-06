#ifndef BASESYSTEM_H_
#define BASESYSTEM_H_

#include <cstdint>
#include <vector>
#include <array>
#include <tuple>

namespace basesystem {

class GameData {
public :
	int32_t max_turn_;
	int32_t height_;
	int32_t width_;
	int32_t agent_num_;
	std::vector<std::vector<int32_t> > field_data_;  // 盤面の得点データ

	GameData() {};
	bool InputGameData();

protected :
private :
};

class AgentPosition {
public :
	int32_t h_;
	int32_t w_;

	AgentPosition() {};
	AgentPosition(int32_t h_, int32_t w_) :
		h_(h_), w_(w_) {};
	inline bool operator==(const AgentPosition&);
	inline bool operator!=(const AgentPosition&);
	inline AgentPosition operator+(const AgentPosition&);
	inline AgentPosition operator-(const AgentPosition&);
	inline AgentPosition operator*(const AgentPosition&);

protected :
private :
};

class TurnData {
public :
	std::int32_t now_turn_;
	std::array<std::vector<AgentPosition>, 2> agents_position_;
	std::vector<std::vector<int32_t> > tile_data_;

	TurnData() {};
	bool MakeTurnData(const GameData&);
	bool InputTurnData(const GameData&);

protected:
private:
	static constexpr int32_t my_agent_ = 0;
	static constexpr int32_t rival_agent_ = 1;
};

};

#endif