package com.botijasoftware.utils;

public class Rectanglef {

	public Rectanglef() {

		X = 0.0f;
		Y = 0.0f;
		width = 0.0f;
		height = 0.0f;
	}
	
	public Rectanglef(float px, float py, float w, float h) {

			X = px;
			Y = py;
			width= w;
			height = h;
	}
	
	public Rectanglef(Vector2 position, Vector2 size) {

			X = position.X;
			Y = position.Y;
			width = size.X;
			height = size.Y;
	}
	
	public Rectanglef(Rectangle rect) {
		X = rect.X;
		Y = rect.Y;
		width = rect.width;
		height = rect.height;
	}

    public Rectanglef(Rectanglef rect) {

        X = rect.X;
        Y = rect.Y;
        width = rect.width;
        height = rect.height;
    }
	
	public void setSize(float w, float h) {
		width = w;
		height = h;
	}
	
	public void setSize(Vector2 v) {
		width = v.X;
		height = v.Y;
	}
	
	public void setPosition(float x, float y) {
		X = x;
		Y = y;
	}
	
	public void setPosition(Vector2 v) {
		X = v.X;
		Y = v.Y;
	}
	
	public void move(float incx,float incy) {
		X += incx;
		Y += incy;
	}
	
	public void move(Vector2 v) {
		X += v.X;
		Y += v.Y;
	}
	
	public boolean contains(float x, float y) {
		return ((x >= X) && ( x <= X + width) && (y >= Y) && (y <= Y + height)); 
	}
	
	public boolean contains(Vector2 v) {
		return ((v.X >= X) && ( v.X <= X + width) && (v.Y >= Y) && (v.Y <= Y + height)); 
	}
	
	public void setCenter(float x, float y) {
		X = x - (width / 2.0f);
		Y = y - (height / 2.0f);
	}
	
	public void setCenter(Vector2 v) {
		X = v.X - (width / 2.0f);
		Y = v.Y - (height / 2.0f);
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

    public Rectanglef clone() {
        return new Rectanglef(X, Y, width, height);
    }
	
	public float X; //top left
	public float Y;
	public float width;
	public float height;

}
