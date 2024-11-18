package model.Position;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.Player.Player;

public class PayDayPosition extends Position{

	public PayDayPosition(int number, String day) {
		super(number, day, "images/pay.png");
	}

	@Override
	public void performAction(Player[] p, int current) {
		p[current].addMoney(3500);
		int fee = (int) (p[current].getLoan() * 0.1);
		
		int bills = p[current].getBills();
		int loan;
		if (p[current].getMoney() < bills) {
			do {
				JFrame f = new JFrame();   
			    String input = JOptionPane.showInputDialog(f,"Your money are not enough to pay the loan fee!"
			    										   + "\nEnter the amount of money you want to borrow\n"
			    										   + "(must be a multiple of 1000):");
			    loan = Integer.parseInt(input);
			} while (loan % 1000 != 0 && loan < bills);
			p[current].setLoan(p[current].getLoan() + bills);
			p[current].addMoney(bills);
		}
		p[current].removeMoney(bills);
		
		if (p[current].getMoney() < fee) {
			do {
				JFrame f = new JFrame();   
			    String input = JOptionPane.showInputDialog(f,"Your money are not enough to pay the loan fee!"
			    										   + "\nEnter the amount of money you want to borrow\n"
			    										   + "(must be a multiple of 1000):");
			    loan = Integer.parseInt(input);
			} while (loan % 1000 != 0 && loan < fee);
			p[current].setLoan(p[current].getLoan() + loan);
			p[current].addMoney(loan);
		}
		p[current].removeMoney(fee);
		
		if (p[current].getLoan() > 0) {
			int payBack = 0;
			do {
				JFrame f = new JFrame();   
			    String input = JOptionPane.showInputDialog(f,"Enter the amount of money you want to pay back:");
			    payBack = Integer.parseInt(input);
			} while (payBack % 1000 != 0);
			p[current].removeMoney(payBack);
			p[current].setLoan(p[current].getLoan() - payBack);
		}
	}

}
