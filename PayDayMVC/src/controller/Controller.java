package controller;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import model.Board.Board;
import model.Player.Player;
import view.ViewGraphics;

public class Controller {
	private Player[] players;
	private Board board;
	private int currentPlayer;
	private int months;
	private int currentMonth;
	private int firstMove;
	private int jackpot = 0;
	private ViewGraphics view;
	private Controller controller;
	public static final Object diceT = new Object();
	public static final Object endTurnT = new Object();
	public static final Object mailCardT = new Object();
	
	/**
	 * Constructor
	 * 
	 * PostCondition: initializes the attributes of the class, creates JOptionPane for the 
	 * 				  input of the total months of the game, randomly chooses the first player.
	 * @param players array containing the 2 players of the game
	 */
	public Controller(Player[] players, Board board, ViewGraphics view) {
		JFrame f = new JFrame();   
	    String input = JOptionPane.showInputDialog(f,"How many months is the game going to last?\nEnter a number 1-3:");
	    months = Integer.parseInt(input);
	    
		firstMove = new Random().nextInt(2);
		currentPlayer = firstMove;
		players[currentPlayer].hasToPlay = true;
		this.view = view;
		this.board = board;
		this.players = players;
		
		this.controller = controller;
		this.view.addDiceListener(new DiceListener());
		this.view.addEndTurnListener(new EndTurnListener());
		this.view.addGetLoanListener(new GetLoanListener());
		this.view.addMailCardListener(new MailCardListener());
	}
	 
	/**
	 * <b>Transformer(Setter)</b>
	 * <p>
	 * <b>Precondition:</b> 1 <= months <= 3
	 * <p>
	 * <b>Postcondition:</b> Set value to months
	 * <p>
	 * @param months the months-rounds of the game
	 */
	public void setMonths(int months) {}
	
	/**
	 * <b>Accessor(Getter)</b>
	 * @return the months of the game
	 */
	//public int getMonths() {}
	
	/**
	 * <b>Accessor(Getter)</b>
	 * @return the current month of the game
	 */
	//public int getCurrentMonth() {}
	
	/**
	 * <b>Observer</b>
	 * @return true if game has ended, else false
	 */
	public boolean isGameFinished() {
		return players[0].getMonth() == months && players[1].getMonth() == months 
			   && players[0].getPosition() == 31 && players[1].getPosition() == 31;
	}
	
	/**
	 * Displays a frame showing the winner of the game.
	 */
	public void displayWinner() {
		view.repaintFrame();
		String winner;
		if (players[0].score() > players[1].score()) {
			winner = players[0].getName();
		} else {
			winner = players[1].getName();
		}
		JFrame f = new JFrame();  
		f.setPreferredSize(new Dimension(200, 100));
		f.setLayout(null);
		f.setBounds(200, 200, 200, 100);
		
		JLabel label = new JLabel(winner + " wins!");
		label.setBounds(50, 0, 100, 20);
		
		JButton exit = new JButton("Close Game");
		exit.setBounds(40, 20, 120, 30);
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				f.dispose();
				System.exit(0);
			} 
		
		});
		
		f.add(label);
		f.add(exit);
		f.pack();
		f.setVisible(true);
		
		AudioInputStream audioInputStream;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/weAreTheChampions.wav"));
			Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.loop(10);
	        clip.start();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <b>Transformer</b>
	 * <p>
	 * <b>Precondition:</b> Game has not ended
	 * <p>
	 * <b>Postcondition:</b> Change currentPlayer after the end of his turn
	 * <p>
	 */
	public void setCurrentPlayer() {}
	
	/**
	 * <b>Accessor(Getter)</b>
	 * @return the player who has turn to play
	 */
	//public Player getCurrrentPlayer() {}
	
	/**
	 * <b>Transformer</b>
	 * <p>
	 * <b>Precondition:</b> The game hasn't started yet
	 * <p>
	 * <b>Postcondition:</b> Initialization of the board and the players.
	 * <p>
	 */
	//public void initGame();
	
	/**
	 * <b>Transformer</b>
	 * <p>
	 * <b>Postcondition:</b> currentPlayer played his turn.
	 * <p>
	 */
	public void playTurn() {
		if (players[currentPlayer].hasToPlay) {
			synchronized(diceT) {
				try {
					diceT.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		players[currentPlayer].diceThrown = false;
		view.removePawn(currentPlayer);
		players[currentPlayer].movePlayer();
		giveJackpot();
		view.drawPawns();
		if (players[currentPlayer].hasToPlay) {
			board.positions.get(players[currentPlayer].getPosition()).performAction(players, currentPlayer);
			view.updatePlayerPanels();
			if (board.positions.get(players[currentPlayer].getPosition()).getClass().getSimpleName().equals("SingleMailCardPosition")) {
				view.updateInfoBox(players[currentPlayer].getName() + " has to draw a Mail Card");
				players[currentPlayer].drawCard = true;
				synchronized(mailCardT) {
					try {
						mailCardT.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				view.updatePlayerPanels();
				view.repaintFrame();
			} else if (board.positions.get(players[currentPlayer].getPosition()).getClass().getSimpleName().equals("DoubleMailCardPosition")) {
				view.updateInfoBox(players[currentPlayer].getName() + " has to draw 2 Mail Cards");
				players[currentPlayer].drawCard = true;
				synchronized(mailCardT) {
					try {
						mailCardT.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				view.updatePlayerPanels();
				view.repaintFrame();
				
				players[currentPlayer].drawCard = true;
				synchronized(mailCardT) {
					try {
						mailCardT.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				view.updatePlayerPanels();
				view.repaintFrame();
			}
			jackpot += players[currentPlayer].getMoneyForJackpot();
			players[currentPlayer].setMoneyForJackpot(0);
			
			view.updateJackpot(jackpot);
			view.updateInfoBox(players[currentPlayer].getName() + ": press End Turn button to finish" + "\n" + "your turn.");
		} 
		synchronized(endTurnT) {
			try {
				endTurnT.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Transfrormer
	 * Changes currentPlayer at the end of each Turn
	 */
	public void updateCurrentPlayer() {
		players[currentPlayer].hasToPlay = false;
		currentPlayer = (currentPlayer + 1)%2;
		players[currentPlayer].hasToPlay = true; 
		if (players[currentPlayer].getPosition() == 31 && players[currentPlayer].getMonth() == months) {
			players[currentPlayer].hasToPlay = false;
		} else if(players[currentPlayer].getPosition() == 31) {
			view.removePawn(currentPlayer);
			players[currentPlayer].setPosition(0);
			view.drawPawns();
			players[currentPlayer].setMonth(players[currentPlayer].getMonth() + 1);
		}
	}
	
	public void giveJackpot() {
		if (players[currentPlayer].getDice() == 6 && jackpot != 0) {
			players[currentPlayer].addMoney(jackpot);
			view.updateInfoBox(players[currentPlayer].getName() + " won " + jackpot + " from jackpot!");
			jackpot = 0;
			view.updateJackpot(jackpot);
		}
	}
	
	public void cryptoThursday(Player p) {}
	
	public void footballSunday(Player p) {}
	
	public JButton matchButton() {
		if (players[currentPlayer].index == 0) {
			return view.p1Dice;
		} else {
			return view.p2Dice;
		}
	}
	
	public Image diceSide(int x) throws IOException {
		Image img; 
    	String imgName;
		switch (x) {
			case 1:
				imgName = "images/dice-1.jpg";
				break;
			case 2:
				imgName = "images/dice-2.jpg";
				break;
			case 3:
				imgName = "images/dice-3.jpg";
				break;
			case 4:
				imgName = "images/dice-4.jpg";
				break;
			case 5:
				imgName = "images/dice-5.jpg";
				break;
			default:
				imgName = "images/dice-6.jpg";
				break;
		}
		img = ImageIO.read(new File(imgName));
		img = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
		return img;
	}
	
	public void displayMessage() {
		int currentMonth = players[0].getMonth();
		if (players[1].getMonth() > currentMonth) {
			currentMonth = players[1].getMonth();
		}
		view.updateInfoBox("Turn: " + players[currentPlayer].getName() +
							"\nMonth " + currentMonth + " of " + months);
	}
	
	public static void main(String[] args) throws IOException {
		
		Board board = new Board();
		
		Player[] players = new Player[2];
		players[0] = new Player("Player 1", 0);
		players[1] = new Player("Player 2", 1);
		
		ViewGraphics view = new ViewGraphics(players, board.positions);
		
		Controller controller = new Controller(players, board, view);
		
		while(!controller.isGameFinished()) {
			controller.displayMessage();
			controller.playTurn();
		}
		controller.displayWinner();
	}
	
	class DiceListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton) e.getSource();
			if ( (players[currentPlayer].index == 0 && (button == view.p1RollDice || button == view.p1Dice)) 
				|| (players[currentPlayer].index == 1 && (button == view.p2RollDice || button == view.p2Dice)) 
				&& players[currentPlayer].getPosition() != 31) {
	        		Random rand = new Random();
					int diceNumber = rand.nextInt(6) + 1;
					try {
						matchButton().setIcon(new ImageIcon(diceSide(diceNumber)));
						AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/diceRoll.wav"));
						Clip clip = AudioSystem.getClip();
		                clip.open(audioInputStream);
		                clip.loop(0);
		                clip.start();
		                players[currentPlayer].diceThrown = true;
		                players[currentPlayer].setDice(diceNumber);
					} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e1) {
						e1.printStackTrace();
					}
					synchronized(diceT) {
						diceT.notify();
					}
				}	
			} 
			
		}
		
	
	class EndTurnListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
			JButton button = (JButton) e.getSource();
			if ( (players[currentPlayer].index == 0 && button == view.p1EndTurn) ||
				 (players[currentPlayer].index == 1 && button == view.p2EndTurn)) {
				updateCurrentPlayer();
				synchronized(endTurnT) {
					endTurnT.notify();
				}
			}
		}
        
    }
	
	class GetLoanListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			int loan = 0;
			JButton button = (JButton) e.getSource();
			if ( (players[currentPlayer].index == 0 && button == view.p1GetLoan) ||
					 (players[currentPlayer].index == 1 && button == view.p2GetLoan) ) {
				do {
					JFrame f = new JFrame();   
				    String input = JOptionPane.showInputDialog(f,"Enter the amount of money you want to borrow\n"
				    										   + "(must be a multiple of 1000):");
				    loan = Integer.parseInt(input);
				} while (loan % 1000 != 0);
				players[currentPlayer].setLoan(players[currentPlayer].getLoan() + loan);
				players[currentPlayer].addMoney(loan);
				view.updateInfoBox(players[currentPlayer].getName() + " got loan " + loan +"€.");
				view.updatePlayerPanels();
			}
		}
	}
	
	class MailCardListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (board.positions.get(players[currentPlayer].getPosition()).getClass().getSimpleName().equals("SingleMailCardPosition")
				|| board.positions.get(players[currentPlayer].getPosition()).getClass().getSimpleName().equals("DoubleMailCardPosition") || players[currentPlayer].drawCard) {
				view.removePawn(currentPlayer);
				board.mailCards.drawCard().MCAction(players, currentPlayer, board);
				view.drawPawns();
				synchronized(mailCardT) {
					mailCardT.notify();
				}
				players[currentPlayer].drawCard = false;
			}
		}
		
	}
}
