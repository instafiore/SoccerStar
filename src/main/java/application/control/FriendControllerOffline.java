package application.control;

import application.Utilities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

public class FriendControllerOffline {

	@FXML
    private Label friend_label;


    @FXML
    private Circle skin_friend;

    @FXML
    public void initialize() {
    	
    	friend_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 20));
    	
    }
    
    
    public void setSkin_friend(String skin_friend) {
		this.skin_friend.setFill(Color.web(skin_friend));
	}
    
    public void setFriend_label(String username) {
		this.friend_label.setText(username);
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
