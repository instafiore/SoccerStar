package application.control;

import java.beans.beancontext.BeanContextServiceAvailableEvent;
import java.util.regex.Pattern;

import application.SceneHandler;
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
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class LoginController {
	
	
    @FXML
    private StackPane stackPane;

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
    	// TODO
    }
    
    private final String REGEXUSERNAME = "([a-zA-Z][^&%$£!]){6,}" ; 
	private final String REGEXPASSWORD = "[^&%$£!]{6,16}" ; 
	
	
	private Pattern pattern1 = Pattern.compile(REGEXUSERNAME);
	private Pattern pattern2 = Pattern.compile(REGEXPASSWORD);
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
    		showError(Protocol.FIELDEMPTY,20);
    		return ;
    	}
    	
    	if(!rulesRespected()) {
    		showError(Protocol.LOGINFAILED, 13);
    		return ;
    	}
    	
    	String stringa = username_field_login.getText() + Protocol.DELIMITERLOGIN + password_field_login.getText() ;
    	
    	Client.getInstance().sendMessage(Protocol.LOGINREQUEST);
    	Client.getInstance().sendMessage(stringa);
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
    void onClick_sign_up_button_login(ActionEvent event) {

    	SceneHandler.getInstance().loadScene("RegistrationPage", false , true);
    	Client.getInstance().setCurrentState(Client.STEP_REGISTRATION);
    }
    
    
    public boolean rulesRespected() {
    	return pattern1.matcher(username_field_login.getText()).matches() && pattern2.matcher(password_field_login.getText()).matches();
    }
    
    private void settingOnKeyReleased() {
    	
    	username_field_login.setOnKeyReleased(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				if(username_field_login.getText().equals("")) {
					username_field_login.getStyleClass().remove("input_field_red");
					username_field_login.getStyleClass().remove("input_field_green");
				}
				else 
				{
					username_field_login.getStyleClass().remove("input_field_green");
					username_field_login.getStyleClass().add("input_field_green");
				}
				
			}
		});
    	password_field_login.setOnKeyReleased(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				if(password_field_login.getText().equals("")) {
					password_field_login.getStyleClass().remove("input_field_red");
					password_field_login.getStyleClass().remove("input_field_green");
				}
				else 
				{
					password_field_login.getStyleClass().remove("input_field_green");
					password_field_login.getStyleClass().add("input_field_green");
				}
				
			}
		});
    }
}
