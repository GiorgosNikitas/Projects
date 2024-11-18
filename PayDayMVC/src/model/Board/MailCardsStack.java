package model.Board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import model.Card.AdvertisementMC;
import model.Card.BillMC;
import model.Card.Card;
import model.Card.CharityMC;
import model.Card.DealCard;
import model.Card.GetPaidByNeighborMC;
import model.Card.MailCard;
import model.Card.MoveToDealPositionMC;
import model.Card.PayTheNeighborMC;
import model.Position.Position;

public class MailCardsStack extends CardsStack {
	public ArrayList<MailCard> mailCards = new ArrayList<MailCard>();

	public MailCardsStack() {
		top = 47;
		initCards();
		shuffleCards();
	}
	
	@Override
	public void initCards() {
		top = mailCards.size() - 1;
		for (int i = 0; i < 48; i++) {
			mailCards.add(new PayTheNeighborMC());
			mailCards.add(new AdvertisementMC());
			mailCards.add(new BillMC());
			mailCards.add(new CharityMC());
			mailCards.add(new GetPaidByNeighborMC());
			mailCards.add(new MoveToDealPositionMC());
		}
	}

	@Override
	public MailCard drawCard() {
		MailCard m;
		m = mailCards.get(top);
		top--;
		if (top < 0) {
			top = mailCards.size() - 1;
		}
		return m;
	}

	@Override
	public boolean hasCards() {
		return top >= 0;
	}

	@Override
	public void shuffleCards() {
	    Collections.shuffle(mailCards);
	    top = mailCards.size() - 1;
	}
}
