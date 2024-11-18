package model.Card;

import javax.swing.JButton;
import javax.swing.JLabel;

import model.Board.Board;
import model.Player.Player;

public abstract class MailCard extends Card {
	JButton button;
	JLabel logo;
	JLabel text;
	
	public MailCard(String message, String image) {
		super(message, image);
	}
	
	public abstract void MCAction(Player[] p, int current, Board board);
	
	public abstract void displayCard();
	
}
