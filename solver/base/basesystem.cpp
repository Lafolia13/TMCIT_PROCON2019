// GameDataの入力フォーマットは
// 	ターン
// 	縦 横
// 	エージェント数
// 	盤面情報

// TurnDataの入力フォーマットは
// 	現在のターン(保留、現状は無視)
// 	味方エージェントの位置
// 	敵エージェントの位置
// 	盤面情報

#include "../base/basesystem.h"

#include <iostream>

namespace base {

bool GameData::Input() {
	std::cin >> this->max_turn_;
	std::cin >> this->height_ >> width_;
	std::cin >> this->agent_num_;

	this->field_data_.resize(this->height_,
							 std::vector<int32_t>(this->width_));

	for (int32_t h = 0; h < this->height_; ++h) {
		for (int32_t w = 0; w < this->width_; ++w) {
			std::cin >> this->field_data_[h][w];
		}
	}

	return true;
}

bool TurnData::Input(const GameData &game_data) {
	// std::cin >> this->now_turn_;
	// if (this->now_turn_ > game_data.max_turn_)
	// 	return false;

	for (int32_t team_id = 0; team_id < 2; ++team_id) {
		for (int32_t agent_id = 0; agent_id < game_data.agent_num_; ++agent_id) {
			std::cin >> this->agents_position_[team_id][agent_id].h_ >>
						this->agents_position_[team_id][agent_id].w_;
		}
	}

	for (int32_t h = 0; h < game_data.height_; ++h) {
		for (int32_t w = 0; w < game_data.width_; ++w) {
			std::cin >> this->tile_data_[h][w];
		}
	}

	return true;
}

}