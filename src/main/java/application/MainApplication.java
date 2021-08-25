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
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApplication extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		MatchView matchView = new MatchView();
		matchView.setPrefWidth(Settings.WIDTHFRAME);
		matchView.setPrefHeight(Settings.HEIGHTFRAME);
			
		SceneHandler.getInstance().initStage(primaryStage);
		
		SceneHandler.getInstance().addPane("MatchView", matchView);
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
