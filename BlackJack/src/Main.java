import java.util.Scanner; 

/**
 * Main driver of the project.
 * @author Oliver
 *
 */
public class Main {
	
	/*
	 * Class instances
	 */
	private static Scanner kb = new Scanner(System.in);
	private static Deck deck = new Deck();
	private static Player player = new Player();
	private static Player dealer = new Player();
	
	/*
	 * Class variables
	 */
	private static int wager;
	private static String ready;
	private static String choice;
	
	/**
	 * Introduces game and starts the first round.
	 * @param args
	 */
	public static void main(String[] args) {	
		/*
		 * Welcome banner
		 */
		System.out.print("=======================================\n");
		System.out.print("WELCOME TO BLACKJACK!");
		System.out.print("\n=======================================\n\n");
		
		/*
		 * Prompt user to begin game when they are ready
		 */
		System.out.print("Are you ready to play? Yes/No. ");
		ready = kb.nextLine();
		ready = ready.toUpperCase();
		while(!ready.equals("YES")) {
			System.out.print("Answer 'Yes' to begin.");
			ready = kb.nextLine();
			ready = ready.toUpperCase();
		}
		
		// Start first round
		startRound();
	}
	
	/**
	 * Starts round. 
	 */
	private static void startRound() {
		/*
		 * Create and shuffle a deck at the start of every round
		 */
		deck.create();
		deck.shuffle();
		
		/*
		 * Initiate sequence of events for a round of blackjack.
		 */
		setWager();
		dealHands();
		System.out.print("\nDEALER'S HAND");
		System.out.print("\n" + dealer.getHand().get(0).getRank() + " of " + dealer.getHand().get(0).getSuit());
		printHand();
		if(dealer.sumHand() != 21) {
			if(player.sumHand() != 21) {
				playerTurn();
				if(player.sumHand() <= 21) dealerTurn();
			}
		}
		
		/*
		 * End of round (and start of a new one, potentially)
		 */
		finalOutcome();
		if(anotherRound()) startRound();	
	}
	
	/**
	 * Deal hands to dealer and player.
	 */
	private static void dealHands() {
		player.addCard(deck.getNextCard());
		dealer.addCard(deck.getNextCard());
		player.addCard(deck.getNextCard());
		dealer.addCard(deck.getNextCard());
	}
	
	/**
	 * Player's turn.
	 */
	private static void playerTurn() {	
		/*
		 * Prompt choice until players "Stays"
		 */
		while(true) {
			System.out.println();
			choice = kb.nextLine();
			choice = choice.toUpperCase();
			
			/*
			 * Player action
			 */
			//Stay
			if(choice.equals("STAY")) break;
			
			// Hit
			if(choice.equals("HIT")) {
				player.addCard(deck.getNextCard());
				printHand();
			}
			// Check for bust
			if(player.sumHand() > 21) {
				// Check for aces
				if(!checkForAces()) {
					printHand();
					System.out.print("\nBUST!");
					break;
				}
			}
		}
	}
	
	/**
	 * Dealer's turn.
	 */
	private static void dealerTurn() {
		int i = 2;
		/*
		 * Print dealer's hand
		 */
		System.out.print("\n\n\nDEALER'S HAND: ");
		System.out.print("\n" + dealer.getHand().get(0).getRank() + " of " + dealer.getHand().get(0).getSuit());
		System.out.print("\n" + dealer.getHand().get(1).getRank() + " of " + dealer.getHand().get(1).getSuit());

		/*
		 * Draw until hand value is between 17 and 21 or until bust
		 */
		while(dealer.sumHand() < 17) {
			dealer.addCard(deck.getNextCard());
			System.out.print("\n" + dealer.getHand().get(i).getRank() + " of " + dealer.getHand().get(i).getSuit());
			
			if(dealer.sumHand() > 21) {
				if(!checkForAces()) {
					System.out.print("\nBUST!");
					break;
				}
			}
			
			i++;
		}
	}
	
	/**
	 * Display the contents of the player's hand to the console.
	 */
	private static void printHand() {
		System.out.print("\n\nPLAYER'S HAND: ");
		for(int i = 0; i < player.getHand().size(); i++) {
			System.out.print("\n" + player.getHand().get(i).getRank() + " of " + player.getHand().get(i).getSuit());
		}
		System.out.print("\nThis hand has a value of " + player.sumHand());
	}
	
	/**
	 * State who wins and who loses the round.
	 */
	private static void finalOutcome() {
		System.out.print("\n\n===================================\n");
		System.out.print("ROUND END");
		System.out.print("\n===================================\n");
	
		/*
		 * Display winner
		 */
		if(dealer.sumHand() == 21 && dealer.getHand().size() == 2) {
			player.setBank(-wager);
			System.out.print("\nBLACKJACK! Dealer wins!");
		} else if (player.sumHand() == 21 && player.getHand().size() == 2) {
			player.setBank(wager);
			System.out.print("\nBLACKJACK! Player wins!");
		} else if (player.sumHand() > 21) {
			player.setBank(-wager);
			System.out.print("\nPlayer busted. Dealer wins!");
		} else if (dealer.sumHand() > 21) {
			player.setBank(wager);
			System.out.print("\nDealer busted. Player wins!");
		} else if (dealer.sumHand() > player.sumHand()) {
			player.setBank(-wager);
			System.out.print("\nDealer's hand: " + dealer.sumHand() + "\tPlayer's hand: " + player.sumHand());
			System.out.print("\nDealer wins!");
		} else if (player.sumHand() > dealer.sumHand()) {
			player.setBank(wager);
			System.out.print("\nDealer's hand: " + dealer.sumHand() + "\tPlayer's hand: " + player.sumHand());
			System.out.print("\nPlayer wins!");
		} else if (player.sumHand() == dealer.sumHand()) {
			System.out.print("\nDealer's hand: " + dealer.sumHand() + "\tPlayer's hand: " + player.sumHand());
			System.out.print("\nDraw!");
		}
		
		System.out.print("\n\nYou now have " + player.getBank() + " dollars in your bank");
	}
	
	/**
	 * Check for aces (used for changing value of ace from 11 to 1 to avoid busting if necessary).
	 * <p> NOTE: If there is an ace, change its value from 11 to 1
	 * @return true if there is an ace in the hand, false otherwise
	 */
	private static boolean checkForAces() {
		for(int i = 0; i < player.getHand().size(); i++) {
			if(player.getHand().get(i).getValue() == 11) {
				player.getHand().get(i).setValue(deck.getAceValue()[1]);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Let's player place a bet.
	 */
	private static void setWager() {
		System.out.print("\nYou have " + player.getBank() + " dollars in your bank");
		System.out.print("\nHow much would you like to bet? ");
		wager = kb.nextInt();
		
		/*
		 * Make sure bet isn't more than the player has in the bank.
		 */
		while(wager > player.getBank()) {
			System.out.print("\nSorry, you don't have that much money. Please enter an appropriate bet: ");
			wager = kb.nextInt();
		}
	}
	
	/**
	 * Prompts user to see if they want to play another round. 
	 * <p> If they don't want to continue (or they don't have the money to continue, display their final bank and end the game.
	 */
	private static boolean anotherRound() {
		if(player.getBank() > 0) {
			System.out.print("\nWould you like to play another round? Yes/No. ");
			choice = kb.nextLine();
			choice = choice.toUpperCase();
			
			if(choice.equals("YES")) {
				resetHands();
				return true;
			}
		} else {
			System.out.print("Sorry, you have run out money and can not play any more rounds.");
		}
		
		System.out.print("\nYou ended with " +  player.getBank() + " dollars in the bank.");
		if(player.getBank() > 100) {
			System.out.print(" That's a profit of " + (player.getBank() - 100) + " dollars.");
		} else if (player.getBank() < 100) {
			System.out.print(" That's a loss of " + (100 - player.getBank()) + " dollars.");
		} else {
			System.out.print(" That's a profit of " + (player.getBank() - 100) + " dollars.");
		}
		System.out.print("\n\n=======================================\n");
		System.out.print("THANKS FOR PLAYING!");
		System.out.print("\n=======================================");
		
		return false;
	}
	
	/**
	 * If player plays another round, remove all cards from dealer's and player's hands.
	 */
	private static void resetHands() {
		dealer.getHand().clear();
		player.getHand().clear();
	}
}