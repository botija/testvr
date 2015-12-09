package com.botijasoftware.utils.collision;

import com.botijasoftware.utils.Vector3;

public class Capsule {

	private Segment segment;
	public float radius;
	
	public Capsule(Vector3 p0, Vector3 p1, float r) {
		segment = new Segment(p0, p1);
		radius = r;
	}
	
	public void set(Vector3 p0, Vector3 p1) {
		segment.set(p0,p1);
	}
	
	public boolean collides(Vector3 p) {
		return (segment.distancesq(p) <= radius*radius);
	}
	
	public boolean collides(Sphere s) {
		return (segment.distancesq(s.getCenter()) <= (s.radius*s.radius + radius*radius) );
	}
	
	public boolean collides(Ray r) {
		return false;
	}
	
	public boolean collides(Capsule c) {
		return false;
	}
	
	public boolean collides(Segment s) {
		return false;
	}

	public float getRadius() {
		return radius;
	}
	
	public void setRadius(float r) {
		this.radius = r;
	}

	public Vector3 getP0() {
		return segment.p0;
	}
	
	public Vector3 getP1() {
		return segment.p1;
	}
	
}