package com.botijasoftware.utils;

public class Vector3i {
	
	public Vector3i() {
		X = Y = Z = 0;
	}

	public Vector3i(int val) {
		X = val;
		Y = val;
		Z = val;
	}
	public Vector3i(int valx, int valy, int valz) {
		X = valx;
		Y = valy;
		Z = valz;
	}
	
	public void setValue(int val) {
		X = val;
		Y = val;
		Z = val;
	}
	
	public void setValue(int valx, int valy, int valz) {
		X = valx;
		Y = valy;
		Z = valz;
	}
	
	public void setValue(Vector3i v) {
		X = v.X;
		Y = v.Y;
		Z = v.Z;
	}
	
	public Vector3i clone() {
		return new Vector3i(X,Y,Z);
	}
	
	public Vector3i normalize() {
		float len = length();
		if (len != 0.0f) {
			X = (int) (X / len);
			Y = (int) (Y / len);
			Z = (int) (Z / len);
		}
		return this;
	}
	
	public Vector3i add(Vector3i v) {
		X += v.X;
		Y += v.Y;
		Z += v.Z;
		return this;
	}
	
	public Vector3i add(int val) {
		X += val;
		Y += val;
		Z += val;
		return this;
	}
	
	public Vector3i sub(Vector3i v) {
		X -= v.X;
		Y -= v.Y;
		Z -= v.Z;
		return this;
	}
	
	public Vector3i sub(int val) {
		X -= val;
		Y -= val;
		Z -= val;
		return this;
	}
	
	public Vector3i mul(Vector3i v) {
		X *= v.X;
		Y *= v.Y;
		Z *= v.Z;
		return this;
	}
	
	public Vector3i mul(int val) {
		X *= val;
		Y *= val;
		Z *= val;
		return this;
	}
	
	public Vector3i div(Vector3i v) {
		if (v.X != 0.0f && v.Y != 0.0f && v.Z != 0.0f){
			X /= (X / v.X);
			Y /= (Y / v.X);
			Z /= (Z / v.X);
		}
		return this;
	}
	
	public Vector3i div(int val) {
		if (val != 0.0f){
			float inv = 1.0f/val;
			X = (int)(X * inv);
			Y = (int)(Y * inv);
			Z = (int)(Z * inv);
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
		return (X == 0 && Y == 0 && Z == 0);
	}
	
	public boolean equals(int val) {
		return (X == val && Y == val && Z == val);
	}
	
	public boolean equals(Vector3i v) {
		return (X == v.X && Y == v.Y && Z == v.Z);
	}
	
	public float distancesq(Vector3i v) {
		return ((X - v.X) * (X - v.X) + (Y - v.Y) * (Y - v.Y) + (Z - v.Z) * (Z - v.Z));
	}
	
	public float distance(Vector3i v) {
		return (float)Math.sqrt( (X - v.X) * (X - v.X) + (Y - v.Y) * (Y - v.Y) + (Z - v.Z) * (Z - v.Z) );
	}
	
	public float dot(Vector3i v) {
		return X * v.X + Y * v.Y + Z * v.Z;
	}
	
	public Vector3i cross(Vector3i v) {
		float nx = Y * v.Z - Z * v.Y;
		float ny = Z * v.X - X * v.Z;
		float nz = X * v.Y - Y * v.X;
		X = (int)nx;
		Y = (int)ny;
		Z = (int)nz;
		return this;
	}
	
	public Vector3i reflect(Vector3i v) {
		//i - (2 * n * dot(i, n))
		sub(v.mul( (int)(2 * dot(v))));
		return this;
	}
	

	public Vector3i zero() {
		X = 0;
		Y = 0;
		Z = 0;
		return this;
	}
	
	public Vector3i lerp(Vector3i v, float factor) {
		if (factor <=0.0f) {
			return this;
		}
		else if (factor >= 1.0f) {
			X = v.X;
			Y = v.Y;
			Z = v.Z;
			return this;
		}
		
		X = X + (int)(factor * ( v.X - X));
		Y = Y + (int)(factor * ( v.Y - Y));
		Z = Z + (int)(factor * ( v.Z - Z));
		
		return this;
	}
	
	public static Vector3i cross(Vector3i v, Vector3i u) {
		int nx = v.Y * u.Z - v.Z * u.Y;
		int ny = v.Z * u.X - v.X * u.Z;
		int nz = v.X * u.Y - v.Y * u.X;		
		return new Vector3i(nx, ny, nz);
	}
	
	public static Vector3i orthoNormalize(Vector3i n, Vector3i u) {
		n.normalize();
		Vector3i v = cross(n,u);
		v.normalize();
		return cross(v,n);		
	}
	
	public static Vector3i lerp(Vector3i v, Vector3i u, float factor) {
		if (factor <=0.0f) {
			return v.clone();
		}
		else if (factor >= 1.0f) {
			return u.clone();
		}
		
		int x = v.X + (int)(factor * ( u.X - v.X));
		int y = v.Y + (int)(factor * ( u.Y - v.Y));
		int z = v.Z + (int)(factor * ( u.Z - v.Z));
		
		return new Vector3i(x,y,z);
	}
	
	public int X;
	public int Y;
	public int Z;
	public static final Vector3i ZERO = new Vector3i(0, 0, 0);
    public static final Vector3i ONE = new Vector3i(1, 1, 1);
    public static final Vector3i RIGHT = new Vector3i(1, 0, 0);
    public static final Vector3i UP = new Vector3i(0, 1, 0);
    public static final Vector3i FORWARD = new Vector3i(0, 0, 1);

}
