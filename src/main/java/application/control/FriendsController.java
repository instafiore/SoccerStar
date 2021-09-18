package application.control;

import application.SceneHandler;
import application.Utilities;
import application.model.game.handler.FriendsHandler;
import application.net.client.Client;
import application.net.common.Protocol;
import application.view.Dialog;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.util.Pair;

public class FriendsController {

	@FXML
    private Label information_label;

    @FXML
    private VBox vbox;

    @FXML
    private BorderPane PaneOnlineFriend;

    @FXML
    private Label label_friends_online;

    @FXML
    private VBox box_friends_online;

    @FXML
    private Label label_all_friends;

    @FXML
    private VBox box_all_friends;

    @FXML
    private Button back_button_friends_page;

    @FXML
    private TextField username_field;

    @FXML
    private Button add_friend;

  
    
    private boolean ready = false ;
    
    private static final String FriendsTitle = "Friends";
    
    
    @FXML
    public void initialize() {
    	
    	Client.getInstance().setCurrentState(Client.FRIENDS);
    	
    	add_friend.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 13));
    	back_button_friends_page.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 20));
    	username_field.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 16));
    	label_friends_online.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 23));
    	label_all_friends.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 23));
    	
    	back_button_friends_page.setOnMouseEntered(new HoverButton());
    	back_button_friends_page.setOnMouseExited(new HoverButton());
    	
    	showText(FriendsTitle, 40, Dialog.INFORMATION_WINDOW, 20);
    	
    	Client.getInstance().sendMessage(Protocol.INFORMATIONFRIENDS) ;

    }
    
    
    public void init() {
    	
    	box_all_friends.getChildren().clear();
    	box_friends_online.getChildren().clear();
    	
    	if(FriendsHandler.getInstance().getFriendsOnline().isEmpty() && FriendsHandler.getInstance().getFriendsOffline().isEmpty())
    	{
    		vbox.getChildren().remove(PaneOnlineFriend);    		
    		box_all_friends.getChildren().add(SceneHandler.getInstance().loadPane("NoFriendYet").getKey());
    		
    	}else if(FriendsHandler.getInstance().getFriendsOnline().isEmpty()){
    		
    		vbox.getChildren().remove(PaneOnlineFriend);
    		
    		for(Pair<String, String> pair : FriendsHandler.getInstance().getFriendsOffline())
    		{
    			String username_friend_offline = pair.getKey() ;
    			String skin_friend_offline = pair.getValue() ;
    			
    			Pane pane = (Pane) SceneHandler.getInstance().loadPane("OfflineFriendPane").getKey() ;
    			
    			FriendControllerOffline friendControllerOffline = (FriendControllerOffline) SceneHandler.getInstance().getLoader("OfflineFriendPane").getController() ;
    			
    			friendControllerOffline.setFriend_label(username_friend_offline);
    			friendControllerOffline.setSkin_friend(skin_friend_offline);
    			
    			box_all_friends.getChildren().add(pane);
    		}
    		
    	}else {
    		
    		for(Pair<String, String> pair : FriendsHandler.getInstance().getFriendsOnline())
    		{
    			String username_friend_online = pair.getKey() ;
    			String skin_friend_online = pair.getValue() ;
    			
    			Pane pane = (Pane) SceneHandler.getInstance().loadPane("OnlineFriendPane").getKey() ;
    			
    			FriendControllerOnline friendControllerOnline = (FriendControllerOnline) SceneHandler.getInstance().getLoader("OnlineFriendPane").getController() ;
    			
    			friendControllerOnline.setFriend_label(username_friend_online);
    			friendControllerOnline.setSkin_friend(skin_friend_online);
    			
    			box_friends_online.getChildren().add(pane);
    		}
    		
    		for(Pair<String, String> pair : FriendsHandler.getInstance().getFriendsOffline())
    		{
    			String username_friend_offline = pair.getKey() ;
    			String skin_friend_offline = pair.getValue() ;
    			
    			Pane pane = (Pane) SceneHandler.getInstance().loadPane("OfflineFriendPane").getKey() ;
    			
    			FriendControllerOffline friendControllerOffline = (FriendControllerOffline) SceneHandler.getInstance().getLoader("OfflineFriendPane").getController() ;
    			
    			friendControllerOffline.setFriend_label(username_friend_offline);
    			friendControllerOffline.setSkin_friend(skin_friend_offline);
    			
    			box_all_friends.getChildren().add(pane);
    		}
    	}
    	
    }
    
    
    public void showText(String text,int fontSize,String type,double duration) {
    	if(!ready)
			return ;
    	String color = "" ;
		
		if(type.equals(Dialog.ERROR_WINDOW)) {
			color = "#ff1313" ;
		}else if(type.equals(Dialog.INFORMATION_WINDOW)) {
			color = "#ffffff" ;
		}else if(type.equals(Dialog.ATTENTION_WINDOW)) {
			color = "#ff5e28" ;
		}
    	
    	information_label.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), fontSize));
    	information_label.setText(text);
    	information_label.setTextFill(Color.web(color, 1));
    
    	FadeTransition trans = new FadeTransition(Duration.seconds(duration),information_label);
		
		trans.setFromValue(1.0);
        trans.setToValue(0.0);
        trans.play();
    }

    
    public void setReady(boolean ready) {
		this.ready = ready;
	}
    
    public boolean isReady() {
		return ready;
    }

    @FXML
    void onClickBack_button_friends_page(ActionEvent event) {
    	if(!ready)
    		return ;
    	SceneHandler.getInstance().loadScene("MainPage", true, true);
    }
    
    @FXML
    void onCLickAdd_friend(ActionEvent event) {
    	if(!ready)
    		return ;
    }


}
