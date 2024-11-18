package model.Position;

import model.Player.Player;

public abstract class BothPlayersDicePosition extends DicePosition{

	public BothPlayersDicePosition(int number, String day, String logo) {
		super(number, day, logo);
	}

	//public abstract void performAction(Player p1, Player p2, int diceNumber);
}
