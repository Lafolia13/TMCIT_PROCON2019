package tmcit.yasu.util;

import java.awt.Color;
import java.awt.Font;

import javax.swing.border.LineBorder;

public class Constant {
	public static int MAP_SIZE = 600;
	public static int PREVIEW_MAP_SIZE = 350;

	public static int MY_TERRITORY = 0;
	public static int RIVAL_TERRITORY = 1;
	public static int NONE_TERRITORY = 2;

	public static Font MAP_SCORE_FONT = new Font("Arial", Font.BOLD, 20);
	public static Font PREVIEW_MAP_SCORE_FONT = new Font("Arial", Font.BOLD, 10);
	public static int MAP_SCORE_BIAS = 5;
	public static int PREVIEW_MAP_SCORE_BIAS = 2;

	// background color
	public static Color NONE_BACK_COLOR = new Color(255, 255, 255);
	public static Color MY_BACK_COLOR = new Color(0, 191, 255, 128);
	public static Color RIVAL_BACK_COLOR = new Color(255, 127, 80, 128);

	// player color
	public static Color MY_COLOR = new Color(0, 0, 255, 95);
	public static Color RIVAL_COLOR = new Color(255, 0, 0, 95);
	
	// arrow color
	public static Color WALK_COLOR = new Color(0, 255, 0);
	public static Color ERASE_COLOR = new Color(148, 0, 211);

	// Game
	public static int SLEEP_TIME = 50;


	// Util
	public static int[] DIR_X = {-1, 0, 1, -1, 0, 1, -1, 0, 1};
	public static int[] DIR_Y = {-1, -1, -1, 0, 0, 0, 1, 1, 1};
	public static int[] DIR4_X = {0, 0, 1, -1};
	public static int[] DIR4_Y = {1, -1, 0, 0};


	// Setting


	// UI
	// common
	public static LineBorder DEFAULT_LINE_BORDER = new LineBorder(Color.BLACK);
	public static Font DEFAULT_FONT = new Font("ＭＳ ゴシック", Font.BOLD, 20);
	public static Font SMALL_FONT = new Font("ＭＳ ゴシック", Font.BOLD, 15);
	public static String[] PARAM_COLUMN_NAMES = {"パラメータ名", "説明", "初期値"};
	public static String[] PRESET_PARAM_COLUMN_NAMES = {"パラメータ名", "説明", "値"};
	// mainFrame
}
