package application;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import application.control.ClientSucceedController;
import application.control.MatchController;
import application.model.game.handler.MatchHandler;
import application.net.client.Client;
import application.view.MatchView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class MainApplication extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		BorderPane mainMatchView = new BorderPane();
		
		mainMatchView.setPrefWidth(Settings.MATCHWIDTHFRAME);
		mainMatchView.setPrefHeight(Settings.MATCHHEIGHTFRAME);
		
		MatchView matchView = new MatchView();
		matchView.setPrefWidth(Settings.FIELDWIDTHFRAME);
		matchView.setPrefHeight(Settings.FIELDHEIGHTFRAME);
			
		FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource(File.separator+"application"+File.separator+"view"+File.separator+"MenuMatchPane.fxml"));
		
		Pane menuMatchPane = (Pane) fxmlLoader.load(); 
		
		mainMatchView.setTop(menuMatchPane);
		mainMatchView.setCenter(matchView);
		
		SceneHandler.getInstance().initStage(primaryStage);
		SceneHandler.getInstance().addPane("MatchView", mainMatchView);
		SceneHandler.getInstance().loadPane("LoginPage");
		SceneHandler.getInstance().loadPane("RegistrationPage");
		SceneHandler.getInstance().loadPane("MainPage");
		SceneHandler.getInstance().loadScene("LoginPage", false);

		
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
