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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.util.Pair;

public class ShopController {

	 @FXML
    private Button back_button_shop_page;

    @FXML
    private Label information_label;

    @FXML
    private Button buy_coins_button_shop_page;

    @FXML
    private FlowPane box_skins;

    @FXML
    private Label label_balls;

    @FXML
    private FlowPane box_lineups;

    @FXML
    private Label label_lineups;
    
    @FXML
    private Label coins;


    
    private static final String ShopTitle = "Shop";
    
    @FXML
    public void initialize() {
    	Client.getInstance().setCurrentState(Client.SHOP);
    	
    	coins.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 31));
    	back_button_shop_page.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 20));
    	buy_coins_button_shop_page.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 14));
    	label_balls.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 23));
    	label_lineups.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 23));
    	
    	
    	back_button_shop_page.setOnMouseEntered(new HoverButton());
    	back_button_shop_page.setOnMouseExited(new HoverButton());
    	
    	buy_coins_button_shop_page.setOnMouseEntered(new HoverButton());
    	buy_coins_button_shop_page.setOnMouseExited(new HoverButton());
    	
    	showText(ShopTitle, 40, Dialog.INFORMATION_WINDOW, 20);
    	
    	Client.getInstance().sendMessage(Protocol.INFORMATIONSHOP);

    }
    
    public void setCoins(String coins) {
		this.coins.setText(coins);
	}

    public void init() {
    	
    	box_lineups.getChildren().clear();
    	box_skins.getChildren().clear();
    	
		for(Skin skin : SkinHandler.getInstance().getSkins()) {
			Pair<Pane, Object> pair = SceneHandler.getInstance().loadPane("InformationSkinPane");
			SkinController skinController = (SkinController) pair.getValue() ;
			
			skinController.setName(skin.getName());
			skinController.setOwn(skin.isOwned());
			skinController.setPrice(skin.getPrice());
			skinController.setColor(skin.getColor());
			box_skins.getChildren().add(pair.getKey());
		}
		
		for(Lineup lineup : LineupHandler.getInstance().getLineups()) {
			Pair<Pane, Object> pair = SceneHandler.getInstance().loadPane("InformationLineupPane");
			LineupController lineupController = (LineupController) pair.getValue() ;
			
			lineupController.setName(lineup.getName());
			lineupController.setOwn(lineup.isOwned());
			lineupController.setPrice(lineup.getPrice());
			// TODO IMAGE
			
			box_lineups.getChildren().add(pair.getKey());
		}
	}

	@FXML
    void onClickBack_button_shop_page(ActionEvent event) {
		SceneHandler.getInstance().loadScene("MainPage", true, true);
    }

    @FXML
    void onClickBuy_coins_button_shop_page(ActionEvent event) {

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

}
