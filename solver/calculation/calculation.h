#ifndef CALCULATION_H_
#define CALCULATION_H_

#include "../base/basesystem.cpp"

#include <cstdint>
#include <utility>

namespace calculation {

class Point {
public :
	int32_t all_point_ = 0;
	int32_t tile_point_ = 0;
	int32_t area_point_ = 0;

	Point() {};

protected :
private :
};

// 引数のPositionが盤面内を指しているかを示す
inline bool IntoField(const base::Position&, const base::GameData&);

// 囲まれているマスを探索
int32_t FindSurroundedMasu(const base::Position&, const base::GameData&,
					   const base::TurnData&, const int32_t&);

// base::team_idのチームの得点を返す
Point CalculationOnePoint(const base::GameData&, const base::TurnData&,
						  const int32_t &team_id);

// 両チームの得点を返す
inline std::pair<Point, Point> CalculationAllPoint(const base::GameData&,
											const base::TurnData&);

}

#endif