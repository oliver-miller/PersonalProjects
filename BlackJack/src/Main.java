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
	private static Player split1 = new Player();
	private static Player split2 = new Player();
	private static Player[] split = {split1, split2};
	private static Player dealer = new Player();
	
	/*
	 * Class variables
	 */
	private static int wager;
	private static boolean splitCheck = false;
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
		printHand(player);
		if(dealer.sumHand() != 21) {
			if(player.sumHand() != 21) {
				playerTurn(player);
				dealerTurn();
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
	 * <li> Stay: End turn and let dealer play their turn
	 * <li> Hit: Player gets dealt another card
	 * <li> Double: Player doubles their wager and gets dealt only 1 more card
	 * <li> Split: If player has a pair, then they can't split their hand into two separate hands (with an equivalent wager on each)
	 */
	private static void playerTurn(Player p) {	
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
				hit(p);
			}
			
			// Double
			if(choice.equals("DOUBLE")) {
				if(dbl(p)) break;
			}
			
			// Split
			if(choice.equals("SPLIT")) {
				if(p.getHand().get(0).getRank().equals(p.getHand().get(1).getRank())) {
					split();
					break;
				} else {
					System.out.print("\nSorry, you can't split unless you have a pair.");
				}
			}
				
			// Check for bust
			if(player.sumHand() > 21) {
				// Check for aces
				if(!checkForAces()) {
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
		System.out.print("\n\nDEALER'S HAND: ");
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
	private static void printHand(Player p) {
		System.out.print("\n\nPLAYER'S HAND: ");
		for(int i = 0; i < p.getHand().size(); i++) {
			System.out.print("\n" + p.getHand().get(i).getRank() + " of " + p.getHand().get(i).getSuit());
		}
		System.out.print("\nThis hand has a value of " + p.sumHand());
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
		if(!splitCheck) {
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
		} else if (splitCheck) {
			for(int i = 0; i < split.length; i++) {
				System.out.print("\n\nFor hand #" + (i + 1));
				
				if(dealer.sumHand() == 21 && dealer.getHand().size() == 2) {
					player.setBank(-wager);
					System.out.print("\nBLACKJACK! Dealer wins!");
				} else if (split[i].sumHand() == 21 && split[i].getHand().size() == 2) {
					player.setBank(wager);
					System.out.print("\nBLACKJACK! Player wins!");
				} else if (split[i].sumHand() > 21) {
					player.setBank(-wager);
					System.out.print("\nPlayer busted. Dealer wins!");
				} else if (dealer.sumHand() > 21) {
					player.setBank(wager);
					System.out.print("\nDealer busted. Player wins!");
				} else if (dealer.sumHand() > split[i].sumHand()) {
					player.setBank(-wager);
					System.out.print("\nDealer's hand: " + dealer.sumHand() + "\tPlayer's hand: " + split[i].sumHand());
					System.out.print("\nDealer wins!");
				} else if (split[i].sumHand() > dealer.sumHand()) {
					player.setBank(wager);
					System.out.print("\nDealer's hand: " + dealer.sumHand() + "\tPlayer's hand: " + split[i].sumHand());
					System.out.print("\nPlayer wins!");
				} else if (split[i].sumHand() == dealer.sumHand()) {
					System.out.print("\nDealer's hand: " + dealer.sumHand() + "\tPlayer's hand: " + split[i].sumHand());
					System.out.print("\nDraw!");
				}
			}
		}
		
		System.out.print("\n\nYou now have $" + player.getBank() + " in your bank");
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
		System.out.print("\nYou have $" + player.getBank() + " in your bank");
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
			while(true) {
				if(choice.equals("YES")) {
					resetRound();
					return true;
				} else if(choice.equals("NO")) {
					break;
				}
				System.out.print("Sorry, please say Yes or No. ");
				choice = kb.nextLine();
				choice = choice.toUpperCase();
			}
		} else {
			System.out.print("\nSorry, you have run out money and can not play any more rounds.");
		}
		
		System.out.print("\nYou ended with $" +  player.getBank() + " in the bank.");
		if(player.getBank() > 100) {
			System.out.print(" That's a profit of $" + (player.getBank() - 100));
		} else if (player.getBank() < 100) {
			System.out.print(" That's a loss of $" + (100 - player.getBank()));
		} else {
			System.out.print(" That's a profit of $" + (player.getBank() - 100));
		}
		System.out.print("\n\n=======================================\n");
		System.out.print("THANKS FOR PLAYING!");
		System.out.print("\n=======================================");
		
		return false;
	}
	
	/**
	 * If player plays another round, remove all cards from dealer's and player's hands.
	 */
	private static void resetRound() {
		dealer.getHand().clear();
		player.getHand().clear();
		split[0].getHand().clear();
		split[1].getHand().clear();
		splitCheck = false;
	}
	
	/**
	 * Hit
	 */
	private static void hit(Player p) {
		p.addCard(deck.getNextCard());
		printHand(p);
	}
	
	/**
	 * Double
	 */
	private static boolean dbl(Player p) {
		if(p.getHand().size() == 2 && wager * 2 < p.getBank()) {	
			p.addCard(deck.getNextCard());
			wager *= 2;
			System.out.print("\nYour new wager is $" + wager);
			printHand(p);
			
			// Check for bust
			if(player.sumHand() > 21) {
				// Check for aces
				if(!checkForAces()) {
					System.out.print("\nBUST!");
				}
			}
			
			return true;
		} else if (p.getHand().size() != 2) {
			System.out.print("\nYou cannot double if you already drawn a card.");
		} else if (wager * 2 > player.getBank()) {
			System.out.print("\nSorry, you don't have the money to double your bet.");
		}
		
		return false;
	}
	
	/**
	 * "Split" action for player's turn
	 */
	private static void split() {
		splitCheck = true;
		for(int i = 0; i < split.length; i++) {
			System.out.print("\n\nHand #" + (i + 1));
			
			split[i].addCard(player.getHand().get(i));
			split[i].addCard(deck.getNextCard());
			printHand(split[i]);
			
			playerTurn(split[i]);
		}
	}
}