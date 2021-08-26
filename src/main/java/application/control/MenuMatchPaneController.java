package application.control;

import application.SceneHandler;
import application.net.client.Client;
import application.net.common.Protocol;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuMatchPaneController {

    @FXML
    private Button leave_button_match;

    @FXML
    void OnClick_leave_button_match(ActionEvent event) {
    	
    	Client.getInstance().sendMessage(Protocol.LEFTGAME);
    	Client.getInstance().getCurrentMatch().setMatch_activated(false);
    	Client.getInstance().setCurrentMatch(null);
    	
    }

}
