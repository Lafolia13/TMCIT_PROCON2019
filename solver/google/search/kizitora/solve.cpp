#include "../../base/base_system.h"
#include "../../calculation/calculation.h"
#include "evaluation.h"
#include "solve.h"

#include <iostream>
#include <array>
#include <vector>
#include <list>
#include <queue>

namespace search {
	namespace kizitora {

		// 値は仮置きのもの
		constexpr int32_t beam_width = 3000;
		constexpr int32_t beam_depth = 5;

		// 敵のエージェントを盤面から消す
		void EraseRivalAgent(const GameData& game_data, TurnData& turn_data) {
			for (int agent_id = 0; agent_id < game_data.agent_num_; agent_id++) {
				const Position& agent_position =
					turn_data.GetNowPosition(kRival, agent_id);
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
			const std::vector<vector<Move> >& agents_moves,
			const int32_t agent_id,
			std::vector<int32_t>& moves_id,
			std::vector<Move>& ret_move) {
			if (agent_id == agents_moves.size())
				return false;

			int32_t& target_id = moves_id[agent_id];
			const std::vector<Move> target_moves = agents_moves[agent_id];

			if (target_id == target_moves.size()) {
				target_id = 0;
				ret_move[agent_id] = target_moves[target_id++];

				return NextPermutation(agents_moves, agent_id + 1,
					moves_id, ret_move);
			}
			else {
				ret_move[agent_id] = target_moves[target_id++];

				return true;
			}
		}

		// (team_id, agent_id)なエージェントの近傍への行動群を返します
		// 具体的には、空白、自タイルのときWalk、敵タイルのときEraseをします
		std::vector<Move> GetAgentActions(const GameData& game_data,
			const TurnData& turn_data,
			const int32_t& team_id,
			const int32_t& agent_id) {
			std::vector<Move> ret;
			const Position& agent_position = turn_data.GetNowPosition(team_id, agent_id);
			Position target_position;
			Move agent_move(team_id, agent_id, 0, 0);

			for (int32_t i = 0; i < kNextToNine.size(); ++i) {
				if (i == 4) continue;
				const Position& next_to = kNextToNine[i];
				target_position = agent_position + next_to;
				if (GameData::IntoField(game_data, target_position) == false)
					continue;

				const int32_t& target_tile_data =
					turn_data.GetTileData(target_position);
				agent_move.target_id_ = i;
				if (target_tile_data == kBrank || target_tile_data == team_id) {
					agent_move.agent_action_ = kWalk;
				}
				else {
					agent_move.agent_action_ = kErase;
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
		bool IsUniqeNode(
			const std::vector<Move>& check_moves,
			const TurnData& before_turn_data,
			Node& check_node,
			std::set<std::vector<std::vector<Position> >>& checked_states) {
			std::vector<std::vector<Position> > check_state;

			std::vector<Position>& targeted_positions =
				check_node.targeted_positions_;

			for (int32_t agent_id = 0; agent_id < check_moves.size(); ++agent_id) {	// check_moves.size() = agents_num_
				const Position& targeted_position =
					before_turn_data.agents_position_[kAlly][agent_id] +
					kNextToNine[check_moves[agent_id].target_id_];
				targeted_positions.push_back(targeted_position);
			}
			std::sort(targeted_positions.begin(), targeted_positions.end());
			check_state.push_back(targeted_positions);

			std::vector<Position>& agents_position =
				check_node.turn_data_.agents_position_[kAlly];
			sort(agents_position.begin(), agents_position.end());
			check_state.push_back(agents_position);

			if (checked_states.count(check_state) == 1) return false;

			checked_states.insert(check_state);
			return true;
		}

		// 各タイルの9近傍の平均値を計算する
		std::array<std::array<float, 20>, 20> CalcFieldData2AverageData(const GameData & game_data) {
			std::array<std::array<float, 20>, 20> average_field_data = { 0 };

			for (int posY = 0; posY < game_data.height; ++posY) {
				for (int posX = 0; posX < game_data.width; ++posX) {
					int cnt = 0; // フィールドの隅や辺にあるマスの場合、9近傍すべてを探索できないため、何マス探索できたか数える
					const Position now_position(posY, posX);

					for (int i = 0; i < kNextToNine.size(); ++i) {
						const Position& next_to = kNextToNine[i];
						const Position& target_position = now_position + next_to;
						if (game_data.IntoField(target_position) == false) continue;

						average_field_data[posY][posX] += game_data.field_data[target_position.h][target_position.w];
						cnt++;
					}
					average_field_data[posY][posX] /= cnt;
				}
			}
			return average_field_data;
		}

		// エージェントの人数が多い場合に、CalcFieldData2AverageDataで得た配列を元に各エージェントの遷移先を1~2ほど枝刈りする
		// 各タイルの平均値が分かると、その周囲にあるタイルのポイントがどれくらい高いのか、低いのかが分かる。
		// 平均値の低いところは、高いところよりも評価値が低くなりやすいと考えられるので、枝刈りをする。
		void ReduceAgentMoves(const GameData& game_data,
			const TurnData& turn_data,
			const std::array<std::array<float, 20>, 20> & average_field_data,
			std::vector<std::vector<Move>>& agent_moves) {

			// エージェントの人数より遷移数の最大値を決める。最大値の値は、ビーム幅が10^3のときに計算量が10^8ぐらいになるようにしています。
			int transition_max = 8;
			if (game_data.agent_num >= 8) transition_max = 6;
			else if (game_data.agent_num >= 6) transition_max = 7;

			for (int agent_id = 0; agent_id < game_data.agent_num; ++agent_id) {
				if (transition_max >= agent_moves[agent_id].size()) continue;

				std::vector<std::pair<int, Move>> average_with_move(agent_moves[agent_id].size());
				for (int i = 0; i < agent_moves[agent_id].size(); ++i) {
					const Position& target_position = turn_data.GetPosition(agent_moves[agent_id][i].team_id, agent_moves[agent_id][i].agent_id) +
						kNextToNine[agent_moves[agent_id][i].direction];

					average_with_move[i] = make_pair(average_field_data[target_position.h][target_position.w], agent_moves[agent_id][i]);
				}

				// average_field_dataの値が昇順になるような評価関数
				auto cmp = [](std::pair<int, Move> l, std::pair<int, Move> r) { return l.first < r.first; };
				sort(average_with_move.begin(), average_with_move.end(), cmp);

				// 遷移数の最大値を超えている分だけ削除する。
				for (int j = 0; j < average_with_move.size() - transition_max; ++j) {
					agent_moves[agent_id].erase(agent_moves[agent_id].begin());
				}
			}
		}

		std::array<Move, 8> BeamSearch(const GameData& game_data, TurnData& turn_data) {
			TurnData first_turn_data = turn_data;

			if (turn_data.now_turn_ == 0) MakeEvaluationCorrection(game_data);

			TurnData start_turn_data = turn_data;
			EraseRivalAgent(game_data, start_turn_data);

			std::priority_queue<Node, std::vector<Node>, std::greater<Node> > p_queue, next_queue;
			std::set<std::vector<std::vector<Position> >> checked_states;
			
			// ここらへん新しくオブジェクトを作りたくないので一番上にまとめてます
			std::vector<std::vector<Move> > agents_moves(game_data.agent_num); // あるTurnDataでの味方全員の行動群のリスト
			std::vector<Move> check_moves(game_data.agent_num);				// agents_movesからできる組み合わせ。NextTurnData()をします
			std::vector<int32_t> moves_id(game_data.agent_num);						// 組み合わせのインデックス
			Node next_node;																// input用
			p_queue.push(Node(start_turn_data, next_node));									// next_nodeはempty

			int32_t state_count = 0;
			for (int32_t i = 0; i < std::min(beam_depth, game_data.max_turn -
				turn_data.now_turn); ++i) {
				while (p_queue.size() > 0) {
					Node now_node = p_queue.top();
					p_queue.pop();
					// Node毎に盤面が異なるので行動群を作ります
					for (int32_t agent_id = 0; agent_id < game_data.agent_num; ++agent_id) {
						agents_moves[agent_id] = GetAgentActions(game_data, turn_data, kAlly, agent_id);
					}

					// エージェントが多い場合、計算量削減のために各エージェントの遷移数を減らす
					if (game_data.agent_num >= 6) {
						std::array<std::array<float, 20>, 20> average_field_data = CalcFieldData2AverageData(game_data);
						ReduceAgentMoves(game_data, turn_data, average_field_data, agents_moves);
					}

					// fillは埋める動作です。moves_idの初期化。
					// check_movesも0000の状態に(NextPermutaionはindexをインクリメントするのは代入の後なので最初のNextPermutationは0000になります)
					std::fill(moves_id.begin(), moves_id.end(), 0);
					for (int32_t agent_id = 0; agent_id < game_data.agent_num; ++agent_id) {
						check_moves[agent_id] = agents_moves[agent_id][0];
					}

					while (NextPermutation(agents_moves, 0, moves_id, check_moves) == true) {
						TurnData next_turn_data =
							NextTurnData(game_data, now_node.turn_data_, check_moves);
						++next_turn_data.now_turn;
						next_node = Node(next_turn_data, now_node);

						if (IsUniqeNode(check_moves, now_node.turn_data_,
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

			std::array<Move, 8> ret;
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
