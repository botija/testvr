package com.botijasoftware.utils.collision;

import com.botijasoftware.utils.Vector3;

public class CollisionVolume {
	public boolean collides(CollisionVolume c) {
		return false;
	}
	
	public boolean collides(Vector3 v) {
		return false;
	}
	
	public boolean collides(Sphere s) {
		return false;
	}
	
	public boolean collides(Capsule c) {
		return false;
	}
	
	public boolean collides(Cube c) {
		return false;
	}
	
	public boolean collides(Segment s) {
		return false;
	}
	
	public boolean collides(Ray r) {
		return false;
	}
	
	public boolean collides(Plane p) {
		return false;
	}
	
	public void setPosition(Vector3 v) {
		
	}
	
	public Vector3 getPosition() {
		return new Vector3(0,0,0);
	}
	
	public void setRadius(float r) {
		
	}
	
	public float getRadius() {
		return 0.0f;
	}
}