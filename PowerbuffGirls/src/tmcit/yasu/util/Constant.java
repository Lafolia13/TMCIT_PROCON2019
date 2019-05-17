package tmcit.yasu.util;

import java.awt.Color;
import java.awt.Font;

import javax.swing.border.LineBorder;

public class Constant {
	// UI
	public static LineBorder LINE_BORDER = new LineBorder(Color.BLACK, 1);
	public static Font MAIN_FONT = new Font("ＭＳ ゴシック", Font.BOLD, 15);
	
	// solver setting
	public static String[] PARAM_COLUMN_NAMES = {"パラメータ名", "初期値", "最小値", "最大値", "幅", "説明"};
}
