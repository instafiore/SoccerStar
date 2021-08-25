package application.net.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.security.crypto.bcrypt.BCrypt;

import application.SceneHandler;
import application.model.game.entity.LoginClient;
import application.model.game.entity.RegistrationClient;
import application.net.common.Protocol;

public class Database {
	
	
	private static final String QUERY_INSERT_REGISTRATIONCLIENT = "insert into Account(username,password,email) values(?,?,?);";
	private static final String SEARCH_CLIENT = "select * from Account where username = ? ;";
	private static final String CHECK_LOGIN = "select * from Account where username = ? and password = ? ";
	
	private Connection connection;
	private static Database instance = null;
	
	private PreparedStatement insertion_client_query;
	private PreparedStatement search_client_query;
	private PreparedStatement check_login_query;
	
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
		
		initializeQuery();
	}
	
	
	private void initializeQuery() throws SQLException{
		
		insertion_client_query = connection.prepareStatement(QUERY_INSERT_REGISTRATIONCLIENT);
		search_client_query = connection.prepareStatement(SEARCH_CLIENT);
		check_login_query = connection.prepareStatement(CHECK_LOGIN);
		
	}
	
	public void closeConnection() throws SQLException {
		
		if(connection != null  && !connection.isClosed())
			connection.close();
		
		connection = null ;
	}
	
	public boolean insertUser(RegistrationClient user) {
		
		if(checkUser(user.getUsername()))
		{
			// Error
			System.out.println("User already exits");
			return false;
		}
		try {
			insertion_client_query.setString(1, user.getUsername());
			insertion_client_query.setString(2, user.getPassword());
			insertion_client_query.setString(3, user.getEmail());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		try {
			insertion_client_query.executeUpdate() ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	
	public boolean checkUser(String user) {
		
		try {
			search_client_query.setString(1, user);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			ResultSet result = search_client_query.executeQuery();
			if(result.next())
				return true;
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return false;
	}
	
	public boolean checkLogin(LoginClient user) {
		
		try {
			search_client_query.setString(1, user.getUsername());
			ResultSet resultSet = search_client_query.executeQuery();
			
			if(!resultSet.next())
				return false;
			
			String password_crypted = resultSet.getString("password");
			
			return BCrypt.checkpw(user.getPassword(), password_crypted) ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
	}
	
	
	public String cryptoPassword(String originalPassoword) {
		
		return BCrypt.hashpw(originalPassoword, BCrypt.gensalt(12));
    
	}
}
