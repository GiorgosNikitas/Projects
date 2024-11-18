package model.Position;

import model.Player.Player;

public abstract class OnePlayerDicePosition extends DicePosition{

	public OnePlayerDicePosition(int number, String day, String logo) {
		super(number, day, logo);
	}
	
	//public abstract void performAction(Player p1, int diceNumber);
}
