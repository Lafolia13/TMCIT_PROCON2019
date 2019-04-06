#include "basesystem.h"

#include <iostream>

namespace basesystem {

bool GameData::InputGameData() {
	std::cin >> this->max_turn_;
	std::cin >> this->width_ >> height_;
	std::cin >> this->agent_num_;

	this->field_data_.resize(this->height_, std::vector<int32_t>(this->width_));
	for (int32_t h = 0; h < this->height_; ++h) {
		for (int32_t w = 0; w < this->width_; ++w) {
			std::cin >> this->field_data_[h][w];
		}
	}

	return true;
}

inline bool AgentPosition::operator==(const AgentPosition &another) {
	return this->h_ == another.h_ && this->w_ == another.w_;
}

inline bool AgentPosition::operator!=(const AgentPosition &another) {
	return !(this->h_ == another.h_ && this->w_ == another.w_);
}

inline AgentPosition AgentPosition::operator+(const AgentPosition &another) {
	return AgentPosition(this->h_ + another.h_, this->w_ + another.w_);
}

inline AgentPosition AgentPosition::operator-(const AgentPosition &another) {
	return AgentPosition(this->h_ - another.h_, this->w_ - another.w_);
}

inline AgentPosition AgentPosition::operator*(const AgentPosition &another) {
	return AgentPosition(this->h_ * another.h_, this->w_ * another.w_);
}

bool TurnData::MakeTurnData(const GameData &game_data) {
	for (int32_t i = 0; i < 2; ++i)
		this->agents_position_[i].resize(game_data.agent_num_);

	this->tile_data_.resize(game_data.height_, std::vector<int32_t>(game_data.width_));

	return true;
}

bool TurnData::InputTurnData(const GameData &game_data) {
	std::cin >> this->now_turn_;
	if (this->now_turn_ > game_data.max_turn_)
		return false;

	// はじめのターンは初期化
	if (this->now_turn_ == 0)
		this->MakeTurnData(game_data);

	for (int32_t i = 0; i < 2; ++i) {
		for (int32_t agent_id = 0; agent_id < game_data.agent_num_; ++agent_id) {
			std::cin >> this->agents_position_[i][agent_id].h_ >> this->agents_position_[i][agent_id].w_;
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