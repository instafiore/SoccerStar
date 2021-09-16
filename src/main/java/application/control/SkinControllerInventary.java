package application.control;

import application.SceneHandler;
import application.Utilities;
import application.model.game.entity.Skin;
import application.model.game.handler.SkinHandler;
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

public class SkinControllerInventary {

	public static final String USING = "USING" ;
	public static final String USE = "USE" ;
	
    @FXML
    private Circle skin;

    @FXML
    private Label name;

    @FXML
    private Button use_button;
    
    private String color = "" ;
    
    private boolean using = false;
    
    @FXML
    public void initialize() {
    	name.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 16));
    	use_button.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 13));
    	
    	use_button.setOnMouseEntered(new HoverButton());
    	use_button.setOnMouseExited(new HoverButton());
    	
    }
    
    public void setUsing(boolean using) {
		this.using = using;
		if(using)
			use_button.setText(USING);
		else
			use_button.setText(USE);
	}
    
    public void setColor(String color) {
    	this.color = color ;
    	skin.setFill(Color.web(color));
    }
    
    public void setName(String name) {
		this.name.setText(name);
	}
    

    @FXML
    void onClickUse_button(ActionEvent event) {
    	InventaryController inventaryController = (InventaryController) SceneHandler.getInstance().getLoader("InventaryPage").getController() ;
    	if(using)
    	{
    		inventaryController.showText(Protocol.ALREADYUSING, 20, Dialog.INFORMATION_WINDOW, 5);
    		return ;
    	}
    	
    	Client.getInstance().sendMessage(Protocol.USETHISSKIN);
       	Client.getInstance().sendMessage(color);
       	
       	SkinHandler.getInstance().setUsing(name.getText());
       	
       	inventaryController.init();
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
