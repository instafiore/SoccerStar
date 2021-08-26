package application.control;

import application.Settings;
import application.model.game.entity.Ball;
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

	private MatchHandler matchHandler = null ;
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

	public static MatchController getInstance() {
		if(instance == null)
			instance = new MatchController();
		return instance ;
	}
	
	public void addMatchHandler(MatchHandler matchHandler) {
		this.matchHandler = matchHandler ;
	}
	
	public void addMatchView(MatchView matchView) {
		this.matchView = matchView ;
	}
	
	public MatchHandler getBallManager() {
		return matchHandler;
	}

	public void update() {
		
		matchHandler.moveBalls(matchView.getField());
		matchView.draw();
		
	}

	public void handle(MouseEvent event) {
		if(event.getEventType() == MouseEvent.MOUSE_PRESSED)
		{
			initialX = event.getX();
			initialY = event.getY();
			
			ballTook = matchHandler.tookBall(initialX, initialY); 
			
			if( ballTook != null && ballTook.getPlayer() == 1 && ballTook.getColor() != Ball.WHITE && matchHandler.allStopped() && matchHandler.getTurn())
			{
				ballTook.setColor(Ball.TOOK);
			}
			else {
				initialX = null;
				initialY = null;
				System.out.println("[CLIENT] IS NOT YOUR TURN");
			}
		}
		else if(event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
			
			if(initialX == null)
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
			
			if(ballTook!= null && initialX != null) {
				
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
				
				matchHandler.setTurn(false);
				
				initialX = null;
				initialY = null;
				ballTook = null;
				matchView.setLine(null);
			
				return;
			}
		}
	}
}
