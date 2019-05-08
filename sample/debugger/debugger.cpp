#include <iostream>
#include <cstdint>
#include <vector>
#include <array>

using namespace std;

struct Position {
	int32_t h_;
	int32_t w_;

	Position() {}
	Position(int32_t h, int32_t w) :
		h_(h),
		w_(w)
	{}
};

int32_t main() {
	int32_t turn;
	int32_t width;
	int32_t height;
	int32_t agents_num;
	vector<vector<int32_t> > field_data;
	array<vector<Position>, 2> agents_position;
	vector<vector<int> > tile_data;

	cin >> turn;
	cin >> width;
	cin >> height;

	field_data.resize(height, vector<int32_t>(width));
	tile_data.resize(height, vector<int32_t>(width, 2));

	for (int32_t h = 0; h < height; ++h)
		for (int32_t w = 0; w < width; ++w)
			cin >> field_data[h][w];

	cin >> agents_num;
	for (int32_t team = 0; team < 2; ++team){
		agents_position[team].resize(agents_num);
		for (int32_t agent_id = 0; agent_id < agents_num; ++agent_id){
			cin >> agents_position[team][agent_id].w_ >> agents_position[team][agent_id].h_;
			tile_data[agents_position[team][agent_id].h_][agents_position[team][agent_id].w_] = team;
		}
	}


	cout << turn << endl;
	cout << width << " " << height << endl;
	cout << agents_num << endl;
	for (auto hline : field_data) {
		for (auto point : hline)
			cout << point << " ";
		cout << endl;
	}

	cout << 0 << endl;
	for (auto hlile : tile_data) {
		for (auto tile : hlile)
			cout << tile << " ";
		cout << endl;
	}
	for (int team = 0; team < 2; ++team)
		for (auto position : agents_position[team])
			cout << position.w_ << " " << position.h_ << endl;

	return 0;
}