#include "../../search/toriaezu/toriaezu.h"

namespace toriaezu {


array<Move, 8> BeamSearch(const GameData &game_data, const TurnData &turn_data) {
	const int_fast32_t kBeamWidth = 200, kBeamDepth = 3;

	array<Move, 8> ret = {};
	TurnData start_turn_data = turn_data;

	array<Node, kBeamWidth> p_queue, next_queue;


	return ret;
}

}
