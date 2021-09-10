package application.net.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import application.Settings;
import application.model.game.entity.LoginClient;
import application.model.game.entity.RegistrationClient;
import application.net.common.Protocol;

public class ClientHandler implements Runnable {

	private static final String INITIALWAITING = "[CLIENTHANDLER] Waiting for a registration or login request";
	
	private Socket client = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private String username = null;
	private boolean throwMessagesMatch = false;
	private Server server;

	public String getUsername() {
		return username;
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

			} else {

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
					return ;
				}else {
					sendMessage(Protocol.GENERALERROR);
					printConnectionLost();
					return;
				}
			}else if(message.equals(Protocol.LOGOUT)) {
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
		RequestMatchHandler.getInstace().removePlayerField1(this);
		RequestMatchHandler.getInstace().removePlayerField2(this);
		RequestMatchHandler.getInstace().removePlayerField3(this);
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

				if (message == null) {
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
