package com.botijasoftware.utils.collision;


import com.botijasoftware.utils.Vector3;

public class CollisionCapsule extends CollisionVolume {
	private Capsule capsule;
	
	public CollisionCapsule(Vector3 p0, Vector3 p1, float radius) {
		capsule = new Capsule(p0, p1, radius);
	}
	
	public boolean collides(CollisionVolume c) {
		return c.collides(capsule);
	}
	
	public boolean collides(Sphere s) {
		return capsule.collides(s);
	}
	
	public boolean collides(Vector3 v) {
		return capsule.collides(v);
	}
	
	public void setRadius(float r) {
		capsule.setRadius(r);
	}
	
	public float getRadius() {
		return capsule.radius;
	}
	
	public void set(Vector3 v0, Vector3 v1) {
		capsule.set(v0, v1);
	}
	
	public Vector3 getP0() {
		return capsule.getP0();
	}
	
	public Vector3 getP1() {
		return capsule.getP1();
	}
	
}