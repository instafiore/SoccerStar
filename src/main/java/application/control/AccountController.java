package application.control;

import java.io.InputStream;

import application.SceneHandler;
import application.Utilities;
import application.net.client.Client;
import application.net.common.Protocol;
import application.view.Dialog;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class AccountController {

    @FXML
    private Button back_button_account;

    @FXML
    private Label information_label;

    @FXML
    private Button history_button_account;

    @FXML
    private Button inventory_button_account;

    @FXML
    private Button buy_coins_button_account;

    @FXML
    private Button friends_button_account;

    @FXML
    private Circle ball_account;

    @FXML
    private Label username_field_account;

    @FXML
    private Label email_label_account;

    @FXML
    private Label email_field_account;

    @FXML
    private Label card_label_account;

    @FXML
    private Label card_field_account;

    @FXML
    private Button add_payment_method_button;

    @FXML
    private Label coins_label_account;
    
    @FXML
    private Button change_password_button;
    

    @FXML
    private PasswordField old_password_field_account;

    @FXML
    private PasswordField new_password_field_account;

    
    private static final String AccountTitle = "Account" ;
   
    @FXML
    public void initialize() {
    	Client.getInstance().setCurrentState(Client.ACCOUNT);
    	 	
    	back_button_account.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 20));
    	information_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 25));
    	history_button_account.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 17));
    	inventory_button_account.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 17));
    	buy_coins_button_account.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 17));
    	friends_button_account.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 17));
    	username_field_account.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	email_label_account.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	card_label_account.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	card_field_account.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	add_payment_method_button.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 12));
    	coins_label_account.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 31));
    	email_field_account.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 13));
    	change_password_button.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 13));
    	old_password_field_account.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 13));
    	new_password_field_account.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 13));
    	
    	back_button_account.setOnMouseEntered(new HoverButton());
    	back_button_account.setOnMouseExited(new HoverButton());
    	
    	history_button_account.setOnMouseEntered(new HoverButton());
    	history_button_account.setOnMouseExited(new HoverButton());
    	
    	buy_coins_button_account.setOnMouseEntered(new HoverButton());
    	buy_coins_button_account.setOnMouseExited(new HoverButton());
    	
    	friends_button_account.setOnMouseEntered(new HoverButton());
    	friends_button_account.setOnMouseExited(new HoverButton());
    	
    	inventory_button_account.setOnMouseEntered(new HoverButton());
    	inventory_button_account.setOnMouseExited(new HoverButton());
    	
    	add_payment_method_button.setOnMouseEntered(new HoverButton());
    	add_payment_method_button.setOnMouseExited(new HoverButton());
    	
    	change_password_button.setOnMouseEntered(new HoverButton());
    	change_password_button.setOnMouseExited(new HoverButton());
    	
    	new_password_field_account.setOnKeyReleased(new InputFieldController(InputFieldController.PASSWORD));
    	
    	
    	Client.getInstance().sendMessage(Protocol.INFORMATIONACCOUNT);
    	showText(AccountTitle, 26, Dialog.INFORMATION_WINDOW, 6);
    }
    
    @FXML
    void onClickChange_password_button(ActionEvent event) {
    	
    	if(old_password_field_account.getText().equals("") || new_password_field_account.getText().equals(""))
    	{
    		showText(Protocol.FIELDEMPTY, 18 , Dialog.ERROR_WINDOW, 3);
    		return ;
    	}
    	
    	if(old_password_field_account.getText().equals(new_password_field_account.getText())) {
    		showText(Protocol.SAMEPASSWORD, 13 , Dialog.ERROR_WINDOW, 3);
    		return ;
    	}
    	
    	if(!Utilities.getInstance().rulePasswordRespected(old_password_field_account.getText()))
    	{
    		showText(Protocol.OLDPASSOWORDNOTCORRECT, 13 , Dialog.ERROR_WINDOW, 3);
    		return ;
    	}
    	
    	if(!Utilities.getInstance().rulePasswordRespected(new_password_field_account.getText()))
    	{
    		showText(Protocol.NEWPASSOWORDISNOTVALID, 15 , Dialog.ERROR_WINDOW, 3);
    		return ;
    	}
    	
    	Client.getInstance().sendMessage(Protocol.CHANGEPASSWORD);
    	Client.getInstance().sendMessage(old_password_field_account.getText()+Protocol.DELIMITEROLDNEWPASSWORD+new_password_field_account.getText());
    	old_password_field_account.getStyleClass().remove("input_field_green");
    	old_password_field_account.getStyleClass().remove("input_field_red");
    	old_password_field_account.setText("");
    	new_password_field_account.setText("");
    }

    
    public void changeColorBall(String color) {
    	ball_account.setFill(Color.web(color));
    }
    
    public void setUsername_field_account(String username_field_account) {
		this.username_field_account.setText(username_field_account);
	}
    
    public void setEmail_field_account(String email_field_account) {
		this.email_field_account.setText(email_field_account);
	}
    
    
    
    public void setCard_field_account(String card_field_account) {
		this.card_field_account.setText(card_field_account);
	}
    
    public void setCoins_label_account(String coins_label_account) {
		this.coins_label_account.setText(coins_label_account);
	}
    
    @FXML
    void onClickAdd_payment_method_button(ActionEvent event) {

    }

    @FXML
    void onClickBack_button_account(ActionEvent event) {
    	SceneHandler.getInstance().loadScene("MainPage", true, true);
    }

    @FXML
    void onClickBuy_coins_button_account(ActionEvent event) {

    }

    @FXML
    void onClickFriends_button_account(ActionEvent event) {

    }

    @FXML
    void onClickHistory_button_account(ActionEvent event) {
    	SceneHandler.getInstance().loadScene("HistoryPage", true, true);
    }
    
    @FXML
    void onClickInventory_button_account(ActionEvent event) {
    	SceneHandler.getInstance().loadScene("InventaryPage", true, true);
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


}
