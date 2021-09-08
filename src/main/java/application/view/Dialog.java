package application.view;

import javax.naming.spi.DirStateFactory.Result;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Dialog {

	public static final String ERROR_WINDOW = "Error";
	public static final String INFORMATION_WINDOW = "An information";
	public static final int OK = 0;
	
	private static final int MARGININFORMATIONDIALOG = 40 ;
	private static final String OK_BUTTON = "OK";

	private static Dialog instance = null;
	private Stage stage = null;
	private Scene scene = null;
	private VBox root = null;
	private int res = - 1 ;
	
	public static Dialog getInstance() {
		if (instance == null)
			instance = new Dialog();
		return instance;
	}

	private Dialog() {
		root = new VBox();
		stage = new Stage();
		scene = new Scene(root);
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
	}

	public int showInformationDialog(String title ,String text_message) {
		
		res = -1;
		root.getChildren().clear();
		stage.setTitle(title);

		Label textLabel = new Label(text_message);
		Button ok_button = new Button(OK_BUTTON);
		BorderPane borderPane = new BorderPane();
		
		borderPane.setRight(ok_button);
		
		root.getChildren().add(textLabel);
		root.getChildren().add(borderPane);
		
		root.setMargin(textLabel, new Insets(MARGININFORMATIONDIALOG));
		
		ok_button.setOnAction(ev -> {
			res = OK ;
			stage.close();
		});

		stage.showAndWait();
		
		return res;
	}
}
