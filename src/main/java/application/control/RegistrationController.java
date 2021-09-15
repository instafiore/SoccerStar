package application.control;

import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCrypt;

import application.SceneHandler;
import application.Utilities;
import application.net.client.Client;
import application.net.common.Protocol;
import application.net.server.Database;
import application.view.Dialog;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class RegistrationController {


	private static final String INFORMATIONRULES = "" ;
	
    @FXML
    private Label soccerstart_label;

    @FXML
    private Label username_label;

    @FXML
    private TextField username_field_registration;

    @FXML
    private Label password_label;

    @FXML
    private PasswordField password_field_registration;

    @FXML
    private Label repeatpassword_label;

    @FXML
    private PasswordField repeat_password_field_registration;

    @FXML
    private Label email_label;

    @FXML
    private TextField email_field_registration;

    @FXML
    private Button sign_up_button_registration;

    @FXML
    private Button back_button_registration;
    
    @FXML
    private Label information_label;

    @FXML
    private Button information_password_rule;


    
    @FXML
    void onClickInformation_password_rule(ActionEvent event) {
    	Dialog.getInstance().showInformationDialog(INFORMATIONRULES, Protocol.RULESREGISTRATION);
    }

    @FXML
    public void initialize() {
    	
    	Client.getInstance().setCurrentState(Client.STEP_REGISTRATION);
    	
    	soccerstart_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 43));
    	username_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	username_field_registration.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	password_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	password_field_registration.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	repeat_password_field_registration.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	email_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	email_field_registration.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	username_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	sign_up_button_registration.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	back_button_registration.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	repeatpassword_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
   
    	information_password_rule.setOnMouseEntered(new HoverButton());
    	information_password_rule.setOnMouseExited(new HoverButton());
    	
    	sign_up_button_registration.setOnMouseEntered(new HoverButton());
    	sign_up_button_registration.setOnMouseExited(new HoverButton());
    	
    	back_button_registration.setOnMouseEntered(new HoverButton());
    	back_button_registration.setOnMouseExited(new HoverButton());
    	
    	settingOnKeyReleased();
    }

    public void showError(String text,int fontSize) {
    	
    	information_label.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), fontSize));
    	information_label.setText(text);
    	information_label.setTextFill(Color.web("#ff1313", 1));
    
    	FadeTransition trans = new FadeTransition(Duration.seconds(4),information_label);
		
		trans.setFromValue(1.0);
        trans.setToValue(0.0);
        trans.play();
    }
    @FXML
    void onClick_back_button_registration(ActionEvent event) {
    	SceneHandler.getInstance().loadScene("LoginPage", false , true);
    	Client.getInstance().setCurrentState(Client.STEP_LOGIN);
    }

    @FXML
    void onClick_sign_up_button_registration(ActionEvent event) {
    	
    	if(username_field_registration.getText().equals("") || password_field_registration.equals("") || repeat_password_field_registration.getText().equals("") || email_field_registration.equals(""))
    	{
    		showError(Protocol.FIELDEMPTY,20);
    		return ;
    	}
    	
    	if(!password_field_registration.getText().equals(repeat_password_field_registration.getText()))
    	{
    		showError(Protocol.ERRORNOTSAMEPASSWORD, 15);
    		return ;
    	}
    	
    	if(!Utilities.rulesRespected(username_field_registration.getText(),password_field_registration.getText(),email_field_registration.getText())) {
    		showError(Utilities.RULESDIDTRESPECTED, 15);
    		return ;
    	}
    	
    	String passwordCrypto = Database.getInstance().cryptoPassword(password_field_registration.getText());
    	
    	String stringa = username_field_registration.getText() + Protocol.DELIMITERREGISTRATION + passwordCrypto + Protocol.DELIMITERREGISTRATION + email_field_registration.getText() ;
    	
    	Client.getInstance().sendMessage(Protocol.REGISTRATIONREQUEST);
    	Client.getInstance().sendMessage(stringa);
    }


    
    private void settingOnKeyReleased() {
    	
    	username_field_registration.setOnKeyReleased(new InputFieldController(InputFieldController.USERNAME));
    	password_field_registration.setOnKeyReleased(new InputFieldController(InputFieldController.PASSWORD));
    	repeat_password_field_registration.setOnKeyReleased(new InputFieldController(InputFieldController.NOCONTROL));	
    	email_field_registration.setOnKeyReleased(new InputFieldController(InputFieldController.EMAIL));
    }
    
    @FXML
    void onKeyPressed(KeyEvent event) {

    }

}
