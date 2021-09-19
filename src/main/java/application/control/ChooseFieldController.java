package application.control;

import application.SceneHandler;
import application.Settings;
import application.Utilities;
import application.net.client.Client;
import application.net.common.Protocol;
import application.view.Dialog;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class ChooseFieldController {

	
	@FXML
    private BorderPane root;

    @FXML
    private BorderPane container_field;

    @FXML
    private Label name_field;

    @FXML
    private Label information_label;

    @FXML
    private Button left_button;

    @FXML
    private Button right_button;

    @FXML
    private Button cancel_button;

    private String friendToChallenge = "" ;

    private static final String CHALLENGEROOM = "CHALLENGE ROOM" ;
    private static final double TIMETRANSACTION = 1.0 ;
    
    private static final String REQUESTSENT = "Friendly battle request sent to " ;
	private static final String REQUESTCANCELED = "Request canceled";
  
    private int currentField = Client.FIELD1 ;
    private boolean cancel_attive = false ;
    
    
    public void setFriendToChallenge(String friendToChallenge) {
		this.friendToChallenge = friendToChallenge;
	}
    
    public String getFriendToChallenge() {
		return friendToChallenge;
	}
    
    public boolean isCancel_attive() {
		return cancel_attive;
	}
    
    @FXML
    void onMouseReleadesField(MouseEvent event) {
    	
    	if(isCancel_attive())
    		return ;
    	
    	cancel_button.setDisable(false);
    	cancel_button.setOpacity(1);
    	
    	cancel_attive = true ;
    	
    	FadeTransition trans = new FadeTransition(Duration.seconds(1.5),cancel_button );
	
	    trans.setFromValue(0.0);
	    trans.setToValue(1);
	    trans.play();
	    
	    showText(REQUESTSENT +  friendToChallenge, 9 , Dialog.INFORMATION_WINDOW, 1000);

        switch (getCurrentField()) {
		case Client.FIELD1:
			Client.getInstance().sendMessage(Protocol.FRIENDLYREQUESTFIELD1);
			break;
		case Client.FIELD2:
			Client.getInstance().sendMessage(Protocol.FRIENDLYREQUESTFIELD2);
			break;
		case Client.FIELD3:
			Client.getInstance().sendMessage(Protocol.FRIENDLYREQUESTFIELD3);
			break;
		}
        Client.getInstance().sendMessage(friendToChallenge);
    }


    @FXML
    public void initialize() {
    	
    	
    	name_field.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 18));
    	cancel_button.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 10));
    	
    	left_button.setOnMouseEntered(new HoverButton());
    	left_button.setOnMouseExited(new HoverButton());
    	
    	right_button.setOnMouseEntered(new HoverButton());
    	right_button.setOnMouseExited(new HoverButton());
	
    	cancel_button.setOpacity(0);
    	cancel_button.setDisable(true);
    	
    	cancel_button.setOnAction(ev ->{

    	    showText(REQUESTCANCELED , 14 , Dialog.INFORMATION_WINDOW, 4);

    		FadeTransition trans = new FadeTransition(Duration.seconds(1.5),cancel_button );
    		cancel_attive = false ;
    	    trans.setFromValue(1);
    	    trans.setToValue(0);
    	    trans.play();
    	    cancel_button.setDisable(true);
    	});
    	
    	
    	changeBackgroundButtonField();
    	changeDataButtonField();
    	
    	showText(CHALLENGEROOM, 15, Dialog.INFORMATION_WINDOW,5);
    	
    	
    }
    
    private void clickButtonLeft() {
		currentField--;
		if(currentField < Client.FIELD1)
			currentField = Client.FIELD3 ;
	}
	
    private void clickButtonRight() {
		currentField++;
		if(currentField > Client.FIELD3)
			currentField = Client.FIELD1 ;
	}
	public int getCurrentField() {
		return currentField;
	}
    
	@FXML
    void onClickLeftButton(ActionEvent event) {
    	if(isCancel_attive())
    	{
    		showText(Protocol.LEAVEWITHOUTCANCEL, 10, Dialog.ERROR_WINDOW,2);
    		return;
    	}
    	
    	clickButtonLeft();
    	changeBackgroundButtonField();
    	changeDataButtonField();
    }

    @FXML
    void onClickRightButton(ActionEvent event) {
    	if(isCancel_attive())
    	{
    		showText(Protocol.LEAVEWITHOUTCANCEL, 10, Dialog.ERROR_WINDOW,2);
    		return;
    	}
    	
    	clickButtonRight();
    	changeBackgroundButtonField();
    	changeDataButtonField();
    }
    
    private void changeDataButtonField() {
    	switch (getCurrentField()) {
		case Client.FIELD1:
			name_field.setText(Settings.FIELD1);
			break;
		case Client.FIELD2:
			name_field.setText(Settings.FIELD2);
			break;
		case Client.FIELD3:
			name_field.setText(Settings.FIELD3);
			break;
		}
    }
   
    private void changeBackgroundButtonField() {
    	
    	switch (getCurrentField()) {
		case Client.FIELD1:
			container_field.getStyleClass().remove("field2");
			container_field.getStyleClass().remove("field3");
			container_field.getStyleClass().add("field1");
			break;
		case Client.FIELD2:
			container_field.getStyleClass().remove("field1");
			container_field.getStyleClass().remove("field3");
			container_field.getStyleClass().add("field2");
			break;
		case Client.FIELD3:
			container_field.getStyleClass().remove("field1");
			container_field.getStyleClass().remove("field2");
			container_field.getStyleClass().add("field3");
			break;
		}
    	 FadeTransition trans = new FadeTransition(Duration.seconds(TIMETRANSACTION),container_field );
         trans.setFromValue(.20);
         trans.setToValue(1);
         trans.play();
    	
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
