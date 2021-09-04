package application.net.common;

import java.util.ArrayList;

public class Protocol {

	
	// ERRORS
	public static final String GENERALERROR = "General error , app will reloading...";
	public static final String INPUT_STREAM_NULL = "Input stream is null";
	public static final String OUTPUT_STREAM_NULL = "Output stream is null";
	public static final String RELOADING_APP = "App will be reloaded";
	public static final String CONNECTION_LOST ="Connection lost";
	public static final String SERVERDISCONNETED = "Server is disconnected";
	public static final String BADBEHAVIOROFCLIENT = "Bad client behavior ";
	public static final String MATCHSUCCEED = "Match completed without errors";
	public static final String MATCHFAILED = "Match completed with errors";
	
	// Email
	public static final String EMAILASSISTANCE = "fiorentinosalvatore65@gmail.com";
	
	
	
	// Login
	public static final String LOGINREQUEST = "New login request" ;
	public static final String DELIMITERLOGIN = "-" ;
	public static final String LOGINCOMPLETED = "Login completed!" ;
	public static final String LOGINFAILED = "We had a problem with your login , please try again or contact email: " + EMAILASSISTANCE;
	
	
	// Registration
	public static final String REGISTRATIONREQUEST = "New registration request" ;
	public static final String DELIMITERREGISTRATION = "-" ;
	public static final String REGISTRATIONCOMPLETED = "Registration completed!";
	public static final String REGISTRATIONFAILED = "We had a problem with your registration , please try again or contact email: " + EMAILASSISTANCE;
	
	// Match
	public static final String PREPARINGMATCH = "Let's prepare the match" ;
	public static final String NEWGAMEREQUESTFIELD1 = "A new game request on field 1" ;
	public static final String NEWGAMEREQUESTFIELD2 = "A new game request on field 2" ;
	public static final String NEWGAMEREQUESTFIELD3 = "A new game request on field 3" ;
	public static final String MOVEBALL = "The player moved the ball" ; 
	public static final String SCORED = "The player scored" ;
	public static final String LEFTGAME = "The player left the game" ;
	public static final String GAMEOVER = "The game is over" ;
	public static final String GAMESTARTED = "The game has started" ;
	public static final String MYUSERNAMEIS = "Username is:";
	public static final String USERNAMEGUEST = "The username guest is" ;
	public static final String ITSTHETURNOF = "It's the turn of" ;
	public static final String ITSYOURTURN = "It's your turn" ;
	public static final String ITSNOTYOURTURN = "It's not your turn" ;
	public static final String TYPEOFLINEUP = "Type of lineup" ;
	public static final String NOBALLTOOK = "You didn't take a ball" ;
	public static final String NOTYOURBALL = "That is not your ball" ;
	public static final String NOTALLARESTOPPED = "Every ball is not stopped yet" ;
	public static final String INFORMATIONMATCHMESSAGE = "A information match message" ;
	public static final String STRINGINFORMATIONDELIMITER = "$";
	public static final String INFORMATIONDELIMITER = "&";
	public static final String BALLDELIMITER = ";";
	public static final String INFORMATIONBALLDELIMITER = "#";
	public static final String POSITIONBALLDELIMITER = ":" ;
	
	public static ArrayList<String> protocolMatch(){
		
		ArrayList<String> strings = new ArrayList<String>();
		
		strings.add(PREPARINGMATCH);
		strings.add(MOVEBALL);
		strings.add(SCORED);
		strings.add(LEFTGAME);
		strings.add(GAMEOVER);
		strings.add(GAMESTARTED);
		strings.add(MYUSERNAMEIS);
		strings.add(USERNAMEGUEST);
		strings.add(ITSTHETURNOF);
		strings.add(ITSYOURTURN);
		strings.add(ITSNOTYOURTURN);
		strings.add(TYPEOFLINEUP);
		
		return strings ;
	}
	
	public static Double[] parseCoordinates(String coordinates) {
		
		Double[] coordinatesI = new Double[2];
		
		String[] a = coordinates.split("&");
		
		coordinatesI[0] = Double.parseDouble(a[0]);
		coordinatesI[1] = Double.parseDouble(a[1]);
		
		return coordinatesI;
	}
	
}
