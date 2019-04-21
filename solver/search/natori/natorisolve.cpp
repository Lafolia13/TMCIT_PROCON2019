#include "../../search/natori/natorisolve.h"
#include "../../search/natori/evaluation.h"

#include <queue>

namespace search {
namespace natori {

constexpr int32_t beam_width = 1000;
constexpr int32_t beam_depth = 7;

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

// 評価関数です。
int32_t NodesEvaluation(const base::GameData &game_data,
						const base::TurnData &now_turn_data,
						const base::TurnData &next_turn_data,
						const base::TurnData &start_turn_data,
						const std::vector<action::Move> check_moves) {
	int32_t ret_evaluation = 0;

	return ret_evaluation;
}

// 名取高専の部誌に書いてある感じでビームサーチを書きます
std::vector<action::Move> BeamSearch(const base::GameData &game_data,
									 const base::TurnData &turn_data) {
	std::priority_queue<Node, std::vector<Node>,
						std::greater<Node> > p_queue, next_queue;

	// ここらへん新しくオブジェクトを作りたくないので一番上にまとめてます
	std::vector<std::vector<action::Move> > agents_moves(game_data.agent_num_); // あるTurnDataでの味方全員の行動群のリスト
	std::vector<action::Move> check_moves(game_data.agent_num_);				// agents_movesからできる組み合わせ。NextTurnData()をします
	std::vector<int32_t> moves_id(game_data.agent_num_);						// 組み合わせのインデックス
	Node next_node;																// input用
	p_queue.push(Node(turn_data, check_moves, 0));
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

				const int32_t evaluation =
					now_node.evaluation_ +
					NodesEvaluation(game_data, now_node.turn_data_,
									next_turn_data, turn_data, check_moves);

				next_node = Node(next_turn_data, check_moves, evaluation);
				// root_move_に一番初め(i = 0)のときのcheck_movesを入れます
				if (i == 0) {
					next_node.root_move_ = check_moves;
				} else {
					next_node.root_move_ = now_node.root_move_;
				}

				if (next_queue.size() == 0 ||
					next_queue.top().evaluation_ < evaluation)
					next_queue.push(next_node);

				if (next_queue.size() > beam_width)
					next_queue.pop();
			}
		}

		// p_queueは空なのでswapすると移す動作を書かなくて良いし早いです
		swap(p_queue, next_queue);
	}

	std::vector<action::Move> ret;
	// キューの中は昇順なので一番下が最も評価の高いノードです
	while (p_queue.size() > 0) {
		ret = p_queue.top().root_move_;
		p_queue.pop();
	}
	return ret;
}

}
}