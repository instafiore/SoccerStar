package application.control;

import application.Utilities;
import application.model.game.handler.SoundHandler;
import application.net.client.Client;
import application.net.common.Protocol;
import application.view.Dialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

public class FriendControllerOnline {

    @FXML
    private Label friend_label;

    @FXML
    private Button friendly_battle_button;
    
    @FXML
    private Circle skin_friend;

    @FXML
    public void initialize() {
    	
    	friend_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 20));
    	friendly_battle_button.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 8));
    	
    	friendly_battle_button.setOnMouseEntered(new HoverButton());
    	friendly_battle_button.setOnMouseExited(new HoverButton());
    	
    }
    
    
    public void setSkin_friend(String skin_friend) {
		this.skin_friend.setFill(Color.web(skin_friend));
	}
    
    public void setFriend_label(String username) {
    	if(username.length() > 10 )
    		friend_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 14));
    	
		this.friend_label.setText(username);
	}
    
    @FXML
    void onClickFriendly_battle_button(ActionEvent event) {
    	SoundHandler.getInstance().startHit();
    	
    	Client.getInstance().sendMessage(Protocol.CHALLENGEHIM);
    	Client.getInstance().sendMessage(friend_label.getText());

    }
    
    @FXML
    void onMouseEnteredSkin(MouseEvent event) {
    	skin_friend.setScaleX(1.2);
    	skin_friend.setScaleY(1.2);
    }

    @FXML
    void onMouseExitedSkin(MouseEvent event) {
    	skin_friend.setScaleX(1);
    	skin_friend.setScaleY(1);
    }
}
