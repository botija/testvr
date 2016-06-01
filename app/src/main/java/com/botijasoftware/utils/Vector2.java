package com.botijasoftware.utils;

public class Vector2 {
	
	public Vector2() {
		X = Y = 0.0f;
	}

	public Vector2(float val) {
		X = val;
		Y = val;
	}
	public Vector2(float valx, float valy) {
		X = valx;
		Y = valy;
	}
	
	public void setValue(float val) {
		X = val;
		Y = val;
	}
	
	public void setValue(float valx, float valy) {
		X = valx;
		Y = valy;
	}
	
	public void setValue(Vector2 v) {
		X = v.X;
		Y = v.Y;
	}
	
	public Vector2 clone() {
		return new Vector2(X,Y);
	}
	
	public Vector2 normalize() {
		float len = length();
		if (len != 0.0f) {
			X /= len;
			Y /= len;
		}
		return this;
	}
	
	public Vector2 add(Vector2 v) {
		X += v.X;
		Y += v.Y;
		return this;
	}
	
	public Vector2 add(float val) {
		X += val;
		Y += val;
		return this;
	}
	
	public Vector2 sub(Vector2 v) {
		X -= v.X;
		Y -= v.Y;
		return this;
	}
	
	public Vector2 sub(float val) {
		X -= val;
		Y -= val;
		return this;
	}
	
	public Vector2 mul(Vector2 v) {
		X *= v.X;
		Y *= v.Y;
		return this;
	}
	
	public Vector2 mul(float val) {
		X *= val;
		Y *= val;
		return this;
	}
	
	public Vector2 div(Vector2 v) {
		if (v.X != 0.0f && v.Y != 0.0f){
			X /= v.X;
			Y /= v.Y;
		}
		return this;
	}
	
	public Vector2 div(float val) {
		if (val != 0.0f){
			float inv = 1.0f/val;
			X *= inv;
			Y *= inv;
		}
		return this;
	}
	
	public float lengthsq() {
		return X * X + Y * Y;
	}
	
	public float length() {
		return (float)Math.sqrt( X * X + Y * Y );
	}
	
	public boolean isZero() {
		return (X <= SIGMA && X >= -SIGMA && Y <= SIGMA && Y >= -SIGMA);
	}
	
	public boolean equals(float val) {
		float val_plus = val + SIGMA;
		float val_minus = val - SIGMA;
		return (X <= val_plus && X >= val_minus && Y <= val_plus && Y >= val_minus);
	}
	
	public boolean equals(Vector2 v) {
		return (X <= v.X+SIGMA && X >= v.X-SIGMA && Y <= v.Y+SIGMA && Y >= v.Y-SIGMA);
	}
	
	public float distancesq(Vector2 v) {
		return ((X - v.X) * (X - v.X) + (Y - v.Y) * (Y - v.Y));
	}
	
	public float distance(Vector2 v) {
		return (float)Math.sqrt( (X - v.X) * (X - v.X) + (Y - v.Y) * (Y - v.Y) );
	}
	
	public float dot(Vector2 v) {
		return X * v.X + Y * v.Y;
	}
	
	public Vector2 reflect(Vector2 v) {
		//i - (2 * n * dot(i, n))
		sub(v.mul( 2.0f * dot(v)));
		return this;
	}
	
	public Vector2 lerp(Vector2 v, float factor) {
		if (factor <=0.0f) {
			return this;
		}
		else if (factor >= 1.0f) {
			X = v.X;
			Y = v.Y;
			return this;
		}
		
		X = X + factor * ( v.X - X);
		Y = Y + factor * ( v.Y - Y);
		
		return this;
	}
	
	public Vector2 zero() {
		X = 0.0f;
		Y = 0.0f;
		return this;
	}
	
	public static Vector2 lerp(Vector2 v, Vector2 u, float factor) {
		if (factor <=0.0f) {
			return v.clone();
		}
		else if (factor >= 1.0f) {
			return u.clone();
		}
		
		float x = v.X + factor * ( u.X - v.X);
		float y = v.Y + factor * ( u.Y - v.Y);
		
		return new Vector2(x,y);
	}
	
	public float X;
	public float Y;
	private final static float SIGMA = 0.000001f;
	public static final Vector2 ZERO = new Vector2(0.0f, 0.0f);
    public static final Vector2 ONE = new Vector2(1.0f, 1.0f);
    public static final Vector2 RIGHT = new Vector2(1.0f, 0.0f);
    public static final Vector2 UP = new Vector2(0.0f, 1.0f);

}
