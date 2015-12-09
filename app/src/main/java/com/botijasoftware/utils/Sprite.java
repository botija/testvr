package com.botijasoftware.utils;

public class Sprite {

	protected Rectanglef mRectangle;
	protected Texture mTexture;
	protected boolean mFlip;
	protected Vector2 mScale = new Vector2(1.0f, 1.0f);
	protected float mRotation = 0.0f;
	protected ColorRGBAb mColor;
	protected boolean mVisible = true;


	public Sprite(Rectanglef rectangle, Texture texture, float scalex, float scaley, float rotation, ColorRGBAb color, boolean flip) {
	
		mRotation = rotation;
		mFlip = flip;

		mRectangle = rectangle;
		setTexture(texture);
		setRectangle(rectangle);
        setScale(scalex, scaley);
		setColor( color );

	}

    public Sprite(Rectanglef rectangle, Texture texture, float scale, float rotation, ColorRGBAb color, boolean flip) {
        this(rectangle, texture, scale, scale, rotation, color, flip);
    }
	
	public Sprite(Rectanglef rectangle, Texture texture) {
		this(rectangle, texture, 1.0f, 0.0f, new ColorRGBAb((byte) 255, (byte) 255, (byte) 255, (byte) 255), false);
	}
	
	public Sprite(Rectanglef rectangle, Texture texture, boolean flip) {
		this(rectangle,texture, 1.0f, 0.0f, new ColorRGBAb((byte)255,(byte)255,(byte)255,(byte)255), flip);
	}
	
	public Sprite(Rectanglef rectangle, Texture texture, ColorRGBAb color) {
		this(rectangle,texture, 1.0f, 0.0f, color, false);
	}
	
	public Sprite(Rectanglef rectangle, Texture texture, float scale) {
		this(rectangle,texture, scale, 0.0f, new ColorRGBAb((byte)255,(byte)255,(byte)255,(byte)255), false);
	}
	
	public Sprite(Rectanglef rectangle, Texture texture, float scale, float rotation) {
		this(rectangle,texture, scale, rotation, new ColorRGBAb((byte)255,(byte)255,(byte)255,(byte)255), false);
	}
	
	public Sprite(Rectanglef rectangle, Texture texture, float scale, float rotation, boolean flip) {
		this(rectangle,texture, scale, rotation, new ColorRGBAb((byte)255,(byte)255,(byte)255,(byte)255), flip);
	}
	
	
	public void setTexture(Texture texture) {
		mTexture = texture;
				
		TextureCoords coords = mTexture.getTextureCoords();
		
		if (mFlip) {
			coords.flipVertical();
		}

	}
	
	public void setTexture(Texture texture, boolean flip) {
		
		mFlip = flip;
		setTexture(texture);	
	}
	
	public void setRectangle(Rectanglef rectangle) {
	
		mRectangle = rectangle;
		
		float w = mRectangle.width * mScale.X;
		float h = mRectangle.height * mScale.Y;

	}
	
	public void setPosition(float x, float y) {
		mRectangle.X = x;
		mRectangle.Y = y;
	}
	
	public void setPosition(Vector2 v) {
		mRectangle.X = v.X;
		mRectangle.Y = v.Y;
	}
	
	public void setCenter(Vector2 v) {
		mRectangle.setCenter(v);
	}
	
	public void setCenter(float x, float y) {
		mRectangle.setCenter(x, y);
	}
	
	public void setSize(float nw, float nh) {
		mRectangle.width = nw;
		mRectangle.height = nh;
		
		setRectangle(mRectangle);
	}
	
	public void setSize(Vector2 v) {
		mRectangle.width = v.X;
		mRectangle.height = v.Y;

		setRectangle(mRectangle);
	}
	
	public void setScale(float scalex, float scaley) {
		mScale.X = scalex;
        mScale.Y = scaley;
		setRectangle(mRectangle);
	}

    public void setScale(Vector2 scale) {
        mScale.X = scale.X;
        mScale.Y = scale.Y;
        setRectangle(mRectangle);
    }
	
	public void setRotation(float rotation) {
		mRotation = rotation;
	}
	
	public void setColor(ColorRGBAb color) {
		
		mColor = color;

	}
	
	public void setAlpha(float alpha) {
		mColor.A = (byte) (255*alpha);
		setColor(mColor);
	}
	
	public void setAlpha(byte alpha) {
		mColor.A = alpha;
		setColor(mColor);
	}

	
	public Rectanglef getRectangle() {
		return mRectangle;
	}
	
	public void show() {
		mVisible = true;
	}
	
	public void hide() {
		mVisible = false;
	}
	
	public boolean isVisible() {
		return mVisible;
	}

    public float getFloatAlpha() {
        return (mColor.A / 255.0f);
    }

    public byte getByteAlpha() {
        return mColor.A;
    }

	public Texture getTexture() {
		return mTexture;
	}


	public void move( float x, float y) {
		mRectangle.X += x;
		mRectangle.Y += y;
	}


	public void scale( float x, float y) {
		mScale.X *= x;
		mScale.Y *= y;
	}


}
