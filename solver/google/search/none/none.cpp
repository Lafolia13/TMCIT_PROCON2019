#include "none.h"

namespace none {

array<Move, 8> AllNone() {
	array<Move, 8> ret;
	for (int_fast32_t &&i = 0; i < 8; ++i)
		ret[i].action = kNone;

	return ret;
}

}