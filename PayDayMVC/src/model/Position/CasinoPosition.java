package model.Position;

import model.Player.Player;

public class CasinoPosition extends OnePlayerDicePosition {

	public CasinoPosition(int number, String day) {
		super(number, day, "images/casino.png");
	}

	@Override
	public void performAction(Player[] p, int current) {
		if (p[current].getDice() % 2 == 1) {
			p[current].removeMoney(500);
			p[current].setMoneyForJackpot(p[current].getMoneyForJackpot() + 500);
		} else {
			p[current].addMoney(500);
		}
	}

}
