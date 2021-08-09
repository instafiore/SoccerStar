package application;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import application.control.MatchController;
import application.model.game.handler.MatchHandler;
import application.view.MatchView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApplication extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		MatchView root = new MatchView();
		MatchHandler ballManager = new MatchHandler();
		
		MatchController ballController = new MatchController(ballManager, root);
		
		root.addController(ballController);
		
		Scene scene = new Scene(root,Settings.WIDTHFRAME,Settings.HEIGHTFRAME);
	
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Balls javaFX");
//		primaryStage.setFullScreen(true);
		primaryStage.show();
		
		Updater updater = new Updater(ballController);
		
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
