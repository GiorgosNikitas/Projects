package model.Position;

import model.Player.Player;

public abstract class Position {
	private int number;
	private String day;
	private String logo;
	
	/**
	 * <b>Constructor</b>: Constructs a new Position with the given date and day of the week.
	 */
	Position(int number, String day, String logo) {
		this.number = number;
		this.day = day;
		this.logo = logo;
	}	
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public int getNumber() {
		return number;
	}
	
	public void setDay(String day) {
		this.day = day;
	}
	
	public String getDay() {
		return day;
	}
	
	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	public String getLogo() {
		return logo;
	}
	
	public abstract void performAction(Player[] p, int current);
}	