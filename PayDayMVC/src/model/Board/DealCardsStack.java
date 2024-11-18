package model.Board;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import model.Card.Card;
import model.Card.DealCard;

public class DealCardsStack extends CardsStack {
	DealCard[] dealCards = new DealCard[20];
	
	@Override
	public void initCards() {
		top = dealCards.length - 1;
	}

	@Override
	public Card drawCard() {
		DealCard d;
		d = dealCards[top];
		top--;
		if (top < 0) {
			top = dealCards.length - 1;
		}
		return d;
	}

	@Override
	public boolean hasCards() {
		return top >= 0;
	}

	@Override
	public void shuffleCards() {
		List<DealCard> list = Arrays.asList(dealCards);
	    Collections.shuffle(list);
	    list.toArray(dealCards);
	    top = dealCards.length - 1;
	}

}
