package model.Position;

import model.Player.Player;

public class SweepstakesPosition extends OnePlayerDicePosition {

	public SweepstakesPosition(int number, String day) {
		super(number, day, "images/sweep.png");
	}

	@Override
	public void performAction(Player[] p, int current) {
		p[current].addMoney(1000 * p[current].getDice());
	}
}
