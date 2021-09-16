package application.control;

import application.Settings;
import application.Utilities;
import application.model.game.entity.Skin;
import application.model.game.handler.SkinHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class InformationMatchController {

	public static final int FIELD1 = 0 ;
	public static final int FIELD2 = 1 ;
	public static final int FIELD3 = 2 ;
	
	 @FXML
    private TextField home;

    @FXML
    private TextField guest;

    @FXML
    private BorderPane data_match;

    @FXML
    private Label name_field_label;

    @FXML
    private Label result_match_label;

    @FXML
    private Label date_label;

    @FXML
    private Label time_label;

    @FXML
    public void initialize() {
    	
    	home.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 18));
    	guest.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 18));
    	result_match_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 24));
    	date_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 9));
    	name_field_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 12));
    	time_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 9));
    	
    }
    	

    
    public static int getColorField(String color) {
    	if(color.equals(Settings.COLORFIELD1))
    		return FIELD1 ;
    	if(color.equals(Settings.COLORFIELD2))
    		return FIELD2;
    	return FIELD3 ;
    }

    
    public void setHome(String home,String color) {
    	
		this.home.setText(home);
		this.home.setStyle("-fx-background-color:"+color+";");
		
	}
    
    public void setGuest(String guest,String color) {
    	
		this.guest.setText(guest);
		this.guest.setStyle("-fx-background-color:"+color+";");
		
	}
    
    public void setResult(String result,String date,String time , String name_field ,int field) {
		this.result_match_label.setText(result);
		date_label.setText(date);
		time_label.setText(time);
		name_field_label.setText(name_field);
		switch (field) {
		case FIELD1:
			this.data_match.getStyleClass().add("field1");
			break;
		case FIELD2:
			this.data_match.getStyleClass().add("field2");
			break;
		case FIELD3:
			this.data_match.getStyleClass().add("field3");
			break;
		}
	}
    
 }
