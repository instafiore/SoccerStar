package application.control;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class InformationMatchController {

	public static final int COLOR1 = 0 ;
	public static final int COLOR2 = 1 ;
	public static final int COLOR3 = 2 ;
	public static final int COLOR4 = 3 ;
	public static final int COLOR5 = 4 ;
	public static final int COLOR6 = 5 ;
	
	public static final int FIELD1 = 0 ;
	public static final int FIELD2 = 1 ;
	public static final int FIELD3 = 2 ;
	
    @FXML
    private TextField home;

    @FXML
    private TextField result;

    @FXML
    private TextField guest;

    
    public void setHome(String home,int color) {
		this.home.setText(home);
		
		switch (color) {
		case COLOR1:
			this.home.getStyleClass().add("color1");
			break;
		case COLOR2:
			this.home.getStyleClass().add("color2");
			break;
		case COLOR3:
			this.home.getStyleClass().add("color3");
			break;
		case COLOR4:
			this.home.getStyleClass().add("color4");
			break;
		case COLOR5:
			this.home.getStyleClass().add("color5");
			break;
		case COLOR6:
			this.home.getStyleClass().add("color6");
			break;
		}
	}
    
    public void setGuest(String guest,int color) {
		this.guest.setText(guest);
		
		switch (color) {
		case COLOR1:
			this.guest.getStyleClass().add("color1");
			break;
		case COLOR2:
			this.guest.getStyleClass().add("color2");
			break;
		case COLOR3:
			this.guest.getStyleClass().add("color3");
			break;
		case COLOR4:
			this.guest.getStyleClass().add("color4");
			break;
		case COLOR5:
			this.guest.getStyleClass().add("color5");
			break;
		case COLOR6:
			this.guest.getStyleClass().add("color6");
			break;
		}
	}
    
    public void setResult(String result,int field) {
		this.result.setText(result);
		switch (field) {
		case FIELD1:
			this.result.getStyleClass().add("field1");
			break;
		case FIELD2:
			this.result.getStyleClass().add("field2");
			break;
		case FIELD3:
			this.result.getStyleClass().add("field3");
			break;
		}
	}
    
 }
