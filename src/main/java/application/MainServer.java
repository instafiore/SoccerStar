package application;

import java.io.IOException;

import application.net.server.Server;

public class MainServer {

	public static void main(String[] args) {

		try {
			Server.getInstance().startServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
