package model.Position;

import model.Player.Player;

public abstract class CardPosition extends Position {

	CardPosition(int number, String day, String logo) {
		super(number, day, logo);
		// TODO Auto-generated constructor stub
	}

	public abstract void performAction();
}
