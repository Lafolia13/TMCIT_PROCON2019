#pragma once

#include "..//..//base/base_system.h"

#include <set>

namespace search {
	namespace kizitora {

		class Node {
		public:
			std::vector<Move> root_move_ = {};
			TurnData turn_data_ = {};
			std::vector<Position> targeted_positions_ = {};	// 指定した座標をソート済で入れる
			int32_t evaluation_ = 0;

			Node() {};
			Node(const TurnData& turn_data,
				const Node& node) :
				turn_data_(turn_data),
				root_move_(node.root_move_),
				targeted_positions_(node.targeted_positions_)
			{};

			bool operator<(const Node& another) const {
				return this->evaluation_ < another.evaluation_;
			};
			bool operator>(const Node& another) const {
				return this->evaluation_ > another.evaluation_;
			};

		protected:
		private:
		};

		// vector<int32_t = index>配列から次のMoveの組み合わせを作ります
		bool NextPermutation(const std::vector<std::vector<Move> >& agents_moves,
			int32_t agent_id,
			std::vector<int32_t>& moves_id,
			std::vector<Move>& ret_move);

		// エージェントの周囲に対する行動を返します
		std::vector<Move> GetAgentActions(const GameData& game_data,
			const TurnData& turn_data,
			const int32_t& team_id,
			const int32_t& agent_id);

		// 同一盤面の除去
		bool IsUniqeNode(const std::vector<Move>& check_moves,
			const TurnData& before_turn_data,
			Node& check_node,
			std::set<std::vector<std::vector<Position> >>& checked_states);

		// 盤面から敵エージェントの存在を消去する
		void EraseRivalAgent(const GameData& game_data,
			TurnData& turn_data);

		// 各タイルの9近傍の平均値を計算する
		std::array<std::array<float, 20>, 20> CalcFieldData2AverageData(const GameData& game_data);

		// エージェントの人数が多い場合に、CalcFieldData2AverageDataで得た配列を元に各エージェントの遷移先を1~2ほど枝刈りする
		// 各タイルの平均値が分かると、その周囲にあるタイルのポイントがどれくらい高いのか、低いのかが分かる。
		// 平均値の低いところは、高いところよりも評価値が低くなりやすいと考えられるので、枝刈りをする。
		void ReduceAgentMoves(const GameData& game_data,
			const TurnData& turn_data,
			const std::array<std::array<float, 20>, 20> & average_field_data,
			std::vector<std::vector<Move>>& agent_moves);

		std::array<Move, 8> BeamSearch(const GameData & game_data, const TurnData & turn_data); 
	}
}