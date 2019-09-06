#pragma once

#include "..//..//base/base_system.h"

#include <set>

namespace search {
	namespace kizitora {

		class Node {
		public:
			std::vector<Move> root_move_ = {};
			TurnData turn_data_ = {};
			std::vector<Position> targeted_positions_ = {};	// �w�肵�����W���\�[�g�ςœ����
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

		// vector<int32_t = index>�z�񂩂玟��Move�̑g�ݍ��킹�����܂�
		bool NextPermutation(const std::vector<std::vector<Move> >& agents_moves,
			int32_t agent_id,
			std::vector<int32_t>& moves_id,
			std::vector<Move>& ret_move);

		// �G�[�W�F���g�̎��͂ɑ΂���s����Ԃ��܂�
		std::vector<Move> GetAgentActions(const GameData& game_data,
			const TurnData& turn_data,
			const int32_t& team_id,
			const int32_t& agent_id);

		// ����Ֆʂ̏���
		bool IsUniqeNode(const std::vector<Move>& check_moves,
			const TurnData& before_turn_data,
			Node& check_node,
			std::set<std::vector<std::vector<Position> >>& checked_states);

		// �Ֆʂ���G�G�[�W�F���g�̑��݂���������
		void EraseRivalAgent(const GameData& game_data,
			TurnData& turn_data);

		// �e�^�C����9�ߖT�̕��ϒl���v�Z����
		std::array<std::array<float, 20>, 20> CalcFieldData2AverageData(const GameData& game_data);

		// �G�[�W�F���g�̐l���������ꍇ�ɁACalcFieldData2AverageData�œ����z������Ɋe�G�[�W�F���g�̑J�ڐ��1~2�قǎ}���肷��
		// �e�^�C���̕��ϒl��������ƁA���̎��͂ɂ���^�C���̃|�C���g���ǂꂭ�炢�����̂��A�Ⴂ�̂���������B
		// ���ϒl�̒Ⴂ�Ƃ���́A�����Ƃ�������]���l���Ⴍ�Ȃ�₷���ƍl������̂ŁA�}���������B
		void ReduceAgentMoves(const GameData& game_data,
			const TurnData& turn_data,
			const std::array<std::array<float, 20>, 20> & average_field_data,
			std::vector<std::vector<Move>>& agent_moves);

		std::array<Move, 8> BeamSearch(const GameData & game_data, const TurnData & turn_data); 
	}
}