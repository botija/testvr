package com.botijasoftware.utils.curves;

import com.botijasoftware.utils.Vector3;

public class Bezier3  extends Curve3 {

	public Bezier3(Vector3 p0, Vector3 p1, Vector3 p2, Vector3 p3) {
		super(p0, p1, p2, p3);
	}
	
	public Vector3 interpolate(float t, Vector3 v) {

		if (t >= 0.0f && t <= 1.0f) {

			v.X = ((p0.X) + (3*-p0.X + 3*p1.X) * t + ( 3*p0.X - 6*p1.X + 3*p2.X) *t*t + (-p0.X + 3*p1.X -3*p2.X + p3.X)*t*t*t);
			v.Y = ((p0.Y) + (3*-p0.Y + 3*p1.Y) * t + ( 3*p0.Y - 6*p1.Y + 3*p2.Y) *t*t + (-p0.Y + 3*p1.Y -3*p2.Y + p3.Y)*t*t*t);
			v.Z = ((p0.Z) + (3*-p0.Z + 3*p1.Z) * t + ( 3*p0.Z - 6*p1.Z + 3*p2.Z) *t*t + (-p0.Z + 3*p1.Z -3*p2.Z + p3.Z)*t*t*t);
			
		}
		return v;
	}
	
}