#ifndef CHANGE_ACTION_H
#define CHANGE_ACTION_H

#include "../base/base_system.h"

void ChangeWalk(const GameData&, const int_fast32_t&,
				array<Move, 16>&, TurnData*);

void ChangeErase(const GameData&, const int_fast32_t&,
				array<Move, 16>&, TurnData*);

void CheckConflict(TurnData*, array<Move, 16>&, int_fast32_t&,
				   array<array<int_fast32_t, 20>, 20>&);

vector<vector<Move>> GetAgentsAllMoves(const GameData&, TurnData&,
									   const int_fast32_t&, const bool&,
									   const bool&);

#endif