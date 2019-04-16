#include "../base/basesystem.cpp"
#include "../calculation/calculation.cpp"
#include "../action/changeaction.cpp"
#include "../search/natori/natorisolve.cpp"

#include <cstdint>
#include <iostream>
#include <vector>

namespace search {

bool OutPutMove(std::vector<action::Move> &ret_moves) {
	sort(ret_moves.begin(), ret_moves.end());
	for (auto agent_move : ret_moves) {
		std::cout << action::kToCharactor[agent_move.agent_action_] <<
			agent_move.target_id_ << std::endl;
	}

	return true;
}

}

int main() {
	base::GameData game_data;
	game_data.Input();
	base::TurnData turn_data(game_data);

	while (turn_data.Input(game_data) == true) {
		std::vector<action::Move> ret_moves;

		ret_moves = search::natori::BeamSearch(game_data, turn_data);

		search::OutPutMove(ret_moves);
	}

	return 0;
}