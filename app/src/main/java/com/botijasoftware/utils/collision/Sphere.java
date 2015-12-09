package com.botijasoftware.utils.collision;

import com.botijasoftware.utils.Vector3;

public class Sphere {
	
	public Sphere(float px, float py, float pz, float r) {

			X = px;
			Y = py;
			Z = pz;
			radius = r;
	}
	
	public Sphere(Vector3 position, float r) {

			X = position.X;
			Y = position.Y;
			Z = position.Z;
			radius = r;
	}
	
	public void setRadius(float r) {
		radius = r;
	}
	
	public void setPosition(float x, float y, float z) {
		X = x;
		Y = y;
	}
	
	public void setPosition(Vector3 v) {
		X = v.X;
		Y = v.Y;
		Z = v.Z;
	}
	
	public void move(float incx, float incy, float incz) {
		X += incx;
		Y += incy;
		Z += incz;
	}
	
	public void move(Vector3 v) {
		X += v.X;
		Y += v.Y;
		Z += v.Z;
	}
	
	public boolean collides(float x, float y, float z) {
		float dx = X - x;
		float dy = Y - y;
		float dz = Z - z;
		return ( dx <= radius && dx >= -radius && dy <= radius && dy >= -radius && dz <= radius && dz >= -radius);
	}
	
	public boolean collides(Vector3 v) {
		return collides(v.X, v.Y, v.Z);
	}
	
	public boolean collides(Ray r) {
		return r.collides(this);
	}
	
	public boolean collides(Capsule c) {
		return c.collides(this);
	}
	
	public boolean collides(Sphere s) {
		float d = (getCenter().distance(s.getCenter()));
		return d <= (radius + s.radius);
	}
	
	public void setCenter(float x, float y, float z) {
		X = x;
		Y = y;
		Z = z;
	}
	
	public void setCenter(Vector3 v) {
		X = v.X;
		Y = v.Y;
		Z = v.Z;
	}
	
	public Vector3 getCenter() {
		return new Vector3( X, Y, Z );
	}
	
	
	public float X = 0; //center
	public float Y = 0;  
	public float Z = 0;  
	public float radius;

}
