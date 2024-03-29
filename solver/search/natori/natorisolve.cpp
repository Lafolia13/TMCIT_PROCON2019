#include "../../search/natori/natorisolve.h"
#include "../../search/natori/evaluation.h"

#include <queue>
#include <set>
#include <algorithm>

namespace search {
namespace natori {

constexpr int32_t beam_width = 3000;
constexpr int32_t beam_depth = 5;


// タイルはそのままで、敵の存在だけ消します
void EraseRivalAgent(const base::GameData &game_data,
					 base::TurnData &turn_data) {
	for (int agent_id = 0; agent_id < game_data.agent_num_; agent_id++) {
		const base::Position &agent_position =
			turn_data.GetNowPosition(base::kRival, agent_id);
		turn_data.stay_agent_[agent_position.h_][agent_position.w_] = false;
	}

	return;
}

// agents_movesはGetAgentActionsで作成した近傍に対する行動群のリストです
// agent_idはいま行動を変えようとしているエージェントのidを指し、
// moves_idは各エージェントがどのMoveとなっているかを表します
// ret_moveに次のmoveリストを代入します
// agents_movesの各リストのサイズが2とすると、
// 0 0 0
// 1 0 0
// 0 1 0
// 1 1 0
// 0 0 1
// ...
// のような順で組み合わせが更新されていきます
// 関数の開始時に最後の組み合わせだとfalseが返り、それ以外ではtrueが返ります
bool NextPermutation(
		const std::vector<std::vector<action::Move> > &agents_moves,
		const int32_t agent_id, std::vector<int32_t> &moves_id,
		std::vector<action::Move> &ret_move) {
	if (agent_id == agents_moves.size())
		return false;

	int32_t &target_id = moves_id[agent_id];
	const std::vector<action::Move> target_moves = agents_moves[agent_id];

	if (target_id == target_moves.size()) {
		target_id = 0;
		ret_move[agent_id] = target_moves[target_id++];

		return NextPermutation(agents_moves, agent_id+1,
							   moves_id, ret_move);
	} else {
		ret_move[agent_id] = target_moves[target_id++];

		return true;
	}
}

// (team_id, agent_id)なエージェントの近傍への行動群を返します
// 具体的には、空白、自タイルのときWalk、敵タイルのときEraseをします
std::vector<action::Move> GetAgentActions(const base::GameData &game_data,
										  const base::TurnData &turn_data,
										  const int32_t &team_id,
										  const int32_t &agent_id) {
	std::vector<action::Move> ret;
	const base::Position &agent_position = turn_data.GetNowPosition(team_id,
																	agent_id);
	base::Position target_position;
	action::Move agent_move(team_id, agent_id, 0, 0);

	for (int32_t i = 0; i < action::kNextToNine.size(); ++i) {
		if (i == 4) continue;
		const base::Position &next_to = action::kNextToNine[i];
		target_position = agent_position + next_to;
		if (IntoField(game_data, target_position) == false)
			continue;

		const int32_t &target_tile_data =
			turn_data.GetTileData(target_position);
		agent_move.target_id_ = i;
		if (target_tile_data == base::kBrank || target_tile_data == team_id) {
			agent_move.agent_action_ = action::kWalk;
		} else {
			agent_move.agent_action_ = action::kErase;
		}
		ret.push_back(agent_move);
	}

	return ret;
}

// Nodeの同一判定
// 指定した座標群、プレイヤーの位置がchecked_state内に存在していたらfalse
// なかったら追加してtrue
// また、このときにプレイヤーの位置、指定座標群をソートします
// 初期状態以外では、エージェントはswapしても問題はないです
// 逆をいえば、初期状態のときにするとroot_moves_が壊れるのでソートしてはいけない
bool NotYetCheckNode(
		const std::vector<action::Move> &check_moves,
		const base::TurnData &before_turn_data, Node &check_node,
		std::set<std::vector<std::vector<base::Position> >> &checked_states) {
	std::vector<std::vector<base::Position> > check_state;

	std::vector<base::Position> &targeted_positions =
		check_node.targeted_positions_;

	for (int32_t agent_id = 0; agent_id < check_moves.size(); ++agent_id) {	// check_moves.size() = agents_num_
		const base::Position &targeted_position =
			before_turn_data.agents_position_[base::kAlly][agent_id] +
			action::kNextToNine[check_moves[agent_id].target_id_];
		targeted_positions.push_back(targeted_position);
	}
	std::sort(targeted_positions.begin(), targeted_positions.end());
	check_state.push_back(targeted_positions);

	std::vector<base::Position> &agents_position =
		check_node.turn_data_.agents_position_[base::kAlly];
	sort(agents_position.begin(), agents_position.end());
	check_state.push_back(agents_position);

	if (checked_states.count(check_state) == 1) return false;

	checked_states.insert(check_state);
	return true;
}

// 評価関数です
// evaluation.cppにまとめるべきな気もしますが、なんとなくこっちに書いてます
// 引数は実はこんなにいりません。消してないだけです
int32_t NodesEvaluation(const base::GameData &game_data,
						const base::TurnData &now_turn_data,
						const base::TurnData &next_turn_data,
						const base::TurnData &start_turn_data,
						const std::vector<action::Move> &check_moves) {

	int32_t ret_evaluation = 0;

	auto before_point = calculation::CalculationAllPoint(game_data,
														 now_turn_data);
	auto now_point = calculation::CalculationAllPoint(game_data,
													  next_turn_data);
	// ret_evaluation += GetPointDefference(before_point.first, now_point.first);
	ret_evaluation += GetTileDefference(before_point.first, now_point.first);
	ret_evaluation += GetAreaDefference(before_point.first, now_point.first);
	ret_evaluation += GetEraseRivalTile(before_point.second, now_point.second);
	if (now_turn_data.now_turn_ == start_turn_data.now_turn_)
		ret_evaluation += GetFirstEvaluation(ret_evaluation);

	return ret_evaluation;
}

// 名取高専の部誌に書いてある感じでビームサーチを書きます
std::vector<action::Move> BeamSearch(const base::GameData &game_data,
									 const base::TurnData &turn_data) {
	if (turn_data.now_turn_ == 0)
		MakeEvaluationCorrection(game_data);

	base::TurnData start_turn_data = turn_data;
	EraseRivalAgent(game_data, start_turn_data);

	std::priority_queue<Node, std::vector<Node>, std::greater<Node> > p_queue, next_queue;
	std::set<std::vector<std::vector<base::Position> >> checked_states;

	// ここらへん新しくオブジェクトを作りたくないので一番上にまとめてます
	std::vector<std::vector<action::Move> > agents_moves(game_data.agent_num_); // あるTurnDataでの味方全員の行動群のリスト
	std::vector<action::Move> check_moves(game_data.agent_num_);				// agents_movesからできる組み合わせ。NextTurnData()をします
	std::vector<int32_t> moves_id(game_data.agent_num_);						// 組み合わせのインデックス
	Node next_node;																// input用
	p_queue.push(Node(start_turn_data, next_node));									// next_nodeはempty

	int32_t state_count = 0;
	for (int32_t i = 0; i < std::min(beam_depth, game_data.max_turn_ -
									 turn_data.now_turn_); ++i) {
		while (p_queue.size() > 0) {
			Node now_node = p_queue.top();
			p_queue.pop();
			// Node毎に盤面が異なるので行動群を作ります
			for (int32_t agent_id = 0; agent_id < game_data.agent_num_;
				 ++agent_id) {
				agents_moves[agent_id] = GetAgentActions(game_data, turn_data,
														 base::kAlly,
														 agent_id);
			}

			// fillは埋める動作です。moves_idの初期化。
			// check_movesも0000の状態に(NextPermutaionはindexをインクリメントするのは代入の後なので最初のNextPermutationは0000になります)
			std::fill(moves_id.begin(), moves_id.end(), 0);
			for (int32_t agent_id = 0; agent_id < game_data.agent_num_;
				++agent_id) {
				check_moves[agent_id] = agents_moves[agent_id][0];
			}

			while (NextPermutation(agents_moves, 0, moves_id,
								   check_moves) == true) {
				base::TurnData next_turn_data =
					NextTurnData(game_data, now_node.turn_data_, check_moves);
				++next_turn_data.now_turn_;
				next_node = Node(next_turn_data, now_node);

				if (NotYetCheckNode(check_moves, now_node.turn_data_,
									next_node, checked_states) == false) {
					continue;
				}


				next_node.evaluation_ =
					now_node.evaluation_ +
					NodesEvaluation(game_data, now_node.turn_data_,
									next_turn_data, start_turn_data, check_moves);

				// root_move_に一番初め(i = 0)のときのcheck_movesを入れます
				if (i == 0)
					next_node.root_move_ = check_moves;

				if (next_queue.size() <= beam_width ||
					next_queue.top().evaluation_ < next_node.evaluation_)
					next_queue.push(next_node);

				if (next_queue.size() > beam_width)
					next_queue.pop();

				// std::cerr << next_queue.top().evaluation_ << std::endl;
				state_count++;
			}
		}

		// p_queueは空なのでswapすると移す動作を書かなくて良いし早いです
		swap(p_queue, next_queue);
		checked_states.clear();
	}
	std::cerr << "count is " << state_count << std::endl;

	std::vector<action::Move> ret;
	// キューの中は昇順なので一番下が最も評価の高いノードです
	while (p_queue.size() > 0) {
		ret = p_queue.top().root_move_;
		p_queue.pop();

		if (p_queue.size() == 1) {
			std::cerr << "last state" << std::endl;
			for (auto x : p_queue.top().turn_data_.tile_data_) {
				for (auto y : x)
					std::cerr << y << " ";
				std::cerr << std::endl;
			}
			std::cerr << std::endl;
		}
	}
	return ret;
}

}
}