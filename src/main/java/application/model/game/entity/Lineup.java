package application.model.game.entity;


import java.util.ArrayList;

import application.Settings;
import application.model.game.physics.VectorFioreNoSync;
import application.model.game.physics.VelocityNoSync;

public class Lineup {
	
	public static final int LINEUP1 = 1 ;
	public static final int LINEUP2 = 2 ;
	public static final int LINEUP3 = 3 ;
	
	private static Lineup instace = null ;
	
	public static Lineup getInstace() {
		if(instace == null)
			instace = new Lineup();
		return instace;
	}

	public ArrayList<Ball> getLineup(int typeOfLineup){
		
		ArrayList<Ball> balls = new ArrayList<Ball>();
		
		for(int j = 0 ; j < 5 ; ++j)
			balls.add(new Ball(new VectorFioreNoSync(0.0), new VelocityNoSync(0.0), Settings.DIMENSIONSTANDARDBALL, Ball.BLUE));
		
		placeBalls(balls,typeOfLineup);
	
		return balls ;
	}
	
	public ArrayList<Ball> getLineupMirrored(int typeOfLineup){
		
		ArrayList<Ball> balls = new ArrayList<Ball>();
		
		for(int j = 0 ; j < 5 ; ++j)
			balls.add(new Ball(new VectorFioreNoSync(0.0), new VelocityNoSync(0.0), Settings.DIMENSIONSTANDARDBALL, Ball.RED));
		
		placeBallsMirrored(balls,typeOfLineup);
	
		return balls ;
	}
	
	public Ball getBallToPlay() {
		
		double x = Settings.FIELDWIDTHFRAME * 0.50 - Settings.DIMENSIONOFBALLTOPLAY ;
		double y = Settings.FIELDHEIGHTFRAME * 0.50 - Settings.DIMENSIONOFBALLTOPLAY ;
		VectorFioreNoSync position11 = new VectorFioreNoSync(x,y);
		
		Ball ball = new Ball(position11,new VelocityNoSync(0.0),Settings.DIMENSIONOFBALLTOPLAY , Ball.WHITE);
		ball.setColor(Ball.WHITE);
		
		return ball ;
	}
	
	private  void  placeBalls(ArrayList<Ball> balls , int typeOfLineup) {
		
		if(balls.isEmpty())
			return;
		
		ArrayList<VectorFioreNoSync> positions = new ArrayList<VectorFioreNoSync>(5);
		
		int i = 0 ;
		for(VectorFioreNoSync p : parseLineup(balls, typeOfLineup))
			balls.get(i++).setPosition(p);
		
	}
	
	private  void  placeBallsMirrored(ArrayList<Ball> balls , int typeOfLineup) {
		
		if(balls.isEmpty())
			return;
		
		ArrayList<VectorFioreNoSync> positions = new ArrayList<VectorFioreNoSync>(5);
		
		int i = 0 ;
		for(VectorFioreNoSync p : parseLineupMirrored(balls, typeOfLineup))
			balls.get(i++).setPosition(p);
		
	}
	
	private VectorFioreNoSync[] getCoordinates(int typeOfLineup) {
		
		VectorFioreNoSync[] coordinates = new VectorFioreNoSync[5];
		
		switch (typeOfLineup) {
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
		
		return coordinates;
		
	}
	
	private VectorFioreNoSync[] getCoordinatesMirrored(VectorFioreNoSync[] coordinates) {
		
		for(VectorFioreNoSync c : coordinates)
			c.setX(1.0 - c.getX());
		
		return coordinates ;
	}
	
	private ArrayList<VectorFioreNoSync> parseLineup(ArrayList<Ball> balls ,int typeOfLineup) {
		
		ArrayList<VectorFioreNoSync> positions = new ArrayList<VectorFioreNoSync>(5);
		
		VectorFioreNoSync[] coordinates = getCoordinates(typeOfLineup);
		
		positions.add(new VectorFioreNoSync(Settings.FIELDWIDTHFRAME * coordinates[0].getX() - balls.get(0).getRadius(), Settings.FIELDHEIGHTFRAME * coordinates[0].getY() - balls.get(0).getRadius())) ;
		positions.add(new VectorFioreNoSync(Settings.FIELDWIDTHFRAME * coordinates[1].getX() - balls.get(1).getRadius(), Settings.FIELDHEIGHTFRAME * coordinates[1].getY() - balls.get(1).getRadius())) ;
		positions.add(new VectorFioreNoSync(Settings.FIELDWIDTHFRAME * coordinates[2].getX() - balls.get(2).getRadius(), Settings.FIELDHEIGHTFRAME * coordinates[2].getY() - balls.get(2).getRadius())) ;
		positions.add(new VectorFioreNoSync(Settings.FIELDWIDTHFRAME * coordinates[3].getX() - balls.get(3).getRadius(), Settings.FIELDHEIGHTFRAME * coordinates[3].getY() - balls.get(3).getRadius())) ;
		positions.add(new VectorFioreNoSync(Settings.FIELDWIDTHFRAME * coordinates[4].getX() - balls.get(4).getRadius(), Settings.FIELDHEIGHTFRAME * coordinates[4].getY() - balls.get(4).getRadius())) ;
		
		return positions ;
	}
	
	private ArrayList<VectorFioreNoSync> parseLineupMirrored(ArrayList<Ball> balls ,int typeOfLineup) {
		
		ArrayList<VectorFioreNoSync> positions = new ArrayList<VectorFioreNoSync>(5);
		
		VectorFioreNoSync[] coordinates = getCoordinates(typeOfLineup);
		
		coordinates  = getCoordinatesMirrored(coordinates);
		
		positions.add(new VectorFioreNoSync(Settings.FIELDWIDTHFRAME * coordinates[0].getX() - balls.get(0).getRadius(), Settings.FIELDHEIGHTFRAME * coordinates[0].getY() - balls.get(0).getRadius())) ;
		positions.add(new VectorFioreNoSync(Settings.FIELDWIDTHFRAME * coordinates[1].getX() - balls.get(1).getRadius(), Settings.FIELDHEIGHTFRAME * coordinates[1].getY() - balls.get(1).getRadius())) ;
		positions.add(new VectorFioreNoSync(Settings.FIELDWIDTHFRAME * coordinates[2].getX() - balls.get(2).getRadius(), Settings.FIELDHEIGHTFRAME * coordinates[2].getY() - balls.get(2).getRadius())) ;
		positions.add(new VectorFioreNoSync(Settings.FIELDWIDTHFRAME * coordinates[3].getX() - balls.get(3).getRadius(), Settings.FIELDHEIGHTFRAME * coordinates[3].getY() - balls.get(3).getRadius())) ;
		positions.add(new VectorFioreNoSync(Settings.FIELDWIDTHFRAME * coordinates[4].getX() - balls.get(4).getRadius(), Settings.FIELDHEIGHTFRAME * coordinates[4].getY() - balls.get(4).getRadius())) ;
		
		return positions ;
	}

}
