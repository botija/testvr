package com.botijasoftware.utils.collision;

import com.botijasoftware.utils.Vector3;

public class CollisionPoint extends CollisionVolume {
	private Vector3 point;
	
	public CollisionPoint(Vector3 point) {
		this.point = point;
	}
	
	public boolean collides(CollisionVolume c) {
		return c.collides(point);
	}
	
	public boolean collides(Sphere s) {
		return s.collides(point);
	}
	
	
	public boolean collides(Capsule c) {
		return c.collides(point);
	}
	
	public boolean collides(Vector3 v) {
		return v.equals(point);
	}
	
	public Vector3 getPosition() {
		return point;
	}
	
	public void setPosition(Vector3 v) {
		point = v;
	}
	
}