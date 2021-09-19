package application.control;

import application.SceneHandler;
import application.Utilities;
import application.model.game.entity.Lineup;
import application.model.game.handler.LineupHandler;
import application.model.game.handler.SkinHandler;
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

public class LineupControllerInventory {

	private static final String USING = "USING" ;
	private static final String USE = "USE" ;
	
    @FXML
    private Label name;

    @FXML
    private Button use_button;

    @FXML
    private ImageView image_lineup;
    
    private boolean using = false ;
    
    
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
    
    
    public void setName(String name) {
		this.name.setText(name);
	}
   

    @FXML
    void onClickUse_button(ActionEvent event) {
    	
    	InventoryController inventaryController = (InventoryController) SceneHandler.getInstance().getLoader("InventoryPage").getController()  ;
    	if(!inventaryController.isReady())
    		return ;
    	if(using)
    	{
    		inventaryController.showText(Protocol.ALREADYUSING, 20, Dialog.INFORMATION_WINDOW, 5);
    		return ;
    	}

    	int idLineup = LineupHandler.getInstance().getLineup(name.getText()).getId() ;
    	
    	Client.getInstance().sendMessage(Protocol.USETHISLINEUP);
    	Client.getInstance().sendMessage(""+idLineup);
    	
    	LineupHandler.getInstance().setUsing(idLineup);
    	
    	inventaryController.init();
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
