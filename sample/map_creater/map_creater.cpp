#include <cstdint>
#include <iostream>
#include <string>
#include <array>
#include <vector>
#include <ctime>

using namespace std;

constexpr int32_t max_turn = 60;
constexpr int32_t min_turn = 40;
constexpr int32_t max_size = 20;
constexpr int32_t min_size = 10;
constexpr int32_t max_agent = 8;
constexpr int32_t min_agent = 2;
constexpr int32_t max_point = 16;
constexpr int32_t min_point = 0;
enum Agent { kAlly, kRival };

struct Position {
	int32_t h_ = 0;
	int32_t w_ = 0;

	Position(){}
	Position(int32_t h, int32_t w) :
		h_(h),
		w_(w)
	{}
};

void Message() {
	cerr <<
		" This program creates game data according to the "
		"format of the Visualizer. "
		<< endl <<
		" It creates vertically symmetrical field. "
		<< endl <<
		" You can select turn, height, width and numberof agents "
		"or all random. "
		<< endl;

	return;
}

void InputData(int32_t &turn, int32_t &width, int32_t &height,
			   int32_t &agents_num) {
	cerr << " Create all random map ? y/n : ";

	string input;
	while(cin >> input) {
		if (input == "y" || input == "Y" || input == "n" || input == "N") {
			break;
		} else {
			cerr << " Prease one more time : ";
		}
	}

	if (input == "n" || input == "N") {
		cerr << " Turn ? : ";
		cin >> turn;
		cerr << " Width ? : ";
		cin >> width;
		cerr << " Height ? : ";
		cin >> height;
		cerr << " Number of agents ? : ";
		cin >> agents_num;
	} else {
		turn = 50;
		width = rand()%(max_size - min_size) + min_size;
		height = rand()%(max_size - min_size) + min_size;
		agents_num = rand()%(max_agent - min_agent) + min_agent;
	}

	return;
}

void CreateMap(vector<vector<int32_t> > &field_data,
			   array<vector<Position>, 2> &agents_position) {
	const int32_t height = field_data.size();
	const int32_t width = field_data[0].size();
	const int32_t agents_num = agents_position[0].size();

	for (int32_t h = 0; h < height; ++h) {
		for (int32_t w = 0; w < width/2 + (width&1); ++w) {
			field_data[h][w] = rand()%(max_point - min_point) + min_point;
			if (rand()%100 < 15)
				field_data[h][w] *= -1;

			field_data[h][width - w - 1] = field_data[h][w];
		}
	}

	vector<vector<bool > > used(height, vector<bool>(width, false));
	for (int i = 0; i < agents_num; ++i) {
		Position p(rand()%height, rand()%width);

		if ((width&1 && width/2+width&1 == p.w_) || used[p.h_][p.w_]) {
			i--;
			continue;
		}

		agents_position[0][i] = p;
		agents_position[1][i] = Position(p.h_, width - p.w_ - 1);

		used[agents_position[0][i].h_][agents_position[0][i].w_] = true;
		used[agents_position[1][i].h_][agents_position[1][i].w_] = true;
	}

	return;
}

void OutPut(const int32_t &turn, const int32_t &width, const int32_t &height,
			const int32_t &agents_num, const vector<vector<int32_t> > &field_data,
			const array<vector<Position>, 2> &agents_position) {
	cout <<
		turn << endl <<
		width << endl <<
		height << endl;
	for (auto hline : field_data) {
		for (auto point : hline)
			cout << point << " ";
		cout << endl;
	}
	cout << agents_num << endl;
	for (int32_t i = 0; i < 2; ++i) {
		for (auto position : agents_position[i])
			cout << position.w_ << " " << position.h_ << endl;
	}

	return;
}

int32_t main() {
	srand((unsigned)time(NULL));

	int32_t turn;
	int32_t width;
	int32_t height;
	int32_t agents_num;
	vector<vector<int32_t> > field_data;
	array<vector<Position>, 2> agents_position;

	Message();
	InputData(turn, width, height, agents_num);

	field_data.resize(height, vector<int32_t>(width));
	agents_position[0].resize(agents_num, Position());
	agents_position[1].resize(agents_num, Position());

	CreateMap(field_data, agents_position);

	OutPut(turn, width, height, agents_num,
		   field_data, agents_position);

	return 0;
}