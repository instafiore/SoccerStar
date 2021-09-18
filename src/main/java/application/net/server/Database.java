package application.net.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCrypt;


import application.Settings;
import application.model.game.entity.Account;
import application.model.game.entity.DataMatch;
import application.model.game.entity.Lineup;
import application.model.game.entity.LoginClient;
import application.model.game.entity.RegistrationClient;
import application.model.game.entity.Skin;
import application.net.common.Protocol;

public class Database {
	
	
	private static final String DEFAULTCOLOR = "color1";
	private static final int    DEFAULTLINEUP = 1 ;
	private static final String QUERY_INSERT_REGISTRATIONCLIENT = "insert into Account(username,password,email) values(?,?,?);";
	private static final String SEARCH_CLIENT = "select * from Account where username = ? ;";
	private static final String CHECK_LOGIN = "select * from Account where username = ? and password = ? ;";
	private static final String INSERT_MATCH = "insert into Match(date_match,result,field,home,guest,time_match) values(?,?,?,?,?,?);" ;
	private static final String CHANGEPASSWORD = "update Account set password = ? where username = ? ;";
	private static final String GETMATCHESUSER = "select date_match  , time_match , result , home , guest , field , A1.current_skin as colorHome , A2.current_skin as colorGuest from Match , Account as A1 , Account as A2 where home = A1.username and guest = A2.username and (home = ? or guest = ? ) order by date_match desc , time_match desc ;" ;
	private static final String GETSKINS = "select * from Skin ;";
	private static final String GETSKIN = "select * from Skin where name = ? ;";
	private static final String GETOWNEDSKINS = "select skin from Inventary_Skin where account = ? ;";
	private static final String GETLINEUPS = "select * from Lineup ;";
	private static final String GETLINEUP = "select * from Lineup where id = ? ;" ;
	private static final String GETOWNEDLINEUP = "select lineup from Inventary_Lineup where account = ? ;";
	private static final String INSERT_SKIN = "insert into Inventary_Skin(account,skin) values(?,?);";
	private static final String INSERT_LINEUP = "insert into Inventary_Lineup(account,lineup) values(?,?);";
	private static final String UPDATE_COINS = "update Account set coins = ? where username = ? ;" ;
	private static final String UPDATE_CURRENT_SKIN = "update Account set current_skin = ? where username = ? ;" ;
	private static final String UPDATE_CURRENT_LINEUP = "update Account set current_lineup = ? where username = ? ;" ;
	private static final String SEARCH_FRIENDS_USER = "select friend from Friends where account  = ? ;";
	
	private Connection connection;
	private static Database instance = null;
	private PreparedStatement insertion_client_query;
	private PreparedStatement search_client_query;
	private PreparedStatement check_login_query;
	private PreparedStatement insert_match_query;
	private PreparedStatement change_password_query ;
	private PreparedStatement get_matches_user_query ;
	private PreparedStatement get_skins ;
	private PreparedStatement get_skin ;
	private PreparedStatement get_owned_skins_query ;
	private PreparedStatement get_lineups_query ;
	private PreparedStatement get_lineup_query ;
	private PreparedStatement get_owned_lineup_query ;
	private PreparedStatement insert_skin_query ;
	private PreparedStatement insert_lineup_query ;
	private PreparedStatement update_coins_query ;
	private PreparedStatement update_current_skin_query ;
	private PreparedStatement update_current_lineup_query ;
	private PreparedStatement search_friend_user_query  ;
	
	
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
		get_matches_user_query = connection.prepareStatement(GETMATCHESUSER);
		get_skins = connection.prepareStatement(GETSKINS);
		get_skin = connection.prepareStatement(GETSKIN);
		get_owned_skins_query = connection.prepareStatement(GETOWNEDSKINS);
		get_lineups_query = connection.prepareStatement(GETLINEUPS);
		get_lineup_query = connection.prepareStatement(GETLINEUP);
		get_owned_lineup_query = connection.prepareStatement(GETOWNEDLINEUP);
		insert_skin_query = connection.prepareStatement(INSERT_SKIN);
		insert_lineup_query = connection.prepareStatement(INSERT_LINEUP);
		update_coins_query = connection.prepareStatement(UPDATE_COINS);
		update_current_skin_query = connection.prepareStatement(UPDATE_CURRENT_SKIN);
		update_current_lineup_query = connection.prepareStatement(UPDATE_CURRENT_LINEUP);
		search_friend_user_query = connection.prepareStatement(SEARCH_FRIENDS_USER);

	}
	
	
	public synchronized List<DataMatch> getDataMatches(String username){
		
		ArrayList<DataMatch> dataMatches = new ArrayList<DataMatch>();
		
		try {
			get_matches_user_query.setString(1, username);
			get_matches_user_query.setString(2, username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			ResultSet result = get_matches_user_query.executeQuery();
			while(result.next()) {
				DataMatch dataMatch = new DataMatch();
				String field = "" ;
				dataMatch.setDate(result.getString("date_match"));
				dataMatch.setHome(result.getString("home"));
				dataMatch.setGuest(result.getString("guest"));
				field = result.getString("field") ;
				dataMatch.setField(field);
				dataMatch.setTime(result.getString("time_match"));
				dataMatch.setResultMatch(result.getString("result"));
				dataMatch.setColorHome(result.getString("colorHome"));
				dataMatch.setColorGuest(result.getString("colorGuest"));
				
				String colorField = "" ;
				if(field.equals(Settings.FIELD1))
					colorField = Settings.COLORFIELD1 ;
				else if(field.equals(Settings.FIELD2))
					colorField = Settings.COLORFIELD2 ;
				else 
					colorField = Settings.COLORFIELD3 ;
				dataMatch.setColorField(colorField);
				dataMatches.add(dataMatch);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dataMatches ;
	}
	
	public synchronized void closeConnection() throws SQLException {
		
		if(connection != null  && !connection.isClosed())
			connection.close();
		
		connection = null ;
	}
	
	public synchronized  Account getAccount(String username) throws SQLException {
		Account account = new Account();
		search_client_query.setString(1,username);
		search_client_query.execute();
		
		ResultSet result = search_client_query.executeQuery();
		
		account.setUsername(username);
		account.setPassword(result.getString("password"));
		account.setCoins(result.getInt("coins"));
		account.setLineup(result.getInt("current_lineup"));
		account.setCurrentSkin(result.getString("current_skin"));
		account.setEmail(result.getString("email"));
		
		return account ;
	}
	
	
	public synchronized ArrayList<String> getFriends(String username){
		
		ArrayList<String> friends = new ArrayList<String>();
		
		try {
			search_friend_user_query.setString(1, username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ResultSet resultSet = null ;
		
		try {
			resultSet = search_friend_user_query.executeQuery() ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
		try {
			
			while(resultSet.next())
				friends.add(resultSet.getString("friend"));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return friends ;
	}
	
	
	public synchronized Skin getSkin(String name) throws SQLException {
		
		Skin skin = new Skin() ;
		
		get_skin.setString(1, name);
		
		ResultSet result = get_skin.executeQuery();
		
		if(!result.next())
			return null ;
		
		skin.setName(result.getString("name"));
		skin.setPrice(result.getString("price"));
		skin.setColor(result.getString("color"));
		
		return skin ;
	}
	
	public synchronized Lineup getLineup(int id) throws SQLException {
		
		Lineup lineup = new Lineup() ;
		
		get_lineup_query.setInt(1, id);
		
		ResultSet result = get_lineup_query.executeQuery();
		
		if(!result.next())
			return null ;
		
		lineup.setId(result.getInt("id"));
		lineup.setName(result.getString("name"));
		lineup.setPrice(result.getString("price"));
		lineup.setImage(result.getString("image"));
		// TODO IMAGE
		
		return lineup ;
	}
	
	private synchronized void insertSkinToUsername(String username,String skin) {
		
		try {
			insert_skin_query.setString(1, username);
			insert_skin_query.setString(2, skin);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			insert_skin_query.executeUpdate() ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private synchronized void insertLineupToUsername(String username,int lineup) {
		
		try {
			insert_lineup_query.setString(1, username);
			insert_lineup_query.setInt(2, lineup);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			insert_lineup_query.executeUpdate() ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public synchronized boolean buySkin(String username ,Skin skin) {
			
		Skin skinDatabase = null ;
		try {
			skinDatabase = getSkin(skin.getName());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int price = Integer.parseInt(skin.getPrice());
		
		Account account = null ;
		try {
			account = getAccount(username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int coins = account.getCoins() ;
		
		int newAmoutOfCoins = coins - price ;
		if( newAmoutOfCoins < 0)
			return false ;
		
		insertSkinToUsername(username,skinDatabase.getName());
		updateCoins(username, newAmoutOfCoins);
		
		return true ;
	}
	
	public synchronized boolean buyLineup(String username ,Lineup lineup) {
		
		Lineup lineupDatabase = null ;
		try {
			lineupDatabase = getLineup(lineup.getId());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int price = Integer.parseInt(lineup.getPrice());
		
		Account account = null ;
		try {
			account = getAccount(username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int coins = account.getCoins() ;
		
		int newAmoutOfCoins = coins - price ;
		if( newAmoutOfCoins < 0)
			return false ;
		
		insertLineupToUsername(username,lineupDatabase.getId());
		updateCoins(username, newAmoutOfCoins);
		
		return true ;
	}
	
	private synchronized void updateCoins(String username , int coins) {
		
		try {
			update_coins_query.setInt(1, coins);
			update_coins_query.setString(2, username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			update_coins_query.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void insertCoins(String username,int coins) {
		
		int currentCoins = 0;
		try {
			currentCoins = getAccount(username).getCoins();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int newCoins = currentCoins + coins ;
		
		try {
			update_coins_query.setInt(1, newCoins);
			update_coins_query.setString(2, username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			update_coins_query.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public synchronized void removeCoins(String username,int coins) {
		
		int currentCoins = 0;
		try {
			currentCoins = getAccount(username).getCoins();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int newCoins = currentCoins - coins ;
		
		try {
			update_coins_query.setInt(1, newCoins);
			update_coins_query.setString(2, username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			update_coins_query.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	public synchronized void updateCurrentSkin(String username , String color) {
		
		try {
			update_current_skin_query.setString(1, color);
			update_current_skin_query.setString(2, username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			update_current_skin_query.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void updateCurrentLineup(String username , int id) {
		
		try {
			update_current_lineup_query.setInt(1, id);
			update_current_lineup_query.setString(2, username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			update_current_lineup_query.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized ArrayList<Skin> getSkins() throws SQLException {
		
		ArrayList<Skin> skins = new ArrayList<Skin>();
		
		ResultSet result = get_skins.executeQuery();
	
		while(result.next()) {
			Skin skin = new Skin(); 
			skin.setName(result.getString("name"));
			skin.setPrice(result.getString("price"));
			skin.setColor(result.getString("color"));
			skins.add(skin);
		}
	
		return skins ;
	}
	
	public synchronized String getOwnedSkins(String username) throws SQLException {
		
		get_owned_skins_query.setString(1,username);

		ResultSet result = get_owned_skins_query.executeQuery();
		
		String text = "" ;
		
		while(result.next()) {
			text+=result.getString("skin");
			text+=Protocol.DELIMITERELEMENTSHOP;
		}
		
 		return text ;
	}
	
	public synchronized ArrayList<Lineup> getLineups() throws SQLException {
	
		ArrayList<Lineup> lineups = new ArrayList<Lineup>();
		
		ResultSet result = get_lineups_query.executeQuery();
	
		while(result.next()) {
			Lineup lineup = new Lineup();
			lineup.setId(result.getInt("id"));
			lineup.setName(result.getString("name"));
			lineup.setPrice(result.getString("price"));
			lineup.setImage(result.getString("image"));
			lineups.add(lineup);
		}
	
		return lineups ;
	}
	
	public synchronized String getOwnedLineup(String username) throws SQLException {
		
		get_owned_lineup_query.setString(1,username);

		ResultSet result = get_owned_lineup_query.executeQuery();
		
		String text = "" ;
		
		while(result.next()) {
			text+=result.getString("lineup");
			text+=Protocol.DELIMITERELEMENTSHOP;
		}
 		return text ;
	}
	
	
	
	public synchronized String insertUser(RegistrationClient user) {
		
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
		
		insertDefaultSkin(user.getUsername());
		insertDefaultLineup(user.getUsername());
		
		return Protocol.REGISTRATIONCOMPLETED;
	}
	
	private synchronized void insertDefaultSkin(String username) {
		
		try {
			insert_skin_query.setString(1, username);
			insert_skin_query.setString(2, DEFAULTCOLOR);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			insert_skin_query.executeUpdate() ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private synchronized void insertDefaultLineup(String username) {
		
		try {
			insert_lineup_query.setString(1, username);
			insert_lineup_query.setInt(2, DEFAULTLINEUP);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			insert_lineup_query.executeUpdate() ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public synchronized boolean insertMatch(DataMatch dataMatch) {
		
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
	
	
	public synchronized void changePassword(String username ,String password) {
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
	
	public synchronized boolean checkUser(String user) {
		
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
	
	public synchronized String checkLogin(LoginClient user) {
	
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
	
	public synchronized boolean checkPassword(String username ,String password) {
		
		try {
			search_client_query.setString(1,username);
			ResultSet resultSet = search_client_query.executeQuery();
			
			if(!resultSet.next())
				return false;
			
			String password_crypted = resultSet.getString("password");
			
			return BCrypt.checkpw(password, password_crypted) ;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return false;
	}
	
	
	public synchronized String cryptoPassword(String originalPassoword) {
		
		return BCrypt.hashpw(originalPassoword, BCrypt.gensalt(12));
    
	}
	
	
}
