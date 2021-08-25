package application.control;

import application.net.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainPageController {

    @FXML
    private Button field_button_main_page;

    @FXML
    private Button leave_button_main_page;

    @FXML
    private Button account_button_main_page;

    @FXML
    private Button friends_button_main_page;

    @FXML
    private Button shop_button_main_page;

    @FXML
    void onClick_account_button_main_page(ActionEvent event) {

    }

    @FXML
    void onClick_field_button_main_page(ActionEvent event) {
    	Client.getInstance().startMatch();
    }

    @FXML
    void onClick_friends_button_main_page(ActionEvent event) {

    }

    @FXML
    void onClick_leave_button_main_page(ActionEvent event) {

    }

    @FXML
    void onClick_shop_button_main_page(ActionEvent event) {

    }

}
