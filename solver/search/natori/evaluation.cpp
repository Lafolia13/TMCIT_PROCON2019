#include "../../search/natori/evaluation.h"

#include <queue>

namespace search {
namespace natori {

std::vector<int32_t> evaluation_product;

// MaxScore_hogehuga()は各評価の最大値を決めつけるやつです

// TilePointDefference
// 差分なのでエージェントの数だけ上から足してきます
// 最高値は16*8 = 128
int32_t MaxScore_TPD(const base::GameData &game_data) {
	int32_t ret = 0;
	std::priority_queue<int32_t> que;
	for (auto hline : game_data.field_data_)
		for (auto masu_point : hline)
			que.push(masu_point);

	for (int32_t i = 0; i < game_data.agent_num_; ++i){
		ret += que.top();
		que.pop();
	}

	return ret;
}

// AreaPointDefference
// これは決めつけで、領域ポイントは精々最高値x8程度に収まると思ってます
// 最高値は16*8 = 128
int32_t MaxScore_APD(const base::GameData &game_data) {
	int32_t ret = 0;
	std::priority_queue<int32_t> que;
	for (int32_t h = 1; h < game_data.height_-1; ++h)
		for (int32_t w = 1; w < game_data.width_-1; ++w)
			que.push(std::abs(game_data.field_data_[h][w]));

	ret = que.top()*8;

	return ret;
}

// EraseRivalTile
// TilePointDefferenceと全く同一です
// 領域の減少も加味するのでちょっとだけ増やしてます
// 最高値は16*(8+4) = 192
int32_t MaxScore_ERT(const base::GameData &game_data) {
	int32_t ret = 0;
	std::priority_queue<int32_t> que;
	for (auto hline : game_data.field_data_)
		for (auto masu_point : hline)
			que.push(masu_point);

	for (int32_t i = 0; i < game_data.agent_num_ + 4; ++i){
		ret += que.top();
		que.pop();
	}

	return ret;
}

// 各評価のmax_scoreを得ます
// 評価の追加がめんどくさくなるので参照はkEvaluationNumを使ってください
std::vector<int32_t> GetMaxScores(const base::GameData &game_data) {
	std::vector<int32_t> ret(kEvaluationsNum, 1);

	ret[kTilePointDefference] = MaxScore_TPD(game_data);
	ret[kAreaPointDefference] = MaxScore_APD(game_data);
	ret[kEraseRivalTile] = MaxScore_ERT(game_data);

	return ret;
}

// evaluation.hに書いてある感じで正規化したスコアを出します
// バイアスもここで掛けます
// TPD, APD, ERTのみで結構大きくなることがわかったので、10^5くらいに抑える形でやればいいかなと思います
void MakeEvaluationCorrection(const base::GameData &game_data) {
	int32_t max_scores_product = 1;
	evaluation_product.resize(kEvaluationsNum);

	std::vector<int32_t> max_scores = GetMaxScores(game_data);
	for (auto max_score : max_scores)
		max_scores_product *= max_score;

	if (std::log10(max_scores_product) > 5){
		max_scores_product /=
			std::pow(10, (int)std::log10(max_scores_product) - 5);
	}

	// GetMaxScores()で各値と評価関数の整合とってるのでループを回してます
	for (int32_t evaluation_id = 0; evaluation_id < kEvaluationsNum;
			++evaluation_id) {
		evaluation_product[evaluation_id] =
			max_scores_product / max_scores[evaluation_id] *
			kEvaluationsBias[evaluation_id];
	}

	return;
}

// タイルポイントの差分をとります
int32_t GetTileDefference(const calculation::Point &before_ally_point,
						  const calculation::Point &now_ally_point) {
	int32_t ret =
		now_ally_point.tile_point_ - before_ally_point.tile_point_;

	ret *= evaluation_product[kTilePointDefference];

	return ret;
}

// 領域ポイントの差分をとります
int GetAreaDefference(const calculation::Point &before_ally_point,
					  const calculation::Point &now_ally_point) {
	int32_t ret =
		now_ally_point.area_point_ - before_ally_point.area_point_;

	ret *= evaluation_product[kAreaPointDefference];

	return ret;
}

// 敵のポイントの差分を取ります
int32_t GetEraseRivalTile(const calculation::Point &before_rival_point,
						  const calculation::Point &now_rival_point) {
	int32_t ret = before_rival_point.all_point_ - now_rival_point.all_point_;

	ret *= evaluation_product[kEraseRivalTile];

	return ret;
}

// 一番始めの評価を大きくするやつです。この評価だけ例外
int32_t GetFirstEvaluation(const int32_t &evaluation) {
	return evaluation * kEvaluationsBias[kFirstEvaluation];
}

}
}