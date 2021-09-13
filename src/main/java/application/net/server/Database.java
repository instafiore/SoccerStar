package application.net.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.security.crypto.bcrypt.BCrypt;

import application.SceneHandler;
import application.model.game.entity.Account;
import application.model.game.entity.DataMatch;
import application.model.game.entity.LoginClient;
import application.model.game.entity.RegistrationClient;
import application.net.common.Protocol;

public class Database {
	
	
	private static final String QUERY_INSERT_REGISTRATIONCLIENT = "insert into Account(username,password,email) values(?,?,?);";
	private static final String SEARCH_CLIENT = "select * from Account where username = ? ;";
	private static final String CHECK_LOGIN = "select * from Account where username = ? and password = ? ;";
	private static final String INSERT_MATCH = "insert into Match(date_match,result,field,home,guest,time_match) values(?,?,?,?,?,?);" ;
	private static final String CHANGEPASSWORD= "update Account set password = ? where username = ? ;";
	
	private Connection connection;
	private static Database instance = null;
	
	private PreparedStatement insertion_client_query;
	private PreparedStatement search_client_query;
	private PreparedStatement check_login_query;
	private PreparedStatement insert_match_query;
	private PreparedStatement change_password_query ;
	
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
			System.out.println("[SERVER] Database connected!");
		
		initializeQuery();
	}
	
	
	private void initializeQuery() throws SQLException{
		
		insertion_client_query = connection.prepareStatement(QUERY_INSERT_REGISTRATIONCLIENT);
		search_client_query = connection.prepareStatement(SEARCH_CLIENT);
		check_login_query = connection.prepareStatement(CHECK_LOGIN);
		insert_match_query = connection.prepareStatement(INSERT_MATCH);
		change_password_query = connection.prepareStatement(CHANGEPASSWORD);
	}
	
	public void closeConnection() throws SQLException {
		
		if(connection != null  && !connection.isClosed())
			connection.close();
		
		connection = null ;
	}
	
	public Account getAccount(String username) throws SQLException {
		Account account = new Account();
		search_client_query.setString(1,username);
		search_client_query.execute();
		
		ResultSet result = search_client_query.executeQuery();
		
		account.setUsername(username);
		account.setPassword(result.getString("password"));
		account.setCoins(result.getInt("coins"));
		account.setLineup(result.getInt("lineup"));
		account.setColor_my_balls(result.getString("color_my_balls"));
		account.setColor_ball_to_play(result.getString("color_ball_to_play"));
		account.setEmail(result.getString("email"));
		
		return account ;
	}
	
	public String insertUser(RegistrationClient user) {
		
		if(checkUser(user.getUsername()))
		{
			// Error
			System.out.println("User already exits");
			return Protocol.ALREADYEXISTS;
		}
		try {
			insertion_client_query.setString(1, user.getUsername());
			insertion_client_query.setString(2, user.getPassword());
			insertion_client_query.setString(3, user.getEmail());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Protocol.REGISTRATIONFAILED;
		}
		
		try {
			insertion_client_query.executeUpdate() ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Protocol.REGISTRATIONFAILED;
		}
		
		return Protocol.REGISTRATIONCOMPLETED;
	}
	
	public boolean insertMatch(DataMatch dataMatch) {
		
		try {
			
			
			insert_match_query.setString(1, dataMatch.getDate());
			insert_match_query.setString(2, dataMatch.getResult());
			insert_match_query.setString(3, dataMatch.getField());
			insert_match_query.setString(4, dataMatch.getHome());
			insert_match_query.setString(5, dataMatch.getGuest());
			insert_match_query.setString(6, dataMatch.getTime());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		try {
			insert_match_query.executeUpdate() ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	
	public void changePassword(String username ,String password) {
		String crypto_password = cryptoPassword(password);
		
		try {
			change_password_query.setString(1, crypto_password);
			change_password_query.setString(2, username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			change_password_query.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean checkUser(String user) {
		
		if(Server.getInstance().isOnline(user))
			return false ;
		
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
	
	public String checkLogin(LoginClient user) {
		
		
		
		try {
			search_client_query.setString(1, user.getUsername());
			ResultSet resultSet = search_client_query.executeQuery();
			
			if(!resultSet.next())
				return Protocol.LOGINFAILED;
		
			if(Server.getInstance().isOnline(user.getUsername()))
				return Protocol.ALREADYONLINE ;
			
			String password_crypted = resultSet.getString("password");
			
			if(BCrypt.checkpw(user.getPassword(), password_crypted))
				return Protocol.LOGINCOMPLETED;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		
		return Protocol.LOGINFAILED;
	}
	
	
	public String cryptoPassword(String originalPassoword) {
		
		return BCrypt.hashpw(originalPassoword, BCrypt.gensalt(12));
    
	}
	
	
}
