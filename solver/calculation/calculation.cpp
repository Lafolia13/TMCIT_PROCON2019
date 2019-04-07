// 場合によって、namespace内に領域の数とかいろいろ仕込むかもしれません

#include "../calculation/calculation.h"

#include <array>
#include <queue>
#include <algorithm>

namespace calculation {

// あるマスが既に調べた領域であるかを示す
std::array<std::array<bool, 20>, 20> kCheckedField;
// あるマスの四近傍を示す
constexpr std::array<base::Position, 4> kNextTo = {base::Position(1,0),
												   base::Position(0,1),
												   base::Position(-1,0),
												   base::Position(0,-1)};

inline bool IntoField(const base::Position &check_position,
					  const base::GameData &game_data) {
	return (0 <= check_position.h_ && check_position.h_ < game_data.height_ &&
			0 <= check_position.w_ && check_position.w_ < game_data.width_);
}

// BFSでbase::turn_dataで囲まれている、あるいは場外に隣接する領域の得点を返します
// 場外に隣接した場合にretを負とし、returnの下限を0とすることで同エリア判定及びフラグ立てをスムーズに行わせます
int32_t FindSurroundedMasu(const base::Position &start_position,
					   const base::GameData &game_data,
					   const base::TurnData &turn_data,
					   const int32_t &team_id) {
	int32_t ret = 0;
	static std::queue<base::Position> kSameArea;

	kCheckedField[start_position.h_][start_position.w_] = true;
	kSameArea.push(start_position);

	base::Position next_position;
	while (kSameArea.size() > 0) {
		base::Position now_position = kSameArea.front();
		kSameArea.pop();

		if (now_position.h_ == 0 || now_position.h_ == game_data.height_ - 1 &&
			now_position.w_ == 0 || now_position.w_ == game_data.width_ - 1)
			ret = -99999;

		ret += abs(game_data.field_data_[now_position.h_][now_position.w_]);

		for (auto &neighborhood : kNextTo) {
			next_position = now_position + neighborhood;
			if (!IntoField(next_position, game_data) ||
				kCheckedField[next_position.h_] [next_position.w_] == true ||
				turn_data.tile_data_[next_position.h_]
				[next_position.w_] == team_id) continue;

			kSameArea.push(next_position);
			kCheckedField[next_position.h_][next_position.w_] = true;
		}
	}

	return std::max(0, ret);
}

Point CalculationOnePoint(const base::GameData &game_data,
						  const base::TurnData &turn_data,
						  const int32_t &team_id) {
	for (auto &change_array : kCheckedField)
		change_array.fill(false);

	Point ret;
	for (int32_t h = 0; h < game_data.height_; ++h) {
		for (int32_t w = 0; w < game_data.width_; ++w) {
			if (turn_data.tile_data_[h][w] == team_id) {
				ret.tile_point_ += game_data.field_data_[h][w];
			} else if (turn_data.tile_data_[h][w] == base::kBrank &&
					   kCheckedField[h][w] == false) {
				ret.area_point_ += FindSurroundedMasu(base::Position(h,w),
												  game_data, turn_data,
												  team_id);
			}
		}
	}

	ret.all_point_ = ret.tile_point_ + ret.area_point_;
	return ret;
}

inline std::pair<Point, Point> CalculationAllPoint(
		const base::GameData &game_data,
		const base::TurnData &turn_data) {
	return {CalculationOnePoint(game_data, turn_data, base::kAllyTeam),
			CalculationOnePoint(game_data, turn_data, base::kRivalTeam)};
}

}