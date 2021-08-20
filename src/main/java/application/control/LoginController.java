package application.control;

import application.SceneHandler;
import application.net.client.Client;
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

    }

    @FXML
    void onClick_sign_in_button_login(ActionEvent event) {

    }

    @FXML
    void onClick_sign_up_button_login(ActionEvent event) {
    	
    	SceneHandler.getInstance().loadScene("RegistrationPage", false);
    	Client.getInstance().setCurrentState(Client.STEP_REGISTRATION);
    }

}
