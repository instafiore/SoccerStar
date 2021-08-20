package application.net.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.SceneHandler;
import application.model.game.entity.LoginClient;
import application.model.game.entity.RegistrationClient;

public class Database {
	
	
	private static final String QUERY_INSERT_REGISTRATIONCLIENT = "insert into Account(username,password,email) values(?,?,?);";
	private static final String SEARCH_CLIENT = "select * from Account where username = ? ;";
	
	private Connection connection;
	private static Database instance = null;
	
	private PreparedStatement insertion_client_query;
	private PreparedStatement search_client_query;
	
	private Database() {
		
	}
	
	public static Database getInstance() {
		if(instance == null)
			instance = new Database();
		return instance;
	}
	
	public void connectToDatabase() throws SQLException {
		
		String url = "jdbc:sqlite:DB_SERVER_SOCCER_STAR.db";
		connection = DriverManager.getConnection(url);
		if(connection != null  && !connection.isClosed())
			System.out.println("Database connected!");
		
		insertion_client_query = connection.prepareStatement(QUERY_INSERT_REGISTRATIONCLIENT);
		search_client_query = connection.prepareStatement(SEARCH_CLIENT);
		
	}
	
	public void closeConnection() throws SQLException {
		
		if(connection != null  && !connection.isClosed())
			connection.close();
		
		connection = null ;
	}
	
	public boolean insertUser(RegistrationClient user) {
		
		try {
			search_client_query.setString(1, user.getUsername());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			ResultSet result = search_client_query.executeQuery();
			if(result.next())
			{
				//ERROR
				return false;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			insertion_client_query.setString(1, user.getUsername());
			insertion_client_query.setString(2, user.getPassword());
			insertion_client_query.setString(3, user.getEmail());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			insertion_client_query.executeUpdate() ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	
	public boolean checkUser(LoginClient user) {
		//TODO
		return true;
	}
}
