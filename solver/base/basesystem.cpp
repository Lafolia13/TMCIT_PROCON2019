// GameDataの入力フォーマットは
// 	ターン
// 	縦 横
// 	エージェント数
// 	盤面情報

// TurnDataの入力フォーマットは
// 	現在のターン(保留、現状は無視)
// 	盤面情報
// 	味方エージェントの位置
// 	敵エージェントの位置

#include "../base/basesystem.h"

#include <iostream>
#include <cmath>
#include <algorithm>

namespace base {

bool GameData::Input() {
	std::cin >> this->max_turn_;
	std::cin >> this->width_ >> this->height_;
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
	std::cin >> this->now_turn_;
	if (this->now_turn_ > game_data.max_turn_)
		return false;

	for (int32_t h = 0; h < game_data.height_; ++h) {
		for (int32_t w = 0; w < game_data.width_; ++w) {
			std::cin >> this->tile_data_[h][w];
		}
	}

	// オブジェクトを使いまわしている場合はthis->stay_agent_のフラグが残るため
	// フラグを消してから新たにフラグを立てる
	// 一度のループをしないのは、初期状態ですべてのエージェントの位置が(0,0)になるためです
	for (int32_t team_id = 0; team_id < 2; ++team_id) {
		for (int32_t agent_id = 0; agent_id < game_data.agent_num_;
			 ++agent_id) {
			Position &check_position = this->GetNowPosition(team_id, agent_id);
			this->stay_agent_[check_position.h_][check_position.w_] = false;
		}
	}

	for (int32_t team_id = 0; team_id < 2; ++team_id) {
		for (int32_t agent_id = 0; agent_id < game_data.agent_num_;
			 ++agent_id) {
			Position &check_position = this->GetNowPosition(team_id, agent_id);
			std::cin >> check_position.w_ >> check_position.h_;
			this->stay_agent_[check_position.h_][check_position.w_] = true;
		}
	}

	std::cerr << this->now_turn_ << std::endl;
	for (auto x : this->tile_data_) {
		for (auto y : x)
			std::cerr << y << " ";
		std::cerr << std::endl;
	}
	std::cerr << std::endl;
	for (auto x : this->stay_agent_) {
		for (auto y : x)
			std::cerr << y << " ";
		std::cerr << std::endl;
	}
	std::cerr << std::endl;
	for (auto x : this->agents_position_) {
		for (auto y : x)
			std::cerr << y.h_ << " " << y.w_ << "   ";
		std::cerr << std::endl;
	}

	return true;
}

bool TurnData::IsExistAgent(const Position &check_position) {
	return this->stay_agent_[check_position.h_][check_position.w_] == true;
}

Position& TurnData::GetNowPosition(const int32_t &team_id,
								   const int32_t &agent_id) {
	return this->agents_position_[team_id][agent_id];
}
const Position& TurnData::GetNowPosition(const int32_t &team_id,
								   		 const int32_t &agent_id) const{
	return this->agents_position_[team_id][agent_id];
}

int32_t& TurnData::GetTileData(const Position &check_position) {
	return this->tile_data_[check_position.h_][check_position.w_];
}
const int32_t& TurnData::GetTileData(const Position &check_position) const{
	return this->tile_data_[check_position.h_][check_position.w_];
}

bool IntoField(const GameData &game_data, const Position &check_position) {
	return (0 <= check_position.h_ && check_position.h_ < game_data.height_ &&
			0 <= check_position.w_ && check_position.w_ < game_data.width_);
}

int32_t Distance(const Position &pos_1, const Position &pos_2) {
	return std::max(std::abs(pos_1.h_ - pos_2.h_),
		   std::abs(pos_1.w_ - pos_2.w_));
}

}