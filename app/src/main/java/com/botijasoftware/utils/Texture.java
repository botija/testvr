package com.botijasoftware.utils;

public class Texture {
	
	public Texture(int id, int w, int h) {
		mID = id;
		width = w;
		height = h;
		textCoords = new TextureCoords();
	}
	
	public Texture(int id, int w, int h, TextureCoords tc) {
		mID = id;
		width = w;
		height = h;
		textCoords = tc;		
	}
	
	public int getID() {
		return mID;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public TextureCoords getTextureCoords() {
		return textCoords;
	}

    /*public Texture clone() {

        return new Texture(mID, width, height, textCoords.clone());

    }*/

    public TextureRegion subTexture(float S0, float T0, float S1, float T1) {
        return new TextureRegion(this, new TextureCoords(S0, T0, S1, T1) );
    }

    public void addReference() {
        refcount++;
    }

    public void removeReference() {
        refcount--;
        if (refcount<0)
            refcount = 0;
    }

    public boolean isReferenced() {
        return refcount>0;
    }

    public int getReferenceCount() {
        return refcount;
    }
		
	protected int mID;
	protected int width;
	protected int height;
	protected TextureCoords textCoords;
    protected int refcount = 0;
}