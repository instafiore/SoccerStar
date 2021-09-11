package application.control;

import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCrypt;

import application.SceneHandler;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class RegistrationController {


	private static final String INFORMATIONPASSWORDRULES = "" ;
	
	private final String REGEXUSERNAME = "([a-zA-Z][^&%$£!]){6,}" ; 
	private final String REGEXPASSWORD = "[^&%$£!]{6,16}" ; 
	private final String REGEXEMAIL = ".+@gmail\\.com" ; 
	
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

    private Pattern pattern1 = Pattern.compile(REGEXUSERNAME);
    private Pattern pattern2 = Pattern.compile(REGEXPASSWORD);
    private Pattern pattern3 = Pattern.compile(REGEXEMAIL);
    
    @FXML
    void onClickInformation_password_rule(ActionEvent event) {
    	Dialog.getInstance().showInformationDialog(INFORMATIONPASSWORDRULES, Protocol.RULESREGISTRATION);
    }

    @FXML
    public void initialize() {
    	soccerstart_label.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 43));
    	username_label.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	username_field_registration.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	password_label.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	password_field_registration.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	repeat_password_field_registration.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	email_label.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	email_field_registration.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	username_label.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	sign_up_button_registration.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	back_button_registration.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	repeatpassword_label.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
   
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
    	
    	if(!rulesRespected()) {
    		showError(Protocol.RULESDIDTRESPECTED, 15);
    		return ;
    	}
    	
    	String passwordCrypto = Database.getInstance().cryptoPassword(password_field_registration.getText());
    	
    	String stringa = username_field_registration.getText() + Protocol.DELIMITERREGISTRATION + passwordCrypto + Protocol.DELIMITERREGISTRATION + email_field_registration.getText() ;
    	
    	Client.getInstance().sendMessage(Protocol.REGISTRATIONREQUEST);
    	Client.getInstance().sendMessage(stringa);
    }
    
    public boolean rulesRespected() {
    	return pattern1.matcher(username_field_registration.getText()).matches() && pattern2.matcher(password_field_registration.getText()).matches() && pattern3.matcher(email_field_registration.getText()).matches() ;
    }

    
    private void settingOnKeyReleased() {
    	username_field_registration.setOnKeyReleased(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				
				
				
				if(username_field_registration.getText().equals("")) {
					username_field_registration.getStyleClass().remove("input_field_red");
					username_field_registration.getStyleClass().remove("input_field_green");
					
				}
				else if(!pattern1.matcher(username_field_registration.getText()).matches())
				{
					username_field_registration.getStyleClass().remove("input_field_red");
					username_field_registration.getStyleClass().remove("input_field_green");
					username_field_registration.getStyleClass().add("input_field_red");
				}
				else
				{
					username_field_registration.getStyleClass().remove("input_field_red");
					username_field_registration.getStyleClass().remove("input_field_green");
					username_field_registration.getStyleClass().add("input_field_green");
				}
			}
		});
    	
    	password_field_registration.setOnKeyReleased(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				
				if(password_field_registration.getText().equals("")) {
					password_field_registration.getStyleClass().remove("input_field_red");
					password_field_registration.getStyleClass().remove("input_field_green");
				}
				else if(!pattern2.matcher(password_field_registration.getText()).matches())
				{
					password_field_registration.getStyleClass().remove("input_field_red");
					password_field_registration.getStyleClass().remove("input_field_green");
					password_field_registration.getStyleClass().add("input_field_red");

				}
				else
				{
					password_field_registration.getStyleClass().remove("input_field_red");
					password_field_registration.getStyleClass().remove("input_field_green");
					password_field_registration.getStyleClass().add("input_field_green");
				}
			}
		});
    	
    	
    	repeat_password_field_registration.setOnKeyReleased(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				if(repeat_password_field_registration.getText().equals("")) {
					repeat_password_field_registration.getStyleClass().remove("input_field_red");
					repeat_password_field_registration.getStyleClass().remove("input_field_green");
				}
				else 
				{
					repeat_password_field_registration.getStyleClass().remove("input_field_green");
					repeat_password_field_registration.getStyleClass().add("input_field_green");
				}
				
			}
		});
    	email_field_registration.setOnKeyReleased(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				if(email_field_registration.getText().equals("")) {
					email_field_registration.getStyleClass().remove("input_field_red");
					email_field_registration.getStyleClass().remove("input_field_green");
				}
				else if(!pattern3.matcher(email_field_registration.getText()).matches()) {
					email_field_registration.getStyleClass().remove("input_field_red");
					email_field_registration.getStyleClass().remove("input_field_green");
					email_field_registration.getStyleClass().add("input_field_red");
				}
				else
				{
					email_field_registration.getStyleClass().remove("input_field_red");
					email_field_registration.getStyleClass().remove("input_field_green");
					email_field_registration.getStyleClass().add("input_field_green");
				}
				
				
			}
		});
    }

}
