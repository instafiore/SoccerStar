package application.control;

import application.SceneHandler;
import application.Utilities;
import application.net.client.Client;
import application.net.common.Protocol;
import application.view.Dialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class MenuMatchPaneController {


    @FXML
    private Button leave_button_match;

    @FXML
    private TextField username_home;

    @FXML
    private Label result_label;

    @FXML
    private Label timer_label;

    @FXML
    private TextField username_guest;

    @FXML
    private TextField name_field;
    
    private int goalHome = 0 ;
    private int goalGuest = 0 ;

    @FXML
    public void initialize() {
    	
    	leave_button_match.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 18));
    	username_home.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 26));
    	result_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 30));
    	timer_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 20));
    	username_guest.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 26));
    	name_field.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	result_label.setText("0"+Protocol.DELIMITERGOALMATCH+"0");
    	
    	
    	
    	leave_button_match.setOnMouseEntered(new HoverButton());
    	leave_button_match.setOnMouseExited(new HoverButton());
    }
    
    public void initGoals() {
    	goalHome = 0 ;
    	goalGuest = 0 ;
    	result_label.setText("0"+Protocol.DELIMITERGOALMATCH+"0");
    	
    }
    
    @FXML
    void OnClick_leave_button_match(ActionEvent event) {
    	
    	if(Dialog.getInstance().showConfirmDialog(Dialog.CONFIRMLEFT) != Dialog.YES)
    		return ;
    	
    	Client.getInstance().sendMessage(Protocol.LEFTGAME);
    	Client.getInstance().getCurrentMatch().setMatch_activated(false);
    	Client.getInstance().setCurrentMatch(null);
    	
    }
    
    public void setUsernameHome(String username , String color ) {
    	username_home.setText(username);
    	username_home.setStyle(" -fx-background-color: "+color+"; -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);  -fx-text-fill: #000000;-fx-opacity : 0.8 ;");
    }
    
    public void setUsernameGuest(String username , String color ) {
    	username_guest.setText(username);
    	username_guest.setStyle(" -fx-background-color: "+color+"; -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);  -fx-text-fill: #000000;-fx-opacity : 0.8 ;");
    }

    @FXML
    void onMouseEntered(MouseEvent event) {
    	SceneHandler.getInstance().setCursor(SceneHandler.DEFAULT_CURSOR);
    }

    public void goalHome() {
    	
        goalHome = Integer.parseInt(result_label.getText().split(Protocol.DELIMITERGOALMATCH)[0]);
    	
    	++goalHome;
    	
    	result_label.setText(goalHome+Protocol.DELIMITERGOALMATCH+goalGuest);
    }
    
    public void goalGuest() {
    	
    	goalGuest = Integer.parseInt(result_label.getText().split(Protocol.DELIMITERGOALMATCH)[1]);
    	
    	++goalGuest;
    	
    	result_label.setText(goalHome+Protocol.DELIMITERGOALMATCH+goalGuest);
    }
    
}
