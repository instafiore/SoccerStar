package application;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;

public class SceneHandler {

	
	private Scene scene = null ;
	private Stage stage = null;

	private HashMap<String, Pane> panes ;
	private HashMap<String, FXMLLoader> loaders ;
	
	private SceneHandler() {
		panes = new HashMap<String, Pane>();
		loaders = new HashMap<String ,FXMLLoader>();
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
	
	public FXMLLoader getLoader(String name) {
		return loaders.get(name);
	}
	
	public Pair<Pane, Object> loadPane(String namePane) {
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource(File.separator+"application"+File.separator+"view"+File.separator+namePane+".fxml")) ;
		Pane pane = null ;
		Object object = null ;
		try {
			pane = (Pane) loader.load(); 
			object = loader.getController() ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Pair<Pane, Object>(pane, object) ;
	}
	public void loadScene(String namePane , boolean resizable , boolean usingSceneBuilder) {
		
		Pane pane = null ;
		
		if(!usingSceneBuilder)
			pane = panes.get(namePane);
		else {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(File.separator+"application"+File.separator+"view"+File.separator+namePane+".fxml")) ;
			loaders.put(namePane, loader);
			try {
				pane = (Pane) loader.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		
		if(pane == null)
		{
			System.err.println("Pane null");
			return;
		}
		
		stage.hide();
		stage.setMinWidth(pane.getPrefWidth());
		stage.setMinHeight(pane.getPrefHeight()+30);
		stage.setWidth(pane.getPrefWidth());
		stage.setHeight(pane.getPrefHeight()+30);
		
		if(scene == null)
			scene = new Scene(pane, pane.getPrefWidth() , pane.getPrefHeight());
		else {
			scene.setRoot(pane);
		}
		
		stage.setScene(scene);
		stage.setResizable(resizable);
		stage.show();
		
	}
	
	
}
