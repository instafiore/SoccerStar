package application.control;

import application.SceneHandler;
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
	
	private String colorHome = "" ;
	private String colorGuest = "" ;
	private String usernameGuest = "" ;
	
	private Double initialX = null ;
	private Double initialY = null ;

	private Ball ballTook = null;
	
	private boolean hoverABall = false ;
	
	private MatchController() {
		super();
	}

	
	public void setUsernameGuest(String usernameGuest) {
		this.usernameGuest = usernameGuest;
	}
	public String getUsernameGuest() {
		return usernameGuest;
	}
	
	public void setColorHome(String colorHome) {
		this.colorHome = colorHome;
	}
	
	public void setColorGuest(String colorGuest) {
		this.colorGuest = colorGuest;
	}
	
	public String getColorHome() {
		return colorHome;
	}
	
	public String getColorGuest() {
		return colorGuest;
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
	
	public MatchView getMatchView() {
		return matchView;
	}


	public void update() {
		
		matchView.draw();
		
	}

	public void handle(MouseEvent event) {
		
		if(!parseMatchInformation.isReady())
			return ;
		
		if(event.getEventType() == MouseEvent.MOUSE_MOVED)
		{
			double x = event.getX();
			double y = event.getY();

			ballTook = parseMatchInformation.tookBall(x, y); 
			
			if(ballTook != null && ballTook.getPlayer() == Ball.PLAYER1 && ballTook.getPlayer() != Ball.WHITE  && parseMatchInformation.getLastInformationMatch().isTurn() )
			{
				ballTook.setHover(true);
				if(!hoverABall)
				{
					Client.getInstance().sendMessage(Protocol.HOVERBALL);
					Client.getInstance().sendMessage(ballTook.getPosition().getX()+Protocol.BALLDELIMITER+ballTook.getPosition().getY());
				}
				hoverABall = true ;
			}else if(hoverABall)
			{
				parseMatchInformation.setHoverFalseAll();
				if(parseMatchInformation.getLastInformationMatch().isTurn() && initialX == null )
					Client.getInstance().sendMessage(Protocol.HOVERNOBALL);
				hoverABall = false ;
			}

			
		}else if(event.getEventType() == MouseEvent.MOUSE_EXITED)
		{
			SceneHandler.getInstance().setCursor(SceneHandler.DEFAULT_CURSOR);
		}else if(event.getEventType() == MouseEvent.MOUSE_PRESSED)
		{
			initialX = event.getX();
			initialY = event.getY();
			
			ballTook = parseMatchInformation.tookBall(initialX, initialY); 
			
			if( !(ballTook != null && ballTook.getPlayer() == Ball.PLAYER1 && ballTook.getPlayer() != Ball.WHITE  && parseMatchInformation.getLastInformationMatch().isTurn()) )
			{
				initialX = null;
				initialY = null;
				if(!parseMatchInformation.getLastInformationMatch().isTurn()) {
					System.out.println("[CLIENT] "+Protocol.ITSNOTYOURTURN);
				}else if(ballTook == null ) {
					System.out.println("[CLIENT] "+Protocol.NOBALLTOOK);
				}else if(ballTook.getPlayer() != Ball.PLAYER1) {
					System.out.println("[CLIENT] "+Protocol.NOTYOURBALL);
				}
				ballTook = null ;
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
			
			if(length.getMagnitude() > Settings.MAXVELOCITY)
				length.setMag(Settings.MAXVELOCITY);
			
			VectorFioreNoSync positionLine = VectorFioreNoSync.add(length, new VectorFioreNoSync(initialX, initialY));
			
			matchView.setLine(new Line(x,y,positionLine.getX(),positionLine.getY()));
			return;
		}else if(event.getEventType() == MouseEvent.MOUSE_RELEASED) {
			
			if(ballTook == null ) 
				return;
			
			double finalX = event.getX();
			double finalY = event.getY();
			
			VectorFioreNoSync length = VectorFioreNoSync.sub(new VectorFioreNoSync(initialX, initialY), new VectorFioreNoSync(finalX, finalY));

			if(length.getMagnitude() <= Settings.MINVELOCITY)
			{
				initialX = null;
				initialY = null;
				matchView.setLine(null);
				ballTook.setPlayer(Ball.PLAYER1);
				return ;
			}
			
			if(length.getMagnitude() > Settings.MAXVELOCITY)
				length.setMag(Settings.MAXVELOCITY);
			
			VelocityNoSync v = new VelocityNoSync(length.getX(),length.getY()) ;
			
			v.mult(Settings.MULTIPLIERVELOCITY);
			
			ballTook.setVelocity(v);
			ballTook.setPlayer(Ball.PLAYER1);
			
			
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
