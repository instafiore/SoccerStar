package application.control;

import org.springframework.security.crypto.bcrypt.BCrypt;

import application.SceneHandler;
import application.net.client.Client;
import application.net.common.Protocol;
import application.net.server.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class RegistrationController {



	
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
    	
    	sign_up_button_registration.setOnMouseEntered(new HoverButton());
    	sign_up_button_registration.setOnMouseExited(new HoverButton());
    	
    	back_button_registration.setOnMouseEntered(new HoverButton());
    	back_button_registration.setOnMouseExited(new HoverButton());
    	
    }

    
    @FXML
    void onClick_back_button_registration(ActionEvent event) {
    	SceneHandler.getInstance().loadScene("LoginPage", false);
    	Client.getInstance().setCurrentState(Client.STEP_LOGIN);
    }

    @FXML
    void onClick_sign_up_button_registration(ActionEvent event) {
    	if(!password_field_registration.getText().equals(repeat_password_field_registration.getText()))
    	{
    		//TODO
    		return ;
    	}
    	
    	String passwordCrypto = Database.getInstance().cryptoPassword(password_field_registration.getText());
    	
    	String stringa = username_field_registration.getText() + Protocol.DELIMITERREGISTRATION + passwordCrypto + Protocol.DELIMITERREGISTRATION + email_field_registration.getText() ;
    	
    	Client.getInstance().sendMessage(Protocol.REGISTRATIONREQUEST);
    	Client.getInstance().sendMessage(stringa);
    }

}
