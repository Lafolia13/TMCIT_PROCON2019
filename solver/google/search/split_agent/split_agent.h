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
	int_fast32_t start_turn = {};
	array<Move, 8> first_move = {};

	Node() {};
	Node (const TurnData &turn_data, const double &evaluation,
		  const int_fast32_t &start_turn) :
		turn_data(turn_data),
		evaluation(evaluation),
		start_turn(start_turn)
	{};

	void GetKey(const int_fast32_t&);

	bool operator>(const Node &right) const {
		return evaluation > right.evaluation;
	};
};

int_fast32_t GetBeamWidth(const GameData&, const int_fast32_t&, const bool&,
						  const bool&);

void EraseAgent(const int_fast32_t&, TurnData&);

void ReduceDirection(const GameData&, const TurnData&, vector<vector<Move>>&);

vector<array<Move, 8>> BeamSearch(const GameData&, const TurnData&,
								  const int_fast32_t&, const int_fast32_t&,
								  const bool&);

array<array<pair<Position, int_fast32_t>, 8>, 2> GetAgentsPositionWidthID(
		const GameData&, const TurnData&);

vector<TurnData> GetSplitTurnData(
		const GameData&, const TurnData&,
		const array<array<pair<Position, int_fast32_t>, 8>, 2>&);

array<vector<vector<int_fast32_t>>, 2> GetTruthIndex(
		const GameData &game_data, const vector<TurnData>&,
		const array<array<pair<Position, int_fast32_t>, 8>, 2>&);

array<vector<vector<array<Move, 8>>>, 2> GetCandidateSplitMoves(
		const GameData&, const vector<TurnData>&, const int_fast32_t&);

void RestoreTruthIndex(const vector<int_fast32_t>&, vector<array<Move, 8>>&);

void MakeTrasitionMoves(const GameData&, const vector<array<Move, 8>>&,
						vector<Move>&);

vector<vector<Move>> RivalAllSearch(const GameData&, const TurnData&,
									const vector<vector<array<Move, 8>>>&,
									const int_fast32_t&);

array<Move, 8> AllyAllSearch(const GameData&, const TurnData&,
							 const vector<vector<array<Move, 8>>>&,
							 const vector<vector<Move>>&,
							 const int_fast32_t&);

array<Move, 8> SplitSearch(const GameData&, const TurnData&,
						   const int_fast32_t &);

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

const array<int_fast32_t, 9> one_transition_table = {
	0,
	8,
	8*8,
	6*6*6,
	8*8 + 8*8,
	8*8 + 6*6*6,
	6*6*6 + 6*6*6,
	8*8 + 8*8 + 6*6*6,
	8*8 + 6*6*6 + 6*6*6
};

}

#endif