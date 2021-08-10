package application;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SceneHandler {

	
	private Scene scene ;
	private Stage stage ;

	private HashMap<String, Pane> panes ;
	
	private SceneHandler() {
		panes = new HashMap<String, Pane>();
	}
	
	public static SceneHandler instance = null ;
	
	public static SceneHandler getInstance() {
		
		if(instance == null)
			instance = new SceneHandler() ;
		return instance;
	}
	
	public void initStage(Stage primaryStage) {
		stage = primaryStage ;
	}
	
	// Using existingPane
	public void addPane(String namePane , Pane pane) {
		
		panes.put(namePane, pane);
		
	}
	
	// Using sceneBuilder
	public void loadPane(String namePane ) {
		
		FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource(File.separator+"application"+File.separator+"view"+File.separator+namePane+".fxml"));
		
		try {
			Pane pane = (Pane) fxmlLoader.load();
			panes.put(namePane, pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadScene(String namePane , boolean resizable ) {
		
		Pane pane = panes.get(namePane);
		
		if(pane == null)
		{
			System.out.println("Pane null");
			return;
		}
		
		scene = new Scene(pane, pane.getPrefWidth() , pane.getPrefHeight());
		
		stage.hide();
		stage.setScene(scene);
		stage.setResizable(resizable);
		
		stage.setMinWidth(scene.getWidth());
		stage.setMinHeight(scene.getHeight()+30);
		stage.setWidth(scene.getWidth());
		stage.setHeight(scene.getHeight()+30);
		
		stage.show();
		
	}
	

}