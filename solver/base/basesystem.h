// 盤面情報は[縦(height、あるいはh)][横(width、あるいはw)]で統一されています
// すべてのにおいて対応させてください
// 盤面や位置に関する変数について、x,yは禁止とします

#ifndef BASESYSTEM_H_
#define BASESYSTEM_H_

#include <cstdint>
#include <vector>

namespace base {

// タイルの種類
enum TileColors {
	kAlly = 0,
	kRival = 1,
	kBrank = 2,
};

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
	int32_t h_ = 0;
	int32_t w_ = 0;

	Position() {};
	constexpr Position(const int32_t &h, const int32_t &w) :
		h_(h), w_(w) {};
	bool operator==(const Position &another) const {
		return this->h_ == another.h_ && this->w_ == another.w_;
	}

	bool operator!=(const Position &another) const {
		return !(this->h_ == another.h_ && this->w_ == another.w_);
	}

	bool operator<(const Position &another) const {
		return this->h_ == another.h_ ?
			   this->w_ < another.w_ :
			   this->h_ < another.h_;
	}

	Position operator+(const Position &another) const {
		return Position(this->h_ + another.h_, this->w_ + another.w_);
	}

	Position operator-(const Position &another) const {
		return Position(this->h_ - another.h_, this->w_ - another.w_);
	}

	Position operator*(const Position &another) const {
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
		tile_data_(game_data.height_,
				   std::vector<int32_t>(game_data.width_,kBrank)),
		stay_agent_(game_data.height_,std::vector<bool>(game_data.width_))
	{};
	bool Input(const GameData&);
	bool IsExistAgent(const Position&);
	Position& GetNowPosition(const int32_t &team_id, const int32_t &agent_id);
	const Position& GetNowPosition(const int32_t &team_id, const int32_t &agent_id) const;
	int32_t& GetTileData(const Position&);
	const int32_t& GetTileData(const Position&) const;

protected :
private :
};

// 引数のPositionが盤面内を指しているかを示す
bool IntoField(const GameData&, const Position&);

// チェビシェフ距離を返す
int32_t Distance(const Position&, const Position&);

};

#endif