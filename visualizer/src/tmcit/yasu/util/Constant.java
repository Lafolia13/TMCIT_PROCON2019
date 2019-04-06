package tmcit.yasu.util;

import java.awt.Color;
import java.awt.Font;

public class Constant {
	public static int MAP_SIZE = 600;

	public static int NONE_TERRITORY = 0;
	public static int MY_TERRITORY = 1;
	public static int RIVAL_TERRITORY = 2;

	public static Font MAP_SCORE_FONT = new Font("Arial", Font.BOLD, 20);
	public static int MAP_SCORE_BIAS = 5;

	// background color
	public static Color NONE_BACK_COLOR = new Color(255, 255, 255);
	public static Color MY_BACK_COLOR = new Color(0, 191, 255, 128);
	public static Color RIVAL_BACK_COLOR = new Color(255, 127, 80, 128);

	// player color
	public static Color MY_COLOR = new Color(0, 0, 255, 95);
	public static Color RIVAL_COLOR = new Color(255, 0, 0, 95);

}
