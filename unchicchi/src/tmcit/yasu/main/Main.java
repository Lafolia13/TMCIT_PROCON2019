package tmcit.yasu.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tmcit.yasu.data.Action;
import tmcit.yasu.data.Actions;
import tmcit.yasu.exception.InvalidMatchesException;
import tmcit.yasu.exception.InvalidTokenException;
import tmcit.yasu.exception.TooEarlyException;
import tmcit.yasu.exception.UnacceptableTimeExeption;
import tmcit.yasu.ui.MainFrame;
import tmcit.yasu.util.Network;

public class Main {

	public static void main(String[] args) {
		new MainFrame();
	}

}
