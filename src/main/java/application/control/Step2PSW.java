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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class Step2PSW {

    @FXML
    private Label information_label;

    @FXML
    private TextArea text_area_step2psw;

    @FXML
    private TextField code_field_step2psw;

    @FXML
    private PasswordField reset_password_field;

    @FXML
    private Button reset_button_step2psw;

    @FXML
    private Button back_button_step2PSW;
    
    private boolean ready = false ;
    
    public void setReady(boolean ready) {
		this.ready = ready;
	}
    
    public boolean isReady() {
		return ready;
    }
    @FXML
    public void initialize() {
    	
    	Client.getInstance().setCurrentState(Client.STEP2PSW);
    	
    	information_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 18));
    	text_area_step2psw.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 13));
    	code_field_step2psw.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	reset_password_field.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	reset_button_step2psw.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 13));
    	back_button_step2PSW.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 12));
    	
    	code_field_step2psw.setPromptText("Insert code");
    	reset_password_field.setPromptText("Insert new password");
    	
    	reset_button_step2psw.setOnMouseEntered(new HoverButton());
    	reset_button_step2psw.setOnMouseExited(new HoverButton());
    	
    	back_button_step2PSW.setOnMouseEntered(new HoverButton());
    	back_button_step2PSW.setOnMouseExited(new HoverButton());
    	
    	code_field_step2psw.setOnKeyReleased(new InputFieldController());
    	reset_password_field.setOnKeyReleased(new InputFieldController(InputFieldController.PASSWORD));
    	
    	Step1PSW step1psw = SceneHandler.getInstance().getLoader("Step1PSW").getController() ;
    	step1psw.setPressed(false);
 
    }
    
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
    void onClickBack_button_step2PSW(ActionEvent event) {
    	SoundHandler.getInstance().startHit();
    	
    	if(!ready)
    		return ;
    	SceneHandler.getInstance().loadScene("Step1PSW", false, true);
    	Client.getInstance().sendMessage(Protocol.CANCELPASSWORDRECOVERY);
    }
    
    @FXML
    void onClickReset_button_step2psw(ActionEvent event) {
    	SoundHandler.getInstance().startHit();
    	
    	if(!ready)
    		return ;
    	if(code_field_step2psw.getText().equals("") || reset_password_field.getText().equals(""))
    	{
    		showError(Protocol.FIELDEMPTY, 16);
    		return ;
    	}
    	
    	if(!Utilities.rulePasswordRespected(reset_password_field.getText()))
    	{
    		showError(Utilities.RULEPASSWORDNOTRESPECTED, 15);
    		return ;
    	}
    	
    	String message = code_field_step2psw.getText()+ Protocol.DELIMITERCODEPASSOWRD + reset_password_field.getText() ;
    	Client.getInstance().sendMessage(Protocol.CODEPASSWORD);
    	Client.getInstance().sendMessage(message);
    }
    
    public void setText_area_step2psw(String text_area_step2psw) {
		this.text_area_step2psw.setText(text_area_step2psw);;
	}
    
  
 
}
