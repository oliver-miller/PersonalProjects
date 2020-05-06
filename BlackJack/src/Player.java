import java.util.ArrayList;

/**
 * Class used to store a player's hand and all of its characteristics. Also used for the dealer.
 * @author Oliver
 *
 */
public class Player {
	private String name;
	private int value;
	private ArrayList<Card> hand = new ArrayList<Card>();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value += value;
	}
	
	public void addCard(Card card) {
		this.hand.add(card);
	}
	
	public ArrayList<Card> getHand() {
		return hand;
	}
	
	public int sumHand() {
		int sum = 0;
		for(int i = 0; i < this.getHand().size(); i++) {
			sum += this.getHand().get(i).getValue();
		}
		
		return sum;
	}
}
