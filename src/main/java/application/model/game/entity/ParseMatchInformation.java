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


	private ArrayList<InformationMatch> informationMatchQueue = null ;
	private InformationMatch lastInformationMatch = null ;
	
	
	public ParseMatchInformation() {
		informationMatchQueue = new ArrayList<InformationMatch>();
	}
	
	public void addNewInformation(String string) {
		
		ArrayList<Ball> balls = new ArrayList<Ball>() ;
		boolean turn ;
		
		StringTokenizer stringTokenizer = new StringTokenizer(string,Protocol.STRINGINFORMATIONDELIMITER);
		
		while (stringTokenizer.hasMoreTokens()) {
			
			StringTokenizer stringTokenizer1 = new StringTokenizer(stringTokenizer.nextToken(), Protocol.INFORMATIONDELIMITER);
			StringTokenizer stringTokenizer2 = new StringTokenizer(stringTokenizer1.nextToken(),Protocol.BALLDELIMITER);
			
			while(stringTokenizer2.hasMoreElements()) {
				
				
				// POSITION # COLOR 
				String[] informationBall = stringTokenizer2.nextToken().split(Protocol.INFORMATIONBALLDELIMITER);
				
				Double x = Double.parseDouble(informationBall[0].split(Protocol.POSITIONBALLDELIMITER)[0]);
				Double y = Double.parseDouble(informationBall[0].split(Protocol.POSITIONBALLDELIMITER)[1]);
				
				int color = Integer.parseInt(informationBall[1]);
				
				Ball b = null ;
				
				if(color != Ball.WHITE)
					b = new Ball(new VectorFioreNoSync(x,y), new VelocityNoSync(0.0), Settings.DIMENSIONSTANDARDBALL , color) ;
				else
					b = new Ball(new VectorFioreNoSync(x,y), new VelocityNoSync(0.0), Settings.DIMENSIONOFBALLTOPLAY , color) ;

				balls.add(b);
			}
			
			System.out.println(balls.size());
			
			turn = Boolean.parseBoolean(stringTokenizer1.nextToken());
			
			InformationMatch informationMatch = new InformationMatch(turn, balls);

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
			
			int color = b.getColor() ;
			
			if(player == Ball.RED)
			{
				x = Settings.FIELDWIDTHFRAME - x - b.getRadius() * 2;
				
				switch (color) {
				case Ball.BLUE:
					color = Ball.RED ;
					break;
				case Ball.RED:
					color = Ball.BLUE ;
					break;
				case Ball.WHITE:
					color = Ball.WHITE ;
					break;
				}
				
			}
			
			string += x + Protocol.POSITIONBALLDELIMITER + y ; 
			string += Protocol.INFORMATIONBALLDELIMITER ;
			string += "" + color ;
			string += Protocol.BALLDELIMITER ;
		}
		
		string += Protocol.INFORMATIONDELIMITER ;
		string += "" + turn ;
	
		return string ;
		
	}
	
	
	public ArrayList<InformationMatch> getInformationMatchQueue() {
		return informationMatchQueue;
	}
	
	public InformationMatch getInformationMatch() {
		if(informationMatchQueue == null)
			return null ;
		
		if(informationMatchQueue.isEmpty())
			return getLastInformationMatch();
		
		lastInformationMatch = informationMatchQueue.get(0);
		informationMatchQueue.remove(0);
		return lastInformationMatch;
	}
	
	public InformationMatch getLastInformationMatch() {
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

	
	private boolean intersect(Ball b1,double x,double y) {
		
		VectorFioreNoSync position = new VectorFioreNoSync(x, y);
		VectorFioreNoSync dist = VectorFioreNoSync.sub(b1.getPositionCenter(), position);
			
		return dist.getMagnitude() <= b1.getRadius();
	}
	
	
	
}
