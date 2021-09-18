package application.view;

import java.util.ArrayList;

import javax.imageio.ImageIO;

import application.SceneHandler;
import application.Settings;
import application.control.MatchController;
import application.model.game.entity.Ball;
import application.model.game.entity.Frame;
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
	private int currentField = FIELD1 ;
	private String colorBorder = "";
	private String colorDoor = "" ;
	private boolean isEntered = false;
	
	public static final int FIELD1 = 0 ;
	public static final int FIELD2 = 1 ;
	public static final int FIELD3 = 2 ;
	
	private static final int STROKEWIDTHBALL = 7 ;
	
	private static final String NOTYOURTURNBLUE = "#00004c" ;
	private static final String YOURTURNBLUE = "#0000ff" ;
	private static final String NOTYOURTURNRED = "#cc0000" ;
	private static final String YOURTURNRED = "#ff0000" ;
	public  static final String COLORBALLWHITE = "#ffcc00";
	
	private static final String COLORHOVERRED = "ff7f7f";
	private static final String COLORTOOK = "#6666ff";
	private static final String COLORDOOR = "#005900";
	
	public void setEntered(boolean isEntered) {
		this.isEntered = isEntered;
	}
	
	public boolean isEntered() {
		return isEntered;
	}
	
	public MatchView() {
		canvas = new Canvas();
		canvas.widthProperty().bind(this.widthProperty());
		canvas.heightProperty().bind(this.heightProperty());
		this.getChildren().add(canvas);
		
	}
	
	public void setField(int idField) {
		
		switch (idField) {
		case FIELD1:
			fieldImg = new Image(getClass().getResourceAsStream("/application/view/field1.png"),Settings.FIELDWIDTHFRAME -  Settings.BORDERVERTICAL * 2 , Settings.FIELDHEIGHTFRAME - Settings.BORDERHORIZONTAL * 2 , false , true);
			field = new Field(Settings.FIELD1 , Settings.BORDERHORIZONTAL, Settings.BORDERVERTICAL, Settings.FIELDWIDTHFRAME, Settings.FIELDHEIGHTFRAME, fieldImg );
			currentField = FIELD1 ;
			break;
		case FIELD2:
			fieldImg = new Image(getClass().getResourceAsStream("/application/view/field2.png"),Settings.FIELDWIDTHFRAME -  Settings.BORDERVERTICAL * 2 , Settings.FIELDHEIGHTFRAME - Settings.BORDERHORIZONTAL * 2 , false , true);
			field = new Field(Settings.FIELD2 , Settings.BORDERHORIZONTAL, Settings.BORDERVERTICAL, Settings.FIELDWIDTHFRAME, Settings.FIELDHEIGHTFRAME, fieldImg );
			currentField = FIELD2 ;
			break;
		case FIELD3:
			fieldImg = new Image(getClass().getResourceAsStream("/application/view/field3.png"),Settings.FIELDWIDTHFRAME -  Settings.BORDERVERTICAL * 2 , Settings.FIELDHEIGHTFRAME - Settings.BORDERHORIZONTAL * 2 , false , true);
			field = new Field(Settings.FIELD3 , Settings.BORDERHORIZONTAL, Settings.BORDERVERTICAL, Settings.FIELDWIDTHFRAME, Settings.FIELDHEIGHTFRAME, fieldImg );
			currentField = FIELD3 ;
			break;
		default:
			break;
		}
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
		this.setOnMouseMoved(matchController);
		this.setOnMouseExited(matchController);
		this.setOnMouseEntered(matchController);
	}
	
	
	
	public void draw() {
		
		
		
		canvas.getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());		
		
		drawField();
		
		Frame informationMatch = null ;
		
		if (matchController == null || matchController.getParseMatchInformation() == null)
			return;
		
		
		informationMatch = matchController.getParseMatchInformation().getInformationMatch();
		
		
		ArrayList<Ball> balls = informationMatch.getBalls();
		boolean turn = informationMatch.isTurn();
		
		if(line != null)
			drawLine(line);
		
		boolean hover_some_ball = false ;
		
		for(Ball ball : balls) {
			
		switch (ball.getPlayer()) {
		
			case Ball.PLAYER2:
				if(!turn)
					canvas.getGraphicsContext2D().setStroke(Color.web(YOURTURNRED, 1.0));
				else	
					canvas.getGraphicsContext2D().setStroke(Color.web(NOTYOURTURNRED, 1.0));
				if(ball.isHover())
					canvas.getGraphicsContext2D().setStroke(Color.web(COLORHOVERRED, 1.0));
				canvas.getGraphicsContext2D().setLineWidth(STROKEWIDTHBALL);
				canvas.getGraphicsContext2D().strokeOval((int)  Math.round(ball.getPosition().getX()), (int)  Math.round(ball.getPosition().getY()),(int)  Math.round(ball.getRadius()*2),(int)  Math.round(ball.getRadius()*2));
				canvas.getGraphicsContext2D().setFill(Color.web(ball.getColor(), 1.0));
				break;
			case Ball.PLAYER1:
				if(turn)
					canvas.getGraphicsContext2D().setStroke(Color.web(YOURTURNBLUE, 1.0));
				else
					canvas.getGraphicsContext2D().setStroke(Color.web(NOTYOURTURNBLUE, 1.0));
				if(ball.isHover())
				{
					canvas.getGraphicsContext2D().setStroke(Color.web(COLORTOOK, 1.0));
					hover_some_ball = true ;
				}
				canvas.getGraphicsContext2D().setLineWidth(STROKEWIDTHBALL);
				canvas.getGraphicsContext2D().strokeOval((int)  Math.round(ball.getPosition().getX()), (int)  Math.round(ball.getPosition().getY()),(int)  Math.round(ball.getRadius()*2),(int)  Math.round(ball.getRadius()*2));
				canvas.getGraphicsContext2D().setFill(Color.web(ball.getColor(), 1.0));
				break;
			case Ball.WHITE:
				canvas.getGraphicsContext2D().setLineWidth(1);
				canvas.getGraphicsContext2D().setFill(Color.web(COLORBALLWHITE, 1.0));
				break;
			default:
				break;
			}
			canvas.getGraphicsContext2D().fillOval((int)  Math.round(ball.getPosition().getX()), (int)  Math.round(ball.getPosition().getY()),(int)  Math.round(ball.getRadius()*2),(int)  Math.round(ball.getRadius()*2));
		}
		
		if(hover_some_ball)
			SceneHandler.getInstance().setCursor(SceneHandler.HOVER_BALL_CURSOR);
		else if(isEntered )
			SceneHandler.getInstance().setCursor(SceneHandler.MATCH_CURSOR);
		else
			SceneHandler.getInstance().setCursor(SceneHandler.DEFAULT_CURSOR);
	}
	
	private void drawField() {
		
		drawBorders();
		
		canvas.getGraphicsContext2D().drawImage(fieldImg, field.getBorderVertical() , field.getBorderHorizontal());
	}

	
	private void drawBorders() {
		
		String colorLine = "" ;
		
		switch (currentField) {
		case FIELD1:
			colorBorder = Settings.COLORBORDERFIELD1 ;
			colorDoor = Settings.COLORFIELD1 ;
			colorLine = "#ffffff" ;
			break;
		case FIELD2:
			colorBorder = Settings.COLORBORDERFIELD2 ;
			colorDoor = Settings.COLORFIELD2 ;
			colorLine = "#ffffff" ;
			break;
		case FIELD3:
			colorBorder = Settings.COLORBORDERFIELD3 ;
			colorDoor = Settings.COLORFIELD3 ;
			colorLine = "#FF00FF" ;
			break;
		default:
			colorBorder = Settings.COLORBORDERFIELD1 ;
			colorDoor = Settings.COLORFIELD1 ;
			colorLine = "#ffffff" ;
			break;
		}
			
			canvas.getGraphicsContext2D().setFill(Color.web(colorBorder, 0.7));
			
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
			
			canvas.getGraphicsContext2D().setFill(Color.web(colorDoor, 0.7));
			
			// Left door
			canvas.getGraphicsContext2D().setStroke(Color.web(colorLine, 1));
			canvas.getGraphicsContext2D().setLineWidth(4);
			canvas.getGraphicsContext2D().strokeRect(0,field.getHeight()/3,field.getBorderVertical(),field.getHeight()/3);
			canvas.getGraphicsContext2D().fillRect(0,field.getHeight()/3,field.getBorderVertical(),field.getHeight()/3);
			
			// Right door
			canvas.getGraphicsContext2D().setStroke(Color.web(colorLine, 1));
			canvas.getGraphicsContext2D().setLineWidth(4);
			canvas.getGraphicsContext2D().strokeRect(0,field.getHeight()/3,field.getBorderVertical(),field.getHeight()/3);
			canvas.getGraphicsContext2D().fillRect(field.getWidth()-field.getBorderVertical(),field.getHeight()/3,field.getBorderVertical(),field.getHeight()/3);
			
	}
	
	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	public void drawLine(Line line) {

		// Line 1
		double initialXLine1 = line.getStartX() ;
		double initialYLine1 = line.getStartY() ;
		double finalXLine1 = line.getEndX() ;
		double finalYLine1 = line.getEndY() ;
		
		
		canvas.getGraphicsContext2D().setLineWidth(5);
		canvas.getGraphicsContext2D().setStroke(Color.web(COLORTOOK, 0.7));
		canvas.getGraphicsContext2D().strokeLine(initialXLine1,initialYLine1,finalXLine1,finalYLine1);

	}
	
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
