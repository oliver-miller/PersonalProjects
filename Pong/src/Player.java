import acm.program.*;

@SuppressWarnings("serial")
public class Player extends ConsoleProgram{
	
	private int score;
	private String name;
	
	public Player() {}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void p1GoalScored() {
		this.score++;
		Pong.getP1IntField().setValue(this.score);;
	}
	
	public void p2GoalScored() {
		this.score++;
		Pong.getP2IntField().setValue(this.score);
	}
}
