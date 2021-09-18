package application.model.game.entity;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

import application.Settings;
import application.model.game.physics.VectorFioreNoSync;
import application.model.game.physics.VelocityNoSync;
import application.net.common.Protocol;

public class ParseMatchInformation {


	private ArrayList<Frame> informationMatchQueue = null ;
	private Frame lastInformationMatch = null ;
	private boolean ready = false ;
	private boolean homeScored = false ;
	private boolean guestScored = false ;
	
	public ParseMatchInformation() {
		informationMatchQueue = new ArrayList<Frame>();
	}
	
	public void setHomeScored(boolean homeScored) {
		this.homeScored = homeScored;
	}
	
	public void setGuestScored(boolean guestScored) {
		this.guestScored = guestScored;
	}
	
	public boolean isGuestScored() {
		return guestScored;
	}
	public boolean isHomeScored() {
		return homeScored;
	}
	
	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public void addNewInformation(String string) {
		
		ArrayList<Ball> balls ;
		boolean turn ;
		
		StringTokenizer stringTokenizer = new StringTokenizer(string,Protocol.STRINGINFORMATIONDELIMITER);
		
		while (stringTokenizer.hasMoreTokens()) {
			
			StringTokenizer stringTokenizer1 = new StringTokenizer(stringTokenizer.nextToken(), Protocol.INFORMATIONDELIMITER);
			StringTokenizer stringTokenizer2 = new StringTokenizer(stringTokenizer1.nextToken(),Protocol.BALLDELIMITER);
			balls = new ArrayList<Ball>() ;
			while(stringTokenizer2.hasMoreElements()) {
				
				
				// POSITION # PLAYER 
				String[] informationBall = stringTokenizer2.nextToken().split(Protocol.INFORMATIONBALLDELIMITER);
				
				Double x = Double.parseDouble(informationBall[0].split(Protocol.POSITIONBALLDELIMITER)[0]);
				Double y = Double.parseDouble(informationBall[0].split(Protocol.POSITIONBALLDELIMITER)[1]);
				
				int player = Integer.parseInt(informationBall[1]);
				
				Ball b = null ;
				
				String color = informationBall[2] ;
				
				if(player != Ball.WHITE)
					b = new Ball(new VectorFioreNoSync(x,y), new VelocityNoSync(0.0), Settings.DIMENSIONSTANDARDBALL ,color , player) ;
				else
					b = new Ball(new VectorFioreNoSync(x,y), new VelocityNoSync(0.0), Settings.DIMENSIONOFBALLTOPLAY ,color , player) ;

				balls.add(b);
			}
			
			turn = Boolean.parseBoolean(stringTokenizer1.nextToken());
			
			Frame informationMatch = new Frame(turn, balls);

			informationMatchQueue.add(informationMatch);
		}
		
	}
	
	public static String getString(ArrayList<Ball> balls,boolean turn , int player) {
		
		String string = "" ; 
		
		double x ;
		double y ;
		
		for(Ball b : balls) {
			
			x = b.getPosition().getX();
			y = b.getPosition().getY();
			
			int owner = b.getPlayer() ;
			
			if(player == Ball.PLAYER2)
			{
				x = Settings.FIELDWIDTHFRAME - x - b.getRadius() * 2;
				
				switch (owner) {
				case Ball.PLAYER1:
					owner = Ball.PLAYER2 ;
					break;
				case Ball.PLAYER2:
					owner = Ball.PLAYER1 ;
					break;
				case Ball.WHITE:
					owner = Ball.WHITE ;
					break;
				}
				
			}
			
			string += x + Protocol.POSITIONBALLDELIMITER + y ; 
			string += Protocol.INFORMATIONBALLDELIMITER ;
			string += "" + owner ;
			string += Protocol.INFORMATIONBALLDELIMITER ;
			string += b.getColor() ;
			string += Protocol.BALLDELIMITER ;
		}
		
		string += Protocol.INFORMATIONDELIMITER ;
		string += "" + turn ;
	
		return string ;
		
	}
	
	
	public ArrayList<Frame> getInformationMatchQueue() {
		return informationMatchQueue;
	}
	
	public Frame getInformationMatch() {
		if(informationMatchQueue == null)
			return null ;
		
		if(informationMatchQueue.isEmpty())
			return getLastInformationMatch();
		
		lastInformationMatch = informationMatchQueue.get(0);
		informationMatchQueue.remove(0);
		return lastInformationMatch;
	}
	
	public Frame getLastInformationMatch() {
		if(lastInformationMatch == null && !informationMatchQueue.isEmpty())
			lastInformationMatch = informationMatchQueue.get(0);
		return lastInformationMatch;
	}
	
	public Ball tookBall(double x , double y) {
		for(Ball b: lastInformationMatch.getBalls())
		{
			
			if(intersect(b, x, y)) 
				return b;
		}
		return null;
	}

	public void setHoverFalseAll() {
		for(Ball ball : getLastInformationMatch().getBalls())
			ball.setHover(false);
	}
	
	public void setHover(VectorFioreNoSync position) {
		for(Ball ball : getLastInformationMatch().getBalls())
			if(ball.getPosition().equals(position))
			{
				ball.setHover(true);

				return ;
			}

	}
	
	private boolean intersect(Ball b1,double x,double y) {

		VectorFioreNoSync position = new VectorFioreNoSync(x, y);
		VectorFioreNoSync dist = VectorFioreNoSync.sub(b1.getPositionCenter(), position);
			
		return dist.getMagnitude() <= b1.getRadius() ;
	}
	
	
	
}
