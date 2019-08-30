package tmcit.yasu.data;

import java.util.List;

public class Field {
	public int width, height, startedAtUnixTime, turn;
	public List< List<Integer> > points;
	public List< List<Integer> > tiled;
	public List<Team> teams;
	public List<Action> actions;
}
