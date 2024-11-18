package model.Position;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;

import model.Player.Player;

public class RadioPosition extends BothPlayersDicePosition {
	public EventDice e;
	private int[] num = new int[2];
	public static final Object diceT = new Object();
	
	public RadioPosition(int number, String day) {
		super(number, day, "images/radio.png");
	}

	@Override
	public void performAction(Player[] p, int current) {
		e = new EventDice(diceT);
		int index = -2;
		do {
			index += 2;
			synchronized(diceT) {
				try {
					diceT.wait();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			
			synchronized(diceT) {
				try {
					diceT.wait();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		} while(e.diceThrows.get(index) == e.diceThrows.get(index + 1));
		e.dispose();
		if (e.diceThrows.get(index) > e.diceThrows.get(index + 1)) {
			p[current].addMoney(1000);
		} else {
			p[(current+1)%2].addMoney(1000);
		}
		
	}
	
}
