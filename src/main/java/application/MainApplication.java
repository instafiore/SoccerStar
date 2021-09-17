package application;

import java.io.File;
import application.control.ClientSucceedController;
import application.control.MatchController;
import application.control.WindowController;
import application.net.client.Client;
import application.net.server.Mail;
import application.view.MatchView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class MainApplication extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		BorderPane mainMatchView = new BorderPane();
		
		mainMatchView.setPrefWidth(Settings.MATCHWIDTHFRAME);
		mainMatchView.setPrefHeight(Settings.MATCHHEIGHTFRAME);
		
		MatchView matchView = new MatchView();
		matchView.setPrefWidth(Settings.FIELDWIDTHFRAME);
		matchView.setPrefHeight(Settings.FIELDHEIGHTFRAME);
		
		Pane menuMatchPane = (Pane) SceneHandler.getInstance().loadPane("MenuMatchPane").getKey(); 
		
		mainMatchView.setTop(menuMatchPane);
		mainMatchView.setCenter(matchView);
		primaryStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new WindowController());
		
		SceneHandler.getInstance().initStage(primaryStage);
		SceneHandler.getInstance().addPane("MatchView", mainMatchView);
		SceneHandler.getInstance().loadScene("LoginPage", false , true);

		
		MatchController.getInstance().addMatchView(matchView);		
		matchView.addController(MatchController.getInstance());

				
		Updater.getInstance().addMatchController(MatchController.getInstance());
		
		Client.getInstance().connectToServer();
		Client.getInstance().setCurrentState(Client.STEP_LOGIN);
		Client.getInstance().setOnSucceeded(new ClientSucceedController());
		
		
		
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
