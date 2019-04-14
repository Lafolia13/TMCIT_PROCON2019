// 場合によって、namespace内に領域の数とかいろいろ仕込むかもしれません

#include "../calculation/calculation.h"

#include <queue>
#include <algorithm>

namespace calculation {

// BFSでbase::turn_dataで囲まれている、あるいは場外に隣接する領域の得点を返します
// 場外に隣接した場合にretを負とし、returnの下限を0とすることで同エリア判定及びフラグ立てをスムーズに行わせます
// 必ずstart_positionは領域となりうるマスすなわち自チームのタイルが置かれていないマスを指定してください
int32_t FindSurroundedMasu(const base::GameData &game_data,
						   const base::TurnData &turn_data,
						   const base::Position &start_position,
						   const int32_t &team_id,
						   std::vector<std::vector<bool> > &checked_field) {
	int32_t ret = 0;
	std::queue<base::Position> same_area;

	checked_field[start_position.h_][start_position.w_] = true;
	same_area.push(start_position);

	base::Position next_position;
	while (same_area.size() > 0) {
		base::Position now_position = same_area.front();
		same_area.pop();

		if (now_position.h_ == 0 || now_position.h_ == game_data.height_ - 1 ||
			now_position.w_ == 0 || now_position.w_ == game_data.width_ - 1)
			ret = -99999;

		ret += abs(game_data.field_data_[now_position.h_][now_position.w_]);

		for (auto &neighborhood : kNextToFour) {
			next_position = now_position + neighborhood;
			const int32_t &target_tile_data =
				turn_data.GetTileData(next_position);
			if (IntoField(game_data, next_position) == false ||
				checked_field[next_position.h_][next_position.w_] == true ||
				target_tile_data == team_id) continue;

			same_area.push(next_position);
			checked_field[next_position.h_][next_position.w_] = true;
		}
	}

	return std::max(0, ret);
}

Point CalculationOnePoint(const base::GameData &game_data,
						  const base::TurnData &turn_data,
						  const int32_t &team_id) {
	// あるマスが既に調べた領域であるかを示す
	std::vector<std::vector<bool> > checked_field(game_data.height_,
												  std::vector<bool>(
												  game_data.width_, false));

	Point ret;
	for (int32_t h = 0; h < game_data.height_; ++h) {
		for (int32_t w = 0; w < game_data.width_; ++w) {
			if (turn_data.tile_data_[h][w] == team_id) {
				ret.tile_point_ += game_data.field_data_[h][w];
			} else if (turn_data.tile_data_[h][w] != team_id &&
					   checked_field[h][w] == false) {
				ret.area_point_ += FindSurroundedMasu(game_data, turn_data,
													  base::Position(h,w),
													  team_id, checked_field);
			}
		}
	}

	ret.all_point_ = ret.tile_point_ + ret.area_point_;
	return ret;
}

std::pair<Point, Point> CalculationAllPoint(
		const base::GameData &game_data,
		const base::TurnData &turn_data) {
	return {CalculationOnePoint(game_data, turn_data, base::kAlly),
			CalculationOnePoint(game_data, turn_data, base::kRival)};
}

}