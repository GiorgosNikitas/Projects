package model.Position;

import model.Card.DealCard;
import model.Player.Player;

public class BuyerPosition extends OnePlayerDicePosition {

	public BuyerPosition(int number, String day) {
		super(number, day, "images/buyer.png");
	}

	@Override
	public void performAction(Player[] p, int current) {
		// TODO Auto-generated method stub
		
	}

}
