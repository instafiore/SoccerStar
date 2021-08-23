package application.control;

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
import javafx.scene.input.MouseEvent;

public class LoginController {

    @FXML
    private Button sign_in_button_login;

    @FXML
    private TextField username_field_login;

    @FXML
    private PasswordField password_field_login;

    @FXML
    private Label password_forgot_button;

    @FXML
    private Button sign_up_button_login;

    @FXML
    void onClick_password_forgot_button(MouseEvent event) {
    	// TODO
    }

    @FXML
    void onClick_sign_in_button_login(ActionEvent event) {
    	
    	String stringa = username_field_login.getText() + Protocol.DELIMITERLOGIN + password_field_login.getText() ;
    	
    	Client.getInstance().sendMessage(Protocol.LOGINREQUEST);
    	Client.getInstance().sendMessage(stringa);
    }

    @FXML
    void onClick_sign_up_button_login(ActionEvent event) {
    	
    	SceneHandler.getInstance().loadScene("RegistrationPage", false);
    	Client.getInstance().setCurrentState(Client.STEP_REGISTRATION);
    }

}
