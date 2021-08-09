package application.model.game.physics;

public class VelocityNoSync extends VectorFioreNoSync{

	public VelocityNoSync(Double magnitude) {
		super(magnitude);	
	}
	
	public VelocityNoSync(Double x , Double y) {
		super(x,y);
	}
	
	
	public VelocityNoSync getInPixelPerMillisecond() {
		
		VelocityNoSync v = new VelocityNoSync(this.getX(),this.getY());
		
		v.div(1000.0);
		
		return v ;
	}

}
