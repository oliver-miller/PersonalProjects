/**
 * Class that will be instantiated for every card. Each card will have a member for its rank, suit and value.
 * @author Oliver
 *
 */
public class Card {
	
	private String suit;
	private String rank;
	private int value;

	public String getSuit() {
		return suit;
	}
	
	public void setSuit(String suit) {
		this.suit = suit;
	}
	
	public String getRank() {
		return rank;
	}
	
	public void setRank(String rank) {
		this.rank = rank;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
