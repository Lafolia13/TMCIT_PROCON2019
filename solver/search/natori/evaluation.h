// まともな評価値を書きます

// ・一番はじめの利得を重要視したいです
// ・自分のタイルに移動(無駄な動き)は低評価したいです
// ・タイルポイントと領域ポイントの比にバイアスを掛けたいです
// ・エージェントの近傍にある正の点数の合計を評価したいです(現在位置(移動位置)からの発展性も大事なので)
// ・負のマスにはなるたけ歩きたくないです

#ifndef EVALUATION_H
#define EVALUATION_H

#include "../../base/basesystem.h"
#include "../../calculation/calculation.h"
#include "../../action/changeaction.h"

namespace search {
namespace natori {

int32_t GetPointDefference(const base::GameData&, const base::TurnData&);

}
}

#endif