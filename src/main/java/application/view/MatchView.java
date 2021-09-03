package application.view;

import javax.imageio.ImageIO;

import application.Settings;
import application.control.MatchController;
import application.model.game.entity.Ball;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class MatchView extends StackPane{

	private static final int INTERSECT_UP = 0;
	private static final int INTERSECT_DOWN = 1;
	private static final int INTERSECT_LEFT = 2;
	private static final int INTERSECT_RIGHT = 3;
	
	
	private Canvas canvas;
	private MatchController matchController;
	private int stroke = 40;
	private Image fieldImg;
	private Line line = null;
	private Field field = null ;
	
	public MatchView() {
		canvas = new Canvas();
		canvas.widthProperty().bind(this.widthProperty());
		canvas.heightProperty().bind(this.heightProperty());
		this.getChildren().add(canvas);
		Double borderHorizontal = 10.0 ;
	    Double borderVertical = 45.0 ;
	    
		fieldImg = new Image(getClass().getResourceAsStream("/application/view/field.png"),Settings.FIELDWIDTHFRAME - borderVertical * 2 , Settings.FIELDHEIGHTFRAME - borderHorizontal * 2 , false , true);
		field = new Field(borderHorizontal, borderVertical, Settings.FIELDWIDTHFRAME, Settings.FIELDHEIGHTFRAME, fieldImg , 15.0);
	
	}
	
	public Field getField() {
		return field;
	}
	
	public void addController(MatchController matchController) 
	{
		this.matchController = matchController;
		this.setOnMousePressed(matchController);
		this.setOnMouseReleased(matchController);
		this.setOnMouseDragged(matchController);
	}
	
	
	
	public void draw() {
		
		canvas.getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());		
		
		drawField();
		
		
		if (matchController == null) return;
		
		
		
		for(Ball ball : matchController.getMatchHandler().getBalls()) {

			switch (ball.getColor()) {
			case Ball.RED:
				canvas.getGraphicsContext2D().setFill(Color.web("#ff0000", 1.0));
				break;
			case Ball.BLUE:
				if(matchController.getMatchHandler().getTurn())
					canvas.getGraphicsContext2D().setFill(Color.web("#16004d", 1.0));
				else
					canvas.getGraphicsContext2D().setFill(Color.web("#205a8c", 1.0));
				break;
			case Ball.WHITE:
				canvas.getGraphicsContext2D().setFill(Color.web("#fbf6f6", 1.0));
				break;
			case Ball.TOOK:
				canvas.getGraphicsContext2D().setFill(Color.web("#f28444",1.0));
			default:
				canvas.getGraphicsContext2D().setFill(Color.web("#ff7f27", 1.0));
				break;
			}
			canvas.getGraphicsContext2D().fillOval((int)  Math.round(ball.getPosition().getX()), (int)  Math.round(ball.getPosition().getY()),(int)  Math.round(ball.getRadius()*2),(int)  Math.round(ball.getRadius()*2));
		}
		
		if(line != null)
			drawLine(line);
	}
	
	private void drawField() {
		
		drawBorders();
		
		canvas.getGraphicsContext2D().drawImage(fieldImg, field.getBorderVertical() , field.getBorderHorizontal());
	}

	
	private void drawBorders() {
			
			canvas.getGraphicsContext2D().setFill(Color.web("#00b300", 0.7));
			
			// Upper border
			canvas.getGraphicsContext2D().fillRect(0,0,field.getWidth(),field.getBorderHorizontal());
			
			// Lower border
			canvas.getGraphicsContext2D().fillRect(0,field.getHeight()-field.getBorderHorizontal(),field.getWidth(),field.getBorderHorizontal());
			
			// Left border up
			canvas.getGraphicsContext2D().fillRect(0,0,field.getBorderVertical(),field.getHeight()/3);
			
			// Left border down
			canvas.getGraphicsContext2D().fillRect(0,field.getHeight()*2/3,field.getBorderVertical(),field.getHeight()/3);
			
			// Right border up
			canvas.getGraphicsContext2D().fillRect(field.getWidth()-field.getBorderVertical(),0,field.getBorderVertical(),field.getHeight()/3);
			
			// Right border down
			canvas.getGraphicsContext2D().fillRect(field.getWidth()-field.getBorderVertical(),field.getHeight()*2/3,field.getBorderVertical(),field.getHeight()/3);
			
			canvas.getGraphicsContext2D().setFill(Color.web("#005900", 0.7));
			
			// Left door
			canvas.getGraphicsContext2D().fillRect(0,field.getHeight()/3,field.getBorderVertical(),field.getHeight()/3);
			
			// Right door
			canvas.getGraphicsContext2D().fillRect(field.getWidth()-field.getBorderVertical(),field.getHeight()/3,field.getBorderVertical(),field.getHeight()/3);
			
	}
	
	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	public void drawLine(Line line) {	
		canvas.getGraphicsContext2D().setStroke(Color.web("#ff0000", 1.0));
		canvas.getGraphicsContext2D().strokeLine(line.getStartX(),line.getStartY(),line.getEndX(),line.getEndY());
		canvas.getGraphicsContext2D().setLineWidth(5);
		
	}
	
//	public void drawArrow(Arrow arrow) {
//	
//		if(arrow  == null )
//			return;
//		
//		System.out.print(arrow);
//		this.arrowImage = new Image(getClass().getResourceAsStream("/application/view/arrow.png"), arrow.getLength().getX(),arrow.getLength().getY(), true, true);
//		
//		double x = arrow.getPosition().getX();
//		double y = arrow.getPosition().getY();
//		double dx = arrow.getLength().getX();
//		double dy = arrow.getLength().getY();
//		
//		double magnitude = arrow.getLength().getMagnitude();
//		double angle =Math.toDegrees(Math.acos(-dx/magnitude));
//		
//
//		if( dy >= 0 )
//			angle = 180 - angle ;
//		else
//			angle += 180 ;
//		
//
//
//	}
	
	
	private int intersectBorders(Ball b) {
		if(b.getPosition().getY() <= 0 + stroke )
			return 0;
		if(b.getPosition().getY() + b.getRadius()*2 >= getHeight() - stroke )
			return 1;
		if(b.getPosition().getX() <= 0 + stroke )
			return 2;
		if(b.getPosition().getX() + b.getRadius()*2 >= getWidth() - stroke )
			return 3;
		return -1;
	}
	
	
	public int getStroke() {
		return stroke;
	}
	
}
