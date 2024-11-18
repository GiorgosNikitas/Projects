package model.Position;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.Player.Player;

public class LotteryPosition extends BothPlayersDicePosition {
	private EventDice e;
	public static final Object diceT = new Object();
	
	public LotteryPosition(int number, String day) {
		super(number, day, "images/lottery.png");
	}

	@Override
	public void performAction(Player[] p, int current) {
		JFrame f = new JFrame("Lottery");  
		String choice;
	    
		int num1;
		do {
			choice = JOptionPane.showInputDialog(f,"Enter a number (1-6)");
			num1 = Integer.parseInt(choice);
		} while (num1 < 1 || num1 > 6);
	    
		int num2;
		do {
	    choice = JOptionPane.showInputDialog(f,"Enter a number (1-6), except " + num1);
	    num2 = Integer.parseInt(choice);
		} while(num2 < 1 || num2 > 6 || num2 == num1);
	    
	    e = new EventDice(diceT);
	    
	    int i = -1;
	    do {
	    	i++;
	    	synchronized(diceT) {
	    		try {
					diceT.wait();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
	    	}
	    } while(e.diceThrows.get(i) != num1 && e.diceThrows.get(i) != num2);
	    e.dispose();
	    
	    if (e.diceThrows.get(i) == num1) {
	    	p[current].addMoney(1000);
	    } else {
	    	p[(current+1)%2].addMoney(1000);
	    }
	}
	
}
