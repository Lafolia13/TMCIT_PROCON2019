#ifndef CALCULATOIN_H
#define CALCULATOIN_H

#include "../base/base_system.h"

int_fast32_t CheckSurrounded(const GameData&, const int_fast32_t&,
							 const Position&, TurnData*,
							 array<array<int_fast32_t, 20>, 20>&);

#endif