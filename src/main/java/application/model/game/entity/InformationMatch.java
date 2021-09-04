package application.model.game.entity;

import java.util.ArrayList;

public class InformationMatch {

	
	private boolean turn ;
	private ArrayList<Ball> balls ;
	
	public InformationMatch(boolean turn, ArrayList<Ball> balls) {
		super();
		this.turn = turn;
		this.balls = balls;
	}
	
	public ArrayList<Ball> getBalls() {
		return balls;
	}
	
	public boolean isTurn() {
		return turn;
	}
}
