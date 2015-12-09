package com.botijasoftware.utils.curves;

import com.botijasoftware.utils.Vector2;

public class Bezier2 extends Curve2 {


	public Bezier2(Vector2 p0, Vector2 p1, Vector2 p2, Vector2 p3) {
		super(p0, p1, p2, p3);
	}
		
	public Vector2 interpolate(float t, Vector2 v) {

		if (t >= 0.0f && t <= 1.0f) {

			v.X = ((p0.X) + (3*-p0.X + 3*p1.X) * t + ( 3*p0.X - 6*p1.X + 3*p2.X) *t*t + (-p0.X + 3*p1.X -3*p2.X + p3.X)*t*t*t);
			v.Y = ((p0.Y) + (3*-p0.Y + 3*p1.Y) * t + ( 3*p0.Y - 6*p1.Y + 3*p2.Y) *t*t + (-p0.Y + 3*p1.Y -3*p2.Y + p3.Y)*t*t*t);
			
		}
		return v;
	}
	
}