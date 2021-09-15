package application.control;


import java.beans.beancontext.BeanContextServiceAvailableEvent;
import java.io.InputStream;
import java.util.regex.Pattern;

import application.SceneHandler;
import application.Utilities;
import application.net.client.Client;
import application.net.common.Protocol;
import application.view.Dialog;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class LoginController {
	InputStream inputStream = getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf") ;
	


    @FXML
    private AnchorPane root;

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

    @FXML
    private Label information_label;
    @FXML
    void onClick_password_forgot_button(MouseEvent event) {
    	SceneHandler.getInstance().loadScene("Step1PSW", false, true);
    }
 
    @FXML
    public void initialize() {
    	
    	Client.getInstance().setCurrentState(Client.STEP_LOGIN);
   
    	
    	sign_in_button_login.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 14));
    	username_field_login.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 14));
    	password_field_login.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 14));
    	password_forgot_button.setFont(new Font(15));
    	sign_up_button_login.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 14));
    	username_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	soccerstar_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 42));
    	password_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
 
    	
    	sign_in_button_login.setOnMouseEntered(new HoverButton());
    	sign_in_button_login.setOnMouseExited(new HoverButton());
    	
    	sign_up_button_login.setOnMouseEntered(new HoverButton());
    	sign_up_button_login.setOnMouseExited(new HoverButton());
    
    	password_forgot_button.setOnMouseEntered(new HoverButton());
    	password_forgot_button.setOnMouseExited(new HoverButton());
    	
    	
    	settingOnKeyReleased();
    }

    @FXML
    void onClick_sign_in_button_login(ActionEvent event) {

    	if(username_field_login.getText().equals("") || password_field_login.equals(""))
    	{
    		showText(Protocol.FIELDEMPTY,20,Dialog.ERROR_WINDOW,4);
    		return ;
    	}
    	
    	boolean f1 = Utilities.getInstance().rulePasswordRespected(password_field_login.getText()) ;
    	boolean f2 = Utilities.getInstance().ruleUsernameRespected(username_field_login.getText()) ;
    	if(!f1 || !f2 ) {
    		
    		if(!f1 && !f2)
    			showText(Protocol.LOGINFAILED, 15, Dialog.ERROR_WINDOW, 2) ;
    		else if (!f1 && f2)
    			showText(Protocol.INCORRECTPASSWORD, 15, Dialog.ERROR_WINDOW, 2) ;
    		else
    			showText(Protocol.INCORRECTUSERNAME, 15, Dialog.ERROR_WINDOW, 2) ;
    		
    		return ;
    	}
    	
    	String stringa = username_field_login.getText() + Protocol.DELIMITERLOGIN + password_field_login.getText() ;
    	
    	Client.getInstance().sendMessage(Protocol.LOGINREQUEST);
    	Client.getInstance().sendMessage(stringa);
    }
    
    public void showText(String text,int fontSize,String type,double duration) {
    	
    	String color = "" ;
		
		if(type.equals(Dialog.ERROR_WINDOW)) {
			color = "#ff1313" ;
		}else if(type.equals(Dialog.INFORMATION_WINDOW)) {
			color = "#ffffff" ;
		}else if(type.equals(Dialog.ATTENTION_WINDOW)) {
			color = "#ff5e28" ;
		}
    	
    	information_label.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), fontSize));
    	information_label.setText(text);
    	information_label.setTextFill(Color.web(color, 1));
    
    	FadeTransition trans = new FadeTransition(Duration.seconds(duration),information_label);
		
		trans.setFromValue(1.0);
        trans.setToValue(0.0);
        trans.play();
    }

    @FXML
    void onClick_sign_up_button_login(ActionEvent event) {

    	SceneHandler.getInstance().loadScene("RegistrationPage", false , true);
    	Client.getInstance().setCurrentState(Client.STEP_REGISTRATION);
    }
    

    
    private void settingOnKeyReleased() {
    	
    	username_field_login.setOnKeyReleased(new InputFieldController(InputFieldController.NOCONTROL));
    	password_field_login.setOnKeyReleased(new InputFieldController(InputFieldController.NOCONTROL));
    
    }
    
    @FXML
    void onKeyPressed(KeyEvent event) {

    }
}
