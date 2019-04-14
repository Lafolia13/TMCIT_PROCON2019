#ifndef CALCULATION_H_
#define CALCULATION_H_

#include "../base/basesystem.h"

#include <cstdint>
#include <array>
#include <vector>
#include <utility>

namespace calculation {

// あるマスの四近傍を示す
constexpr std::array<base::Position, 4> kNextToFour = {base::Position(1,0),
													   base::Position(0,1),
													   base::Position(-1,0),
													   base::Position(0,-1)};
class Point {
public :
	int32_t all_point_ = 0;
	int32_t tile_point_ = 0;
	int32_t area_point_ = 0;

	Point() {};

protected :
private :
};

// Positionから開始して、囲まれているマスを探索。領域ポイントを返す
int32_t FindSurroundedMasu(const base::GameData&, const base::TurnData&,
						   const base::Position&, const int32_t &team_id,
					   	   std::vector<std::vector<bool> >&);

// base::team_idのチームの得点を返す
Point CalculationOnePoint(const base::GameData&, const base::TurnData&,
						  const int32_t &team_id);

// 両チームの得点を返す
std::pair<Point, Point> CalculationAllPoint(const base::GameData&,
											const base::TurnData&);

}

#endif