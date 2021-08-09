package application.net.client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import application.Settings;
import application.net.common.Protocol;

public class Client implements Runnable{

	private BufferedReader in = null;
	private PrintWriter out = null;
	private Socket socket = null;
	
	private static Client instance = null;
	private String username ;
	
	private Client() {

		try {
			socket = new Socket(Settings.ADDRESS_SERVER,Settings.PORT);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()),true);
		} catch (UnknownHostException e) {

			e.printStackTrace();
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
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public static Client getInstance() {
		if(instance == null)
			instance = new Client();
		return instance ;
	}
	
	public void read() throws IOException {
		
		if(in == null)
			return;
		
		String message = null;
		
		if(in.ready())
		{
			message = in.readLine();
			
			
//			if(message.equals(Protocol))
			
		}
		
		
	}
		
	public void startMatch() {
		
		sendMessage(Protocol.NEWGAMEREQUEST);
		MatchClient match = new MatchClient(this);
		
	}

	public void sendMessage(String message) {
		
		if(out == null || message == null)
			return ;
		
		out.println(message);
	}
	
	public void run() {
		
		// Just for now
		startMatch();
		
		
		while(!Thread.interrupted()) {
			
			try {
				read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
