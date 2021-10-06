package application.control;

import application.Utilities;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Font;

public class LoadingController {

    @FXML
    private ProgressIndicator loading_bar;

    @FXML
    private Label soccer_start_label;

    
    @FXML
    public void initialize() {
    	soccer_start_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 46));
    }
    
    public ProgressIndicator getLoading_bar() {
		return loading_bar;
	}
}
