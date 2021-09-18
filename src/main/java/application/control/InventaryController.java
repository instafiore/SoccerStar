package application.control;

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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.util.Pair;

public class InventaryController {


    @FXML
    private FlowPane box_skins;

    @FXML
    private Label label_balls;

    @FXML
    private FlowPane box_lineups;

    @FXML
    private Label label_lineups;

    @FXML
    private Button back_button_inventary_page;

    @FXML
    private Label information_label;
    
    private boolean ready = false ;
    
    public void setReady(boolean ready) {
		this.ready = ready;
	}
    
    public boolean isReady() {
		return ready;
    }
    
  private static final String InventaryTitle = "Inventary";
    
    @FXML
    public void initialize() {
    	Client.getInstance().setCurrentState(Client.INVENTARY);
    	
    	back_button_inventary_page.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 20));
    	label_balls.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 23));
    	label_lineups.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 23));
    	
    	
    	back_button_inventary_page.setOnMouseEntered(new HoverButton());
    	back_button_inventary_page.setOnMouseExited(new HoverButton());
    	
    	showText(InventaryTitle, 40, Dialog.INFORMATION_WINDOW, 20);
    	
    	Client.getInstance().sendMessage(Protocol.INFORMATIONINVENTARY);
    	Client.getInstance().sendMessage(Protocol.SKININUSE);
    	Client.getInstance().sendMessage(Protocol.LINEUPINUSE);
    }
    
    public void init() {
    	
    	box_lineups.getChildren().clear();
    	box_skins.getChildren().clear();
    	
		for(Skin skin : SkinHandler.getInstance().getSkinsOwned()) {
			Pair<Pane, Object> pair = SceneHandler.getInstance().loadPane("InformationSkinInventaryPane");
			SkinControllerInventary skinController = (SkinControllerInventary) pair.getValue() ;
			
			skinController.setName(skin.getName());
			skinController.setUsing(skin.isUsing());
			skinController.setColor(skin.getColor());
			box_skins.getChildren().add(pair.getKey());
		}
		
		for(Lineup lineup : LineupHandler.getInstance().getLineupsOwned()) {
			Pair<Pane, Object> pair = SceneHandler.getInstance().loadPane("InformationLineupInventaryPane");
			LineupControllerInventary lineupController = (LineupControllerInventary) pair.getValue() ;
			lineupController.setName(lineup.getName());
			lineupController.setUsing(lineup.isUsing());
			// TODO IMAGE
			
			box_lineups.getChildren().add(pair.getKey());
		}
	}
    
    public void showText(String text,int fontSize,String type,double duration) {
    	
    	if(!ready)
    		return ;
    	
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
