package com.botijasoftware.utils.curves;

import com.botijasoftware.utils.Vector2;

public abstract class Curve2 {

	public Vector2 p0, p1, p2, p3;
	public Vector2 tangent0 = new Vector2(0,0);
	public Vector2 tangent1 = new Vector2(0,0);

	public Curve2(Vector2 p0, Vector2 p1, Vector2 p2, Vector2 p3) {
		set(p0, p1, p2, p3);
	}
	
	public void set(Vector2 p0, Vector2 p1, Vector2 p2, Vector2 p3) {
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		tangent0.setValue(p2);
		tangent0.sub(p0);
		tangent0.mul(0.5f);
		tangent0.normalize();
		tangent1.setValue(p3);
		tangent1.sub(p1);
		tangent1.mul(0.5f);
		tangent1.normalize();
	}

	public void setTangent(Vector2 tangent) {
		this.tangent0.setValue(tangent);
	}


	public Vector2 interpolate(float t) {
		return interpolate(t, new Vector2(0,0));
	}
	
	public abstract Vector2 interpolate(float t, Vector2 v);
	
	public Vector2 interpolateTangent(float t) {
		return interpolateTangent(t, new Vector2(0,0));
	}
	
	public Vector2 interpolateTangent(float t, Vector2 v) {
		
		if (t >= 0.0f && t <= 1.0f) {
			v.X = tangent1.X*t + tangent0.X*(1-t);
			v.Y = tangent1.Y*t + tangent0.Y*(1-t);
		}
		return v;
	}
}
