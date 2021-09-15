package application.control;

import application.SceneHandler;
import application.Utilities;
import application.model.game.entity.Skin;
import application.model.game.handler.SkinHandler;
import application.net.client.Client;
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
    private Label information_label;
	
    @FXML
    private Button back_button_shop_page;

    @FXML
    private Button buy_coins_button_shop_page;


    @FXML
    private FlowPane box_skins;
    
    private static final String ShopTitle = "Shop";
    
    @FXML
    public void initialize() {
    	Client.getInstance().setCurrentState(Client.SHOP);
    	
    	back_button_shop_page.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 20));
    	buy_coins_button_shop_page.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 14));
    	
    	back_button_shop_page.setOnMouseEntered(new HoverButton());
    	back_button_shop_page.setOnMouseExited(new HoverButton());
    	
    	buy_coins_button_shop_page.setOnMouseEntered(new HoverButton());
    	buy_coins_button_shop_page.setOnMouseExited(new HoverButton());
    	
    	showText(ShopTitle, 40, Dialog.INFORMATION_WINDOW, 20);
    	 
    	init();
    }

    private void init() {
    	
		for(Skin skin : SkinHandler.getInstance().getSkins()) {
			Pair<Pane, Object> pair = SceneHandler.getInstance().loadPane("InformationSkinPane");
			SkinController skinController = (SkinController) pair.getValue() ;
			
			skinController.setName(skin.getName());
			if(!skin.isOwned())	
				skinController.setPrice(skin.getPrice());
			else 
				skinController.setPrice(SkinController.OWNED);
			skinController.setColor(skin.getColor());
			skinController.setOwn(skin.isOwned());
			box_skins.getChildren().add(pair.getKey());
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
