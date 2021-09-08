package application.control;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class HoverCommonButton implements EventHandler<MouseEvent>{

    private static final String GREEN_BUTTON_HOVER = "#009a00" ;
    private static final int TIMETRANSACTION = 10 ;
    
    private static final int STEPTRANSATION = 5 ;
	
	@Override
	public void handle(MouseEvent event) {
		
		Button button = (Button) event.getSource();
		
		double opacity = 1 ;
    	double factor = 0.1 ;
		
		if(event.getEventType().equals(MouseEvent.MOUSE_ENTERED)){
			
			opacity = 1 ;
			factor = 0.1 ;
			
	    	for(int i = 0 ; i < STEPTRANSATION ;++i)
	    	{
	    		try {
					Thread.sleep(TIMETRANSACTION);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		button.setStyle("{"
	    				+ "-fx-background-color: #009a00;"
	    				+ "-fx-opacity: "+opacity+";"
	    				+ "}");
	    		
	    		opacity -= factor ;
	    		
	    	}
		}else {
			
			opacity = 0.5 ;
			factor = 0.1 ;
			
	    	for(int i = 0 ; i < STEPTRANSATION ;++i)
	    	{
	 
	    		try {
					Thread.sleep(TIMETRANSACTION);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		button.setStyle("{"
	    				+ "-fx-background-color: #009a00;"
	    				+ "-fx-opacity: "+opacity+";"
	    				+ "}");
	    		
	    		opacity += factor ;
	    		
	    	}
			
		}
 
	}

}
