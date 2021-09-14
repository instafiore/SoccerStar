package application.control;

import java.util.List;

import application.SceneHandler;
import application.Utilities;
import application.model.game.entity.DataMatch;
import application.model.game.entity.MatchChart;
import application.net.client.Client;
import application.net.common.Protocol;
import application.view.Dialog;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.util.Pair;

public class HistoryController {

    @FXML
    private BorderPane root;
	
    @FXML
    private Button back_button_history;

    @FXML
    private Label information_label;

    @FXML
    private VBox box_history_match;

    @FXML
    private ScrollPane scroll_pane;
    
    @FXML
    private BorderPane boxChartMatches;
    
    private static final String HistoryTitle = "History" ;

    @FXML
    public void initialize() {
    	Client.getInstance().setCurrentState(Client.HISTORY);
    	
    	back_button_history.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 20));
    	information_label.setFont(Font.loadFont(getClass().getResourceAsStream(Utilities.getInstance().getPathFont()), 20));

    	back_button_history.setOnMouseEntered(new HoverButton());
    	back_button_history.setOnMouseExited(new HoverButton());
    	
    	scroll_pane.setFitToWidth(true);
    	scroll_pane.setHbarPolicy(ScrollBarPolicy.NEVER);
    	
    	showText(HistoryTitle, 40, Dialog.INFORMATION_WINDOW, 180);
    	
    	Client.getInstance().sendMessage(Protocol.INFORMATIONHISTORY);
    }
    
    
    public void init(List<DataMatch> dataMatches) {
    	
    	for(DataMatch dataMatch : dataMatches) {
    		Pair<Pane, Object> pair = SceneHandler.getInstance().loadPane("InformationMatchPane") ;
    		Pane pane = pair.getKey();
    		InformationMatchController informationMatchController = (InformationMatchController) pair.getValue() ;
    		String home = dataMatch.getHome() ;
    		String guest = dataMatch.getGuest() ;
    		if(home.equals(Client.getInstance().getUsername()))
    		{
    			informationMatchController.setHome(home, InformationMatchController.getColorPlayer(dataMatch.getColorHome()));
    			informationMatchController.setGuest(guest, InformationMatchController.getColorPlayer(dataMatch.getColorGuest()));
    			informationMatchController.setResult(dataMatch.getResult(), InformationMatchController.getColorField(dataMatch.getColorField()));
    			System.out.println(dataMatch.getColorField());
    		}else {
    			informationMatchController.setHome(guest, InformationMatchController.getColorPlayer(dataMatch.getColorGuest()));
    			informationMatchController.setGuest(home, InformationMatchController.getColorPlayer(dataMatch.getColorHome()));
    			informationMatchController.setResult(dataMatch.getResultReversed(), InformationMatchController.getColorField(dataMatch.getColorField()));
    		}

    		box_history_match.getChildren().add(pane);
    	}
    	
    	CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
    	MatchChart matchChart = new MatchChart(Client.getInstance().getUsername(), xAxis, yAxis, dataMatches) ;
    	
    	boxChartMatches.setCenter(matchChart);
    }
    
    @FXML
    void onClickBack_button_history(ActionEvent event) {
    	SceneHandler.getInstance().loadScene("MainPage", true, true);
    }
    
    @FXML
    void onMouseEnteredBoxChartMatch(MouseEvent event) {
    	FadeTransition trans = new FadeTransition(Duration.seconds(2),boxChartMatches);
		
		trans.setFromValue(0.85);
        trans.setToValue(1);
        trans.play();
    }

    @FXML
    void onMouseEnteredBoxMatches(MouseEvent event) {
    	FadeTransition trans = new FadeTransition(Duration.seconds(2),box_history_match);
		
		trans.setFromValue(0.85);
        trans.setToValue(1);
        trans.play();
    }

    @FXML
    void onMouseExitedBoxChartMatch(MouseEvent event) {
    	FadeTransition trans = new FadeTransition(Duration.seconds(2),boxChartMatches);
		
		trans.setFromValue(1.0);
        trans.setToValue(0.85);
        trans.play();
    }

    @FXML
    void onMouseExitedBoxMatches(MouseEvent event) {
    	FadeTransition trans = new FadeTransition(Duration.seconds(2),box_history_match);
		
		trans.setFromValue(1.0);
        trans.setToValue(0.85);
        trans.play();
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
