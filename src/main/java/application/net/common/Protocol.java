package application.net.common;

import java.util.ArrayList;
import java.util.StringTokenizer;

import application.model.game.physics.VectorFioreNoSync;

public class Protocol {

	
	// Match
	public static final String NEWGAMEREQUEST = "A new game request" ;
	public static final String MOVEBALL = "The player moved the ball" ; 
	public static final String SCORED = "The player scored" ;
	public static final String LEFTGAME = "The player left the game" ;
	public static final String GAMEOVER = "The game is over" ;
	public static final String GAMESTARTED = "The game has started" ;
	public static final String MYUSERNAMEIS = "Username is:";
	public static final String USERNAMEGUEST = "The username guest is" ;
	public static final String ITSTHETURNOF = "It's the turn of" ;
	public static final String ITSYOURTURN = "It's your turn" ;
	public static final String ITSNOTYOURTURN = "It's not your turn";
	public static final String TYPEOFLINEUP = "Type of lineup" ;
	
	
	public static ArrayList<VectorFioreNoSync> parsePositions(String positions){
		
		ArrayList<VectorFioreNoSync> array = new ArrayList<VectorFioreNoSync>();
		StringTokenizer stringTokenizer = new StringTokenizer(positions, ";");
		
		while(stringTokenizer.hasMoreTokens()) {
			
			String token = stringTokenizer.nextToken();
			
			double x ;
			double y ;
			
			String[] v = token.split("&");
			
			x = Integer.parseInt(v[0]);
			y = Integer.parseInt(v[1]);
			
			array.add(new VectorFioreNoSync(x, y));
		}
		
		return array;
	}
	
	
	public static Double[] parseCoordinates(String coordinates) {
		
		Double[] coordinatesI = new Double[2];
		
		String[] a = coordinates.split("-");
		
		coordinatesI[0] = Double.parseDouble(a[0]);
		coordinatesI[1] = Double.parseDouble(a[1]);
		
		return coordinatesI;
	}
	
	
}
