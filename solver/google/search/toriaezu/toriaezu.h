#ifndef TORIAEZU_H
#define TORIAEZU_H

#include "../../base/base_system.h"
#include "../../action/change_action.h"
#include "../../calculation/calculation.h"

namespace toriaezu{

struct Node {
	TurnData turn_data = {};
	double evaluation = {};
	int_fast32_t key = {};
	array<Move, 8> fast_move = {};

	Node() {};
	Node (const TurnData &turn_data, const double evaluation) :
		turn_data(turn_data),
		evaluation(evaluation)
	{};

	void GetKey();
};

}

#endif