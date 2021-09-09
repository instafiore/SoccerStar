package application.control;


import application.Settings;
import application.net.client.Client;
import application.view.Dialog;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class MainPageController {

	

    private static final double TIMETRANSACTION = 1.0 ;
    
    @FXML
    private BorderPane field_button_main_page;

    @FXML
    private Button leave_button_main_page;

    @FXML
    private Button account_button_main_page;

    @FXML
    private Button friends_button_main_page;

    @FXML
    private Button shop_button_main_page;
    
    @FXML
    private Button left_triangle_button;

    @FXML
    private Button right_triangle_button;
    
    @FXML
    private Label name_field_mainpage;

    @FXML
    private Label data_field_mainpage;
    
    
    @FXML
    public void initialize() {
    	
    	leave_button_main_page.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	account_button_main_page.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	friends_button_main_page.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	friends_button_main_page.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 15));
    	name_field_mainpage.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 26));
    	data_field_mainpage.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 11));
    	
    	
    	field_button_main_page.setOnMouseEntered(new HoverButton());
    	field_button_main_page.setOnMouseExited(new HoverButton());
    	
    	leave_button_main_page.setOnMouseEntered(new HoverButton());
    	leave_button_main_page.setOnMouseExited(new HoverButton());
    	
    	account_button_main_page.setOnMouseEntered(new HoverButton());
    	account_button_main_page.setOnMouseExited(new HoverButton());
    	
    	friends_button_main_page.setOnMouseEntered(new HoverButton());
    	friends_button_main_page.setOnMouseExited(new HoverButton());
    	
    	friends_button_main_page.setOnMouseEntered(new HoverButton());
    	friends_button_main_page.setOnMouseExited(new HoverButton());
    	
    	shop_button_main_page.setOnMouseEntered(new HoverButton());
    	shop_button_main_page.setOnMouseExited(new HoverButton());
    	

    	left_triangle_button.setOnMouseEntered(new HoverButton());
    	left_triangle_button.setOnMouseExited(new HoverButton());
    	
    	right_triangle_button.setOnMouseEntered(new HoverButton());
    	right_triangle_button.setOnMouseExited(new HoverButton());
    	
    	changeDataButtonField();
    }

    @FXML
    void onClick_account_button_main_page(ActionEvent event) {

    }


    @FXML
    void onClickButtonField(MouseEvent event) {
    	
    	switch (Client.getInstance().getCurrentField()) {
		case Client.FIELD1:
			Client.getInstance().startMatchField1();
			break;
		case Client.FIELD2:
			Client.getInstance().startMatchField2();
			break;
		case Client.FIELD3:
			Client.getInstance().startMatchField3();
			break;
		}
    	
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
    
    
    private void changeDataButtonField() {
    	switch (Client.getInstance().getCurrentField()) {
		case Client.FIELD1:
			name_field_mainpage.setText(Settings.FIELD1);
			data_field_mainpage.setText("price: "+Settings.PRICEFIELD1+"	reward: "+Settings.REWARDFIELD1);
			break;
		case Client.FIELD2:
			name_field_mainpage.setText(Settings.FIELD2);
			data_field_mainpage.setText("price: "+Settings.PRICEFIELD2+"	reward: "+Settings.REWARDFIELD2);
			break;
		case Client.FIELD3:
			name_field_mainpage.setText(Settings.FIELD3);
			data_field_mainpage.setText("price: "+Settings.PRICEFIELD3+"	reward: "+Settings.REWARDFIELD3);
			break;
		}
    }
   
    private void changeBackgroundButtonField() {
    	
    	switch (Client.getInstance().getCurrentField()) {
		case Client.FIELD1:
			field_button_main_page.getStyleClass().remove("field2");
			field_button_main_page.getStyleClass().remove("field3");
			field_button_main_page.getStyleClass().add("field1");
			break;
		case Client.FIELD2:
			field_button_main_page.getStyleClass().remove("field1");
			field_button_main_page.getStyleClass().remove("field3");
			field_button_main_page.getStyleClass().add("field2");
			break;
		case Client.FIELD3:
			field_button_main_page.getStyleClass().remove("field1");
			field_button_main_page.getStyleClass().remove("field2");
			field_button_main_page.getStyleClass().add("field3");
			break;
		}
    	 FadeTransition trans = new FadeTransition(Duration.seconds(TIMETRANSACTION),field_button_main_page );
         trans.setFromValue(.20);
         trans.setToValue(1);
         trans.play();
    	
    }
    
    @FXML
    void onClickLeftTriangleButton(ActionEvent event) {
    	Client.getInstance().clickButtonLeft();
    	changeBackgroundButtonField();
    	changeDataButtonField();
    }
   

    @FXML
    void onClickRightTriangleButton(ActionEvent event) {
    	Client.getInstance().clickButtonRight();
    	changeBackgroundButtonField();
    	changeDataButtonField();
    }

}
