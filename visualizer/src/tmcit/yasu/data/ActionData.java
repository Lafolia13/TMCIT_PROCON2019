package tmcit.yasu.data;

import java.awt.Point;

public class ActionData {
	public int playerNum, way, agentIndex;
	public Point target;
	public char command, agentChar;

	public ActionData(int playerNum0, int way0, int targetX, int targetY, char command0, char agentChar0, int agentIndex0) {
		playerNum = playerNum0;
		way = way0;
		target = new Point(targetX, targetY);
		command = command0;
		agentChar = agentChar0;
		agentIndex = agentIndex0;
	}

}
