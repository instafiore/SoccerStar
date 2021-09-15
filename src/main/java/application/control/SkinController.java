package application.control;



import application.Utilities;
import application.net.client.Client;
import application.net.common.Protocol;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

public class SkinController {
	
	public static final String OWNED = "OWNED" ;
	
    @FXML
    private Circle skin;

    @FXML
    private Label name;

    @FXML
    private Button buy_button;

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
    	buy_button.setText(price);
    	if(price.equals("0"))
		{
			setOwn(true);
			setPrice(OWNED);
		}
    }
    
    public void setColor(String color) {
    	skin.setFill(Color.web(color));
    }
    
    @FXML
    void onClickBuy_button(ActionEvent event) {
    	if(owned)
    	{
    		return ;
    	}
    	Client.getInstance().sendMessage(Protocol.BUYSKIN);
    	Client.getInstance().sendMessage(name.getText());
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
