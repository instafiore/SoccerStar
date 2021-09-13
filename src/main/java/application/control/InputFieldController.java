package application.control;

import application.Utilities;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class InputFieldController implements EventHandler<KeyEvent>{

	public static final int NOCONTROL = 0 ;
	public static final int USERNAME = 1 ;
	public static final int PASSWORD = 2 ;
	public static final int EMAIL = 3 ;
	
	private int type = NOCONTROL ;
	
	
	public InputFieldController(int type) {
		super();
		this.type = type;
	}

	public InputFieldController() {}

	@Override
	public void handle(KeyEvent event) {
		
		
		TextField textField = (TextField) event.getSource() ;
		
		String text = textField.getText() ;
		
		if(event.getEventType().equals(KeyEvent.KEY_RELEASED)) {
			
			if(text.equals("")) {
				setNothing(textField);
				return ;
			}
			
			switch (type) {
			case NOCONTROL:
				setGreen(textField);
				break;
			case USERNAME:
				if(!Utilities.ruleUsernameRespected(text))	
					setRed(textField);
				else
					setGreen(textField);
				break;
			case PASSWORD:
				if(!Utilities.rulePasswordRespected(text))	
					setRed(textField);
				else
					setGreen(textField);
				break;
			case EMAIL:
				if(!Utilities.ruleEmailRespected(text))	
					setRed(textField);
				else
					setGreen(textField);
				break;
			}
			
		}
	}
	
	
	private void setRed(TextField textField) {
		textField.getStyleClass().remove("input_field_red");
		textField.getStyleClass().remove("input_field_green");
		textField.getStyleClass().add("input_field_red");
	}

	private void setGreen(TextField textField) {
		textField.getStyleClass().remove("input_field_red");
		textField.getStyleClass().remove("input_field_green");
		textField.getStyleClass().add("input_field_green");
	}
	
	private void setNothing(TextField textField) {
		textField.getStyleClass().remove("input_field_red");
		textField.getStyleClass().remove("input_field_green");
	}
}
