package com.botijasoftware.utils.collision;

import com.botijasoftware.utils.Vector3;

public class Cube {
	
	public Cube(float px, float py, float pz, float w, float h, float d) {

			X = px;
			Y = py;
			Z = pz;
			width= w;
			height = h;
			depth = d;
	}
	
	public Cube(Vector3 position, Vector3 size) {

			X = position.X;
			Y = position.Y;
			Z = position.Z;
			width= size.X;
			height = size.Y;
			depth = size.Z;
	}
	
	public void setSize(float w, float h, float d) {
		width = w;
		height = h;
		depth = d;
	}
	
	public void setSize(Vector3 v) {
		width = v.X;
		height = v.Y;
		depth = v.Z;
	}
	
	public void setPosition(float x, float y, float z) {
		X = x;
		Y = y;
		Z = z;
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
	
	public boolean contains(float x, float y, float z) {
		return ((x >= X) && ( x <= X + width) && (y >= Y) && (y <= Y + height) && (z >= Z) && (z <= Z + depth)); 
	}
	
	public boolean contains(Vector3 v) {
		return (((int)v.X >= X) && ( v.X <= X + width) && (v.Y >= Y) && (v.Y <= Y + height) && (v.Z >= Z) && (v.Z <= Z + depth)); 
	}
	
	public void setCenter(float x, float y, float z) {
		X = x - (width / 2);
		Y = y + (height / 2);
		Z = z + (depth / 2);
	}
	
	public void setCenter(Vector3 v) {
		X = v.X - (width / 2);
		Y = v.Y + (height / 2);
		Z = v.Z + (depth / 2);
	}
	
	public Vector3 getCenter() {
		return new Vector3( X + (width / 2.0f), Y - (height / 2.0f), Z - (depth / 2.0f));
	}
	
	
	public float X = 0; //top left
	public float Y = 0;
	public float Z = 0;
	public float width = 0;
	public float height = 0;
	public float depth = 0;

}
