package application;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;

public class SceneHandler {

	public static final int DEFAULT_CURSOR = 0 ;
	public static final int MATCH_CURSOR = 1 ;
	public static final int HOVER_BALL_CURSOR = 2 ;
	
	private Scene scene = null ;
	private Stage stage = null;

	private HashMap<String, Pane> panes ;
	private HashMap<String, FXMLLoader> loaders ;
	private Image match_cursor = null ;
	private Image hover_ball_cursor = null ;
	
	private SceneHandler() {
		panes = new HashMap<String, Pane>();
		loaders = new HashMap<String ,FXMLLoader>();
		match_cursor = new Image(getClass().getResourceAsStream("/application/view/matchCursor.png"),Settings.CURSORWIDTH , Settings.CURSORHEIGHT , true, true) ;
		hover_ball_cursor = new Image(getClass().getResourceAsStream("/application/view/tookBallCursor.png"),Settings.CURSORWIDTH , Settings.CURSORHEIGHT , true, true) ;
	}
	
	public Stage getStage() {
		return stage;
	}
	
	public static SceneHandler instance = null ;
	
	public static SceneHandler getInstance() {
		
		if(instance == null)
			instance = new SceneHandler() ;
		return instance;
	}
	
	public void initStage(Stage primaryStage) {
		stage = primaryStage ;
		Image icon = new Image(getClass().getResourceAsStream("/application/view/icon.png"));
	    stage.getIcons().add(icon);
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
			loaders.put(namePane, loader);
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
	
	
	public void setCursor(int cursor) {
		
		switch (cursor) {
		case DEFAULT_CURSOR:
			scene.setCursor(Cursor.DEFAULT);
			break;
		case MATCH_CURSOR:
			scene.setCursor(new ImageCursor(match_cursor));
			break;
		case HOVER_BALL_CURSOR:
			scene.setCursor(new ImageCursor(hover_ball_cursor));
			break;
		default:
			scene.setCursor(Cursor.DEFAULT);
			break;
		}
		
		
	}
	
}
