#ifndef BASESYSTEM_H_
#define BASESYSTEM_H_

#include <cstdint>
#include <vector>
#include <array>
#include <tuple>

namespace basesystem {

// ゲーム自体の情報
class GameData {
public :
	int32_t max_turn_;
	int32_t height_;
	int32_t width_;
	int32_t agent_num_;
	std::vector<std::vector<int32_t> > field_data_;  // 盤面の得点データ

	void InputGameData();

protected :
private :
};

// エージェントの場所
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

// 各ターンの情報
class TurnData {
public :
	std::array<std::vector<int32_t>, 2> agents_position_;
	std::vector<std::vector<int32_t> > tile_data_;  // 盤面のタイルデータ

	void InputTurnData();

protected:
private:
	static constexpr int32_t my_agent_ = 0;
	static constexpr int32_t rival_agent_ = 1;
};

std::tuple<int32_t, int32_t, int32_t, int32_t> CalclationScore(GameData&, TurnData&);



};

#endif