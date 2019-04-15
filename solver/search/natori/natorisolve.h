#include "../../base/basesystem.h"
#include "../../calculation/calculation.h"
#include "../../action/changeaction.h"

#include <vector>
#include <cstdint>

namespace search {
namespace natori {

class Node {
public :
	std::vector<action::Move> root_move_;
	base::TurnData turn_data_;
	std::vector<action::Move> agnts_move_;
	int32_t evaluation_;

	Node() {};
	Node(const base::TurnData &turn_data,
		 const std::vector<action::Move> &agents_move,
		 const int32_t evaluation) :
		turn_data_(turn_data),
		agnts_move_(agents_move),
		evaluation_(evaluation)
	{};

	bool operator>(const Node &another) const {
		return this->evaluation_ > another.evaluation_;
	};

protected :
private :
};

std::vector<action::Move> NatoriSolver(const base::GameData&,
									   const base::TurnData&);

}
}