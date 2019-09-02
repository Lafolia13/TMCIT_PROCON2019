package tmcit.yasu.util;

import java.awt.Color;
import java.awt.Font;

import javax.swing.border.LineBorder;

public class Constant {
	// UI
	public static LineBorder DEFAULT_LINE_BORDER = new LineBorder(Color.BLACK);
	public static Font DEFAULT_FONT = new Font("ＭＳ ゴシック", Font.BOLD, 20);
	public static Font SMALL_FONT = new Font("ＭＳ ゴシック", Font.BOLD, 15);
	public static String[] PRESET_PARAM_COLUMN_NAMES = {"パラメータ名", "説明", "値"};

	public static LineBorder NONE_SELECTED_LINE_BORDER = new LineBorder(Color.BLACK, 3);
	public static LineBorder SELECTED_LINE_BORDER = new LineBorder(Color.RED, 3);
	public static int MAP_SIZE = 400;
}
