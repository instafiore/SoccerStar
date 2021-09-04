package application.model.game.entity;

public class Field {
	
	private String name ;
	private double borderHorizontal ;
	private double borderVertical ;
	private double width ;
	private double height ;
	private double mu ;
		
	public Field(String name , double borderHorizontal, double borderVertical, double width, double height,double mu) {
		super();
		this.name = name ;
		this.borderHorizontal = borderHorizontal;
		this.borderVertical = borderVertical;
		this.width = width;
		this.height = height;
		this.mu = mu ;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getBorderHorizontal() {
		return borderHorizontal;
	}

	public double getBorderVertical() {
		return borderVertical;
	}

	public double getWidth() {
		return width;
	}

	public double getMu() {
		return mu;
	}

	public double getHeight() {
		return height;
	}

	public boolean tookBorderTop(Ball b) {
		
		return b.getPosition().getY() < borderHorizontal ;
	}
	
	public boolean tookBorderRight(Ball b) {
			
		return b.getPosition().getX() + b.getRadius().shortValue() * 2 > width - borderVertical && ( b.getPosition().getY() < height/3 ||  b.getPosition().getY() > height*2/3);
	}
	
	public boolean tookBorderLeft(Ball b) {
		
		return  b.getPosition().getX() < borderVertical && ( b.getPosition().getY() < height / 3 ||  b.getPosition().getY() > height*2/3) ;
	}
	
	public boolean tookBorderUpDoor(Ball b) {
		
		return b.getPosition().getY() < height / 3 && (b.getPosition().getX()  + b.getRadius() * 2  > width - borderVertical || b.getPosition().getX() < borderVertical);
	}
	
	public boolean tookBorderBottomDoor(Ball b) {
	
		return b.getPosition().getY() + b.getRadius() * 2 > height * 2 / 3 && (b.getPosition().getX()  + b.getRadius() * 2  > width - borderVertical || b.getPosition().getX() < borderVertical);
	}
	
	public boolean goalLeft(Ball b) {
		
		return   b.getPosition().getX() + b.getRadius() * 2 < borderVertical    && ( b.getPosition().getY() > height / 3 && b.getPosition().getY() + b.getRadius().shortValue() < height * 2 / 3 ) && b.getColor() == Ball.WHITE;
	}
	
	public boolean goalRight(Ball b) {
			
		return   b.getPosition().getX()  > width - borderVertical   && ( b.getPosition().getY() > height / 3 && b.getPosition().getY() + b.getRadius().shortValue() < height * 2 / 3 ) && b.getColor() == Ball.WHITE ;
	}
	
	public boolean tookBorderLeftInDoor(Ball b) {
		
		return b.getPosition().getX() <= 0 && !( b.getPosition().getY() < height/3 ||  b.getPosition().getY() > height*2/3) ;
	}
	
	public boolean tookBorderRightInDoor(Ball b) {
		
		return b.getPosition().getX() + b.getRadius() * 2  >= width  && !( b.getPosition().getY() < height/3 ||  b.getPosition().getY() > height*2/3) ;
	}
	
	public boolean tookBorderDown(Ball b) {
		
		return b.getPosition().getY() + b.getRadius().shortValue() * 2 > height - borderHorizontal ;
	}
	
	public boolean inDoor(Ball b) {
		
		return  ( b.getPosition().getX() + b.getRadius() * 2 > width - borderVertical || b.getPosition().getX()  < borderVertical )   && ( b.getPosition().getY() + b.getRadius().shortValue() * 2 > height / 3 && b.getPosition().getY() + b.getRadius().shortValue() < height * 2 / 3 );
	}
	
}
