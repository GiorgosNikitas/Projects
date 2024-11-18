package view;

import java.awt.Dimension;

import model.Player.Player;
import model.Position.Position;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class ViewGraphics extends JFrame {
	private JPanel tiles;
	private JButton[] buttonTiles = new JButton[32];
	private JLabel[] tileLabels = new JLabel[32];
	private JFrame main;
	private JPanel player1, player2;
	private JLabel p1Name, p1Money, p1Loan, p1Bills;
	public JButton p1RollDice, p1DealCards, p1GetLoan, p1EndTurn, p1Dice;
	private JLabel p2Name, p2Money, p2Loan, p2Bills;
	public JButton p2RollDice, p2DealCards, p2GetLoan, p2EndTurn, p2Dice; 
	private JButton mailCardsBtn;
	private JButton dealCardsBtn;
	private JTextArea infoBox;
	private JLabel jackpotLogo;
	private JPanel pawn1Panel;
	private JLabel p1Pawn;
	private JLabel p2Pawn;
	private JLabel background;
	private JLabel logo;
	private JPanel jackpotPanel;
	private JLabel jackpotMoney;
	private Clip clip;
    private AudioInputStream audioInputStream;
    private ArrayList<Position> positions;
    private Player[] players;
	
	public ViewGraphics(Player[] players, ArrayList<Position> positions) throws IOException {
		this.positions = positions;
		this.players = players;
		main = new JFrame("Pay Day");
		main.setPreferredSize(new Dimension(1000, 820));
		main.setLayout(null);
	    tiles = new JPanel();
	    tiles.setLayout(new GridLayout(5, 7)); 
	    tiles.setPreferredSize(new Dimension(700, 500));
	    Color c = new Color(0, 150, 0);
	    tiles.setBackground(c);
	    
	    logo = new JLabel();
	    BufferedImage logoImg = ImageIO.read(new File("images/logo.png"));
        Image image = logoImg.getScaledInstance(700, 160, Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(image);
        logo.setIcon(icon);
	    logo.setBounds(0, 0, 700, 160);
	    main.add(logo);
	   
	    initTiles(positions);
	    
	    drawPawns();
	    
	    initP1Panel();
	    
	    initP2Panel();
	    
	    infoBox = new JTextArea("INFO BOX");
	    infoBox.setBounds(720, 320, 250, 120);
	    infoBox.setEditable(false);
	    infoBox.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 3), 
				  		  BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
				  		  BorderFactory.createLoweredBevelBorder())));
	    main.add(infoBox);	  
	    
	    Image mailCards = ImageIO.read(new File("images/mailCard.png"));
	    mailCards = mailCards.getScaledInstance(120, 60, java.awt.Image.SCALE_SMOOTH);
	    mailCardsBtn = new JButton(new ImageIcon(mailCards));
	    mailCardsBtn.setBounds(720, 450, 120, 60);
	    main.add(mailCardsBtn);
	    
	    Image dealCards = ImageIO.read(new File("images/dealCard.png"));
	    dealCards = dealCards.getScaledInstance(120, 60, java.awt.Image.SCALE_SMOOTH);
	    dealCardsBtn = new JButton(new ImageIcon(dealCards));
	    dealCardsBtn.setBounds(850, 450, 120, 60);
	    main.add(dealCardsBtn);
	    
	    jackpotLogo = new JLabel();
	    Image jackpotImg = ImageIO.read(new File("images/jackpot.png"));
	    jackpotImg.getScaledInstance(213, 120, java.awt.Image.SCALE_SMOOTH);
	    jackpotLogo.setIcon(new ImageIcon(jackpotImg));
	    jackpotLogo.setOpaque(true);
	    jackpotPanel = new JPanel();
	    jackpotPanel.setLayout(null);
	    jackpotPanel.add(jackpotLogo);
	    jackpotLogo.setBounds(0, 0, 213, 90);
	    jackpotPanel.setBounds(445, 650, 213, 121);
	    jackpotPanel.setBackground(c);
	    jackpotMoney = new JLabel("Jackpot: 0");
	    jackpotMoney.setBounds(0, 90, 200, 20);
	    jackpotPanel.add(jackpotMoney);
	    main.add(jackpotPanel);
	    main.add(tiles);
	    
	    pawn1Panel = new JPanel();
	    p1Pawn = new JLabel();
	    Image bluePawn = ImageIO.read(new File("images/bluePawn.jpg"));
	    bluePawn = bluePawn.getScaledInstance(20, 50, java.awt.Image.SCALE_SMOOTH);
	    p1Pawn.setIcon(new ImageIcon(bluePawn));
	    pawn1Panel.add(p1Pawn);
	    buttonTiles[0].add(pawn1Panel);
	    
	    main.pack();
	    main.setVisible(true);
	    main.getContentPane().setBackground(c);
	    main.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void drawPawns() {
		if (players[0].getPosition() == players[1].getPosition()) {
			BufferedImage tile = null;
		    try {
				tile = ImageIO.read(new File(positions.get(players[0].getPosition()).getLogo()));
				tile = resize(tile, 100, 90);
			} catch (IOException e2) {
				e2.printStackTrace();
			}

		    BufferedImage pawn1 = null;

		    try {
				pawn1 = ImageIO.read(new File(players[0].getPawn()));
				pawn1 = resize(pawn1, 100, 90);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		    int w = Math.max(tile.getWidth(), pawn1.getWidth());
		    int h = Math.max(tile.getHeight(), pawn1.getHeight());

		    BufferedImage combined1 = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		    
		    Graphics g = combined1.getGraphics();
		    g.drawImage(tile, 0, 0, null);
		    g.drawImage(pawn1, -10, 0, null);

		    try {
				ImageIO.write(combined1, "PNG", new File("images/pawn1OnTile.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		    
		    combined1 = null;

		    try {
		    	combined1 = ImageIO.read(new File("images/pawn1OnTile.png"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		    
		    BufferedImage pawn2 = null;

		    try {
				pawn2 = ImageIO.read(new File(players[1].getPawn()));
				pawn2 = resize(pawn2, 100,90);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		    
		    w = Math.max(combined1.getWidth(), pawn2.getWidth());
		    h = Math.max(combined1.getHeight(), pawn2.getHeight());

		    BufferedImage combined2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		    
		    Graphics g2 = combined2.getGraphics();
		    g2.drawImage(combined1, 0, 0, null);
		    g2.drawImage(pawn2, 35, 0, null);
		    
		    try {
				ImageIO.write(combined2, "PNG", new File("images/bothPawnsOnTile.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		    
		    buttonTiles[players[0].getPosition()].setIcon(new ImageIcon(combined2));
		    main.repaint();
		} else {
			for (int i = 0; i < 2; i++) {
				BufferedImage tile = null;
			    try {
					tile = ImageIO.read(new File(positions.get(players[i].getPosition()).getLogo()));
					tile = resize(tile, 100, 90);
				} catch (IOException e2) {
					e2.printStackTrace();
				}
		
			    BufferedImage pawn = null;
		
			    try {
					pawn = ImageIO.read(new File(players[i].getPawn()));
					pawn = resize(pawn, 100, 90);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		
			    int w = Math.max(tile.getWidth(), pawn.getWidth());
			    int h = Math.max(tile.getHeight(), pawn.getHeight());
		
			    BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			    
			    Graphics g = combined.getGraphics();
			    g.drawImage(tile, 0, 0, null);
			    g.drawImage(pawn, 0, 0, null);
		
			    try {
					ImageIO.write(combined, "PNG", new File("images/pawn" + i + "OnTile.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			    
			    buttonTiles[players[i].getPosition()].setIcon(new ImageIcon(combined));
			    main.repaint();
			}
		}
	}
	
	public void removePawn(int index) {
		if (players[0].getPosition() == players[1].getPosition()) {
			int index2 = (index+1)%2;
			BufferedImage tile = null;
		    try {
				tile = ImageIO.read(new File(positions.get(players[index2].getPosition()).getLogo()));
				tile = resize(tile, 100, 90);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
	
		    BufferedImage pawn = null;
	
		    try {
				pawn = ImageIO.read(new File(players[index2].getPawn()));
				pawn = resize(pawn, 90, 80);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	
		    int w = Math.max(tile.getWidth(), pawn.getWidth());
		    int h = Math.max(tile.getHeight(), pawn.getHeight());
	
		    BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		    
		    Graphics g = combined.getGraphics();
		    g.drawImage(tile, 0, 0, null);
		    g.drawImage(pawn, 0, 0, null);
	
		    try {
				ImageIO.write(combined, "PNG", new File("images/pawn" + index2 + "OnTile.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		    
		    buttonTiles[players[index2].getPosition()].setIcon(new ImageIcon(combined));
		} else {
			BufferedImage tile = null;
		    try {
				tile = ImageIO.read(new File(positions.get(players[index].getPosition()).getLogo()));
				tile = resize(tile, 100, 90);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			buttonTiles[players[index].getPosition()].setIcon(new ImageIcon(tile));
		}
	}
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
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
	
	public void initP1Panel() {
		player1 = new JPanel();
	    player1.setLayout(null);
	    player1.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.blue, 3), 
	    				  BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
	    				  BorderFactory.createLoweredBevelBorder())));
	    player1.setBounds(720, 70, 250, 240);
	    
	    p1Name = new JLabel("Player 1");
	    player1.add(p1Name);
	    p1Name.setBounds(105 , 10, 100, 20);
	    
	    p1Money = new JLabel("Money: " + players[0].getMoney());
	    player1.add(p1Money);
	    p1Money.setBounds(10, 40, 100, 20);
	    
	    p1Loan = new JLabel("Loan: " + players[0].getLoan());
	    player1.add(p1Loan);
	    p1Loan.setBounds(10, 80, 100, 20);
	    
	    p1Bills = new JLabel("Bills: " + players[0].getBills());
	    player1.add(p1Bills);
	    p1Bills.setBounds(10, 120, 100, 20);
	    
	    p1DealCards = new JButton("My Deal Cards");
	    p1DealCards.setBounds(10, 180, 125, 20);
	    p1DealCards.setBackground(new Color(51, 153, 255));
	    player1.add(p1DealCards);
	    
	    p1GetLoan = new JButton("Get Loan");
	    p1GetLoan.setBounds(10, 210, 110, 20);
	    p1GetLoan.setBackground(new Color(51, 153, 255));
	    player1.add(p1GetLoan);
	    
	    p1EndTurn = new JButton("End Turn");
	    p1EndTurn.setBounds(130, 210, 110, 20);
	    p1EndTurn.setBackground(new Color(51, 153, 255));
	    player1.add(p1EndTurn);
		
		Random rand = new Random();
		Image img = null;
		try {
			img = ImageIO.read(new File("images/dice-1.jpg"));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		img = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
		
	    p1RollDice = new JButton("Roll Dice");
	    p1RollDice.setBounds(10, 150, 125, 20);
	    p1RollDice.setBackground(new Color(51, 153, 255));
	    player1.add(p1RollDice); 
	    
	    p1Dice = new JButton(new ImageIcon(img));
	    p1Dice.setBounds(165, 150, 50, 50);
	    player1.add(p1Dice);
	    
	    Color c1 = new Color(153, 204, 255);
	    player1.setBackground(c1);
	    main.add(player1);
	}
	
	public void initP2Panel() {
		player2 = new JPanel();
	    player2.setLayout(null);
	    player2.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.yellow, 3), 
	    				  BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), 
	    				  BorderFactory.createLoweredBevelBorder())));
	    player2.setBounds(720, 520, 250, 240);
	    
	    p2Name = new JLabel("Player 2");
	    player2.add(p2Name);
	    p2Name.setBounds(105 , 10, 100, 20);
	    
	    p2Money = new JLabel("Money: " + players[1].getMoney());
	    player2.add(p2Money);
	    p2Money.setBounds(10, 40, 100, 20);
	    
	    p2Loan = new JLabel("Loan: " + players[1].getLoan());
	    player2.add(p2Loan);
	    p2Loan.setBounds(10, 80, 100, 20);
	    
	    p2Bills = new JLabel("Bills: " + players[1].getBills());
	    player2.add(p2Bills);
	    p2Bills.setBounds(10, 120, 100, 20);
	    
	    p2DealCards = new JButton("My Deal Cards");
	    p2DealCards.setBounds(10, 180, 125, 20);
	    p2DealCards.setBackground(new Color(255,255,0));
	    player2.add(p2DealCards);
	    
	    p2GetLoan = new JButton("Get Loan");
	    p2GetLoan.setBounds(10, 210, 110, 20);
	    p2GetLoan.setBackground(new Color(255,255,0));
	    player2.add(p2GetLoan);
	    
	    p2EndTurn = new JButton("End Turn");
	    p2EndTurn.setBounds(130, 210, 110, 20);
	    p2EndTurn.setBackground(new Color(255,255,0));
	    player2.add(p2EndTurn);
	    
		Random rand = new Random();
		Image img = null;
		try {
			img = ImageIO.read(new File("images/dice-1.jpg"));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		img = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
		
		p2RollDice = new JButton("Roll Dice");
	    p2RollDice.setBounds(10, 150, 125, 20);
	    player2.add(p2RollDice);
	    p2RollDice.setBackground(new Color(255,255,0));
	    
	    p2Dice = new JButton(new ImageIcon(img));
	    p2Dice.setBounds(165, 150, 50, 50);
	    player2.add(p2Dice);
	 
	    Color c2 = new Color(255,255,153);
	    player2.setBackground(c2);   
	    main.add(player2);
	}
	

	public void addDiceListener(ActionListener listenForDiceButton){
		p1RollDice.addActionListener(listenForDiceButton);
		p1Dice.addActionListener(listenForDiceButton);
		p2RollDice.addActionListener(listenForDiceButton);
		p2Dice.addActionListener(listenForDiceButton);
	}
	
	public void addEndTurnListener(ActionListener listenForEndTurnButton) {
		p1EndTurn.addActionListener(listenForEndTurnButton);
		p2EndTurn.addActionListener(listenForEndTurnButton);
	}
	
	public void addGetLoanListener(ActionListener listenForGetLoanButton) {
		p1GetLoan.addActionListener(listenForGetLoanButton);
		p2GetLoan.addActionListener(listenForGetLoanButton);
	}
	
	public void addMailCardListener(ActionListener listenForMailCardButton) {
		mailCardsBtn.addActionListener(listenForMailCardButton);
	}
	
	
	public void initTiles(ArrayList<Position> positions) throws IOException {
	    Image[] img =  new Image[32];
	    for (int i = 0; i < positions.size(); i++) {
	    	tileLabels[i] = new JLabel(positions.get(i).getDay() + " " + positions.get(i).getNumber());
	    	if (i == 0) {
	    		tileLabels[i].setText(positions.get(i).getDay());
	    	}
	    	img[i] = ImageIO.read(new File(positions.get(i).getLogo()));
	    	img[i] = img[i].getScaledInstance(100, 90, java.awt.Image.SCALE_SMOOTH);	
	    	
	    	buttonTiles[i] =  new JButton();
	    	buttonTiles[i].setLayout(null);
	    	buttonTiles[i].setSize(50, 70);
	    	buttonTiles[i].add(tileLabels[i]);
	    	
	    	buttonTiles[i].setIcon(new ImageIcon(img[i]));
	    	buttonTiles[i].setMargin(new Insets(30, 0, 0, 0));
	    	buttonTiles[i].setBackground(Color.YELLOW);
	    	buttonTiles[i].setFocusPainted(false);
	    	
	    	tileLabels[i].setBounds(10, 5, 100, 20);
	    	tiles.add(buttonTiles[i]);
	    }
	 
	    tiles.setBounds(0,160,700,600);
	}		
	
	public void updateJackpot(int jackpot) {
		jackpotMoney.setText("Jackpot: " + jackpot);
	}

	public void updateInfoBox(String text) {
		infoBox.setText(text);
		infoBox.repaint();
	}
	
	public void updatePlayerPanels() {
		p1Money.setText("Money: " + players[0].getMoney());
		p1Loan.setText("Loan: " + players[0].getLoan());
		p1Bills.setText("Bills: " + players[0].getBills());
		player1.repaint();
		
		p2Money.setText("Money: " + players[1].getMoney());
		p2Loan.setText("Loan: " + players[1].getLoan());
		p2Bills.setText("Bills: " + players[1].getBills());
		player2.repaint();
	}
	
	public void repaintFrame() {
		main.repaint();
	}
}

