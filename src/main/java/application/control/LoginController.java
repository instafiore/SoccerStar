package application.control;

import application.SceneHandler;
import application.net.client.Client;
import application.net.common.Protocol;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LoginController {
	
	@FXML
    private Button sign_in_button_login;

    @FXML
    private Label username_label;

    @FXML
    private TextField username_field_login;

    @FXML
    private Label password_label;

    @FXML
    private PasswordField password_field_login;

    @FXML
    private Label soccerstar_label;

    @FXML
    private Label password_forgot_button;

    @FXML
    private Button sign_up_button_login;

    
    private static final String GREEN_BUTTON_HOVER = "#009a00" ;
    
    
    private static final int STEPTRANSATION = 100 ;
    
    @FXML
    void onClick_password_forgot_button(MouseEvent event) {
    	// TODO
    }
    
    
    @FXML
    public void initialize() {
    	sign_in_button_login.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 14));
    	username_field_login.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 14));
    	password_field_login.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 14));
    	password_forgot_button.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 13));
    	sign_up_button_login.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 14));
    	username_label.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	soccerstar_label.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 42));
    	password_label.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
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
    
    @FXML
    void onMouseEntered(MouseDragEvent event) {
    	
    	System.out.println("HERE");
    	double opacity = 1 ;
    	double less = STEPTRANSATION / 1000.0 ;
    	
//    	try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		sign_in_button_login.setStyle("{"
				+ "-fx-background-color: #009a00;"
				+ "-fx-opacity;0.5"
				+ "}");
//    	for(int i = 0 ; i < STEPTRANSATION ;++i)
//    	{
//    		System.out.println("HERE");
//    		try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//    		sign_in_button_login.setTextFill(Color.web(GREEN_BUTTON_HOVER, opacity -= less ));
//    		
//    	}
    		
    }

}
