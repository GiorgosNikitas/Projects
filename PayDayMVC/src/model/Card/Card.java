package model.Card;

import model.Player.Player;

public class Card {
	protected String message;
	protected String image;
	
	public Card(String message, String image) {
		this.message = message;
		this.image = image;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public String getImage() {
		return image;
	}
	
	
}