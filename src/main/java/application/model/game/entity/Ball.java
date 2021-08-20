package application.model.game.entity;

import application.Settings;
import application.model.game.physics.VectorFioreNoSync;
import application.model.game.physics.VelocityNoSync;

public class Ball {

	private VectorFioreNoSync position;
	private VectorFioreNoSync positionCenter;
	private VelocityNoSync velocity;
	private Double radius;
	private int color = BLUE;
	private boolean inDoor = false;
	private int player ;
	
	public static final int PLAYER1 = 1 ;
	public static final int PLAYER2 = 2 ;
	public static final int NOPLAYER = 3 ;
	public static final int RED = 0;
	public static final int BLUE = 1 ;
	public static final int WHITE = 2 ;
	public static final int TOOK = 3 ;
	

	
	public Ball(VectorFioreNoSync position, VelocityNoSync velocity, Double radius, int player) {
		super();
		this.position = position;
		this.radius = radius;
		updatePositionCenter();
		this.velocity = velocity;
		velocity.setMag(velocity.getMagnitude()*Settings.FREQUENCY);
		this.player = player ;
	}
	
	public void updatePositionCenter() {
		this.positionCenter = new VectorFioreNoSync(position.getX()+radius, position.getY()+radius);
	}
	public void move(Field field) {
		
		if(this.getVelocity().getMagnitude() == 0.0)
			return;
		
		this.getPosition().add(this.getVelocity().getInPixelPerMillisecond());
		VectorFioreNoSync friction = new VectorFioreNoSync(this.getVelocity().getX(),this.getVelocity().getY());
		friction.mult(-1.0);
		friction.setMag(this.getRadius()*field.getMu());
		Double newMag = this.getVelocity().getMagnitude() - friction.getMagnitude() ;
		if(newMag < 0 )
				newMag = 0.0 ;
		this.getVelocity().setMag(newMag);
		
	}
	
	public void setInDoor(boolean inDoor) {
		this.inDoor = inDoor;
	}
	
	public boolean isInDoor() {
		return inDoor;
	}
	
	public void invertVelocityX() {
		velocity.multX(-1.0);
	}
	
	public void invertVelocityY() {
		velocity.multY(-1.0);
	}
	
	public VectorFioreNoSync getPosition() {
		return position;
	}

	public void setPosition(VectorFioreNoSync position) {
		this.position = position;
		updatePositionCenter();

	}

	public VelocityNoSync getVelocity() {
		return velocity;
	}

	public void setVelocity(VelocityNoSync velocity) {
		this.velocity = velocity;
	}

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
		updatePositionCenter();
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public int getColor() {
		return color;
	}
	
	public int getPlayer() {
		return player;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null || this.getClass() != obj.getClass())
			return false;

		Ball b = (Ball) obj;

		return position.equals(b.position) && velocity.equals(b.velocity) && radius == b.radius;
	}
	
	public VectorFioreNoSync getPositionCenter() {
		return positionCenter;
	}
	
	@Override
	public String toString() {
		return "Position("+this.getPosition().getX()+","+this.getPosition().getY()+") , Velocity("+this.getVelocity().getX()+","+this.getVelocity().getY()+")";
	}

}
