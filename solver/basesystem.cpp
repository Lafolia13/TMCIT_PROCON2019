#include "basesystem.h"

namespace basesystem {

void GameData::InputGameData() {

	return;
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

void TurnData::InputTurnData() {

}

}