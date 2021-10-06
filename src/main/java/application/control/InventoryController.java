package application.control;

import java.util.ArrayList;

import application.SceneHandler;
import application.Utilities;
import application.model.game.entity.Lineup;
import application.model.game.entity.Skin;
import application.model.game.handler.LineupHandler;
import application.model.game.handler.SkinHandler;
import application.net.client.Client;
import application.net.common.Protocol;
import application.view.Dialog;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.util.Pair;

public class InventoryController {


    @FXML
    private VBox vbox;

    @FXML
    private BorderPane balls_sector;

    @FXML
    private FlowPane box_skins;

    @FXML
    private Label label_balls;

    @FXML
    private BorderPane lineups_sector;

    @FXML
    private FlowPane box_lineups;

    @FXML
    private Label label_lineups;

    @FXML
    private Button back_button_inventary_page;

    @FXML
    private Label information_label;
    
    private ProgressIndicator progressindicator ;
    
    private boolean ready = false ;
    private boolean firstTime = true ;
    
    public void setReady(boolean ready) {
		this.ready = ready;
	}
    
    public boolean isReady() {
		return ready;
    }
    
  private static final String InventaryTitle = "Inventory";
  
  
    
    @FXML
    public void initialize() {
    	Client.getInstance().setCurrentState(Client.INVENTORY);
    	
    	progressindicator = new ProgressIndicator() ;
    	
    	progressindicator.setMinSize(400, 200);
    	
    	back_button_inventary_page.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 20));
    	label_balls.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 23));
    	label_lineups.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 23));
    	
    	
    	back_button_inventary_page.setOnMouseEntered(new HoverButton());
    	back_button_inventary_page.setOnMouseExited(new HoverButton());
    	
    	showText(InventaryTitle, 40, Dialog.INFORMATION_WINDOW, 20);
    
    	vbox.getChildren().remove(balls_sector);
    	vbox.getChildren().remove(lineups_sector);
    	vbox.getChildren().add(progressindicator);
    	
    	Client.getInstance().sendMessage(Protocol.INFORMATIONINVENTORY);
    	Client.getInstance().sendMessage(Protocol.SKININUSE);
    	Client.getInstance().sendMessage(Protocol.LINEUPINUSE);
    	Client.getInstance().sendMessage(Protocol.IMAGESLINEUPINVENTORY);
    }
    
    public void init() {
    	
    	
    	
    	box_lineups.getChildren().clear();
    	box_skins.getChildren().clear();
    	
    	
    	double i = 0 ; 
    	double perc ;
    	ArrayList<Skin> skinsOwned = SkinHandler.getInstance().getSkinsOwned() ; 
		for(Skin skin : skinsOwned) {
			Pair<Pane, Object> pair = SceneHandler.getInstance().loadPane("InformationSkinInventoryPane");
			SkinControllerInventory skinController = (SkinControllerInventory) pair.getValue() ;
			
			skinController.setName(skin.getName());
			skinController.setUsing(skin.isUsing());
			skinController.setColor(skin.getColor());
			box_skins.getChildren().add(pair.getKey());
			perc = i++ / skinsOwned.size() / 2 ;
			progressindicator.setProgress(perc);
		}
		
		ArrayList<Lineup> lineupOwned = LineupHandler.getInstance().getLineupsOwned() ;
		perc = 0 ;
		for(Lineup lineup : lineupOwned ) {
			Pair<Pane, Object> pair = SceneHandler.getInstance().loadPane("InformationLineupInventoryPane");
			LineupControllerInventory lineupController = (LineupControllerInventory) pair.getValue() ;
			lineupController.setName(lineup.getName());
			lineupController.setUsing(lineup.isUsing());
			lineupController.setImage(Utilities.getImageFromByteArray(lineup.getImage(), 143, 84));
			box_lineups.getChildren().add(pair.getKey());
			perc = i++ / lineupOwned.size() / 2 + 0.5;
			progressindicator.setProgress(perc);
		}
		
		if(firstTime) {
			vbox.getChildren().remove(progressindicator);
			vbox.getChildren().add(balls_sector);
	    	vbox.getChildren().add(lineups_sector);
	    	firstTime = false; 
		}
		
	}
    
    public void showText(String text,int fontSize,String type,double duration) {
    	
    	
    	String color = "" ;
		
		if(type.equals(Dialog.ERROR_WINDOW)) {
			color = "#ff1313" ;
		}else if(type.equals(Dialog.INFORMATION_WINDOW)) {
			color = "#ffffff" ;
		}else if(type.equals(Dialog.ATTENTION_WINDOW)) {
			color = "#ff5e28" ;
		}
    	
    	information_label.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), fontSize));
    	information_label.setText(text);
    	information_label.setTextFill(Color.web(color, 1));
    
    	FadeTransition trans = new FadeTransition(Duration.seconds(duration),information_label);
		
		trans.setFromValue(1.0);
        trans.setToValue(0.0);
        trans.play();
    }
    
    
    

    @FXML
    void onClickBack_button_inventary_page(ActionEvent event) {
    	if(!ready)
    		return ;
    	SceneHandler.getInstance().loadScene("MainPage", true, true);
    }

   

}
