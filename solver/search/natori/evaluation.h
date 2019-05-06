// まともな評価値を書きます

// ・一番はじめの利得を重要視したいです
// ・自分のタイルに移動(無駄な動き)は低評価したいです
// ・タイルポイントと領域ポイントの比にバイアスを掛けたいです
// ・エージェントの近傍にある正の点数の合計を評価したいです(現在位置(移動位置)からの発展性も大事なので)
// ・負のマスにはなるたけ歩きたくないです
// ・敵の領域を消していたら高評価をしたいです

// 正規化の話がしたいやつ
// 各関数毎にscore/maxScoreがしたいが、整数型でやりたい気持ちで、割り算もしたくない気持ち あと単純にscore<maxScoreなので割ったら0
// 実数空間でallScore = a/max_a*bias_a + b/max_b*bias_b + c/max_c*bias_c + ・・・
// max_nを掛けて
// allScore*(max_a*max_b*max_c*・・・) = a*bias_a*(max_b*max_c*・・・) + b*bias_b*(max_a*max_b*・・・) + c*bias_c*() + ・・・
// というわけですべての除算が消え去ったので整数のお話ができます
// maxScoreが一番大きくなりそうなのは得点部分で、これの最大が20*20*16=6400なんですね int_32なので評価量が肥大化しなければ余裕そう(?)
// あとbiasに関しても整数で扱いたい気持ちなので微妙な比にするともれなく値が大きくなるのでつらいね 積が10^5くらいまでなら大丈夫じゃないかなたぶん

// 流石に大きくなりすぎてよくないのでmax_a*max_b*...をある程度右にシフトすることにします
// 実際問題一定以上の大きさなら何掛けても問題なさそう 各max_nで特徴量は得られるので

#ifndef EVALUATION_H
#define EVALUATION_H

#include "../../base/basesystem.h"
#include "../../calculation/calculation.h"
#include "../../action/changeaction.h"

namespace search {
namespace natori {

enum EvaluationList {
	kTilePointDefference = 0,
	kAreaPointDefference = 1,
	kEraseRivalTile		 = 2,
	kFirstEvaluation	 = 3,
	kEvaluationsNum		 = 4,
};

constexpr std::array<int32_t, kEvaluationsNum> kEvaluationsBias = {
																   3,		// TilePointDefference
																   1,		// AreaPointDefference
																   1,		// EraseRivalTile
																   1		// FirstEvaluation
																   };

std::vector<int32_t> GetMaxScores(const base::GameData&);

void MakeEvaluationCorrection(const base::GameData&);

int32_t GetTileDefference(const calculation::Point &before_ally_point,
						  const calculation::Point &now_ally_point);

int32_t GetAreaDefference(const calculation::Point &before_ally_point,
						  const calculation::Point &now_ally_point);

int32_t GetEraseRivalTile(const calculation::Point &before_rival_point,
						  const calculation::Point &now_rival_point);

int32_t GetFirstEvaluation(const int32_t&);

}
}

#endif