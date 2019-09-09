#ifndef SPLIT_AGENT_H
#define SPLIT_AGENT_H

#include "../../base/base_system.h"
#include "../../action/change_action.h"
#include "../../calculation/calculation.h"

namespace split_agent {

struct Node {
	TurnData turn_data = {};
	double evaluation = {};
	int_fast32_t key = {};
	array<Move, 8> first_move = {};

	Node() {};
	Node (const TurnData &turn_data, const double evaluation) :
		turn_data(turn_data),
		evaluation(evaluation)
	{};

	void GetKey(const int_fast32_t&);

	bool operator>(const Node &right) const {
		return evaluation > right.evaluation;
	};
};

array<Move, 8> BeamSearch(const GameData&, const TurnData&,
						  const int_fast32_t&);

const array<vector<int_fast32_t>, 9> split_table = {
	vector<int_fast32_t>{},
	vector<int_fast32_t>{},
	vector<int_fast32_t>{2},
	vector<int_fast32_t>{3},
	vector<int_fast32_t>{2, 2},
	vector<int_fast32_t>{2, 3},
	vector<int_fast32_t>{3, 3},
	vector<int_fast32_t>{2, 2, 3},
	vector<int_fast32_t>{2, 3, 3}
};

}

#endif