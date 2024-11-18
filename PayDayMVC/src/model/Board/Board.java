package model.Board;

import java.util.ArrayList;
import java.util.Collections;

import model.Card.DealCard;
import model.Card.MailCard;
import model.Position.BuyerPosition;
import model.Position.CasinoPosition;
import model.Position.DealCardPosition;
import model.Position.DoubleMailCardPosition;
import model.Position.LotteryPosition;
import model.Position.PayDayPosition;
import model.Position.Position;
import model.Position.RadioPosition;
import model.Position.SingleMailCardPosition;
import model.Position.StartPosition;
import model.Position.SweepstakesPosition;
import model.Position.YardSalePosition;

public class Board {
	DealCardsStack dealCards;
	public MailCardsStack mailCards;
	public ArrayList<Position> positions = new ArrayList<Position>();
	
	public Board() {
		initBoard();
	}
	
	/**
	 * <b>Transformer</b>
	 * <p>
	 * <b>Precondition:</b> Must not be used in the middle of a month
	 * <p>
	 * <b>Postcondition:</b> Random initialization of the board's positions
	 * <p>
	 */
	public void initPositions() {
		for (int i = 0; i < 4; i++) {
			positions.add(new SingleMailCardPosition(0, ""));
			positions.add(new DoubleMailCardPosition(0, ""));
		}
		for (int i = 0; i < 5; i++) {
			positions.add(new DealCardPosition(0, ""));
		}
		for (int i = 0; i < 2; i++) {
			positions.add(new SweepstakesPosition(0, ""));
			positions.add(new RadioPosition(0, ""));
			positions.add(new CasinoPosition(0, ""));
			positions.add(new YardSalePosition(0, ""));
		}
		for (int i = 0; i < 3; i++) {
			positions.add(new LotteryPosition(0, ""));
		}
		for (int i = 0; i < 6; i++) {
			positions.add(new BuyerPosition(0, ""));
		}
		Collections.shuffle(positions);
		positions.add(new PayDayPosition(31, "Sunday"));
		positions.add(0, new StartPosition());
		for (int i = 1; i < positions.size(); i++) {
			positions.get(i).setNumber(i);
			switch(i%7) {
				case(1):
					positions.get(i).setDay("Monday");
					break;
				case(2):
					positions.get(i).setDay("Tuesday");
					break;
				case(3):
					positions.get(i).setDay("Wednesday");
					break;
				case(4):
					positions.get(i).setDay("Thursday");
					break;
				case(5):
					positions.get(i).setDay("Friday");
					break;
				case(6):
					positions.get(i).setDay("Saturday");
					break;
				default:
					positions.get(i).setDay("Sunday");
					break;
			}
		}
	}
	
	/**
	 * <b>Transformer</b>
	 * <p>
	 * <b>Precondition:</b> Must not be used in the middle of a month
	 * <p>
	 * <b>Postcondition:</b> Initialization of board's positions, mail and deal card stacks
	 * <p>
	 */
	public void initBoard() {
		initPositions();
		mailCards = new MailCardsStack();
	}
}
