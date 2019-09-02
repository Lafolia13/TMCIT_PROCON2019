package tmcit.yasu.util;

import java.awt.Color;
import java.awt.Font;

import javax.swing.border.LineBorder;

public class Constant {
	// UI
	public static LineBorder DEFAULT_LINE_BORDER = new LineBorder(Color.BLACK);
	public static Font DEFAULT_FONT = new Font("ÇlÇr ÉSÉVÉbÉN", Font.BOLD, 20);
	public static Font SMALL_FONT = new Font("ÇlÇr ÉSÉVÉbÉN", Font.BOLD, 15);
	public static String[] PRESET_PARAM_COLUMN_NAMES = {"ÉpÉâÉÅÅ[É^ñº", "ê‡ñæ", "íl"};

	public static LineBorder NONE_SELECTED_LINE_BORDER = new LineBorder(Color.BLACK, 3);
	public static LineBorder SELECTED_LINE_BORDER = new LineBorder(Color.RED, 3);

	
	// paint game
	public static int MAP_SIZE = 500;
	
	// background color
	public static Color NONE_BACK_COLOR = new Color(255, 255, 255);
	public static Color MY_BACK_COLOR = new Color(0, 191, 255, 128);
	public static Color RIVAL_BACK_COLOR = new Color(255, 127, 80, 128);
	
	// player color
	public static Color MY_COLOR = new Color(0, 0, 255, 95);
	public static Color RIVAL_COLOR = new Color(255, 0, 0, 95);

	
	public static Font MAP_SCORE_FONT = new Font("Arial", Font.BOLD, 20);
	public static int MAP_SCORE_BIAS = 5;

}
