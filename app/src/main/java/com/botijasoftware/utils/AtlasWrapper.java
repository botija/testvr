package com.botijasoftware.utils;


//simple class that allows the use of a texture as a 1x1 atlas 
public class AtlasWrapper extends Atlas {
	public AtlasWrapper(Texture texture) {
		super(texture);
		mNumTextures = 1;
		mCoords = new TextureCoords[1];
		mCoords[0] = texture.getTextureCoords();
	}
}