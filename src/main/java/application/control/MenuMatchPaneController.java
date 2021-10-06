package application.control;

import application.SceneHandler;
import application.Settings;
import application.Utilities;
import application.model.game.handler.SoundHandler;
import application.net.client.Client;
import application.net.common.Protocol;
import application.view.Dialog;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class MenuMatchPaneController {


    @FXML
    private Button leave_button_match;

    @FXML
    private TextField username_home;

    @FXML
    private Label result_label;

    @FXML
    private Label information_label;

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
    	information_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 20));
    	username_guest.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 26));
    	name_field.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	result_label.setText("0"+Protocol.DELIMITERGOALMATCH+"0");
    	
    	name_field.setOnMouseEntered(new HoverButton());
    	name_field.setOnMouseExited(new HoverButton());
    
    	
    	
    	leave_button_match.setOnMouseEntered(new HoverButton());
    	leave_button_match.setOnMouseExited(new HoverButton());
    }
    
    public void showText(String text,int fontSize,String type,double duration) {
    	
    	String color = "" ;
		
		if(type.equals(Dialog.ERROR_WINDOW)) {
			color = "#ff1313" ;
		}else if(type.equals(Dialog.INFORMATION_WINDOW)) {
			color = "#008000" ;
		}else if(type.equals(Dialog.ATTENTION_WINDOW)) {
			color = "#e59400" ;
		}
    	
    	information_label.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), fontSize));
    	information_label.setText(text);
    	information_label.setTextFill(Color.web(color, 1));
    
    	FadeTransition trans = new FadeTransition(Duration.seconds(duration),information_label);
		
		trans.setFromValue(1.0);
        trans.setToValue(0.0);
        trans.play();
    }
        
    public void initGoals() {
    	goalHome = 0 ;
    	goalGuest = 0 ;
    	result_label.setText("0"+Protocol.DELIMITERGOALMATCH+"0");
    	
    }
    
    @FXML
    void OnClick_leave_button_match(ActionEvent event) {
    	SoundHandler.getInstance().startHit();
    	
    	if(Dialog.getInstance().showConfirmDialog(Dialog.CONFIRMLEFT) != Dialog.YES)
    		return ;
    	
    	Client.getInstance().sendMessage(Protocol.LEFTGAME);
    	Client.getInstance().setCurrentMatch(null);

    }
    
    public void setUsernameHome(String username , String color ) {
    	if(username.length() > 8)
    		username_home.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 20));
    	else
    		username_home.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 26));
    	username_home.setText(username);
    	username_home.setStyle(" -fx-background-color: "+color+"; -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);  -fx-text-fill: #000000;-fx-opacity : 0.8 ;");
    }
    
    public void setUsernameGuest(String username , String color ) {
    	if(username.length() > 8)
    		username_guest.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 20));
    	else
    		username_guest.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 26));
    	
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
    
    public void setField() {
    	
    	switch (Client.getInstance().getCurrentField()) {
		case Client.FIELD1:
			name_field.setText(Settings.FIELD1);
			name_field.setStyle("-fx-background-color: "+Settings.COLORFIELD1+";-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0)");
			break;
		case Client.FIELD2:
			name_field.setText(Settings.FIELD2);
			name_field.setStyle("-fx-background-color: "+Settings.COLORFIELD2+";-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0)");
					break;
		case Client.FIELD3:
			name_field.setText(Settings.FIELD3);
			name_field.setStyle("-fx-background-color: "+Settings.COLORFIELD3+";-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0)");
			break;
		default:
			name_field.setText(Settings.FIELD1);
			name_field.setStyle("-fx-background-color: "+Settings.COLORFIELD1+";-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0)");
			break;
		}
    }
    
    public void setFieldFriendlyBattle(int field) {
    			
    	name_field.setText(Dialog.FRIENDLY_BATTLE);
    	
    	++field ;
    	
    	switch (field) {
		case Client.FIELD1:
			name_field.setStyle("-fx-background-color: "+Settings.COLORFIELD1+";-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0)");
			break;
		case Client.FIELD2:

			name_field.setStyle("-fx-background-color: "+Settings.COLORFIELD2+";-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0)");
					break;
		case Client.FIELD3:
			name_field.setStyle("-fx-background-color: "+Settings.COLORFIELD3+";-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0)");
			break;
		default:
			name_field.setStyle("-fx-background-color: "+Settings.COLORFIELD1+";-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0)");
			break;
		}
    }
    
    public void goalGuest() {
    	
    	goalGuest = Integer.parseInt(result_label.getText().split(Protocol.DELIMITERGOALMATCH)[1]);
    	
    	++goalGuest;
    	
    	result_label.setText(goalHome+Protocol.DELIMITERGOALMATCH+goalGuest);
    }
    
}
