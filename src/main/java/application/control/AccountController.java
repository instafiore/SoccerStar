package application.control;

import application.SceneHandler;
import application.net.client.Client;
import application.net.common.Protocol;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

public class AccountController {

    @FXML
    private Button back_button_account;

    @FXML
    private Label information_label;

    @FXML
    private Button history_button_account;

    @FXML
    private Button shop_button_account;

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
    public void initialize() {
    	back_button_account.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 20));
    	information_label.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 25));
    	history_button_account.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 17));
    	shop_button_account.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 17));
    	buy_coins_button_account.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 17));
    	friends_button_account.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 17));
    	username_field_account.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	email_label_account.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	card_label_account.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	card_field_account.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	add_payment_method_button.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 12));
    	coins_label_account.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 31));
    	email_field_account.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 13));
    	
    	
    	back_button_account.setOnMouseEntered(new HoverButton());
    	back_button_account.setOnMouseExited(new HoverButton());
    	
    	history_button_account.setOnMouseEntered(new HoverButton());
    	history_button_account.setOnMouseExited(new HoverButton());
    	
    	buy_coins_button_account.setOnMouseEntered(new HoverButton());
    	buy_coins_button_account.setOnMouseExited(new HoverButton());
    	
    	friends_button_account.setOnMouseEntered(new HoverButton());
    	friends_button_account.setOnMouseExited(new HoverButton());
    	
    	shop_button_account.setOnMouseEntered(new HoverButton());
    	shop_button_account.setOnMouseExited(new HoverButton());
    	
    	add_payment_method_button.setOnMouseEntered(new HoverButton());
    	add_payment_method_button.setOnMouseExited(new HoverButton());
    	
    	Client.getInstance().sendMessage(Protocol.INFORMATIONACCOUNT);
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
    	Client.getInstance().setCurrentState(Client.MAINPAGE);
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

    }

    @FXML
    void onClickShop_button_account(ActionEvent event) {

    }

}
