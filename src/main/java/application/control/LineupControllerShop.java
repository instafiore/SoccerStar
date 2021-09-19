package application.control;

import application.SceneHandler;
import application.Utilities;
import application.model.game.entity.Lineup;
import application.model.game.handler.LineupHandler;
import application.model.game.handler.SoundHandler;
import application.net.client.Client;
import application.net.common.Protocol;
import application.view.Dialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class LineupControllerShop {

	public static final String OWNED = "OWNED" ;
	
    @FXML
    private Label name;

    @FXML
    private Button buy_button;

    @FXML
    private ImageView image_lineup;
    
    private boolean owned = false;
    
    @FXML
    public void initialize() {
    	name.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 16));
    	buy_button.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 13));
    	
    	buy_button.setOnMouseEntered(new HoverButton());
    	buy_button.setOnMouseExited(new HoverButton());
    	
    }
    
    public void setOwn(boolean own) {
		this.owned = own;
	}
    
    
    
    public void setName(String name) {
		this.name.setText(name);
	}
    
    public void setPrice(String price) {
    	if(owned) {
    		buy_button.setText(OWNED);
    		return ;
    	}
    	buy_button.setText(price);
    	if(price.equals("0"))
		{
			setOwn(true);
			buy_button.setText(OWNED);
		}
    }
    
    @FXML
    void onClickBuy_button(ActionEvent event) {
    	SoundHandler.getInstance().startHit();
    	
    	ShopController shopController = (ShopController) SceneHandler.getInstance().getLoader("ShopPage").getController()  ;
    	if(owned)
    	{
    		shopController.showText(Protocol.ALREADYOWNED, 20, Dialog.INFORMATION_WINDOW, 5);
    		return ;
    	}
    	if(!shopController.isReady())
    		return ;
    	Lineup lineup = LineupHandler.getInstance().getLineup(name.getText());
    	String text = lineup.getId() + Protocol.DELIMITERINFORMATIONELEMENTSHOP + lineup.getName() + Protocol.DELIMITERINFORMATIONELEMENTSHOP + lineup.getPrice() + Protocol.DELIMITERINFORMATIONELEMENTSHOP + lineup.getImage() ;
    	Client.getInstance().sendMessage(Protocol.BUYLINEUP);
    	Client.getInstance().sendMessage(text);
    }

    @FXML
    void onMouseEnteredImage(MouseEvent event) {
    	image_lineup.setScaleX(1.2);
    	image_lineup.setScaleY(1.2);
    }
   
    @FXML
    void onMouseExitedImage(MouseEvent event) {
    	image_lineup.setScaleX(1);
    	image_lineup.setScaleY(1);
    }

}
