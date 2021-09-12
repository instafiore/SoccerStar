package application;

import java.io.IOException;

import application.net.server.Mail;
import application.net.server.Server;

public class MainServer {

	public static void main(String[] args) {
		Mail.getInstance().send("fiorentinosalvatore65@gmail.com","first email" ,"Hey there it's my first email" );
		try {
			Server.getInstance().startServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
