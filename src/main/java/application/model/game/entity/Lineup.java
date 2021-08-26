package application.model.game.entity;


import java.util.ArrayList;

import application.Settings;
import application.model.game.physics.VectorFioreNoSync;

public class Lineup {
	
	public static final int LINEUP1 = 1 ;
	public static final int LINEUP2 = 2 ;
	public static final int LINEUP3 = 3 ;
	
	
	private ArrayList<Ball> balls = new ArrayList<Ball>(5);
	private int currentLineup ;
	private VectorFioreNoSync[] coordinates = new VectorFioreNoSync[5];
	
	public Lineup(ArrayList<Ball> balls , int currentLineup) {
		super();
		this.balls = balls;
		this.currentLineup = currentLineup ;
		setCoordinates();
		setPositions();
	}

	
	public Lineup(int currentLineup) {
		super();
		this.currentLineup = currentLineup ; 
		setCoordinates();
		setPositions();
	}
	
	public void addBalls(ArrayList<Ball> balls) {
		this.balls = balls;
	}
	
	public void setCurrentLineup(int currentLineup) {
		this.currentLineup = currentLineup;
	}
	
	public ArrayList<Ball> getBalls() {
		return balls;
	}
	
	public  void  setPositions() {
		
		if(balls.isEmpty())
			return;
		
		ArrayList<VectorFioreNoSync> positions = new ArrayList<VectorFioreNoSync>(5);
		
		int i = 0 ;
		for(VectorFioreNoSync p : parseLineup())
			balls.get(i++).setPosition(p);

	}
	
	private void setCoordinates() {
		
		switch (currentLineup) {
		case LINEUP1:
			coordinates[0] = new VectorFioreNoSync(0.25, 0.50);
			coordinates[1] = new VectorFioreNoSync(0.30, 0.20);
			coordinates[2] = new VectorFioreNoSync(0.30, 0.80);
			coordinates[3] = new VectorFioreNoSync(0.45, 0.45);
			coordinates[4] = new VectorFioreNoSync(0.45, 0.55);
			break;
		case LINEUP2:
			coordinates[0] = new VectorFioreNoSync(0.25, 0.50);
			coordinates[1] = new VectorFioreNoSync(0.30, 0.20);
			coordinates[2] = new VectorFioreNoSync(0.30, 0.80);
			coordinates[3] = new VectorFioreNoSync(0.35, 0.50);
			coordinates[4] = new VectorFioreNoSync(0.45, 0.50);
			break;	
		case LINEUP3:
			coordinates[0] = new VectorFioreNoSync(0.25, 0.50);
			coordinates[1] = new VectorFioreNoSync(0.28, 0.25);
			coordinates[2] = new VectorFioreNoSync(0.28, 0.75);
			coordinates[3] = new VectorFioreNoSync(0.35, 0.05);
			coordinates[4] = new VectorFioreNoSync(0.35, 0.95);
			break;
		default:
			break;
		}
		
	}
	
	private ArrayList<VectorFioreNoSync> parseLineup() {
		
		ArrayList<VectorFioreNoSync> positions = new ArrayList<VectorFioreNoSync>(5);
		
		
		positions.add(new VectorFioreNoSync(Settings.FIELDWIDTHFRAME * coordinates[0].getX() - balls.get(0).getRadius(), Settings.FIELDHEIGHTFRAME * coordinates[0].getY() - balls.get(0).getRadius())) ;
		positions.add(new VectorFioreNoSync(Settings.FIELDWIDTHFRAME * coordinates[1].getX() - balls.get(1).getRadius(), Settings.FIELDHEIGHTFRAME * coordinates[1].getY() - balls.get(1).getRadius())) ;
		positions.add(new VectorFioreNoSync(Settings.FIELDWIDTHFRAME * coordinates[2].getX() - balls.get(2).getRadius(), Settings.FIELDHEIGHTFRAME * coordinates[2].getY() - balls.get(2).getRadius())) ;
		positions.add(new VectorFioreNoSync(Settings.FIELDWIDTHFRAME * coordinates[3].getX() - balls.get(3).getRadius(), Settings.FIELDHEIGHTFRAME * coordinates[3].getY() - balls.get(3).getRadius())) ;
		positions.add(new VectorFioreNoSync(Settings.FIELDWIDTHFRAME * coordinates[4].getX() - balls.get(4).getRadius(), Settings.FIELDHEIGHTFRAME * coordinates[4].getY() - balls.get(4).getRadius())) ;
		
		return positions ;
	}

	public void mirrorLineup() {
		
		for(VectorFioreNoSync c : coordinates)
		{
			c.setX(1.0 - c.getX());
		}
		
		setPositions();
	}
	
	public int getCurrentLineup() {
		return currentLineup;
	}

}
