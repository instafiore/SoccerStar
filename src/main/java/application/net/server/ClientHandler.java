package application.net.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.sun.scenario.effect.impl.prism.PrTexture;

import application.Settings;
import application.model.game.entity.Account;
import application.model.game.entity.DataMatch;
import application.model.game.entity.Lineup;
import application.model.game.entity.LoginClient;
import application.model.game.entity.RegistrationClient;
import application.model.game.entity.Skin;
import application.net.common.Protocol;

public class ClientHandler implements Runnable {

	private static final String INITIALWAITING = "[CLIENTHANDLER] Waiting for a registration or login request";
	
	private Socket client = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private String username = null;
	private boolean throwMessagesMatch = false;
	private Server server;
	private String currentField = Settings.FIELD1 ;

	public String getUsername() {
		return username;
	}

	
	public void setCurrentField(String currentField) {
		this.currentField = currentField;
	}
	
	public String getCurrentField() {
		return currentField;
	}
	
	public ClientHandler(Socket client, Server server) {
		super();
		this.client = client;
		this.server = server;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(new BufferedOutputStream(client.getOutputStream()), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BufferedReader getIn() {
		return in;
	}

	public PrintWriter getOut() {
		return out;
	}

	public Socket getClient() {
		return client;
	}

	public void run() {

		String message = null;

		System.out.println(INITIALWAITING);
		
		while (username == null) {

			message = read();

			if (message == null)
				return;

			if (message.equals(Protocol.REGISTRATIONREQUEST)) {

				message = read();

				if (message == null) {
					printConnectionLost();
					return;
				}

				RegistrationClient client = new RegistrationClient();
				client.parseRegistrationClient(message);

				String res = Database.getInstance().insertUser(client) ;
				
				if (res.equals(Protocol.REGISTRATIONCOMPLETED)) {
					sendMessage(Protocol.REGISTRATIONCOMPLETED);
					username = client.getUsername();
					sendMessage(username);

				} else {
					sendMessage(res);
					username = null;

				}

			} else if (message.equals(Protocol.LOGINREQUEST)) {

				message = read();
				if (message == null)
					return;
				LoginClient client = new LoginClient();
				client.parseLoginClient(message);
				
				String res = Database.getInstance().checkLogin(client) ;
				if (res.equals(Protocol.LOGINCOMPLETED)) {
					sendMessage(Protocol.LOGINCOMPLETED);
					username = client.getUsername();
					sendMessage(username);
				} else {
					sendMessage(res);
					username = null;
				}

			}else if(message.equals(Protocol.PASSOWRDFORGOT)) {
				
				String usernamerecory = read() ;
				
				if (usernamerecory == null)
					return;
				
				if(!Database.getInstance().checkUser(usernamerecory))
				{
					sendMessage(Protocol.USERNAMEDOESNTEXIST);
					
				}else{
					
					Mail.getInstance().send(usernamerecory);
					StringBuilder emailCovered = null;
					try {
						emailCovered = new StringBuilder(Database.getInstance().getAccount(usernamerecory).getEmail()) ;
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					for(int i = 0 ; i < emailCovered.length() - 18 ; ++i)
						emailCovered.setCharAt(i, 'x');
					
					String text = "Email sent to "+emailCovered+" , please insert code here and reset your password";
					
					sendMessage(Protocol.EMAILSENT);
					sendMessage(text);
					
					boolean f = true ;
					do {

						message  = read() ;
						
						if (message == null)
							return;
						
						if(message.equals(Protocol.CODEPASSWORD)) {
							
							message = read() ;
							
							if (message == null)
								return;
							
							StringTokenizer stringTokenizer = new StringTokenizer(message,Protocol.DELIMITERCODEPASSOWRD) ;
							
							String code = stringTokenizer.nextToken() ;
							String password = stringTokenizer.nextToken() ;
							
							if(Mail.getInstance().checkCode(usernamerecory, code))
							{
								Database.getInstance().changePassword(usernamerecory, password);
								sendMessage(Protocol.PASSWORDCHANGED);
								f = false ;
							}else
							{
								f = true ;
								sendMessage(Protocol.CODENOTVALID);
							}
						
						}else if(!message.equals(Protocol.CANCELPASSWORDRECOVERY)) {
							// ERROR
							sendMessage(Protocol.GENERALERROR);
							username = null;
							printConnectionLost();
							return;
						}else
							f = false ;
					}while(f);
					
				}
				
			}else {

				// ERROR
				sendMessage(Protocol.GENERALERROR);
				username = null;
				printConnectionLost();
				return;
			}
		}

		server.addUserOnline(username);

		while (!Thread.interrupted()) {

			System.out.println("[CLIENTHANDLER] CLIENT HANDLER IS RUNNING FOR " + username);

			message = read();

			if (message == null)
				return;

			if (message.equals(Protocol.NEWGAMEREQUESTFIELD1)) {

				RequestMatchHandler.getInstace().addPlayerField1(this);
				
				message = read();

				if (message == null)
					return;
				
				if(message.equals(Protocol.GAMESTARTED)) {
					try {
						synchronized (this) {
							wait();
						}	
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(message.equals(Protocol.REQUESTCANCELED)){
					RequestMatchHandler.getInstace().removePlayer(this);
				}else if(message.equals(Protocol.CONNECTION_LOST)) {
					RequestMatchHandler.getInstace().removePlayer(this);
					Server.getInstance().removeUserOnline(username);
					return ;
				}else {
					sendMessage(Protocol.GENERALERROR);
					printConnectionLost();
					return;
				}
				
			} else if (message.equals(Protocol.NEWGAMEREQUESTFIELD2)) {

				RequestMatchHandler.getInstace().addPlayerField2(this);
				
				
				message = read();

				if (message == null)
					return;
				
				if(message.equals(Protocol.GAMESTARTED)) {
					try {
						synchronized (this) {
							wait();
						}	
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(message.equals(Protocol.REQUESTCANCELED)){
					RequestMatchHandler.getInstace().removePlayer(this);
				}else if(message.equals(Protocol.CONNECTION_LOST)) {
					RequestMatchHandler.getInstace().removePlayer(this);
					Server.getInstance().removeUserOnline(username);
					return ;
				}else {
					sendMessage(Protocol.GENERALERROR);
					printConnectionLost();
					return;
				}
			} else if (message.equals(Protocol.NEWGAMEREQUESTFIELD3)) {

				RequestMatchHandler.getInstace().addPlayerField3(this);
		
				
				message = read();

				if (message == null)
					return;
				
				if(message.equals(Protocol.GAMESTARTED)) {
					try {
						synchronized (this) {
							wait();
						}	
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(message.equals(Protocol.REQUESTCANCELED)){
					RequestMatchHandler.getInstace().removePlayer(this);
				}else if(message.equals(Protocol.CONNECTION_LOST)) {
					RequestMatchHandler.getInstace().removePlayer(this);
					Server.getInstance().removeUserOnline(username);
					return ;
				}else {
					sendMessage(Protocol.GENERALERROR);
					printConnectionLost();
					return;
				}
			}else if(message.equals(Protocol.INFORMATIONACCOUNT)) {
				Account account;
				try {
					account = Database.getInstance().getAccount(username);
				} catch (SQLException e) {
					sendMessage(Protocol.GENERALERROR);
					printConnectionLost();
					return;
				}
				String string = account.getUsername() + Protocol.DELIMITERINFORMATIONACCOUNT + account.getPassword() + Protocol.DELIMITERINFORMATIONACCOUNT +
						account.getCoins() + Protocol.DELIMITERINFORMATIONACCOUNT + 
						account.getCurrentSkin() + Protocol.DELIMITERINFORMATIONACCOUNT + account.getEmail() + Protocol.DELIMITERINFORMATIONACCOUNT+
						account.getLineup() ;
				sendMessage(Protocol.INFORMATIONACCOUNT);
				sendMessage(string);
			}else if(message.equals(Protocol.INFORMATIONHISTORY)) {
				List<DataMatch> dataMatches;
				
				dataMatches = Database.getInstance().getDataMatches(username);

				String string = "" ;
				for(DataMatch dataMatch : dataMatches) {
					string += dataMatch.getHome() + Protocol.DELIMITERINFORMATIONDATAMATCH + dataMatch.getGuest()  + Protocol.DELIMITERINFORMATIONDATAMATCH +
							dataMatch.getField()  + Protocol.DELIMITERINFORMATIONDATAMATCH + dataMatch.getResult()  + Protocol.DELIMITERINFORMATIONDATAMATCH +
							dataMatch.getDate()  + Protocol.DELIMITERINFORMATIONDATAMATCH + dataMatch.getTime() + Protocol.DELIMITERINFORMATIONDATAMATCH  +
							dataMatch.getColorField() + Protocol.DELIMITERINFORMATIONDATAMATCH + dataMatch.getColorHome() + Protocol.DELIMITERINFORMATIONDATAMATCH + dataMatch.getColorGuest();
					string +=  Protocol.DELIMITERDATAMATCH ;
				}
			
				sendMessage(Protocol.INFORMATIONHISTORY);
				sendMessage(string);
			}else if(message.equals(Protocol.CHANGEPASSWORD)) {
				
				message = read() ;
				
				if (message == null)
					return;
				
				StringTokenizer stringTokenizer = new StringTokenizer(message, Protocol.DELIMITEROLDNEWPASSWORD);
				String oldPassword = stringTokenizer.nextToken() ;
				String newPassword = stringTokenizer.nextToken() ;
			
				if(!Database.getInstance().checkPassword(username, oldPassword))
					sendMessage(Protocol.OLDPASSOWORDNOTCORRECT);
				else
				{
					Database.getInstance().changePassword(username, newPassword);
					sendMessage(Protocol.PASSWORDCHANGED);
				}
					
				
			}else if(message.equals(Protocol.INITIALINFORMATION)) {
				
				String text = "" ;
				
				String coins = "";
				try {
					coins = "" + Database.getInstance().getAccount(username).getCoins();
				} catch (SQLException e) {
					sendMessage(Protocol.GENERALERROR);
					printConnectionLost();
					return;
				}
			
				text = coins ;
				
				sendMessage(Protocol.INITIALINFORMATION);
				sendMessage(text);
				
			}else if(message.equals(Protocol.INFORMATIONSHOP) || message.equals(Protocol.INFORMATIONINVENTARY)) {
				
				String text = "" ;
				
				String skins = "" ;
				
				try {
					for(Skin skin : Database.getInstance().getSkins()) {
						skins += skin.getName() + Protocol.DELIMITERINFORMATIONELEMENTSHOP + skin.getPrice() + Protocol.DELIMITERINFORMATIONELEMENTSHOP + skin.getColor() ;
						skins += Protocol.DELIMITERELEMENTSHOP ;
					}
				} catch (SQLException e) {
					sendMessage(Protocol.GENERALERROR);
					printConnectionLost();
					return;
				}
				String skin_owned  = ""; 
				try {
					skin_owned = Database.getInstance().getOwnedSkins(username);
				} catch (SQLException e) {
					sendMessage(Protocol.GENERALERROR);
					printConnectionLost();
					return;
				}
				
				String lineups = "" ;
				
				try {
					for(Lineup lineup : Database.getInstance().getLineups()) {
						lineups += lineup.getId() + Protocol.DELIMITERINFORMATIONELEMENTSHOP + lineup.getName() + Protocol.DELIMITERINFORMATIONELEMENTSHOP + lineup.getPrice() + Protocol.DELIMITERINFORMATIONELEMENTSHOP + lineup.getImage() ;
						lineups += Protocol.DELIMITERELEMENTSHOP ;
					}
				} catch (SQLException e) {
					sendMessage(Protocol.GENERALERROR);
					printConnectionLost();
					return;
				}
				String lineup_owned  = ""; 
				try {
					lineup_owned = Database.getInstance().getOwnedLineup(username);
				} catch (SQLException e) {
					sendMessage(Protocol.GENERALERROR);
					printConnectionLost();
					return;
				}
				
				String coins = "";
				try {
					coins = "" + Database.getInstance().getAccount(username).getCoins();
				} catch (SQLException e) {
					sendMessage(Protocol.GENERALERROR);
					printConnectionLost();
					return;
				}
				
				text = skins + Protocol.DELIMITERINFORMATIONSHOP + skin_owned + Protocol.DELIMITERINFORMATIONSHOP + lineups + Protocol.DELIMITERINFORMATIONSHOP + lineup_owned + Protocol.DELIMITERINFORMATIONSHOP + coins; 
				
				sendMessage(message);
				sendMessage(text);
				
			}else  if(message.equals(Protocol.SKININUSE)) {
				
				String colorSkin  = "" ;
				try {
					colorSkin = Database.getInstance().getAccount(username).getCurrentSkin() ;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				sendMessage(Protocol.SKININUSE);
				sendMessage(colorSkin);
				
			}else  if(message.equals(Protocol.LINEUPINUSE)) {
				
				int lineup = 0 ;
				try {
					lineup = Database.getInstance().getAccount(username).getLineup() ;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				sendMessage(Protocol.LINEUPINUSE);
				sendMessage(""+lineup);
				
			}else  if(message.equals(Protocol.BUYSKIN)) {
				
				message = read() ;
				
				if (message == null)
					return;
				
				Skin skin = new Skin();
				skin.loadSkin(message);
				
				if(Database.getInstance().buySkin(username, skin))
					sendMessage(Protocol.ELEMENTSHOPBOUGHT);
				else
					sendMessage(Protocol.ELEMENTSHOPNOTBOUGHT);
				
			}else  if(message.equals(Protocol.USETHISSKIN)) {
				
				String color  = read() ;
				
				if (color == null)
					return;
				
				Database.getInstance().updateCurrentSkin(username, color);
				
			}else  if(message.equals(Protocol.USETHISLINEUP)) {
				
				message  = read() ;
				
				if (message == null)
					return;
				
				int idLineup = Integer.parseInt(message);
				Database.getInstance().updateCurrentLineup(username, idLineup);
				
			}else  if(message.equals(Protocol.BUYLINEUP)) {
				
				message = read() ;
				
				if (message == null)
					return;
				
				Lineup lineup = new Lineup();
				
				lineup.loadLineup(message);
				
				
				if(Database.getInstance().buyLineup(username, lineup))
					sendMessage(Protocol.ELEMENTSHOPBOUGHT);
				else
					sendMessage(Protocol.ELEMENTSHOPNOTBOUGHT);
				
				
			}else  if(message.equals(Protocol.LOGOUT)) {
				logout();
				return ;
			}else if(message.equals(Protocol.CONNECTION_LOST)) {
				return ;
			}
			else{

				sendMessage(Protocol.GENERALERROR);
				printConnectionLost();
				return;

			}

			try {
				Thread.sleep(Settings.REFRESHSERVER);
			} catch (InterruptedException e) {
				sendMessage(Protocol.GENERALERROR);
				printConnectionLost();
				e.printStackTrace();
			}
		}

	}


	public void notifyEndMatch() {
		synchronized (this) {
			throwMessagesMatch = true;
			this.notify();
		}
	}

	private void logout() {
		System.out.println("[CLIENTHANDLER] "+Protocol.LOGUTDONE+username);
		Server.getInstance().removeUserOnline(username);
		username = null ;
		Thread t = new Thread(this);
		t.start();
	}
	
	public void sendMessage(String message) {

		if (out == null || message == null)
			return;

		System.out.print("[CLIENTHANDLER] SENT : " + message);

		if (username != null)
			System.out.println(" to: " + username);
		else
			System.out.println(" to unknown receiver");

		out.println(message);
	}

	private void printConnectionLost() {
		if (username != null) {
			System.out.println("[CLIENTHANDLER] " + Protocol.CONNECTION_LOST + " with: " + username);
			server.removeUserOnline(username);
		} else
			System.out.println("[CLIENTHANDLER] " + Protocol.CONNECTION_LOST);
		closeStreams();
	}

	public String read() {

 		String message = null;
		try {

			do {

				if (in == null || out == null || client.isClosed()) {
					printConnectionLost();
					return null;
				}

				message = in.readLine();

				if (message == null || message.equals(Protocol.CONNECTION_LOST)) {
					printConnectionLost();
					return null;
				}

				if (Protocol.protocolMatch().contains(message) && !throwMessagesMatch) {
					// ERROR
					System.out.println("[CLIENTHANDLER] Received a unexpected: " + Protocol.GENERALERROR+message);
					printConnectionLost();
					return null;
				}

			} while (Protocol.protocolMatch().contains(message) && throwMessagesMatch);

			throwMessagesMatch = false;

			if (username != null)
				System.out.println("[CLIENTHANDLER] Message from : " + username + " -> receive: " + message);
			else
				System.out.println("[CLIENTHANDLER] Unknown sender -> receive: " + message);

		} catch (IOException e) {
			printConnectionLost();
			return null;
		}
		return message;
	}

	private void closeStreams() {

		try {
			if (in != null)
				in.close();
			in = null;
			if (out != null)
				out.close();
			out = null;
			if (client != null && !client.isClosed())
				client.close();
			client = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
