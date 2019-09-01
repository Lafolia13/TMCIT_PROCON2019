#include "../../base/base_system.h"
#include "../../calculation/calculation.h"
#include "evaluation.h"
#include "solve.h"

#include <iostream>
#include <array>
#include <vector>
#include <queue>

namespace search {
	namespace kizitora {

		// �l�͉��u���̂���
		constexpr int32_t beam_width = 3000;
		constexpr int32_t beam_depth = 5;

		// �G�̃G�[�W�F���g��Ֆʂ������
		void EraseRivalAgent(const GameData& game_data, TurnData& turn_data) {
			for (int agent_id = 0; agent_id < game_data.agent_num_; agent_id++) {
				const Position& agent_position =
					turn_data.GetNowPosition(kRival, agent_id);
				turn_data.stay_agent_[agent_position.h_][agent_position.w_] = false;
			}

			return;
		}

		// agents_moves��GetAgentActions�ō쐬�����ߖT�ɑ΂���s���Q�̃��X�g�ł�
		// agent_id�͂��܍s����ς��悤�Ƃ��Ă���G�[�W�F���g��id���w���A
		// moves_id�͊e�G�[�W�F���g���ǂ�Move�ƂȂ��Ă��邩��\���܂�
		// ret_move�Ɏ���move���X�g�������܂�
		// agents_moves�̊e���X�g�̃T�C�Y��2�Ƃ���ƁA
		// 0 0 0
		// 1 0 0
		// 0 1 0
		// 1 1 0
		// 0 0 1
		// ...
		// �̂悤�ȏ��őg�ݍ��킹���X�V����Ă����܂�
		// �֐��̊J�n���ɍŌ�̑g�ݍ��킹����false���Ԃ�A����ȊO�ł�true���Ԃ�܂�
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

		// (team_id, agent_id)�ȃG�[�W�F���g�̋ߖT�ւ̍s���Q��Ԃ��܂�
		// ��̓I�ɂ́A�󔒁A���^�C���̂Ƃ�Walk�A�G�^�C���̂Ƃ�Erase�����܂�
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

		// Node�̓��ꔻ��
		// �w�肵�����W�Q�A�v���C���[�̈ʒu��checked_state���ɑ��݂��Ă�����false
		// �Ȃ�������ǉ�����true
		// �܂��A���̂Ƃ��Ƀv���C���[�̈ʒu�A�w����W�Q���\�[�g���܂�
		// ������ԈȊO�ł́A�G�[�W�F���g��swap���Ă����͂Ȃ��ł�
		// �t�������΁A������Ԃ̂Ƃ��ɂ����root_moves_������̂Ń\�[�g���Ă͂����Ȃ�
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

		std::array<Move, 8> BeamSearch(const GameData& game_data, TurnData& turn_data) {
			TurnData first_turn_data = turn_data;

			if (turn_data.now_turn_ == 0) MakeEvaluationCorrection(game_data);

			TurnData start_turn_data = turn_data;
			EraseRivalAgent(game_data, start_turn_data);

			std::priority_queue<Node, std::vector<Node>, std::greater<Node> > p_queue, next_queue;
			std::set<std::vector<std::vector<Position> >> checked_states;
			
			// ������ւ�V�����I�u�W�F�N�g����肽���Ȃ��̂ň�ԏ�ɂ܂Ƃ߂Ă܂�
			std::vector<std::vector<Move> > agents_moves(game_data.agent_num); // ����TurnData�ł̖����S���̍s���Q�̃��X�g
			std::vector<Move> check_moves(game_data.agent_num);				// agents_moves����ł���g�ݍ��킹�BNextTurnData()�����܂�
			std::vector<int32_t> moves_id(game_data.agent_num);						// �g�ݍ��킹�̃C���f�b�N�X
			Node next_node;																// input�p
			p_queue.push(Node(start_turn_data, next_node));									// next_node��empty

			int32_t state_count = 0;
			for (int32_t i = 0; i < std::min(beam_depth, game_data.max_turn -
				turn_data.now_turn); ++i) {
				while (p_queue.size() > 0) {
					Node now_node = p_queue.top();
					p_queue.pop();
					// Node���ɔՖʂ��قȂ�̂ōs���Q�����܂�
					for (int32_t agent_id = 0; agent_id < game_data.agent_num; ++agent_id) {
						agents_moves[agent_id] = GetAgentActions(game_data, turn_data, kAlly, agent_id);
					}

					// fill�͖��߂铮��ł��Bmoves_id�̏������B
					// check_moves��0000�̏�Ԃ�(NextPermutaion��index���C���N�������g����̂͑���̌�Ȃ̂ōŏ���NextPermutation��0000�ɂȂ�܂�)
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

						// root_move_�Ɉ�ԏ���(i = 0)�̂Ƃ���check_moves�����܂�
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

				// p_queue�͋�Ȃ̂�swap����ƈڂ�����������Ȃ��ėǂ��������ł�
				swap(p_queue, next_queue);
				checked_states.clear();
			}
			std::cerr << "count is " << state_count << std::endl;

			std::array<Move, 8> ret;
			// �L���[�̒��͏����Ȃ̂ň�ԉ����ł��]���̍����m�[�h�ł�
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
