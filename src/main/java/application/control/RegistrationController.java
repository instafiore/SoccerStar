package application.control;

import org.springframework.security.crypto.bcrypt.BCrypt;

import application.SceneHandler;
import application.net.client.Client;
import application.net.common.Protocol;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class RegistrationController {

    @FXML
    private TextField username_field_registration;

    @FXML
    private PasswordField password_field_registration;

    @FXML
    private PasswordField repeat_password_field_registration;

    @FXML
    private TextField email_field_registration;

    @FXML
    private Button sign_up_button_registration;

    @FXML
    private Button back_button_registration;

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
    	String passwordCrypto = BCrypt.hashpw(password_field_registration.getText(), BCrypt.gensalt(12));
    	
    	String stringa = username_field_registration.getText() + Protocol.DELIMITERREGISTRATION + passwordCrypto + Protocol.DELIMITERREGISTRATION + email_field_registration.getText() ;
    	
    	Client.getInstance().sendMessage(Protocol.REGISTRATIONREQUEST);
    	Client.getInstance().sendMessage(stringa);
    }

}
