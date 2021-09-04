package application.control;

import application.Settings;
import application.model.game.entity.Ball;
import application.model.game.entity.ParseMatchInformation;
import application.model.game.handler.MatchHandler;
import application.model.game.physics.VectorFioreNoSync;
import application.model.game.physics.VelocityNoSync;
import application.net.client.Client;
import application.net.common.Protocol;
import application.view.MatchView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;

public class MatchController implements EventHandler<MouseEvent>{

	private ParseMatchInformation parseMatchInformation ;
	private MatchView matchView = null;
	private long interval = 500;
	public static MatchController instance = null;

	private long startPress;
	private long endPress;
	
	private Double initialX = null ;
	private Double initialY = null ;

	private Ball ballTook = null;
	
	
	private MatchController() {
		super();
	}

	public void setParseMatchInformation(ParseMatchInformation parseMatchInformation) {
		this.parseMatchInformation = parseMatchInformation;
	}
	
	public ParseMatchInformation getParseMatchInformation() {
		return parseMatchInformation;
	}
	
	public static MatchController getInstance() {
		if(instance == null)
			instance = new MatchController();
		return instance ;
	}
	
	
	
	public void addMatchView(MatchView matchView) {
		this.matchView = matchView ;
	}
	


	public void update() {
		
		matchView.draw();
		
	}

	public void handle(MouseEvent event) {
		if(event.getEventType() == MouseEvent.MOUSE_PRESSED)
		{
			initialX = event.getX();
			initialY = event.getY();
			
			ballTook = parseMatchInformation.tookBall(initialX, initialY); 
			
			if( ballTook != null && ballTook.getColor() == Ball.BLUE && ballTook.getColor() != Ball.WHITE  && parseMatchInformation.isTurn())
			{
				ballTook.setColor(Ball.TOOK);
			}
			else {
				ballTook = null ;
				initialX = null;
				initialY = null;
				if(!parseMatchInformation.isTurn()) {
					System.out.println("[CLIENT] "+Protocol.ITSNOTYOURTURN);
				}else if(ballTook == null ) {
					System.out.println("[CLIENT] "+Protocol.NOBALLTOOK);
				}else if(ballTook.getColor() != Ball.BLUE) {
					System.out.println("[CLIENT] "+Protocol.NOTYOURBALL);
				}
				
			}
		}
		else if(event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
			
			if(ballTook == null )
				return;
			
			double finalX = event.getX();
			double finalY = event.getY();
			double x = ballTook.getPositionCenter().getX() ;
			double y = ballTook.getPositionCenter().getY() ;
			
			VectorFioreNoSync length = VectorFioreNoSync.sub( new VectorFioreNoSync(finalX, finalY) , new VectorFioreNoSync(initialX, initialY));
			
			if(length.getMagnitude() > Settings.MAXIMUMVELOCITY)
				length.setMag(Settings.MAXIMUMVELOCITY);
			
			VectorFioreNoSync positionLine = VectorFioreNoSync.add(length, new VectorFioreNoSync(initialX, initialY));
			
			matchView.setLine(new Line(x,y,positionLine.getX(),positionLine.getY()));
			return;
		}else if(event.getEventType() == MouseEvent.MOUSE_RELEASED) {
			
			if(ballTook == null ) 
				return;
			
			double finalX = event.getX();
			double finalY = event.getY();
			
			VectorFioreNoSync length = VectorFioreNoSync.sub(new VectorFioreNoSync(initialX, initialY), new VectorFioreNoSync(finalX, finalY));

			if(length.getMagnitude() > Settings.MAXIMUMVELOCITY)
				length.setMag(Settings.MAXIMUMVELOCITY);
			
			VelocityNoSync v = new VelocityNoSync(length.getX(),length.getY()) ;
			
			v.mult(150.0);
			
			ballTook.setVelocity(v);
			ballTook.setColor(Ball.BLUE);
			
			
			String message = ballTook.getPosition().getX() + "&" + ballTook.getPosition().getY() + ";" + ballTook.getVelocity().getX() + "&"+ ballTook.getVelocity().getY();
			
			Client.getInstance().sendMessage(Protocol.MOVEBALL);
			Client.getInstance().sendMessage(message);
		
			
			initialX = null;
			initialY = null;
			ballTook = null;
			matchView.setLine(null);
			
		}
	}
}
