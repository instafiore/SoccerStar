package application.view;

import application.Settings;
import application.model.game.entity.GeneratorLineup;
import javafx.scene.image.Image;

public class Field extends application.model.game.entity.Field{

	private Image imgField = null ;
	
	public Field(String name , double borderHorizontal, double borderVertical, double width, double height,Image imgField) {
		super(name ,borderHorizontal, borderVertical, width, height,Settings.FRICTIONFIELD1);
		this.imgField = imgField ;
	}
	
	public Image getImgField() {
		return imgField;
	}
	
}
