package application.control;

import application.net.client.Client;
import application.view.Dialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class MainPageController {

    @FXML
    private Button field_button_main_page;

    @FXML
    private Button leave_button_main_page;

    @FXML
    private Button account_button_main_page;

    @FXML
    private Button friends_button_main_page;

    @FXML
    private Button shop_button_main_page;
    
    @FXML
    public void initialize() {
    	field_button_main_page.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 30));
    	leave_button_main_page.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	account_button_main_page.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	friends_button_main_page.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	friends_button_main_page.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	shop_button_main_page.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	
    	field_button_main_page.setOnMouseEntered(new HoverButton());
    	field_button_main_page.setOnMouseExited(new HoverButton());
    	
    	leave_button_main_page.setOnMouseEntered(new HoverButton(HoverButton.LEAVEBUTTON));
    	leave_button_main_page.setOnMouseExited(new HoverButton(HoverButton.LEAVEBUTTON));
    	
    	account_button_main_page.setOnMouseEntered(new HoverButton());
    	account_button_main_page.setOnMouseExited(new HoverButton());
    	
    	friends_button_main_page.setOnMouseEntered(new HoverButton());
    	friends_button_main_page.setOnMouseExited(new HoverButton());
    	
    	friends_button_main_page.setOnMouseEntered(new HoverButton());
    	friends_button_main_page.setOnMouseExited(new HoverButton());
    	
    	shop_button_main_page.setOnMouseEntered(new HoverButton());
    	shop_button_main_page.setOnMouseExited(new HoverButton());
    }

    @FXML
    void onClick_account_button_main_page(ActionEvent event) {

    }

    @FXML
    void onClick_field_button_main_page(ActionEvent event) {
    	Client.getInstance().startMatchField1();
    }

    @FXML
    void onClick_friends_button_main_page(ActionEvent event) {

    }

    @FXML
    void onClick_leave_button_main_page(ActionEvent event) {
    	if(Dialog.getInstance().showConfirmDialog(Dialog.CONFIRMLOGOUT) != Dialog.YES)
    		return ;
    	Client.getInstance().logout();
    }

    @FXML
    void onClick_shop_button_main_page(ActionEvent event) {

    }

}
