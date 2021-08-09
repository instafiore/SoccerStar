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
		MatchView matchView = new MatchView();

		MatchController.getInstance().addMatchView(matchView);
		
		matchView.addController(MatchController.getInstance());
		
		Scene scene = new Scene(matchView,Settings.WIDTHFRAME,Settings.HEIGHTFRAME);
	
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Soccer star");
//		primaryStage.setFullScreen(true);
		primaryStage.show();
		
		Updater updater = new Updater(MatchController.getInstance());
		
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
