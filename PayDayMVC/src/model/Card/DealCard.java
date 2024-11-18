package model.Card;

import model.Player.Player;

public class DealCard extends Card {
	private int buyPrice;
	private int sellPrice;
	
	public DealCard(String message, String image, int buyPrice, int sellPrice) {
		super(message, image);
		this.buyPrice = buyPrice;
		this.sellPrice = sellPrice;
	}
	
	public void setBuyPrice(int buyPrice) {
		this.buyPrice = buyPrice;
	}
	
	public int getBuyPrice() {
		return buyPrice;
	}
	
	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}
	
	public int getSellPrice() {
		return sellPrice;
	}

}