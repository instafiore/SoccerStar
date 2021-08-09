package application.view;

import javafx.scene.image.Image;

public class Field extends application.model.game.entity.Field{

	private Image imgField = null ;
	
	public Field(double borderHorizontal, double borderVertical, double width, double height,Image imgField,double mu) {
		super(borderHorizontal, borderVertical, width, height,mu);
		this.imgField = imgField ;
	}
	
	public Image getImgField() {
		return imgField;
	}
	
}
