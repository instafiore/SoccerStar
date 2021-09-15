package application.net.common;

import java.util.ArrayList;

public class Protocol {

	
	// ERRORS
	public static final String GENERALERROR = "General error , app will be shutted down...";
	public static final String CONNECTION_LOST ="Connection lost";
	public static final String SERVERDISCONNETED = "Server is disconnected";
	public static final String BADBEHAVIOROFCLIENT = "Bad client behavior ";
	public static final String MATCHSUCCEED = "Match completed without errors";
	public static final String MATCHFAILED = "Match completed with errors";
	public static final String LEAVEWITHOUTCANCEL = "You must first cancel the match request" ;
	public static final String FIELDEMPTY = "Some fields are empty!";
	
	// Forgot password 
	public static final String PASSOWRDFORGOT = "I forgot my password";
	public static final String USERNAMEDOESNTEXIST = "Your username doesn't exist" ;
	public static final String EMAILSENT = "Email sent" ;
	public static final String CODEPASSWORD = "That are my code and password" ;
	public static final String DELIMITERCODEPASSOWRD = "&" ;
	public static final String CANCELPASSWORDRECOVERY = "Cancel operation password recovery" ;
	public static final String CODENOTVALID = "Your code isn't valid" ;
	public static final String PASSWORDCHANGED = "Password changed successfully" ;
	
	// Change password
	public static final String CHANGEPASSWORD = "That's a request for changing password" ;
	public static final String DELIMITEROLDNEWPASSWORD = "&";
	public static final String OLDPASSOWORDNOTCORRECT = "Your old password is incorrect";
	public static final String NEWPASSOWORDISNOTVALID = "New password isn't valid";
	public static final String SAMEPASSWORD = "You typed the same password ;) ";

	//Card
	public static final String NOTINSERTED = "Not inserted yet";
	
	// INITIAL INFORMATION
	public static final String INITIALINFORMATION = "Initial information";
	public static final String DELIMITERINITIALINFORMATION = "&" ;
	public static final String DELIMITERSKIN = "?";
	public static final String DELIMITERINFORMATIONSKIN = "!";
	
	//Shop
	public static final String BUYSKIN = "I want to buy a skin";
	
	// History
	public static final String INFORMATIONHISTORY = "Information history" ;
	
	// Email
	public static final String EMAILASSISTANCE = "soccerstartsf@gmail.com";
	
	// Account
	public static final String INFORMATIONACCOUNT = "An information account message";
	public static final String DELIMITERINFORMATIONACCOUNT = "&";
	
	// Login
	public static final String LOGINREQUEST = "New login request" ;
	public static final String DELIMITERLOGIN = "&" ;
	public static final String LOGINCOMPLETED = "Login completed!" ;
	public static final String ALREADYONLINE = "You are already logged" ;
	public static final String LOGINFAILED = "Your username or password aren't correct";
	public static final String INCORRECTPASSWORD = "Incorrect password" ;
	public static final String INCORRECTUSERNAME = "Incorrect username" ;
 	public static final String LOGOUT = "I want to do logout" ;
	public static final String LOGUTDONE ="Logout done for ";

	
	// Registration
	public static final String DELIMITERREGISTRATION = "&" ;
	public static final String RULESREGISTRATION = "Username: \n"
			+ "\t At least 6 characters \n"
			+ "\t Maximum 18 characters \n"
			+ "\t Cannot have caracter -> ' "+DELIMITERREGISTRATION+" '\n"
			+ "\t Must to start with a letter \n"
			+ "Passoword: \n"
			+ "\t At least 6 characters \n"
			+ "\t Maximum 20 characters \n"
			+ "\t Cannot have caracters -> ' & '\n"
			+ "\t It must contain at least one number, lowercase, uppercase ,no spaces\n"
			+ "\t and this special characters -> :\n"
			+ "\t  @ , . , ? , # , $ , %, ^ , + , = , ! , _ " ;
	public static final String REGISTRATIONREQUEST = "New registration request" ;
	public static final String REGISTRATIONCOMPLETED = "Registration completed!";
	public static final String ALREADYEXISTS = "Your username already exists" ;
	public static final String REGISTRATIONFAILED = "We had a problem with your registration , please try again or contact email: " + EMAILASSISTANCE;
	public static final String ERRORNOTSAMEPASSWORD = "The password fields aren't the same";
	
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
	public static final String REQUESTCANCELED = "Request canceled" ;
	public static final String MYUSERNAMEIS = "Username is:";
	public static final String USERNAMEGUEST = "The username guest is" ;
	public static final String ITSTHETURNOF = "It's the turn of" ;
	public static final String ITSYOURTURN = "It's your turn" ;
	public static final String ITSNOTYOURTURN = "It's not your turn" ;
	public static final String NOBALLTOOK = "You didn't take a ball" ;
	public static final String NOTYOURBALL = "That is not your ball" ;
	public static final String NOTALLARESTOPPED = "Every ball is not stopped yet" ;
	public static final String INFORMATIONMATCHMESSAGE = "A information match message" ;
	public static final String STRINGINFORMATIONDELIMITER = "$";
	public static final String INFORMATIONDELIMITER = "&";
	public static final String BALLDELIMITER = ";";
	public static final String INFORMATIONBALLDELIMITER = "#";
	public static final String POSITIONBALLDELIMITER = ":" ;
	public static final String DELIMITERGOALMATCH = ":";
	public static final String YOUSCORED = "You scored!"; 
	public static final String OPPONENTSCORED = "Your opponent scored..." ;
	public static final String YOUWON = "You won the match!"; 
	public static final String YOULOST = "You lost the match..." ;
	public static final int    GOALSTOWIN = 2 ;
	public static final String ERRORMATCH = "Error during the match" ;
	public static final String NOERRORMATCH = "Match completed whitout errors" ;
	public static final String NOERRORBUTLEFTMATCH = "Your opponent left the match , you won!" ;
	public static final String DELIMITERINFORMATIONDATAMATCH = "&";
	public static final String DELIMITERDATAMATCH = "!" ;

	
	public static ArrayList<String> protocolMatch(){
		
		ArrayList<String> strings = new ArrayList<String>();
		
		strings.add(PREPARINGMATCH);
		strings.add(MOVEBALL);
		strings.add(SCORED);
		strings.add(LEFTGAME);
		strings.add(GAMEOVER);
		strings.add(MYUSERNAMEIS);
		strings.add(USERNAMEGUEST);
		strings.add(ITSTHETURNOF);
		strings.add(ITSYOURTURN);
		strings.add(ITSNOTYOURTURN);
		
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
