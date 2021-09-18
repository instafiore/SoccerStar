package application.control;



import application.SceneHandler;
import application.Utilities;
import application.net.client.Client;
import application.net.common.Protocol;
import application.view.Dialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

public class SkinControllerShop {
	
	public static final String OWNED = "OWNED" ;
	
    @FXML
    private Circle skin;

    @FXML
    private Label name;

    @FXML
    private Button buy_button;
    
    private String color = "" ;

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
		buy_button.setText(OWNED);
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
    
    public void setColor(String color) {
    	this.color = color ;
    	skin.setFill(Color.web(color));
    }
    
    @FXML
    void onClickBuy_button(ActionEvent event) {
    	ShopController shopController = null ;
    	if(owned)
    	{
    		shopController = (ShopController) SceneHandler.getInstance().getLoader("ShopPage").getController() ;
    		shopController.showText(Protocol.ALREADYOWNED, 20, Dialog.INFORMATION_WINDOW, 5);
    		return ;
    	}
    	if(!shopController.isReady())
    		return ;
    	Client.getInstance().sendMessage(Protocol.BUYSKIN);
    	String text = name.getText() + Protocol.DELIMITERINFORMATIONELEMENTSHOP + buy_button.getText() + Protocol.DELIMITERINFORMATIONELEMENTSHOP + color ;  ;
    	Client.getInstance().sendMessage(text);
    }

    @FXML
    void onMouseEnteredCircle(MouseEvent event) {
    	skin.setScaleX(1.2);
    	skin.setScaleY(1.2);
    }

    @FXML
    void onMouseExitedCircle(MouseEvent event) {
    	skin.setScaleX(1);
    	skin.setScaleY(1);
    }

}
