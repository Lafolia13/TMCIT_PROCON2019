#ifndef TORIAEZU_H
#define TORIAEZU_H

#include "../../base/base_system.cpp"
#include "../../action/change_action.cpp"
#include "../../calculation/calculation.cpp"

struct Node {
	TurnData turn_data = {};
	double evaluation = {};
	int_fast32_t key;
	array<Move, 8> fast_move;

	Node() {};
	Node (const TurnData &turn_data, const double evaluation, int_fast32_t key) :
		turn_data(turn_data),
		evaluation(evaluation),
		key(key)
	{};
};

#endif