// 盤面情報は[縦(height、あるいはh)][横(width、あるいはw)]で統一されています
// すべてのにおいて対応させてください
// 盤面や位置に関する変数について、x,yは禁止とします

#ifndef BASESYSTEM_H_
#define BASESYSTEM_H_

#include <cstdint>
#include <vector>

namespace base {

// タイルの種類
constexpr int32_t my_team = 0;
constexpr int32_t rival_team = 1;
constexpr int32_t brank = 2;

class GameData {
public :
	int32_t max_turn_;
	int32_t height_;
	int32_t width_;
	int32_t agent_num_;
	std::vector<std::vector<int32_t> > field_data_;  // 盤面の得点データ

	GameData() {};
	bool Input();

protected :
private :
};

class Position {
public :
	int32_t h_;
	int32_t w_;

	Position() {};
	constexpr Position(const int32_t &h_, const int32_t &w_) :
		h_(h_), w_(w_) {};
	inline bool operator==(const Position &another) {
		return this->h_ == another.h_ && this->w_ == another.w_;
	}

	inline bool operator!=(const Position &another) {
		return !(this->h_ == another.h_ && this->w_ == another.w_);
	}

	inline Position operator+(const Position &another) {
		return Position(this->h_ + another.h_, this->w_ + another.w_);
	}

	inline Position operator-(const Position &another) {
		return Position(this->h_ - another.h_, this->w_ - another.w_);
	}

	inline Position operator*(const Position &another) {
		return Position(this->h_ * another.h_, this->w_ * another.w_);
	}

protected :
private :
};

// 新しいオブジェクトを生成するときは、生成済みのオブジェクトをコピーするかコンストラクタを使用してください
class TurnData {
public :
	std::int32_t now_turn_;
	std::vector<std::vector<Position> > agents_position_;
	std::vector<std::vector<int32_t> > tile_data_;
	std::vector<std::vector<bool> > stay_agent_; // あるマスにエージェントがいるかを示す

	TurnData() {};
	TurnData(const GameData &game_data) :
		agents_position_(2, std::vector<Position>(game_data.agent_num_)),
		tile_data_(game_data.height_, std::vector<int32_t>(game_data.width_)),
		stay_agent_(game_data.height_, std::vector<bool>(game_data.width_))
	{};
	bool Input(const GameData&);

protected:
private:
};

};

#endif