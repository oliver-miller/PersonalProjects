import java.util.ArrayList;

public class Player {
	private String name;
	private int value;
	private ArrayList<Card> hand;
	
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
		this.value = value;
	}
	
}
