package com.botijasoftware.utils.curves;

import com.botijasoftware.utils.Vector2;

public class Hermite2 extends Curve2 {


	public Hermite2(Vector2 p0, Vector2 p1, Vector2 p2, Vector2 p3) {
		super(p0, p1, p2, p3);
	}
	
	public Vector2 interpolate(float t, Vector2 v) {

		if (t >= 0.0f && t <= 1.0f) {

			v.X = ((p1.X) + (p0.X) * t + (-3*p1.X + 3*p2.X + -2*p0.X - p3.X) *t*t + (2*p1.X - 2*p2.X + p0.X + p3.X)*t*t*t);
			v.Y = ((p1.Y) + (p0.Y) * t + (-3*p1.Y + 3*p2.Y + -2*p0.Y - p3.Y) *t*t + (2*p1.Y - 2*p2.Y + p0.Y + p3.Y)*t*t*t);			
			
		}
		return v;
	}
	
}