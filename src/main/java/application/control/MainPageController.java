package application.control;


import application.SceneHandler;
import application.Settings;
import application.Utilities;
import application.model.game.handler.SoundHandler;
import application.net.client.Client;
import application.net.common.Protocol;
import application.view.Dialog;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class MainPageController {

	

    private static final double TIMETRANSACTION = 1.0 ;
    
 
    @FXML
    private BorderPane root;

    
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
    private Label information_label;
    
    @FXML
    private HBox container_cancel_button;
    
    @FXML
    private Label coins_main_page_label;
    
    private Button cancel_button = new Button("CANCEL");
    
    private static final String WELCOME  = "Welcome ";
    
    private boolean cancel_attive = false ;
    
    private boolean ready = false ;
    
    public void setReady(boolean ready) {
		this.ready = ready;
	}
    
    public boolean isReady() {
		return ready;
	}
    
    
    @FXML
    public void initialize() {
    	SceneHandler.getInstance().setCursor(SceneHandler.DEFAULT_CURSOR);
    	Client.getInstance().setCurrentState(Client.MAINPAGE);
    	
    	leave_button_main_page.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	account_button_main_page.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	friends_button_main_page.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	friends_button_main_page.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	name_field_mainpage.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 20));
    	data_field_mainpage.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 11));
    	cancel_button.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 16));
    	coins_main_page_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 31));
    	
    	cancel_button.setTextFill(Color.WHITE);
    	cancel_button.getStyleClass().add("leave_button");
    	
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
    	
    	cancel_button.setOnMouseEntered(new HoverButton());
    	cancel_button.setOnMouseExited(new HoverButton());
    	
    	cancel_button.setOnAction(ev ->{
    		Client.getInstance().cancelRequest();
    		FadeTransition trans = new FadeTransition(Duration.seconds(1.5),cancel_button );
    		cancel_attive = false ;
    	    trans.setFromValue(0.5);
    	    trans.setToValue(0);
    	    trans.play();
    	    container_cancel_button.getChildren().remove(cancel_button);
    	});
    	
    	FXMLLoader loaderLogin = SceneHandler.getInstance().getLoader("LoginPage") ;
    	FXMLLoader loaderRegistration = SceneHandler.getInstance().getLoader("RegistrationPage");
    	
    	if(loaderLogin != null ) {
    		LoginController loginController = (LoginController) loaderLogin.getController() ;
    		loginController.setPressed(false);
    	}
    	
    	if(loaderRegistration != null ) {
    		
    		RegistrationController registrationController = (RegistrationController) loaderRegistration.getController() ;
        	registrationController.setPressed(false);
    	}
    
    	
    	changeBackgroundButtonField();
    	changeDataButtonField();
    	
    	showText(WELCOME+" "+Client.getInstance().getUsername(), 30, Dialog.INFORMATION_WINDOW,5);
    	
    	Client.getInstance().sendMessage(Protocol.INITIALINFORMATION);
    }
    
   
    public void setCoins_main_page_label(String coins) {
		this.coins_main_page_label.setText(coins);
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
    void onClick_account_button_main_page(ActionEvent event) {
    	SoundHandler.getInstance().startHit();
    	
    	
    	if(cancel_attive)
    	{
    		showText(Protocol.LEAVEWITHOUTCANCEL, 20, Dialog.ERROR_WINDOW,2);
    		return;
    	}
    	
    	if(!isReady())
    		return ;
    	
    	Client.getInstance().setCurrentState(Client.ACCOUNT);
    	SceneHandler.getInstance().loadScene("AccountPage", true, true);
    }

    
    public void setCancel_attive(boolean cancel_attive) {
		this.cancel_attive = cancel_attive;
		container_cancel_button.getChildren().remove(cancel_button);
	}

    @FXML
    void onClickButtonField(MouseEvent event) {
    	SoundHandler.getInstance().startHit();
    	
    	
    	if(Client.getInstance().getCurrentState() != Client.MAINPAGE || !isReady())
    		return ;
    	
    	container_cancel_button.getChildren().add(cancel_button);
    	cancel_attive = true ;
    	
    	
    	FadeTransition trans = new FadeTransition(Duration.seconds(1.5),cancel_button );
	
	    trans.setFromValue(0.0);
	    trans.setToValue(1);
	    trans.play();

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
    	SoundHandler.getInstance().startHit();
    	
    	if(cancel_attive)
    	{
    		showText(Protocol.LEAVEWITHOUTCANCEL, 20, Dialog.ERROR_WINDOW,2);
    		return;
    	}
    	if(!isReady())
    		return ;
    	
    	SceneHandler.getInstance().loadScene("FriendsPage", true, true);
    }

    @FXML
    void onClick_leave_button_main_page(ActionEvent event) {
    	SoundHandler.getInstance().startHit();
    	
    	if(cancel_attive)
    	{
    		showText(Protocol.LEAVEWITHOUTCANCEL, 20, Dialog.ERROR_WINDOW,2);
    		return;
    	}
    	if(!isReady())
    		return ;
    	if(Dialog.getInstance().showConfirmDialog(Dialog.CONFIRMLOGOUT) != Dialog.YES)
    		return ;
    	Client.getInstance().logout();
    }

    @FXML
    void onClick_shop_button_main_page(ActionEvent event) {
    	SoundHandler.getInstance().startHit();
    	
    	if(cancel_attive)
    	{
    		showText(Protocol.LEAVEWITHOUTCANCEL, 20, Dialog.ERROR_WINDOW,2);
    		return;
    	}
    	if(!isReady())
    		return ;
    	SceneHandler.getInstance().loadScene("ShopPage", true, true);
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
    	SoundHandler.getInstance().startHit();
    	
    	if(cancel_attive)
    	{
    		showText(Protocol.LEAVEWITHOUTCANCEL, 20, Dialog.ERROR_WINDOW,2);
    		return;
    	}
    	if(!isReady())
    		return ;
    	Client.getInstance().clickButtonLeft();
    	changeBackgroundButtonField();
    	changeDataButtonField();
    }
   

    @FXML
    void onClickRightTriangleButton(ActionEvent event) {
    	SoundHandler.getInstance().startHit();
    	
    	if(cancel_attive)
    	{
    		showText(Protocol.LEAVEWITHOUTCANCEL, 20, Dialog.ERROR_WINDOW,2);
    		return;
    	}
    	if(!isReady())
    		return ;
    	Client.getInstance().clickButtonRight();
    	changeBackgroundButtonField();
    	changeDataButtonField();
    }
    
 

}
