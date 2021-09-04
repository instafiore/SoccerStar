package application.model.game.handler;

import java.awt.image.RescaleOp;
import java.util.ArrayList;

import application.model.game.entity.Ball;
import application.model.game.entity.DataMatch;
import application.model.game.entity.Field;
import application.model.game.entity.Result;
import application.model.game.physics.VectorFioreNoSync;
import application.model.game.physics.VelocityNoSync;


public class MatchHandler {


	private ArrayList<Ball> balls;
	private double width;
	private double height;
	private boolean turn ;
	private Result result;
	private DataMatch dataMatch ;
	
	boolean f = true;
	
	public MatchHandler(DataMatch dataMatch) {
		balls = new ArrayList<Ball>();
		result = new Result() ;
		this.dataMatch = dataMatch ;
	}
	
	public void moveBalls(Field field) {
		this.width = field.getWidth();
		this.height = field.getHeight();

		for(Ball b1:balls)
		{
			b1.move(field);
			b1.setInDoor(field.inDoor(b1));
			checkMove(field, b1);
			
			for(Ball b2 : balls)
				if(b1 != b2 && intersect(b1, b2))
					collision(b1, b2 , field);
		}
	}
	
	public boolean getTurn() {
		return turn;
	}
	
	public void setTurn(boolean turn) {
		this.turn = turn;
	}
	
	private void checkMove(Field field, Ball b) {
	
		
		if(field.tookBorderUpDoor(b) && b.isInDoor() ) {
			
			b.invertVelocityY();
			b.getPosition().setY(field.getHeight()/3);
			
		}else if(field.tookBorderBottomDoor(b) && b.isInDoor()) {
			
			b.invertVelocityY();
			b.getPosition().setY(field.getHeight()*2/3 - b.getRadius() * 2);
			
		}else if(field.tookBorderLeft(b))
		{
			b.getPosition().setX((double)field.getBorderVertical());
			b.invertVelocityX();
			b.getVelocity().mult(0.80);
			
		}else if(field.tookBorderTop(b) )
		{
			b.getPosition().setY((double)field.getBorderHorizontal());
			b.invertVelocityY();
			b.getVelocity().mult(0.80);
			
		}else if(field.tookBorderRight(b) ) {
			
			b.getPosition().setX((double)width-field.getBorderVertical()-b.getRadius().shortValue()*2);
			b.invertVelocityX();
			b.getVelocity().mult(0.80);
			
		}else if(field.tookBorderDown(b) ) {
			
			b.getPosition().setY((double)height-field.getBorderHorizontal()-b.getRadius().shortValue()*2);
			b.invertVelocityY();
			b.getVelocity().mult(0.80);
			
		}else if(field.tookBorderLeftInDoor(b)) {
			
			b.getPosition().setX(0.0);
			b.invertVelocityX();
			b.getVelocity().mult(0.50);
			
		}else if(field.tookBorderRightInDoor(b)) {
			
			b.getPosition().setX(width - b.getRadius() * 2);
			b.invertVelocityX();
			b.getVelocity().mult(0.50);
			
		}
		
		if(field.goalLeft(b)) {
			
			result.goalGuest();
//			b.getVelocity().mult(0.30);		
		}
		
		if(field.goalRight(b)) {
			
			result.goalHome();
//			b.getVelocity().mult(0.30);
			
		}
		
		b.updatePositionCenter();
	}
	
	public boolean allStopped() {
		
		for(Ball b : balls)
			if(b.getVelocity().getMagnitude() != 0)
				return false;
		return true;
	}
	
	private void collision(Ball b1,Ball b2 , Field field) {
	
		VectorFioreNoSync newPosition = null ; 
		newPosition = newPosition(b1, b2);
		
		if(isInsideField(newPosition, b1 , field) && b1.getVelocity().getMagnitude() != 0.0)
			b1.setPosition(newPosition);
		else
			b2.setPosition(newPosition(b2, b1));
		
		ArrayList<VelocityNoSync> newVelocity = newVelocity(b1, b2);

		b1.setVelocity(newVelocity.get(0));
		b2.setVelocity(newVelocity.get(1));
		
	}
	
	private ArrayList<VelocityNoSync> newVelocity(Ball b1, Ball b2){
		

		double m1 = b1.getRadius();
		double m2 = b2.getRadius();

		double dx = b2.getPositionCenter().getX() - b1.getPositionCenter().getX() ;
		double dy = b2.getPositionCenter().getY() - b1.getPositionCenter().getY() ;
		
		double angle = Math.atan2(dy,dx);
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		
	

		// Rotate velocity
		double vx1 = b1.getVelocity().getX()*cos + b1.getVelocity().getY()*sin;
		double vy1 = b1.getVelocity().getY()*cos - b1.getVelocity().getX()*sin;
		double vx2 = b2.getVelocity().getX()*cos + b2.getVelocity().getY()*sin;
		double vy2 = b2.getVelocity().getY()*cos - b2.getVelocity().getX()*sin;
		
		
		double vx1final = ((m1-m2)*vx1+2*m2*vx2)/(m1+m2);
		double vx2final = ((m2-m1)*vx2+2*m1*vx1)/(m1+m2);
		
		// Update velocity
		vx1 = vx1final;
		vx2 = vx2final;

     	// Rotate vel back
		VelocityNoSync v1 = new VelocityNoSync(vx1*cos - vy1*sin, vy1*cos + vx1*sin);
		VelocityNoSync v2 = new VelocityNoSync(vx2*cos - vy2*sin, vy2*cos + vx2*sin);
		
		ArrayList<VelocityNoSync> array = new ArrayList<VelocityNoSync>() ;
		
		
		array.add(v1);
		array.add(v2);
		
		return array;
	}
	
	private VectorFioreNoSync newPosition(Ball b1 , Ball b2) {
		
		double x1 = b1.getPositionCenter().getX();
		double y1 = b1.getPositionCenter().getY();
		double x2 = b2.getPositionCenter().getX();
		double y2 = b2.getPositionCenter().getY();
		
		VectorFioreNoSync center1 = new VectorFioreNoSync(x1, y1);
		VectorFioreNoSync center2 = new VectorFioreNoSync(x2, y2);
		VectorFioreNoSync dist = VectorFioreNoSync.sub(center1, center2);
		
		dist.setMag(b1.getRadius()+b2.getRadius()+3);
		
		VectorFioreNoSync newPosition = VectorFioreNoSync.add(center2, dist);
		
		newPosition.setX(newPosition.getX()-b1.getRadius());
		newPosition.setY(newPosition.getY()-b1.getRadius());
				
		return newPosition;
	}

	public Ball tookBall(double x , double y) {
		for(Ball b:balls)
		{
			
			if(intersect(b, x, y)) 
				return b;
		}
		return null;
	}
	
	private boolean intersect(Ball b1, Ball b2) {
		VectorFioreNoSync dist = VectorFioreNoSync.sub(b1.getPositionCenter(), b2.getPositionCenter());
		return dist.getMagnitude() < b1.getRadius() +b2.getRadius();
	}
	
	private Boolean isInsideField(VectorFioreNoSync position , Ball b , Field field) {
		return position.getX() >= field.getBorderVertical() && position.getX() +  b.getRadius() * 2 < width - field.getBorderVertical() && position.getY() >= field.getBorderHorizontal() && position.getY() + b.getRadius() * 2 <= height - field.getBorderHorizontal() ;
	}
	
	private boolean intersect(Ball b1,double x,double y) {
		
		VectorFioreNoSync position = new VectorFioreNoSync(x, y);
		VectorFioreNoSync dist = VectorFioreNoSync.sub(b1.getPositionCenter(), position);
		
		return dist.getMagnitude() <= b1.getRadius();
	}
	
	public void add(Ball ball) {
		balls.add(ball);
	}
	
	public Ball getBall(Ball ball) {
		try {
			Ball b = balls.get(balls.indexOf(ball));
			return b;
		} catch (Exception e) {
			return null;
		}
	}


	public ArrayList<Ball> getBalls() {
		return balls;
	}
	
	
	
}
