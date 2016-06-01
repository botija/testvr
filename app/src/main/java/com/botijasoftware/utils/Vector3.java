package com.botijasoftware.utils;

public class Vector3 {
	
	public Vector3() {
		X = Y = Z = 0.0f;
	}

	public Vector3(float val) {
		X = val;
		Y = val;
		Z = val;
	}
	public Vector3(float valx, float valy, float valz) {
		X = valx;
		Y = valy;
		Z = valz;
	}
	
	public void setValue(float val) {
		X = val;
		Y = val;
		Z = val;
	}
	
	public void setValue(float valx, float valy, float valz) {
		X = valx;
		Y = valy;
		Z = valz;
	}
	
	public void setValue(Vector3 v) {
		X = v.X;
		Y = v.Y;
		Z = v.Z;
	}
	
	public Vector3 clone() {
		return new Vector3(X,Y,Z);
	}
	
	public Vector3 normalize() {
		float len = length();
		if (len != 0.0f) {
			X /= len;
			Y /= len;
			Z /= len;
		}
		return this;
	}
	
	public Vector3 add(Vector3 v) {
		X += v.X;
		Y += v.Y;
		Z += v.Z;
		return this;
	}
	
	public Vector3 add(float val) {
		X += val;
		Y += val;
		Z += val;
		return this;
	}
	
	public Vector3 sub(Vector3 v) {
		X -= v.X;
		Y -= v.Y;
		Z -= v.Z;
		return this;
	}
	
	public Vector3 sub(float val) {
		X -= val;
		Y -= val;
		Z -= val;
		return this;
	}
	
	public Vector3 mul(Vector3 v) {
		X *= v.X;
		Y *= v.Y;
		Z *= v.Z;
		return this;
	}
	
	public Vector3 mul(float val) {
		X *= val;
		Y *= val;
		Z *= val;
		return this;
	}
	
	public Vector3 div(Vector3 v) {
		if (v.X != 0.0f && v.Y != 0.0f && v.Z != 0.0f){
			X /= v.X;
			Y /= v.Y;
			Z /= v.Z;
		}
		return this;
	}
	
	public Vector3 div(float val) {
		if (val != 0.0f){
			float inv = 1.0f/val;
			X *= inv;
			Y *= inv;
			Z *= inv;
		}
		return this;
	}
	
	public float lengthsq() {
		return X * X + Y * Y + Z * Z;
	}
	
	public float length() {
		return (float)Math.sqrt( X * X + Y * Y + Z * Z);
	}
	
	public boolean isZero() {
		return (X <= SIGMA && X >= -SIGMA && Y <= SIGMA && Y >= -SIGMA && Z <= SIGMA && Z >= -SIGMA);
	}
	
	public boolean equals(float val) {
		float val_plus = val + SIGMA;
		float val_minus = val - SIGMA;
		return (X <= val_plus && X >= val_minus && Y <= val_plus && Y >= val_minus && Z <= val_plus && Z >= val_minus);
	}
	
	public boolean equals(Vector3 v) {
		return (X <= v.X+SIGMA && X >= v.X-SIGMA && Y <= v.Y+SIGMA && Y >= v.Y-SIGMA && Z <= v.Z+SIGMA && Z >= v.Z-SIGMA);
	}
	
	public float distancesq(Vector3 v) {
		return ((X - v.X) * (X - v.X) + (Y - v.Y) * (Y - v.Y) + (Z - v.Z) * (Z - v.Z));
	}
	
	public float distance(Vector3 v) {
		return (float)Math.sqrt( (X - v.X) * (X - v.X) + (Y - v.Y) * (Y - v.Y) + (Z - v.Z) * (Z - v.Z) );
	}
	
	public float dot(Vector3 v) {
		return X * v.X + Y * v.Y + Z * v.Z;
	}
	
	public Vector3 cross(Vector3 v) {
		float nx = Y * v.Z - Z * v.Y;
		float ny = Z * v.X - X * v.Z;
		float nz = X * v.Y - Y * v.X;
		X = nx;
		Y = ny;
		Z = nz;
		return this;
	}
	
	public Vector3 reflect(Vector3 v) {
		//i - (2 * n * dot(i, n))
		sub(v.mul( 2.0f * dot(v)));
		return this;
	}

	
	public Vector3 lerp(Vector3 v, float factor) {
		if (factor <=0.0f) {
			return this;
		}
		else if (factor >= 1.0f) {
			X = v.X;
			Y = v.Y;
			Z = v.Z;
			return this;
		}
		
		X = X + factor * ( v.X - X);
		Y = Y + factor * ( v.Y - Y);
		Z = Z + factor * ( v.Z - Z);
		
		return this;
	}
	
	
	public Vector3 zero() {
		X = 0.0f;
		Y = 0.0f;
		Z = 0.0f;
		return this;
	}
	
	public static Vector3 cross(Vector3 v, Vector3 u) {
		float nx = v.Y * u.Z - v.Z * u.Y;
		float ny = v.Z * u.X - v.X * u.Z;
		float nz = v.X * u.Y - v.Y * u.X;		
		return new Vector3(nx, ny, nz);
	}
	
	public static Vector3 orthoNormalize(Vector3 n, Vector3 u) {
		n.normalize();
		Vector3 v = cross(n,u);
		v.normalize();
		return cross(v,n);		
	}
	
	public static Vector3 lerp(Vector3 v, Vector3 u, float factor) {
		if (factor <=0.0f) {
			return v.clone();
		}
		else if (factor >= 1.0f) {
			return u.clone();
		}
		
		float x = v.X + factor * ( u.X - v.X);
		float y = v.Y + factor * ( u.Y - v.Y);
		float z = v.Z + factor * ( u.Z - v.Z);
		
		return new Vector3(x,y,z);
	}
	
	public float X;
	public float Y;
	public float Z;
	private final static float SIGMA = 0.000001f;
	public static final Vector3 ZERO = new Vector3(0.0f, 0.0f, 0.0f);
	public static final Vector3 ONE = new Vector3(1.0f, 1.0f, 1.0f);
    public static final Vector3 RIGHT = new Vector3(1.0f, 0.0f, 0.0f);
    public static final Vector3 UP = new Vector3(0.0f, 1.0f, 0.0f);
    public static final Vector3 FORWARD = new Vector3(0.0f, 0.0f, 1.0f);
}
