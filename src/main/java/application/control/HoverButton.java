package application.control;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class HoverButton implements EventHandler<MouseEvent>{

    private static final String GREEN_BUTTON_HOVER = "#009a00" ;
    private static final String RED_BUTTON_HOVER = "#ff0000" ;
    
    private static final int TIMETRANSACTION = 10 ;
    private static final int STEPTRANSATION = 10 ;
	
    private String colorHover = GREEN_BUTTON_HOVER ;
    
    public static final int COMMONBUTTON = 0 ;
    public static final int LEAVEBUTTON = 1 ;
    
    public HoverButton() {}
    
    public HoverButton(int typeOfButton) {
    	switch (typeOfButton) {
		case COMMONBUTTON:
			colorHover = GREEN_BUTTON_HOVER ;
			break;
		case LEAVEBUTTON:
			colorHover = RED_BUTTON_HOVER ;
			break;
		default:
			colorHover = GREEN_BUTTON_HOVER ;
			break;
		}
    }
    
	@Override
	public void handle(MouseEvent event) {
		
		Button button = (Button) event.getSource();
		
		double opacity = 1 ;
    	double factor = 0.1 ;
		
		if(event.getEventType().equals(MouseEvent.MOUSE_ENTERED)){
			
			opacity = 1 ;
			factor = 0.05 ;
			
	    	for(int i = 0 ; i < STEPTRANSATION ;++i)
	    	{
	    		try {
					Thread.sleep(TIMETRANSACTION);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		button.setStyle("{"
	    				+ "-fx-background-color: "+colorHover+";"
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
	    				+ "-fx-background-color: "+colorHover+";"
	    				+ "-fx-opacity: "+opacity+";"
	    				+ "}");
	    		
	    		opacity += factor ;
	    		
	    	}
			
		}
 
	}

}
