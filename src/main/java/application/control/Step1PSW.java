package application.control;

import application.SceneHandler;
import application.Utilities;
import application.model.game.handler.SoundHandler;
import application.net.client.Client;
import application.net.common.Protocol;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class Step1PSW {

    @FXML
    private Button send_username_button_step1psw;

    @FXML
    private TextField username_field_step1psw;

    @FXML
    private TextArea instruction_area_step1psw;
    
    @FXML
    private Label information_label;
    
    @FXML
    private Button back_button_step1PSW;
    
    
    private boolean ready = false ;
    
    public void setReady(boolean ready) {
		this.ready = ready;
	}
    
    public boolean isReady() {
		return ready;
    }
    

    @FXML
    void onClickBack_button_step1PSW(ActionEvent event) {
    	SoundHandler.getInstance().startHit();
    	
    	Client.getInstance().setCurrentState(Client.STEP_LOGIN);
    	SceneHandler.getInstance().loadScene("LoginPage", false, true);
    }

    private boolean pressed = false ;
    
    public void showError(String text,int fontSize) {
    	
    	information_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), fontSize));
    	information_label.setText(text);
    	information_label.setTextFill(Color.web("#ff1313", 1));
    	FadeTransition trans = new FadeTransition(Duration.seconds(4),information_label);
		
		trans.setFromValue(1.0);
        trans.setToValue(0.0);
        trans.play();
    }
    @FXML
    public void initialize() {
    	
    	Client.getInstance().setCurrentState(Client.STEP1PSW);
    	
    	send_username_button_step1psw.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 18));
    	username_field_step1psw.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	instruction_area_step1psw.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 16));
    	back_button_step1PSW.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 13));
    	

    	
    	username_field_step1psw.setPromptText("Insert username");
    	
    	send_username_button_step1psw.setOnMouseEntered(new HoverButton());
    	send_username_button_step1psw.setOnMouseExited(new HoverButton());
    	
    	back_button_step1PSW.setOnMouseEntered(new HoverButton());
    	back_button_step1PSW.setOnMouseExited(new HoverButton());
    	
    	username_field_step1psw.setOnKeyReleased(new InputFieldController());
    
    }
    
    public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}
    
    @FXML
    void onClickSend_username_button_step1psw(ActionEvent event) {
    	SoundHandler.getInstance().startHit();
    	
    	if(pressed)
    		return;
    	
    	pressed = true ;
    	
    	if(username_field_step1psw.getText().equals("") )
    	{
    		showError(Protocol.FIELDEMPTY, 16);
    		return ;
    	}
    	
//    	if(!Utilities.ruleUsernameRespected(username_field_step1psw.getText()))
//    	{
//    		showError(Utilities.RULEUSERNAMNOTRESPECTED, 15);
//    		return ;
//    	}
    	Client.getInstance().sendMessage(Protocol.PASSOWRDFORGOT);
    	Client.getInstance().sendMessage(username_field_step1psw.getText());
    }
    

}
