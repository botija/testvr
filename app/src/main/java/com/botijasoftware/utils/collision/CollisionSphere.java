package com.botijasoftware.utils.collision;

import com.botijasoftware.utils.Vector3;

public class CollisionSphere extends CollisionVolume {
	private Sphere sphere;
	
	public CollisionSphere(Vector3 center, float radius) {
		sphere = new Sphere(center, radius);
	}
	
	public boolean collides(CollisionVolume c) {
		return c.collides(sphere);
	}
	
	public boolean collides(Sphere s) {
		return sphere.collides(s);
	}
	
	public boolean collides(Vector3 v) {
		return sphere.collides(v);
	}
	
	public boolean collides(Capsule c) {
		return c.collides(sphere);
	}
	
	public boolean collides(Ray r) {
		return r.collides(sphere);
	}
	
	public Vector3 getPosition() {
		return sphere.getCenter();
	}
	
	public void setPosition(Vector3 v) {
		sphere.setCenter(v);
	}
	
	public void setRadius(float r) {
		sphere.setRadius(r);
	}
	
	public float getRadius() {
		return sphere.radius;
	}
	
}