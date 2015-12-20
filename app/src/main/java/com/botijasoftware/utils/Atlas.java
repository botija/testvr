package com.botijasoftware.utils;

public class Atlas {

	public Atlas(Texture texture) {
		mTexture = texture;
	}
	
	public void LoadContent(ResourceManager resources) {
	}
	
	public int getTextureID() {
		return mTexture.getID();
	}
	
	public void flipCoords() {
	
		for (int i = 0; i < mNumTextures; i++) {
			mCoords[i].flipVertical(); 
		}
	}
	
	public int getTextureCount() {
		return mNumTextures;
	}
	
	
	public TextureCoords getTextureCoords(int textLinear) {
		return mCoords[textLinear];
	}

	public TextureRegion getAsTexture(int textLinear) {
		return new TextureRegion(mTexture, getTextureCoords(textLinear));
	}
	
	
	protected Texture mTexture;
	protected TextureCoords[] mCoords;
	protected int mNumTextures;
}