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
    private Label information_field;

    @FXML
    private Button left_button;

    @FXML
    private Button right_button;

    @FXML
    private Button cancel_button;
    
    @FXML
    private Label information_label;
    

    private static final String CHALLENGEROOM = "CHALLENGE ROOM" ;
    
    
    private static final double TIMETRANSACTION = 1.0 ;
    
  
    private int currentField = Client.FIELD1 ;
	
	
    
    private boolean cancel_attive = false ;
    
    
    

    @FXML
    public void initialize() {
    	
    	Client.getInstance().setCurrentState(Client.FRIENDLYBATTLE);
    	
    	name_field.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	information_field.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	cancel_button.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 20));
    	
    	left_button.setOnMouseEntered(new HoverButton());
    	left_button.setOnMouseExited(new HoverButton());
    	
    	right_button.setOnMouseEntered(new HoverButton());
    	right_button.setOnMouseExited(new HoverButton());
    	
    	
    	cancel_button.setOnAction(ev ->{
    		Client.getInstance().cancelRequest();
    		FadeTransition trans = new FadeTransition(Duration.seconds(1.5),cancel_button );
    		cancel_attive = false ;
    	    trans.setFromValue(0.5);
    	    trans.setToValue(0);
    	    trans.play();
    	    root.getChildren().remove(cancel_button);
    	});
    	
    	
    	changeBackgroundButtonField();
    	changeDataButtonField();
    	
    	showText(CHALLENGEROOM, 15, Dialog.INFORMATION_WINDOW,5);
    	
    	
    }
    
    public void clickButtonLeft() {
		currentField--;
		if(currentField < Client.FIELD1)
			currentField = Client.FIELD3 ;
	}
	
	public void clickButtonRight() {
		currentField++;
		if(currentField > Client.FIELD3)
			currentField = Client.FIELD1 ;
	}
	public int getCurrentField() {
		return currentField;
	}
    
	
	
	@FXML
    void onClickLeftButton(ActionEvent event) {
    	if(cancel_attive)
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
    	if(cancel_attive)
    	{
    		showText(Protocol.LEAVEWITHOUTCANCEL, 10, Dialog.ERROR_WINDOW,2);
    		return;
    	}
    	
    	clickButtonLeft();
    	changeBackgroundButtonField();
    	changeDataButtonField();
    }
    
    private void changeDataButtonField() {
    	switch (getCurrentField()) {
		case Client.FIELD1:
			name_field.setText(Settings.FIELD1);
			information_field.setText("price: "+Settings.PRICEFIELD1+"	reward: "+Settings.REWARDFIELD1);
			break;
		case Client.FIELD2:
			name_field.setText(Settings.FIELD2);
			information_field.setText("price: "+Settings.PRICEFIELD2+"	reward: "+Settings.REWARDFIELD2);
			break;
		case Client.FIELD3:
			name_field.setText(Settings.FIELD3);
			information_field.setText("price: "+Settings.PRICEFIELD3+"	reward: "+Settings.REWARDFIELD3);
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
