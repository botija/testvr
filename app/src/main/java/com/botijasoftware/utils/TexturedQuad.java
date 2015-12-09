package com.botijasoftware.utils;

public class TexturedQuad extends Sprite {

	public TexturedQuad(Vector2 position, Texture texture) {
		super(new Rectanglef(position.X, position.Y, texture.getWidth(), texture.getHeight()), texture);
	}
	
	
	public TexturedQuad(Vector2 position, Texture texture, boolean flip) {
		super(new Rectanglef(position.X, position.Y, texture.getWidth(), texture.getHeight()),texture, 1.0f, 0.0f, new ColorRGBAb((byte)255,(byte)255,(byte)255,(byte)255), flip);
	}
	
	public TexturedQuad(Vector2 position, Texture texture, ColorRGBAb color) {
		super(new Rectanglef(position.X, position.Y, texture.getWidth(), texture.getHeight()),texture, 1.0f, 0.0f, color, false);
	}
	
	public TexturedQuad(Vector2 position, Texture texture, float scale) {
		super(new Rectanglef(position.X, position.Y, texture.getWidth(), texture.getHeight()),texture, scale, 0.0f, new ColorRGBAb((byte)255,(byte)255,(byte)255,(byte)255), false);
	}
	
	public TexturedQuad(Vector2 position, Texture texture, float scale, float rotation) {
		super(new Rectanglef(position.X, position.Y, texture.getWidth(), texture.getHeight()),texture, scale, rotation, new ColorRGBAb((byte)255,(byte)255,(byte)255,(byte)255), false);
	}
	
	public TexturedQuad(Vector2 position, Texture texture, float scale, float rotation, boolean flip) {
		super(new Rectanglef(position.X, position.Y, texture.getWidth(), texture.getHeight()),texture, scale, rotation, new ColorRGBAb((byte)255,(byte)255,(byte)255,(byte)255), flip);
	}
	
	@Override
	public void setTexture(Texture texture) {
		mRectangle.setSize(texture.getWidth(), texture.getHeight());
		super.setTexture(texture);
	}	
	
	
	@Override
	public void setRectangle(Rectanglef rectangle) {
		
		mRectangle.setPosition(rectangle.X, rectangle.Y);
		super.setRectangle(mRectangle);
				
	}
	
	@Override
	public void setSize(float nw, float nh) {
	}
	
	@Override
	public void setSize(Vector2 v) {
	
	}
	
}