package application.control;

import application.SceneHandler;
import application.Utilities;
import application.net.client.Client;
import application.net.common.Protocol;
import application.view.Dialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class MenuMatchPaneController {

    @FXML
    private Button leave_button_match;

    @FXML
    public void initialize() {
    	leave_button_match.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 15));
    	leave_button_match.setOnMouseEntered(new HoverButton());
    	leave_button_match.setOnMouseExited(new HoverButton());
    }
    
    @FXML
    void OnClick_leave_button_match(ActionEvent event) {
    	
    	if(Dialog.getInstance().showConfirmDialog(Dialog.CONFIRMLEFT) != Dialog.YES)
    		return ;
    	
    	Client.getInstance().sendMessage(Protocol.LEFTGAME);
    	Client.getInstance().getCurrentMatch().setMatch_activated(false);
    	Client.getInstance().setCurrentMatch(null);
    	
    }

}
