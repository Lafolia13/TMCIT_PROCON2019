
#include "../base/base_system.h"

Position& TurnData::GetPosition(const int_fast32_t &team_id, const int_fast32_t &agent_id) {
	return agents_position[team_id][agent_id];
}

// 先に誰もいなかったら行動を変化がなくなるまで繰り返す
void ChangeWalk(const GameData &game_data, int_fast32_t &walk_num,
				array<Move, 16> &walk_agents, TurnData *p_turn_data) {
	TurnData &turn_data = *p_turn_data;

	bool changed = true;
	while (changed) {
		changed = false;
		for (int_fast32_t &&i = 0; i < walk_num; ++i) {
			Position &now_position = turn_data.GetPosition(walk_agents[i].team_id,
														  walk_agents[i].agent_id);
			const Position &target_position = walk_agents[i].target_position;

			if (turn_data.agent_exist[GetBitsetNumber(target_position)] == true)
				continue;

			turn_data.agent_exist.reset(GetBitsetNumber(now_position));
			turn_data.agent_exist.set(GetBitsetNumber(target_position));
			now_position = target_position;

			// 空白のマスを塗って得点を足す
			int_fast32_t &next_color = turn_data.GetTileState(target_position);
			if (next_color == kBrank) {
				next_color = walk_agents[i].team_id;
				turn_data.tile_point[walk_agents[i].team_id] +=
						game_data.GetTilePoint(target_position);
			}

			changed = true;
			swap(walk_agents[i], walk_agents[--walk_num]);
			--i;
		}
	}

	return;
}

void ChangeErase(const GameData &game_data, const int_fast32_t &erase_num,
				array<Move, 16> &erase_agents, TurnData *p_turn_data) {
	TurnData &turn_data = *p_turn_data;

	for (int_fast32_t &&i = 0; i < erase_num; ++i) {
		const Position &now_position = turn_data.GetPosition(erase_agents[i].team_id,
															 erase_agents[i].agent_id);
		const Position &target_position = now_position +
										  kNextToNine[erase_agents[i].direction];

		if (turn_data.agent_exist[GetBitsetNumber(target_position)] == true)
			continue;

		int_fast32_t &erase_color = turn_data.GetTileState(target_position);
		turn_data.tile_point[erase_color] -= game_data.GetTilePoint(target_position);
		erase_color = kBrank;
	}

	return;
}

void CheckConflict(TurnData *p_turn_data,
				   array<Move, 16> &agents_move, int_fast32_t &moves_num,
				   array<array<int_fast32_t, 20>, 20> &target_count) {
	TurnData &turn_data = *p_turn_data;

	for (int_fast32_t &&i = 0; i < moves_num; ++i) {
		Position &agent_position = turn_data.GetPosition(agents_move[i].team_id,
														 agents_move[i].agent_id);

		Position &target_position = agents_move[i].target_position;
		target_position = agent_position + kNextToNine[agents_move[i].direction];

		if (target_count[target_position.h][target_position.w] > 1) {
			swap(agents_move[i], agents_move[--moves_num]);
			i--;
		}
	}
}

// agents_moveは単体で無効となる動作(空白にerase、敵タイルにwalk、盤面外)は存在しない
// タイルポイントの差分計算もやる
void TurnData::Transition(const GameData &game_data,
						  const vector<Move> &agents_move) {
	static array<Move, 16> walk_agents = {}, erase_agents = {};
	static array<array<int_fast32_t, 20>, 20> target_count = {};
	static int_fast32_t walk_num, erase_num, moves_num;

	walk_num = 0, erase_num = 0, moves_num = agents_move.size();
	static Position target_position;
	for (int_fast32_t &&i = 0; i < moves_num; ++i) {
		if (agents_move[i].action == kWalk) {
			walk_agents[walk_num++] = agents_move[i];
		} else if (agents_move[i].action == kErase) {
			erase_agents[erase_num++] = agents_move[i];
		} else {
			continue;
		}
		Position &agent_position = GetPosition(agents_move[i].team_id,
											   agents_move[i].agent_id);
		target_position = agent_position +
						  kNextToNine[agents_move[i].direction];
		target_count[target_position.h][target_position.w]++;
	}

	CheckConflict(this, walk_agents, walk_num, target_count);
	CheckConflict(this, erase_agents, erase_num, target_count);

	ChangeWalk(game_data, walk_num, walk_agents, this);
	ChangeErase(game_data, erase_num, erase_agents, this);

	for (int_fast32_t &&i = 0; i < moves_num; ++i) {
		Position &agent_position = GetPosition(agents_move[i].team_id,
											   agents_move[i].agent_id);
		target_position = agent_position +
						  kNextToNine[agents_move[i].direction];
		target_count[target_position.h][target_position.w] = 0;
	}

	return;
}

vector<vector<Move>> GetAgentsAllMoves(
		const GameData &game_data, TurnData &turn_data,
		const int_fast32_t &team_id, const bool &use_none,
		const bool &erase_my_tile) {
	vector<vector<Move>> ret_moves(turn_data.agent_num);
	static Position next_pos;
	for (int_fast32_t &&agent_id = 0; agent_id < turn_data.agent_num; ++agent_id) {
		const Position &agent_pos = turn_data.GetPosition(team_id, agent_id);
		for (int_fast32_t &&next_to = 0; next_to < 9; ++next_to) {
			next_pos = agent_pos + kNextToNine[next_to];
			if (game_data.IntoField(next_pos) == false) continue;
			const int_fast32_t &tile_color = turn_data.GetTileState(next_pos);
			const int_fast32_t &tile_point = game_data.GetTilePoint(next_pos);

			if (next_to == 4) {
				if (use_none)
					ret_moves[agent_id].push_back(Move(team_id, agent_id,
													   next_to, kNone));
			} else if (tile_color == kBrank) {
				ret_moves[agent_id].push_back(Move(team_id, agent_id,
												   next_to, kWalk));
			} else if (tile_color == team_id) {
				ret_moves[agent_id].push_back(Move(team_id, agent_id,
												   next_to, kWalk));
				if (erase_my_tile && tile_point < 0)
					ret_moves[agent_id].push_back(Move(team_id, agent_id,
													   next_to, kErase));
			} else {
				ret_moves[agent_id].push_back(Move(team_id, agent_id,
												   next_to, kErase));
			}
		}
	}

	return ret_moves;
}

bool NextPermutation(const vector<vector<Move>> &all_moves,
					 const int_fast32_t &change_agent,
					 vector<int_fast32_t> &move_ids,
					 vector<Move> &ret_moves) {

	if (change_agent == all_moves.size()) return false;

	int_fast32_t &next_move_id = move_ids[change_agent];
	const vector<Move> &target_moves = all_moves[change_agent];

	if (next_move_id == target_moves.size()) {
		next_move_id = 0;
		ret_moves[change_agent] = target_moves[next_move_id++];
		return NextPermutation(all_moves, change_agent + 1, move_ids, ret_moves);
	} else {
		ret_moves[change_agent] = target_moves[next_move_id++];
		return true;
	}
}
