package application.control;

import application.Settings;
import application.model.Arrow;
import application.model.Ball;
import application.model.BallManager;
import application.model.Lineup;
import application.model.VectorFioreNoSync;
import application.model.VelocityNoSync;
import application.view.MatchView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;

public class MatchController implements EventHandler<MouseEvent>{

	private MatchHandler ballManager = null;
	private MatchView ballsPanel = null;
	private Arrow arrow = null;
	private long interval = 500;

	private long startPress;
	private long endPress;
	
	private Double initialX = null ;
	private Double initialY = null ;
	
	private Lineup lineup ;
	private Ball ballTook = null;
	
	public MatchController(MatchHandler ballManager, MatchView ballsPanel) {
		super();
		this.ballManager = ballManager;
		this.ballsPanel = ballsPanel;
		
		double x1 = 490;
		double y1 = 300;
		VectorFioreNoSync position1 = new VectorFioreNoSync(x1,y1);
		Ball ball1 = new Ball(position1,new VelocityNoSync(0.0),20.0);
		System.out.println(ball1);
		ballManager.add(ball1);
		double x2 = 400;
		double y2 = 200;
		VectorFioreNoSync position2 = new VectorFioreNoSync(x2,y2);
		Ball ball2 = new Ball(position2,new VelocityNoSync(0.0),20.0);
		System.out.println(ball2);
		ballManager.add(ball2);
		double x3 = 800;
		double y3 = 200;
		VectorFioreNoSync position3 = new VectorFioreNoSync(x3,y3);
		Ball ball3 = new Ball(position3,new VelocityNoSync(0.0),20.0);
		System.out.println(ball3);
		ballManager.add(ball3);
		double x4 = 150;
		double y4 = 150;
		VectorFioreNoSync position4 = new VectorFioreNoSync(x4,y4);
		Ball ball4 = new Ball(position4,new VelocityNoSync(0.0),20.0);
		System.out.println(ball4);
		ballManager.add(ball4);
		double x5 = 450;
		double y5 = 250;
		VectorFioreNoSync position5 = new VectorFioreNoSync(x5,y5);
		Ball ball5 = new Ball(position5,new VelocityNoSync(0.0),20.0);
		System.out.println(ball5);
		ballManager.add(ball5);
		
		lineup = new Lineup(ballManager.getBalls(), Lineup.LINEUP1);
		
		
		
//		double x6 = 850;
//		double y6 = 250;
//		VectorFioreNoSync position6 = new VectorFioreNoSync(x6,y6);
//		Ball ball6 = new Ball(position6,new VelocityNoSync(0.0),20.0);
//		System.out.println(ball6);
//		ballManager.add(ball6);
//		double x7 = 180;
//		double y7 = 180;
//		VectorFioreNoSync position7 = new VectorFioreNoSync(x7,y7);
//		Ball ball7 = new Ball(position7,new VelocityNoSync(0.0),20.0);
//		System.out.println(ball7);
//		ballManager.add(ball7);
//		double x8 = 700;
//		double y8 = 300;
//		VectorFioreNoSync position8 = new VectorFioreNoSync(x8,y8);
//		Ball ball8 = new Ball(position8,new VelocityNoSync(0.0),20.0);
//		System.out.println(ball8);
//		ballManager.add(ball8);
//		double x9 = 600;
//		double y9 = 700;
//		VectorFioreNoSync position9 = new VectorFioreNoSync(x9,y9);
//		Ball ball9 = new Ball(position9,new VelocityNoSync(0.0),20.0);
//		System.out.println(ball9);
//		ballManager.add(ball9);
//		double x10 = 900;
//		double y10 = 700;
//		VectorFioreNoSync position10 = new VectorFioreNoSync(x10,y10);
//		Ball ball10 = new Ball(position10,new VelocityNoSync(0.0),20.0);
//		System.out.println(ball10);
//		ballManager.add(ball10);
		double x11 = Settings.WIDTHFRAME * 0.50 - 6 ;
		double y11 = Settings.HEIGHTFRAME * 0.50 - 6 ;
		VectorFioreNoSync position11 = new VectorFioreNoSync(x11,y11);
		Ball ball11 = new Ball(position11,new VelocityNoSync(0.0),12.0);
		ball11.setColor(Ball.WHITE);
		System.out.println(ball11);
		ballManager.add(ball11);
		
	}

	public MatchHandler getBallManager() {
		return ballManager;
	}

	@Override
	public void handle(MouseEvent event) {
		
		if(event.getEventType() == MouseEvent.MOUSE_PRESSED)
		{
			initialX = event.getX();
			initialY = event.getY();
			
			ballTook = ballManager.tookBall(initialX, initialY); 
			if( ballTook != null)
			{
				ballTook.setColor(Ball.RED);
			}
			else {
				initialX = null;
				initialY = null;
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
			
			ballsPanel.setLine(new Line(x,y,positionLine.getX(),positionLine.getY()));
			return;
		}else if(event.getEventType() == MouseEvent.MOUSE_RELEASED) {
			
			if(ballTook!= null) {
				
				double finalX = event.getX();
				double finalY = event.getY();
				
				VectorFioreNoSync length = VectorFioreNoSync.sub(new VectorFioreNoSync(initialX, initialY), new VectorFioreNoSync(finalX, finalY));

				if(length.getMagnitude() > Settings.MAXIMUMVELOCITY)
					length.setMag(Settings.MAXIMUMVELOCITY);
				
				VelocityNoSync v = new VelocityNoSync(length.getX(),length.getY()) ;
				
				v.mult(150.0);
				
				ballTook.setVelocity(v);
				ballTook.setColor(Ball.BLUE);
				initialX = null;
				initialY = null;
				ballTook = null;
				ballsPanel.setLine(null);
				return;
			}
		}
		
		arrow = null;
	}
	
	public void update() {
		
		ballManager.moveBalls(ballsPanel.getField());
		ballsPanel.draw();
		
	}

	public Arrow getArrow() {
		return arrow;
	}
	
}
