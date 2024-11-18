package model.Board;

import model.Card.Card;

public abstract class CardsStack {
	protected int top;
	
	/**
	 * <b>Transformer</b>
	 * <p>
	 * <b>Postcondition:</b> Creates the cards and shuffles them.
	 * <p> 
	 */
	public abstract void initCards();
	
	/**
	 * <b>Accessor</b> 
	 * <p> 
	 * <b>Precondition:</b> Card array not empty
	 * <p>
	 * <b>Postcondition:</b> Returns the card at the top of the stack. Reduces top by 1.
	 * Shuffles and reuses all cards if stack gets empty.
	 * <p>
	 * @return top card of the stack
	 */
	public abstract Card drawCard();
	
	/**
	 * <b>Observer</b>
	 * @return true is stack not empty, else false
	 */
	public abstract boolean hasCards();
	
	/**
	 * <b>Transformer</b>
	 * <p>
	 * <b>Postcondition:</b> Shuffles the cards of the stack.
	 * <p>
	 */
	public abstract void shuffleCards();
}
