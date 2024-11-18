package model.Player;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import model.Card.DealCard;

public class Player extends PlayerADT {
	
	private AudioInputStream audioInputStream;
	private Clip clip;

	public Player(String name, int index) {
		this.name = name;
		this.index = index;
		initPlayer();
		if (index == 0) {
			this.pawn = "images/pawn_blue.png";
		} else {
			this.pawn = "images/pawn_yellow.png";
		}
		month = 1;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setMoney(int money) {
		this.money = money;
	}

	@Override
	public int getMoney() {
		return money;
	}

	@Override
	public void setLoan(int loan) {
		this.loan = loan;
		
	}

	@Override
	public boolean hasLoan() {
		if (loan > 0) {
			return true;
		}
		return false;
	}

	@Override
	public int getLoan() {
		return loan;
	}

	@Override
	public void setBills(int bills) {
		this.bills = bills;
	}

	@Override
	public int getBills() {
		return bills;
	}

	@Override
	public int getDice() {
		return dice;
	}

	@Override
	public void setPosition(int position) {
		this.position = position;
		
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public void initPlayer() {
		position = 0;
		money += 3500;
	}

	@Override
	public int score() {
		return money - loan;
	}

	@Override
	public void addCard(DealCard d) {
		ownedCards.add(d);
		
	}

	@Override
	public void displayCards() {
		
	}  

	@Override
	public void setDice(int dice) {
		this.dice = dice;
		
	}

	@Override
	public DealCard sellCard(DealCard d) {
		ownedCards.remove(d);
		return d;
	}

	@Override
	public void movePlayer() {
		position += dice;
		if (position > 31) {
			position = 31;
		}
	}

	@Override
	public int throwDice() {
		Random rand = new Random();
		dice = rand.nextInt(6) + 1;
		return dice;
	}

	@Override
	public void setPawn(String pawn) {
		this.pawn = pawn;
		
	}

	@Override
	public String getPawn() {
		return pawn;
	}

	@Override
	public void addMoney(int money) {
		this.money += money;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/coinSound.wav"));
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}
        try {
			clip.open(audioInputStream);
		} catch (LineUnavailableException | IOException e) {
			e.printStackTrace();
		}
        clip.loop(0);
        clip.start();
		
	}

	@Override
	public void removeMoney(int money) {
		this.money -= money;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/loseMoneySound.wav"));
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}
        try {
			clip.open(audioInputStream);
		} catch (LineUnavailableException | IOException e) {
			e.printStackTrace();
		}
        clip.loop(0);
        clip.start();
	}

	@Override
	public void setMonth(int month) {
		this.month = month;
	}

	@Override
	public int getMonth() {
		return month;
	}

	@Override
	public void setMoneyForJackpot(int moneyForJackpot) {
		this.moneyForJackpot = moneyForJackpot;
	}

	@Override
	public int getMoneyForJackpot() {
		return moneyForJackpot;
	}
	
}
