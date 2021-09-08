package application.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Dialog {

	//Messages
	public static final String CONFIRMLOGOUT = "Are you sure you want to logout?";
	public static final String CONFIRMLEFT = "Are you sure you want to leave the match?";
	
	
	public static final String ERROR_WINDOW = "Error";
	public static final String INFORMATION_WINDOW = "An information";
	public static final int OK = 0;
	public static final int NO = 1;
	public static final int YES = 2;
	
	private static final String AREYOUSURE = "Are you sure?";
	private static final String NO_BUTTON = "NO";
	private static final String OK_BUTTON = "OK";
	private static final String YES_BUTTON = "YES";

	
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

	public int showConfirmDialog(String text_message) {
		res = -1;
		root.getChildren().clear();
		stage.setTitle(AREYOUSURE);
		
		Label textLabel = new Label(text_message);
		textLabel.setWrapText(true);
		
		Button yes_button = new Button(YES_BUTTON);
		Button no_button = new Button(NO_BUTTON);
		BorderPane borderPane = new BorderPane();
		
		HBox container = new HBox(no_button,yes_button);
		
		container.setMargin(no_button, new Insets(0,10,0,0));
		container.setMargin(yes_button, new Insets(0,10,0,0));
		
		borderPane.setRight(container);
		borderPane.setMargin(container, new Insets(0,0,10,0));
		
		yes_button.setOnAction(ev ->{
			res = YES ;
			stage.close();
		});
		
		no_button.setOnAction(ev ->{
			res = NO ;
			stage.close();
		});
		
		root.getChildren().add(textLabel);
		root.getChildren().add(borderPane);
		
		root.setMargin(textLabel, new Insets(40));
		
		stage.showAndWait();
		
		return res ;
	}
	
	
	public int showInformationDialog(String title ,String text_message) {
		
		res = -1;
		root.getChildren().clear();
		stage.setTitle(title);

		Label textLabel = new Label(text_message);
		textLabel.setWrapText(true);
		Button ok_button = new Button(OK_BUTTON);
		BorderPane borderPane = new BorderPane();
		
		borderPane.setRight(ok_button);
		borderPane.setMargin(ok_button, new Insets(0, 10, 10, 0));
		
		root.getChildren().add(textLabel);
		root.getChildren().add(borderPane);
		
		root.setMargin(textLabel, new Insets(40));
		
		ok_button.setOnAction(ev -> {
			res = OK ;
			stage.close();
		});

		stage.showAndWait();
		
		return res;
	}
}
