package application.control;

import application.Settings;
import application.Utilities;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

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
    
    @FXML
    public void initialize() {
    	
    	home.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 18));
    	guest.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 18));
    	result.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 26));
    	
    }
    	
    public static int getColorPlayer(String color) {
    	
    	if(color.equals(Settings.COLOR1))
    		return COLOR1 ;
    	if(color.equals(Settings.COLOR2))
    		return COLOR2 ;
    	if(color.equals(Settings.COLOR3))
    		return COLOR3 ;
    	if(color.equals(Settings.COLOR4))
    		return COLOR4 ;
    	if(color.equals(Settings.COLOR5))
    		return COLOR5 ;
    	
    	return COLOR6 ;
    }
    
    public static int getColorField(String color) {
    	if(color.equals(Settings.COLORFIELD1))
    		return FIELD1 ;
    	if(color.equals(Settings.COLORFIELD2))
    		return FIELD2;
    	return FIELD3 ;
    }

    
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
