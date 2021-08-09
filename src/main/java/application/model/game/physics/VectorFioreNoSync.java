package application.model.game.physics;

import java.util.Random;

public class VectorFioreNoSync {

	private Double x;
	private Double y;

	private Double magnitude;

	// Static functions
	public static VectorFioreNoSync add(VectorFioreNoSync v1, VectorFioreNoSync v2) {
		VectorFioreNoSync v3 = new VectorFioreNoSync(0.0, 0.0);
		v3.add(v1);
		v3.add(v2);
		return v3;
	}

	public static VectorFioreNoSync sub(VectorFioreNoSync v1, VectorFioreNoSync v2) {
		VectorFioreNoSync v3 = new VectorFioreNoSync(0.0, 0.0);
		v3.add(v1);
		v3.sub(v2);
		return v3;
	}

	protected static Double calcMag(Double x, Double y) {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}

	// Constructor
	public VectorFioreNoSync(Double x, Double y) {
		this.x = x;
		this.y = y;
		magnitude = calcMag(x, y);
	}

	public VectorFioreNoSync() {
		this.x = 0.0;
		this.y = 0.0;
		magnitude = calcMag(x, y);
	}
	
	public VectorFioreNoSync(Double magnitude) {
		Random r = new Random();
		x = r.nextDouble();
		y = r.nextDouble();
		if(r.nextBoolean())
			x *= -1 ;
		if(r.nextBoolean())
			y *= -1 ;
		this.magnitude = calcMag(x, y);
		this.setMag(magnitude);
	}

	// Not static functions
	public void setX(Double x) {
		this.x = x;
	}

	public Double getX() {
		return x;
	}

	public Double getY() {
		return y;
	}

	 public void setY(Double y) {
		this.y = y;
	}

	public Double getMagnitude() {
		return magnitude;
	}


	public VectorFioreNoSync  add(VectorFioreNoSync VectorFioreNoSync) {
		x += VectorFioreNoSync.x;
		y += VectorFioreNoSync.y;
		magnitude = calcMag(x, y);
		
		return this;
	}

	public VectorFioreNoSync sub(VectorFioreNoSync VectorFioreNoSync) {
		x -= VectorFioreNoSync.x;
		y -= VectorFioreNoSync.y;
		magnitude = calcMag(x, y);
		return this;
	}

	 public VectorFioreNoSync mult(Double m) {
		x *= m;
		y *= m;
		magnitude = calcMag(x, y);
		return this;
	 }

	 public VectorFioreNoSync multX(Double m) {
		x *= m;
		return this;
	}

	 public VectorFioreNoSync multY(Double m) {
		y *= m;
		return this;
	}

	 public VectorFioreNoSync div(Double d) {
		x /= d;
		y /= d;
		magnitude = calcMag(x, y);
		return this;
	}

	 public VectorFioreNoSync setMag(Double magnitude) {
		
		if(this.magnitude == 0)
			return this;
		 
		x = x * magnitude / this.magnitude;
		y = y * magnitude / this.magnitude;
		this.magnitude = magnitude;
		return this;
		
	}

	 public void limit(Double limit) {
		if (magnitude <= limit)
			return;
		this.setMag(limit);
	}

	 public void normalize() {
		this.setMag(1.0);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null || this.getClass() != obj.getClass())
			return true;
		VectorFioreNoSync v = (VectorFioreNoSync) obj;
		return x == v.x && y == v.y;
	}
	
	@Override
	public String toString() {
		return "("+x+","+y+")";
	}
}