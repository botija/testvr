package com.botijasoftware.utils;

public class Circle {
	
	public Circle(int px, int py, int r) {

			X = px;
			Y = py;
			radius = r;
	}
	
	public Circle(Vector2 position, int r) {

			X = (int) position.X;
			Y = (int) position.Y;
			radius = r;
	}
	
	public void setRadius(int r) {
		radius = r;
	}
	
	public void setPosition(int x, int y) {
		X = x;
		Y = y;
	}
	
	public void setPosition(Vector2 v) {
		X = (int) v.X;
		Y = (int) v.Y;
	}
	
	public void move(int incx, int incy) {
		X += incx;
		Y += incy;
	}
	
	public void move(Vector2 v) {
		X += (int)v.X;
		Y += (int)v.Y;
	}
	
	public boolean contains(int x, int y) {
		int dx = X - x;
		int dy = Y - y;
		return ( dx <= radius && dx >= -radius && dy <= radius && dy >= -radius);
	}
	
	public boolean contains(Vector2 v) {
		return contains((int)v.X, (int)v.Y);
	}
	
	public void setCenter(int x, int y) {
		X = x;
		Y = y;
	}
	
	public void setCenter(Vector2 v) {
		X = (int)v.X;
		Y = (int)v.Y;
	}
	
	public Vector2 getCenter() {
		return new Vector2( X, Y );
	}
	
	
	public int X = 0; //center
	public int Y = 0;  
	public int radius;

}
