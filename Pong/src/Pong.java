import java.awt.*;
import acm.graphics.*;
import acm.gui.*;
import acm.program.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class Pong extends GraphicsProgram{
	
	/*
	 * Display
	 */
	// Display dimensions
	private static final int DISPLAY_WIDTH = 1700;
	private static final int DISPLAY_HEIGHT = 850;
	
	// IntFields for player's scores
	private static IntField p1Score;
	private static IntField p2Score;
	
	// GRect for the center field line
	private GRect centerLine;
	
	/*
	 * Ball
	 */
	private GOval ball;
	private static final int BALL_DIAMETER = 50;
	private static int BALL_SPEED_X = 1;
	private static int BALL_SPEED_Y = 1;
	
	/*
	 * Players
	 */
	private Player p1;
	private Player p2;
	private GRect p1Rect;
	private GRect p2Rect;
	private static final int PLAYER_WALL_OFFSET = 10;
	
	/*
	 * Game
	 */
	private static boolean gameRunning;
	private GLabel initialPrompt;
	private GLabel countdown;
	
	/**
	 * Main entry point for the program.
	 */
	public void run() {
		initialPrompt = new GLabel("Press ENTER to start the game.");
		initialPrompt.setFont(new Font("Serif", Font.BOLD, 30));
		add(initialPrompt, (getWidth() - initialPrompt.getWidth())/2, (getHeight() - initialPrompt.getHeight())/2);
		gameRunning = false;
		while(true) {
		pause(1);
			if(gameRunning == true) {
				initialPrompt.setVisible(false);
				startCountdown();
				add(centerLine);
				runGame();
			}
		}
	}
	
	/**
	 * Method that controls the game.
	 */
	public void runGame() {
		while(p1.getScore() < 7 || p2.getScore() < 7) {
			ball.move(BALL_SPEED_X, BALL_SPEED_Y);
			detectCollision();
			detectGoal();
			pause(5);
		}
		gameRunning = false;
	}
	
	/**
	 * Add key listeners. Setup display.
	 */
	public void init() {
		this.resize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		setupField();
		setupBall();
		setupPlayers();
		p1Score = new IntField(0);
		p2Score = new IntField(0);
		addKeyListeners();
		add(p2Score, NORTH);
		add(p1Score, NORTH);

	}
	
	/**
	 * If ball touches either end of the field, goal is scored.
	 */
	private void detectGoal() {
		if(ball.getX() < 0) leftGoalScored();
		if(ball.getX() > DISPLAY_WIDTH - BALL_DIAMETER) rightGoalScored();
	}
	
	/**
	 * If ball touches wall or player, bounce in appropriate direction.
	 */
	private void detectCollision() {
		// Collision with bottom and top wall
		if(ball.getY() < 0 || ball.getY() > DISPLAY_HEIGHT - BALL_DIAMETER) BALL_SPEED_Y *= -1;

		// Collision with P1
		if(ball.getX() == (p1Rect.getX() + p1Rect.getWidth()) && (ball.getY() + ball.getHeight()) > p1Rect.getY() && ball.getY() < (p1Rect.getY() + p1Rect.getHeight())) BALL_SPEED_X *= -1;
		
		// Collision with P2
		if((ball.getX() + ball.getHeight()) == p2Rect.getX() && (ball.getY() + ball.getHeight()) > p2Rect.getY() && ball.getY() < (p2Rect.getY() + p2Rect.getHeight())) BALL_SPEED_X *= -1;
	}
	
	
	
	
	
	
	
	
	/**
	 * Player 1 (on the left) scores a goal
	 */
	private void leftGoalScored() {
		p1.p1GoalScored();
		ball.setLocation(DISPLAY_WIDTH/3, DISPLAY_HEIGHT/2);
		pause(3000);
		BALL_SPEED_X = 1;
		BALL_SPEED_Y = 1;
	}
	
	/**
	 * Player 2 (on the right) scores a goal
	 */
	private void rightGoalScored() {
		p2.p2GoalScored();
		ball.setLocation(DISPLAY_WIDTH*2/3, DISPLAY_HEIGHT/2);
		pause(3000);
		BALL_SPEED_X = -1;
		BALL_SPEED_Y = -1;
	}
	
	/**
	 * Setup display size
	 */
	public void setupField() {
		centerLine = new GRect(DISPLAY_WIDTH/2, 0, 3, DISPLAY_HEIGHT);
		centerLine.setFillColor(Color.BLACK);
		centerLine.setFilled(true);
	}
	
	/**
	 * Setup ball
	 */
	private void setupBall() {
		ball = new GOval((DISPLAY_WIDTH - BALL_DIAMETER)/2, DISPLAY_HEIGHT/2, BALL_DIAMETER, BALL_DIAMETER);
		ball.setFillColor(Color.GREEN);
		ball.setFilled(true);
		add(ball);
	}
	
	/**
	 * Setup players
	 */
	private void setupPlayers() {
		// Player 1
		p1 = new Player();
		p1.setName("Player 1");
		p1.setScore(0);
		p1Rect = new GRect(PLAYER_WALL_OFFSET, DISPLAY_HEIGHT/2, 10, 100);
		p1Rect.setColor(Color.BLUE);
		p1Rect.setFilled(true);
		add(p1Rect);
		
		// Player 2
		p2 = new Player();
		p2.setName("Player 2");
		p2.setScore(0);
		p2Rect = new GRect(DISPLAY_WIDTH - PLAYER_WALL_OFFSET*2, DISPLAY_HEIGHT/2, 10, 100);
		p2Rect.setColor(Color.RED);
		p2Rect.setFilled(true);
		add(p2Rect);
	}
	
	/**
	 * Lookout for key presses
	 */
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == 87) p1Rect.move(0, -10);
		if(e.getKeyCode() == 38) p2Rect.move(0, -10);
		if(e.getKeyCode() == 83) p1Rect.move(0, 10);
		if(e.getKeyCode() == 40) p2Rect.move(0, 10);
		if(e.getKeyCode() == 10) gameRunning = true;
	}
	
	/**
	 * Getter for P1 IntField
	 * @return P1 IntField
	 */
	public static IntField getP1IntField() {
		return p1Score;
	}
	
	/**
	 * Getter for P2 IntField
	 * @return P2 IntField
	 */
	public static IntField getP2IntField() {
		return p2Score;
	}
	
	/**
	 * Countdown to start the game
	 */
	private void startCountdown() {
		countdown = new GLabel("3");
		countdown.setFont(new Font("Serif", Font.BOLD, 100));
		add(countdown, (getWidth() - countdown.getWidth())/2, (getHeight() - countdown.getHeight())/2);
		pause(1000);
		remove(countdown);
		countdown = new GLabel("2");
		countdown.setFont(new Font("Serif", Font.BOLD, 100));
		add(countdown, (getWidth() - countdown.getWidth())/2, (getHeight() - countdown.getHeight())/2);
		pause(1000);
		remove(countdown);
		countdown = new GLabel("1");
		countdown.setFont(new Font("Serif", Font.BOLD, 100));
		add(countdown, (getWidth() - countdown.getWidth())/2, (getHeight() - countdown.getHeight())/2);
		pause(1000);
		remove(countdown);
		countdown = new GLabel("GO!");
		countdown.setFont(new Font("Serif", Font.BOLD, 100));
		add(countdown, (getWidth() - countdown.getWidth())/2, (getHeight() - countdown.getHeight())/2);
		pause(1000);
		countdown.setVisible(false);
	}
}
