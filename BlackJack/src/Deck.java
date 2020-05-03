
public class Deck {
	
	private final int FACE_CARD_VALUE = 10;
	private final String[] SUITS = {"Diamonds", "Hearts", "Clubs", "Spades"};
	private final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
	
	public void create() {
		Card[] deck = new Card[52];
		int cardNum = 0;
		for(int i = 0; i < SUITS.length; i++) {
			for(int j = 0; j < RANKS.length; j++) {
				
				Card currentCard = new Card();
				deck[cardNum] = currentCard;
				currentCard.setSuit(SUITS[i]);
				currentCard.setRank(RANKS[j]);
				if(j < 9) currentCard.setValue(j + 2);
				if(j >= 9) currentCard.setValue(FACE_CARD_VALUE);
				
				cardNum ++;
			}
		}
	}
	/**
	 * Shuffles deck
	 */
	public void shuffle() {
		
	}
}
