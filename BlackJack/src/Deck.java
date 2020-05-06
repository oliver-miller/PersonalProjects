/**
 * Class that will contain 52 Card.java instances to make up one complete deck.
 * @author Oliver
 *
 */
public class Deck {
	
	private static int NEXT_CARD = 0;
	private final int FACE_CARD_VALUE = 10;
	private final int[] ACE_CARD_VALUE = {11, 1};
	private final int NUM_OF_DECKS = 1;
	private final String[] SUITS = {"Diamonds", "Hearts", "Clubs", "Spades"};
	private final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
	
	public Card[] deck = new Card[NUM_OF_DECKS * SUITS.length * RANKS.length];

	/**
	 * Creates a deck (always in the same order).
	 */
	public void create() {
		int cardNum = 0;
		
		for(int numOfDeck = 0; numOfDeck < NUM_OF_DECKS; numOfDeck++) {
			for(int i = 0; i < SUITS.length; i++) {
				for(int j = 0; j < RANKS.length; j++) {
					
					// Generate new Card and assign it to the deck
					Card currentCard = new Card();
					deck[cardNum] = currentCard;
					
					// Give that card a suit, rank and value
					currentCard.setSuit(SUITS[i]);
					currentCard.setRank(RANKS[j]);
					if(j < 9) currentCard.setValue(j + 2);
					if(j >= 9 && !currentCard.getSuit().equals("Ace")) currentCard.setValue(FACE_CARD_VALUE);
					if(j == 12) currentCard.setValue(ACE_CARD_VALUE[0]);
					
					// Increment to fill all 52 cards in a deck
					cardNum ++;
				}
			}
		}
	}
	
	/**
	 * Shuffles deck.
	 */
	public void shuffle() {
		for (int k = 0; k < deck.length; k++) {
            int r = k + (int) (Math.random() * (deck.length-k));
            Card temp = deck[r];
            deck[r] = deck[k];
            deck[k] = temp;
        }
	}
	
	/**
	 * Deals a card to a player.
	 * @return a card of type Card
	 */
	public Card getNextCard() {
		return deck[NEXT_CARD++];
	}

	public int[] getAceValue() {
		return ACE_CARD_VALUE;
	}
	
	
	
}
