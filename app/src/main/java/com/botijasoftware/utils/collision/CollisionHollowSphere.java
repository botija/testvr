package com.botijasoftware.utils.collision;

import com.botijasoftware.utils.Vector3;

public class CollisionHollowSphere extends CollisionVolume {
	private Sphere outsphere;
	private Sphere insphere;
	private float holeangle;
	private float halfholeangle;
	private float angle;
	
	
	public CollisionHollowSphere(Vector3 center, float outradius, float inradius, float holeangle) {
		outsphere = new Sphere(center, outradius);
		insphere = new Sphere(center, inradius);
		this.holeangle = holeangle;
		this.halfholeangle = this.holeangle/2;
	}
	
	private boolean inHole(Vector3 v) {
		float a = (float)Math.atan2(-(v.Y-outsphere.Y), v.X-outsphere.X);
		return (a < angle+halfholeangle) && (a > angle-halfholeangle); 
	}
	
	public boolean collides(CollisionVolume c) {
		return (c.collides(outsphere) && (!c.collides(insphere)) && !inHole(c.getPosition())); 
	}
	
	public boolean collides(Sphere s) {
		return (outsphere.collides(s) && (!insphere.collides(s) && !inHole(s.getCenter()))) ;
	}
	
	public boolean collides(Vector3 v) {
		return (outsphere.collides(v) && (!insphere.collides(v) && !inHole(v)));
	}
	
	public Vector3 getPosition() {
		return insphere.getCenter();
	}
	
	public void setPosition(Vector3 v) {
		outsphere.setCenter(v);
		insphere.setCenter(v);
	}
	
	public void setOutRadius(float r) {
		outsphere.setRadius(r);
	}
	
	public void setInRadius(float r) {
		insphere.setRadius(r);
	}
	
	public float getOutRadius() {
		return outsphere.radius;
	}
	
	public float getInRadius() {
		return insphere.radius;
	}
	
	public void rotate(float angle) {
		this.angle = angle;
	}
	
}