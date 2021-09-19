package application.net.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import application.Settings;

public class Server implements Runnable{

	private ServerSocket server ;
	private ArrayList<String> usersOnline =  new ArrayList<String>();
	private HashMap<String, ClientHandler> clientsHandler = new HashMap<String,ClientHandler>() ;
	private static Server instance = null ;
	
	public static Server getInstance() {
		if(instance == null)
			instance = new Server();
		return instance;
	}
	
	
	private Server() {}
	
	public void startServer() throws IOException {
	
		server = new ServerSocket(Settings.PORT);
		
		System.out.println("[SERVER] Server has started...");
		
		try {
			Database.getInstance().connectToDatabase();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("[SERVER] Database not connected");
			e.printStackTrace();
		}
		
		Thread t = new Thread(this);
		
		t.start();

	}

	public void addUserOnline(String user) {
		usersOnline.add(user);
	}
	
	public void removeUserOnline(String user) {
		usersOnline.remove(user);
		Database.getInstance().updateInAGame(user, false);
	}
	
	public boolean isOnline(String user) {
		return usersOnline.contains(user);
	}
	
	public ArrayList<String> getUsersOnline() {
		return usersOnline;
	}

	public void addClientHandler(String username , ClientHandler clientHandler) {
		clientsHandler.put(username, clientHandler);
	}
	
	public ClientHandler getClientHandler(String username) {
		return clientsHandler.get(username);
	}
	
	public void run() {
		
		while(!Thread.interrupted()){
			
			System.out.println("[SERVER] Waiting for connections...");
			try {
				
				Socket socket = server.accept();
				System.out.println("[SERVER] New Connection");
				ClientHandler clientHandler = new ClientHandler(socket,this);
				Thread t = new Thread(clientHandler);
				t.start();
			
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
		
	}
	
}
