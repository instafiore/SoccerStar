package application.net.server;

import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import application.Settings;
import application.model.game.entity.Field;


public class RequestMatchHandler {

	private ExecutorService executorService = Executors.newCachedThreadPool();
	
	private LinkedList<ClientHandler> field1Queue ;
	private LinkedList<ClientHandler> field2Queue ;
	private LinkedList<ClientHandler> field3Queue ;
	
	private Field field1 = new Field(Settings.FIELD1 , Settings.BORDERHORIZONTAL,Settings.BORDERVERTICAL , Settings.FIELDWIDTHFRAME, Settings.FIELDHEIGHTFRAME,  Settings.FRICTIONFIELD1);
	private Field field2 = new Field(Settings.FIELD2 , Settings.BORDERHORIZONTAL,Settings.BORDERVERTICAL , Settings.FIELDWIDTHFRAME, Settings.FIELDHEIGHTFRAME,  Settings.FRICTIONFIELD2);
	private Field field3 = new Field(Settings.FIELD3 , Settings.BORDERHORIZONTAL,Settings.BORDERVERTICAL , Settings.FIELDWIDTHFRAME, Settings.FIELDHEIGHTFRAME,  Settings.FRICTIONFIELD3);
	
	
	public static RequestMatchHandler instance = null ;
	
	private RequestMatchHandler() {
		
		field1Queue = new LinkedList<ClientHandler>();
		field2Queue = new LinkedList<ClientHandler>();
		field3Queue = new LinkedList<ClientHandler>();
	}
	
	public static RequestMatchHandler getInstace() {
		if(instance == null)
				instance = new RequestMatchHandler();
		return instance ;
	}
	
	public void addPlayerField1(ClientHandler player) {
		field1Queue.add(player);
		flushQueueField1();
	}
	
	public void removePlayerField1(Socket player) {
		field1Queue.remove(player);
	}
	
	private void flushQueueField1() {
		
		ClientHandler player1 = field1Queue.peek();
		if(player1 == null)
				return;
		
		field1Queue.poll();
		
		ClientHandler player2 = field1Queue.peek();
		if(player2 == null)
		{
			field1Queue.add(player1);
			return;
		}
		
		field1Queue.poll();
		
		System.out.println("[SERVER] SENT GAME BY SERVER");
		MatchServer match = new MatchServer(player1, player2,field1);
		executorService.submit(match);

	}
	
	
	public void addPlayerField2(ClientHandler player) {
		field2Queue.add(player);
		flushQueueField2();
	}
	
	public void removePlayerField2(Socket player) {
		field2Queue.remove(player);
	}
	
	private void flushQueueField2() {
		
		ClientHandler player1 = field2Queue.peek();
		if(player1 == null)
				return;
		
		field2Queue.poll();
		
		ClientHandler player2 = field2Queue.peek();
		if(player2 == null)
		{
			field2Queue.add(player1);
			return;
		}
		
		field2Queue.poll();
		
		System.out.println("[SERVER] SENT GAME BY SERVER");
		MatchServer match = new MatchServer(player1, player2,field2);
		executorService.submit(match);

	}
	
	
	public void addPlayerField3(ClientHandler player) {
		field3Queue.add(player);
		flushQueueField3();
	}
	
	public void removePlayerField3(Socket player) {
		field3Queue.remove(player);
	}
	
	private void flushQueueField3() {
		
		ClientHandler player1 = field3Queue.peek();
		if(player1 == null)
				return;
		
		field3Queue.poll();
		
		ClientHandler player2 = field3Queue.peek();
		if(player2 == null)
		{
			field3Queue.add(player1);
			return;
		}
		
		field3Queue.poll();
		
		System.out.println("[SERVER] SENT GAME BY SERVER");
		MatchServer match = new MatchServer(player1, player2,field3);
		executorService.submit(match);

	}

}
