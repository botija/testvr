package com.botijasoftware.utils;

import javax.microedition.khronos.opengles.GL10;

public class Font {

	public Font(int fontTexture, int size, Layout layout)
	{
	    fontTextureID = fontTexture;
		fontsize = size;
		mLayout = layout;
		font = null;
		atlas = null;
	}
	
	public void LoadContent(GL10 gl, ResourceManager resources) {
		font = resources.loadTexture( gl, fontTextureID );
		atlas = new SimpleTextureAtlas( font, 16, 16, 256 );
	}
	
	public Texture getTexture() {
		return font;
	}

    public Texture getTexture(int index) {
        return atlas.getAsTexture(index);
    }
	
	public TextureCoords getTextureCoords(int index) {
		return atlas.getTextureCoords(index);
	}
	
	public int getFontSize() {
		return fontsize;
	}
	
	private Texture font;
	private SimpleTextureAtlas atlas;
	private Layout mLayout;
	private int fontTextureID;
	private int fontsize;
}
