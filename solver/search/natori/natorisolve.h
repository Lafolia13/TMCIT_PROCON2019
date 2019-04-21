#include "../../base/basesystem.h"
#include "../../calculation/calculation.h"
#include "../../action/changeaction.h"

#include <vector>
#include <cstdint>

namespace search {
namespace natori {

// beamsearchに回すよう
// root_move_は一回目の行動を入れます
class Node {
public :
	std::vector<action::Move> root_move_ = {};
	base::TurnData turn_data_ = {};
	std::vector<base::Position> targeted_positions_ = {};	// 指定した座標をソート済で入れる
	int32_t evaluation_ = 0;

	Node() {};
	Node(const base::TurnData &turn_data,
		 const Node &node) :
		turn_data_(turn_data),
		root_move_(node.root_move_),
		targeted_positions_(node.targeted_positions_)
	{};

	bool operator>(const Node &another) const {
		return this->evaluation_ > another.evaluation_;
	};

protected :
private :
};

// vector<int32_t = index>配列から次のMoveの組み合わせを作ります
bool NextPermutation(const std::vector<std::vector<action::Move> >&,
					 int32_t, std::vector<int32_t> &,
					 std::vector<action::Move>&);

// エージェントの周囲に対する行動を返します
std::vector<action::Move> GetAgentActions(const base::GameData&,
										  const base::TurnData&,
										  const int32_t&, const int32_t&);

int32_t NodesEvaluation(const base::GameData&,
						const base::TurnData &now_turn_data,
						const base::TurnData &next_turn_data,
						const base::TurnData &start_turn_data);

// びーむさーちをします
std::vector<action::Move> BeamSearch(const base::GameData&,
									 const base::TurnData&);

}
}