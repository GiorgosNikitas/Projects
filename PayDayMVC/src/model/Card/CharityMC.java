package model.Card;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import model.Board.Board;
import model.Player.Player;

public class CharityMC extends MailCard {
	public int money = 150;
	Image img;
	JFrame frame;
	
	public CharityMC() {
		super("Διάσωση χαλώνας καρέτα-καρέτα", "images/cardImages/caretta.jpg");	
	}
	
	class CharityActionListener implements ActionListener  {

		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
		}
		
	}

	@Override
	public void MCAction(Player[] p, int current, Board board) {
		displayCard();
		p[current].removeMoney(money);
		p[current].setMoneyForJackpot(p[current].getMoneyForJackpot() + money);
	}

	@Override
	public void displayCard() {
		frame = new JFrame("Φιλανθρωπία");
		frame.setPreferredSize(new Dimension(500, 200));
		frame.setLayout(null);
		frame.setBounds(200, 200, 500, 200);
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
		button = new JButton("Πλήρωσε " + money + " στο Jackpot");
		button.setBounds(100, 130, 300, 30);
		button.addActionListener(new CharityActionListener());
		frame.add(button);
		frame.pack();
		frame.setVisible(true);
	}
	
}
