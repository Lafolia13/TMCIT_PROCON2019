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
//		new MainFrame();
		
		Network net = new Network("http://127.0.0.1:52782", "procon30_example_token");
		
		Action a1 = new Action();
		a1.agentID = 10;
		a1.apply = 0;
		a1.dx = 1;
		a1.dy = -1;
		a1.turn = 10;
		a1.type = "move";
		
		Actions actions = new Actions();
		actions.actions.add(a1);
		
		try {
			net.postAction(1, actions);
//			net.getMatcheStatus(1);
		} catch (InvalidTokenException e) {
			e.printStackTrace();
		} catch (InvalidMatchesException e) {
			System.out.println(e.startUnixTime);
			e.printStackTrace();
		} catch (TooEarlyException e) {
			// TODO é©ìÆê∂ê¨Ç≥ÇÍÇΩ catch ÉuÉçÉbÉN
			e.printStackTrace();
		} catch (UnacceptableTimeExeption e) {
			e.printStackTrace();
		}
	}

}
