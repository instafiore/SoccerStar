package application.view;

import java.io.InputStream;

import com.sun.scenario.effect.Effect;

import application.control.HoverButton;
import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Dialog {

	//Messages
	public static final String CONFIRMLOGOUT = "Are you sure you want to logout?";
	public static final String CONFIRMLEFT = "Are you sure you want to leave the match?";
	
	
	public static final String ERROR_WINDOW = "Error";
	public static final String INFORMATION_WINDOW = "An information";
	public static final String ATTENTION_WINDOW = "Attention";
	public static final int OK = 0;
	public static final int NO = 1;
	public static final int YES = 2;
	
	private static final String AREYOUSURE = "Are you sure?";
	private static final String NO_BUTTON = "NO";
	private static final String OK_BUTTON = "OK";
	private static final String YES_BUTTON = "YES";

	private static Dialog instance = null;
	private Stage stage = null;
	private Scene scene = null;
	private VBox root = null;
	private int res = - 1 ;
	private InputStream inputStreamFont = null ;
	
	public static Dialog getInstance() {
		if (instance == null)
			instance = new Dialog();
		return instance;
	}

	private Dialog() {
		root = new VBox();
		root.getStyleClass().add("pane");
		stage = new Stage();
		scene = new Scene(root);
		inputStreamFont =  this.getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf") ;
		scene.getStylesheets().add(getClass().getResource("/application/view/css/stylesheet1.css").toExternalForm());
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		
	}

	public int showConfirmDialog(String text_message) {
		res = -1;
		root.getChildren().clear();
		stage.setTitle(AREYOUSURE);
		
		Label textLabel = new Label(text_message);
		textLabel.setWrapText(true);
		textLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 14));
		textLabel.setTextFill(Color.web("#ffffff"));
		
		Button yes_button = new Button(YES_BUTTON);
		Button no_button = new Button(NO_BUTTON);
		
		yes_button.setOnMouseEntered(new HoverButton());
		yes_button.setOnMouseExited(new HoverButton());
		yes_button.setTextFill(Color.web("#ffffff"));
		
		no_button.getStyleClass().add("leave_button");
		no_button.setOnMouseEntered(new HoverButton());
		no_button.setOnMouseExited(new HoverButton());
		no_button.setTextFill(Color.web("#ffffff"));
		
		BorderPane borderPane = new BorderPane();
		
		HBox container = new HBox(no_button,yes_button);
		
		container.setMargin(no_button, new Insets(0,10,0,0));
		container.setMargin(yes_button, new Insets(0,10,0,0));
		
		borderPane.setRight(container);
		borderPane.setMargin(container, new Insets(0,0,10,0));
		
		yes_button.setOnAction(ev ->{
			res = YES ;
			stage.close();
		});
		
		no_button.setOnAction(ev ->{
			res = NO ;
			stage.close();
		});
		
		root.getChildren().add(textLabel);
		root.getChildren().add(borderPane);
		
		root.setMargin(textLabel, new Insets(40));
		
		stage.showAndWait();
		
		return res ;
	}
	
	
	public int showInformationDialog(String title ,String text_message) {
		
		stage.close();
		
		res = -1;
		root.getChildren().clear();
		stage.setTitle(title);

		Label textLabel = new Label(text_message);
		textLabel.setWrapText(true);
		textLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf"), 14));
		textLabel.setTextFill(Color.web("#ffffff"));
		
		Button ok_button = new Button(OK_BUTTON);
		BorderPane borderPane = new BorderPane();
		
		ok_button.setOnMouseEntered(new HoverButton());
		ok_button.setOnMouseExited(new HoverButton());
		ok_button.setTextFill(Color.web("#ffffff"));
		
		borderPane.setRight(ok_button);
		borderPane.setMargin(ok_button, new Insets(0, 10, 10, 0));
		
		root.getChildren().add(textLabel);
		root.getChildren().add(borderPane);
		
		root.setMargin(textLabel, new Insets(40));
		
		ok_button.setOnAction(ev -> {
			res = OK ;
			stage.close();
		});

		stage.showAndWait();
		
		return res;
	}
	
	
	
	public void drawText(AnchorPane glassPane , String text,double x,double y , String type , int font_size) {
	
//		x -= font_size / 3 * text.length()  ;
//	
//		String color = "" ;
//		
//		if(type.equals(ERROR_WINDOW)) {
//			color = "#ff1313" ;
//		}else if(type.equals(INFORMATION_WINDOW)) {
//			color = "#ffffff" ;
//		}else if(type.equals(ATTENTION_WINDOW)) {
//			color = "#ff5e28" ;
//		}
//		
//		
//		canvas.getGraphicsContext2D().setFont(Font.loadFont(inputStreamFont,font_size));
//		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//		canvas.getGraphicsContext2D().setFill(Color.web(color, 0.8));
//		canvas.getGraphicsContext2D().fillText(text, x, y);
		
		Label label = new Label(text);
		label.setFont(Font.loadFont(inputStreamFont, font_size));
		
		glassPane.getChildren().add(label);
		
		label.setLayoutX(x);
		label.setLayoutY(y);
		
		FadeTransition trans = new FadeTransition(Duration.seconds(1),label);
		
		trans.setFromValue(2.0);
        trans.setToValue(0);
        trans.play();
        
        glassPane.getChildren().remove(label);
        
  
	}
}
