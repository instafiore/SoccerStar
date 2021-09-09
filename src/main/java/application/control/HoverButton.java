package application.control;

import javafx.animation.FadeTransition;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class HoverButton implements EventHandler<MouseEvent>{


    private static final double TIMETRANSACTION = 0.25 ;

   
	@Override
	public void handle(MouseEvent event) {
		
		Node button = (Node) event.getSource();

    
		FadeTransition trans = new FadeTransition(Duration.seconds(TIMETRANSACTION),button );
		
		if(event.getEventType().equals(MouseEvent.MOUSE_ENTERED)){
			
	         trans.setFromValue(1.0);
	         trans.setToValue(.50);
	         trans.play();
		}else {
			
	         trans.setFromValue(.50);
	         trans.setToValue(1);
	         trans.play();
			
		}
 
	}

}
