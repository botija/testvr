package com.botijasoftware.utils;

public class Vector2i {
	
	public Vector2i() {}

	public Vector2i(int val) {
		X = val;
		Y = val;
	}
	public Vector2i(int valx, int valy) {
		X = valx;
		Y = valy;
	}
	
	public void setValue(int val) {
		X = val;
		Y = val;
	}
	
	public void setValue(int valx, int valy) {
		X = valx;
		Y = valy;
	}
	
	public void setValue(Vector2i v) {
		X = v.X;
		Y = v.Y;
	}
	
	public Vector2i clone() {
		return new Vector2i(X,Y);
	}
	
	public Vector2i normalize() {
		float len = length();
		if (len != 0.0f) {
			X = (int) (X / len);
			Y = (int) (Y / len);
		}
		return this;
	}
	
	public Vector2i add(Vector2i v) {
		X += v.X;
		Y += v.Y;
		return this;
	}
	
	public Vector2i add(int val) {
		X += val;
		Y += val;
		return this;
	}
	
	public Vector2i sub(Vector2i v) {
		X -= v.X;
		Y -= v.Y;
		return this;
	}
	
	public Vector2i sub(int val) {
		X -= val;
		Y -= val;
		return this;
	}
	
	public Vector2i mul(Vector2i v) {
		X *= v.X;
		Y *= v.Y;
		return this;
	}
	
	public Vector2i mul(int val) {
		X *= val;
		Y *= val;
		return this;
	}
	
	public Vector2i div(Vector2i v) {
		if (v.X != 0.0f && v.Y != 0.0f){
			X = (X / v.X);
			Y = (Y / v.Y);
		}
		return this;
	}
	
	public Vector2i div(int val) {
		if (val != 0.0f){
			float inv = 1.0f/val;
			X = (int) (X * inv);
			Y = (int) (Y * inv);
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
		return (X == 0 && Y == 0);
	}
	
	public boolean equals(int val) {
		return (X == val && Y == val);
	}
	
	public boolean equals(Vector2i v) {
		return (X == v.X && Y == v.Y);
	}
	
	public float distancesq(Vector2i v) {
		return ((X - v.X) * (X - v.X) + (Y - v.Y) * (Y - v.Y));
	}
	
	public float distance(Vector2i v) {
		return (float)Math.sqrt( (X - v.X) * (X - v.X) + (Y - v.Y) * (Y - v.Y) );
	}
	
	public float dot(Vector2i v) {
		return X * v.X + Y * v.Y;
	}
	
	public Vector2i reflect(Vector2i v) {
		//i - (2 * n * dot(i, n))
		sub(v.mul( (int) (2 * dot(v))));
		return this;
	}
	
	public Vector2i lerp(Vector2i v, float factor) {
		if (factor <=0.0f) {
			return this;
		}
		else if (factor >= 1.0f) {
			X = v.X;
			Y = v.Y;
			return this;
		}
		
		X = X + (int)(factor * ( v.X - X));
		Y = Y + (int)(factor * ( v.Y - Y));
		
		return this;
	}
	
	public Vector2i zero() {
		X = 0;
		Y = 0;
		return this;
	}
	
	
	public static Vector2i lerp(Vector2i v, Vector2i u, float factor) {
		if (factor <=0.0f) {
			return v.clone();
		}
		else if (factor >= 1.0f) {
			return u.clone();
		}
		
		int x = v.X + (int)(factor * ( u.X - v.X));
		int y = v.Y + (int)(factor * ( u.Y - v.Y));
		
		return new Vector2i(x,y);
	}
	
	public int X = 0;
	public int Y = 0;
	public static final Vector2i ZERO = new Vector2i(0, 0);
        public static final Vector2i ONE = new Vector2i(1, 1);
        public static final Vector2i RIGHT = new Vector2i(1, 0);
        public static final Vector2i UP = new Vector2i(0, 1);

}
