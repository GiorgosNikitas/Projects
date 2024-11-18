package model.Player;

import java.util.ArrayList;
import java.util.List;

import model.Card.DealCard;

public abstract class PlayerADT {
	protected String name = "Player";
	protected int money = 0;
	protected int moneyForJackpot = 0;
	protected int loan = 0;
	protected int bills = 0;
	protected int dice = 0;
	protected int position = 0;
	protected String pawn;
	protected int month;
	public int index;
	public boolean diceThrown;
	public boolean hasToPlay;
	public boolean drawCard;
	List<DealCard> ownedCards = new ArrayList<DealCard>();
	
	/**
	 * <b>Transformer(Setter)</b>
	 */
	public abstract void setName(String name);
	
	/**
	 * <b>Accesor(Getter)</b>
	 * @return Player's name
	 */
	public abstract String getName();
	
	/**
	 * <b>Transformer(Setter)</b>
	 */
	public abstract void setMoney(int money);
	
	/**
	 * <b>Accessor(Getter)</b>
	 * @return Player's money
	 */
	public abstract int getMoney();
	
	/**
	 * <b>Transformer(Setter)</b>
	 */
	public abstract void setLoan(int loan);
	
	/**
	 * <b>Observer</b>
	 * <p>
	 * <b>Checks if player has unpaid loan.</b>
	 * <p>
	 * @return true if loan != 0 else false
	 */
	public abstract boolean hasLoan();
	
	/**
	 * <b>Accessor(Getter)</b>
	 * @return Player's loan
	 */
	public abstract int getLoan();
	
	/**
	 * <b>transformer(Setter)</b>
	 */
	public abstract void setBills(int bills);
	
	/**
	 * <b>Accessor(Getter)</b>
	 * @return player's bills that he has to pay
	 */
	public abstract int getBills();
	
	/**
	 * <b>Transformer(Setter)</b>
	 */
	public abstract void setDice(int dice);
	
	/**
	 * <b>Accessor(Getter)</b>
	 * @return Player's last dice throw
	 */
	public abstract int getDice();
	
	/**
	 * <b>Transformer(Setter)</b>
	 */
	public abstract void setMonth(int month);
	
	/**
	 * <b>Accessor(Getter)</b>
	 * @return Player's last dice throw
	 */
	public abstract int getMonth();
	
	/**
	 * <b>Transformer(Setter)</b>
	 */
	public abstract void setPosition(int position);
	
	/**
	 * <b>Getter</b>
	 * @return Player's position on board
	 */
	public abstract int getPosition();
	
	/**
	 * <b>Transformer(Setter)</b>
	 */
	public abstract void setPawn(String pawn);
	
	/**
	 * <b>Accesor(Getter)</b>
	 * @return Player's name
	 */
	public abstract String getPawn();
	
	/**
	 * <b>Transformer(Setter)</b>
	 */
	public abstract void setMoneyForJackpot(int moneyForJackpot);
	
	/**
	 * <b>Getter</b>
	 * @return Player's position on board
	 */
	public abstract int getMoneyForJackpot();
	
	/**
	 * <b>Transformer</b>
	 * <p>
	 * <b>Postcondition:</b> Player's data are initialized
	 * <p>
	 */
	public abstract void initPlayer();
	
	/**
	 * <b>Observer</b>
	 * <p>
	 * <b>Postcondition:</b> Calculates player's score
	 * @return player's score
	 */
	public abstract int score();
	
	/**
	 * <b>Transformer</b>
	 * <p>
	 * <b>Postcondition:</b> Adds a given card to player's collection
	 * @param d the card to be added in player's collection
	 */
	public abstract void addCard(DealCard d);
	
	/**
	 * <b>Observer</b>
	 * <p>
	 * <b>Postcondition:</b> Displays the cards that player has in his collection
	 */
	public abstract void displayCards();
	
	/**
	 * <b>Transformer</b>
	 * <p>
	 * <b>Precondition:</b> Player has at least 1 card.
	 * <p>
	 * <b>Postcondition:</b> A card is removed from player's collection in exchange for its sell price
	 * <p>
	 * @param d the card to be selled 
	 * @return 
	 */
	public abstract DealCard sellCard(DealCard d);
	
	/**
	 * <b>Transformer</b>
	 * <p>
	 * <b>Postcondition:</b> Move player according to his dice roll.
	 * <p>
	 */
	public abstract void movePlayer();
	
	/**
	 * <b>Transformer</b>
	 * @return a random int 1-6.
	 */
	public abstract int throwDice();
	
	public abstract void addMoney(int money);
	
	public abstract void removeMoney(int money);
}