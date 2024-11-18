package model.Card;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import model.Board.Board;
import model.Player.Player;

public class GetPaidByNeighborMC extends MailCard {
	
	public int money = 80;
	Image img;
	JFrame frame;
	
	public GetPaidByNeighborMC() {
		super("Εφτιαξες τον υπολογιστή του γείτονα", "images/cardImages/format.jpeg");
		
		
	}
	
	class GetPaidByNeighborActionListener implements ActionListener  {

		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
		}
		
	}

	/**
	 *
	 */
	@Override
	public void MCAction(Player[] p, int current, Board board) {
		displayCard();
		p[current].addMoney(money);
		p[(current+1)%2].removeMoney(money);
	}

	@Override
	public void displayCard() {
		frame = new JFrame("Πληρωμή από γείτονα");
		frame.setPreferredSize(new Dimension(500, 200));
		frame.setBounds(200, 200, 500, 200);
		frame.setLayout(null);
		logo = new JLabel();
		logo.setBounds(20, 20, 100, 100);
		try {
			img = ImageIO.read(new File(super.image));
			img = img.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logo.setIcon(new ImageIcon(img));
		frame.add(logo);
		text = new JLabel(message);
		text.setBounds(150, 50, 400, 30);
		frame.add(text);
		button = new JButton("Πάρε " + money + "€ από τον αντίπαλο");
		button.setBounds(100, 130, 300, 30);
		button.addActionListener(new GetPaidByNeighborActionListener());
		frame.add(button);
		frame.pack();
		frame.setVisible(true);
	}
	
}
