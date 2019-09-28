#ifndef DISPERSE_AGENT_H
#define DISPERSE_AGENT_H

#include "../../base/base_system.h"
#include "../../action/change_action.h"
#include "../../calculation/calculation.h"

namespace disperse_agent {

struct NodeID {
	int_fast32_t start_turn;
	int_fast32_t now_turn;
	int_fast32_t search_id;

	NodeID() {};
	NodeID(const int_fast32_t &start_turn, const int_fast32_t &now_turn,
			 const int_fast32_t &search_id) :
		start_turn(start_turn),
		now_turn(now_turn),
		search_id(search_id)
	{};

	bool operator==(const NodeID &another) const {
		return start_turn == another.start_turn &&
			   now_turn == another.now_turn &&
			   search_id == another.search_id;
	};
};

struct Node {
	TurnData turn_data = {};
	double evaluation = {};
	array<Move, 8> first_move = {};

	int_fast32_t key = {};
	NodeID node_id;

	Node() {};
	Node (const TurnData &turn_data, const double &evaluation) :
		turn_data(turn_data),
		evaluation(evaluation)
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
								  const int_fast32_t&, const bool&);

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
							 const int_fast32_t&,
							 const bool&);

array<Move, 8> SplitSearch(const GameData&, const TurnData&,
						   const int_fast32_t&, const bool&);

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
	7*7*7,
	8*8 + 8*8,
	8*8 + 7*7*7,
	7*7*7 + 7*7*7,
	8*8 + 8*8 + 7*7*7,
	8*8 + 7*7*7 + 7*7*7
};

}

#endif