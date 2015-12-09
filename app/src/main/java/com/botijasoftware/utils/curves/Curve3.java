package com.botijasoftware.utils.curves;

import com.botijasoftware.utils.Vector3;

public abstract class Curve3 {

	public Vector3 p0, p1, p2, p3;
	public Vector3 tangent0 = new Vector3(0,0,0);
	public Vector3 tangent1 = new Vector3(0,0,0);

	public Curve3(Vector3 p0, Vector3 p1, Vector3 p2, Vector3 p3) {
		set(p0, p1, p2, p3);
	}
	
	public void set(Vector3 p0, Vector3 p1, Vector3 p2, Vector3 p3) {
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

	public Vector3 interpolate(float t) {
		return interpolate(t, new Vector3(0,0,0));
	}
	
	public abstract Vector3 interpolate(float t, Vector3 v);
	
	public Vector3 interpolateTangent(float t) {
		return interpolateTangent(t, new Vector3(0,0,0));
	}
	
	public Vector3 interpolateTangent(float t, Vector3 v) {
		
		if (t >= 0.0f && t <= 1.0f) {
			v.X = tangent1.X*t + tangent0.X*(1-t);
			v.Y = tangent1.Y*t + tangent0.Y*(1-t);
			v.Z = tangent1.Z*t + tangent0.Z*(1-t);
		}
		return v;
	}
}
