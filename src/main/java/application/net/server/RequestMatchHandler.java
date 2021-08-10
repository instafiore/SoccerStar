package application.net.server;

import java.net.Socket;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import application.Settings;
import application.model.game.entity.Field;


public class RequestMatchHandler {

	private ExecutorService executorService = Executors.newCachedThreadPool();
	private LinkedList<ClientHandler> players ;
	private Field field = new Field(Settings.BORDERHORIZONTAL,Settings.BORDERVERTICAL , Settings.WIDTHFRAME, Settings.HEIGHTFRAME,  15.0);
	
	
	public static RequestMatchHandler instance = null ;
	
	private RequestMatchHandler() {
		
		players = new LinkedList<ClientHandler>();
		
	}
	
	public static RequestMatchHandler getInstace() {
		if(instance == null)
				instance = new RequestMatchHandler();
		return instance ;
	}
	
	public void addPlayer(ClientHandler player) {
		players.add(player);
		flushQueue();
	}
	
	public void removePlayer(Socket player) {
		players.remove(player);
	}
	
	private void flushQueue() {
		
		ClientHandler player1 = players.peek();
		if(player1 == null)
				return;
		
		players.poll();
		
		ClientHandler player2 = players.peek();
		if(player2 == null)
		{
			players.add(player1);
			return;
		}
		
		players.poll();
		
		System.out.println("SENT GAME BY SERVER");
		MatchServer match = new MatchServer(player1, player2,field);
		executorService.submit(match);

	}

}
