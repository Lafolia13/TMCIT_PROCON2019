#ifndef IMPROVE_DISPERSE_EVALUATION_H
#define IMPROVE_DISPERSE_EVALUATION_H

#include "../../calculation/calculation.h"

namespace improve_disperse {

const int_fast32_t evaluations_num = 10;

double GetEvaluation(const GameData&, TurnData&, const TurnData&,
					 const vector<Move>&, const int_fast32_t&,
					 const int_fast32_t&, const double&,
					 const bool&);

double AllyTilePointDifference(const GameData&, const TurnData&,
							   const TurnData&, const int_fast32_t&);

double RivalTilePointDifference(const GameData&, const TurnData&,
								const TurnData&, const int_fast32_t&);

double AllyAreaPointDifference(const GameData&, const TurnData&,
							   const TurnData&, const int_fast32_t&);

double RivalAreaPointDifference(const GameData&, const TurnData&,
								const TurnData&, const int_fast32_t&);

double AllyAreaNum(const GameData&, const TurnData&, const int_fast32_t&);

double RivalAreaNum(const GameData&, const TurnData&, const int_fast32_t&);

double StayMinusMasu(const GameData&, const TurnData&, const int_fast32_t&);

double ActionToRivalLocation(const GameData&, const TurnData&,
							 const vector<Move>&, const int_fast32_t&);

double DisperseAgent(const GameData&, const TurnData&, const int_fast32_t&);

double NotMyTeamMasu(const GameData&, const TurnData&,
					 const int_fast32_t&, const int_fast32_t&);

double BeforeEvaluationBias(const GameData&, const double&, const int_fast32_t&);

double FirstEvaluation(const GameData&, const double&, const int_fast32_t&);

}

#endif
