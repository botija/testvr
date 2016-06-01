package com.botijasoftware.utils;

public class Rectangle {

	public Rectangle() {

		X = 0;
		Y = 0;
		width = 0;
		height = 0;
	}
	
	public Rectangle(int px, int py, int w, int h) {

			X = px;
			Y = py;
			width= w;
			height = h;
	}
	
	public Rectangle(Vector2 position, Vector2 size) {

			X = (int) position.X;
			Y = (int) position.Y;
			width= (int) size.X;
			height = (int) size.Y;
	}

    public Rectangle(Rectangle rect) {

        X = rect.X;
        Y = rect.Y;
        width = rect.width;
        height = rect.height;
    }
	
	public Rectangle(Rectanglef rect) {

		X = (int) rect.X;
		Y = (int) rect.Y;
		width= (int) rect.width;
		height = (int) rect.height;
    }
	
	public void setSize(int w, int h) {
		width = w;
		height = h;
	}
	
	public void setSize(Vector2 v) {
		width = (int) v.X;
		height = (int) v.Y;
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
		return ((x >= X) && ( x <= X + width) && (y >= Y) && (y <= Y + height)); 
	}
	
	public boolean contains(Vector2 v) {
		return (((int)v.X >= X) && ( (int)v.X <= X + width) && ((int)v.Y >= Y) && ((int)v.Y <= Y + height)); 
	}
	
	public void setCenter(int x, int y) {
		X = x - (width / 2);
		Y = y - (height / 2);
	}
	
	public void setCenter(Vector2 v) {
		X = (int)v.X - (width / 2);
		Y = (int)v.Y - (height / 2);
	}
	
	public Vector2 getCenter() {
		return new Vector2( X + (width / 2.0f), Y + (height / 2.0f));
	}
	
	public Vector2 getTopLeft() {
		return new Vector2( X, Y );
	}
	
	public Vector2 getTopRight() {
		return new Vector2( X + width, Y );
	}
	
	public Vector2 getBottomLeft() {
		return new Vector2( X, Y - height);
	}
	
	public Vector2 getBottomRight() {
		return new Vector2( X + width, Y - height);
	}

    public Rectangle clone() {
        return new Rectangle(X, Y, width, height);
    }
	
	public int X; //top left
	public int Y;
	public int width;
	public int height;

}
