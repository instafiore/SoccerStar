package application;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

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
		
		MatchController.getInstance().addMatchView(matchView);		
		matchView.addController(MatchController.getInstance());

		Updater.getInstance().addMatchController(MatchController.getInstance());
	
		
		Client.getInstance().connectToServer();
		Client.getInstance().startMatch();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
