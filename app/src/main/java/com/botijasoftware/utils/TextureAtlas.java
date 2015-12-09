package com.botijasoftware.utils;

import javax.microedition.khronos.opengles.GL10;

import android.content.res.TypedArray;

public class TextureAtlas extends Atlas {
	
	public TextureAtlas(Texture texture, int atlasdef ) {
		super(texture);
		mNumTextures = 0;
		atlasDef = atlasdef;
	}
	
	/*loads atlas definition from xml file*/
	public void LoadContent(GL10 gl, ResourceManager resources) {
		TypedArray atlasinfo = resources.getContext().getResources().obtainTypedArray(atlasDef);
		mNumTextures = atlasinfo.getInt(0,0);
				
		mCoords = new TextureCoords[mNumTextures];
		
		for (int i = 1, j = 0; i <= mNumTextures*4; i+=4,j++) {
			mCoords[j] = new TextureCoords(atlasinfo.getFloat(i,0.0f),atlasinfo.getFloat(i+1,0.0f),atlasinfo.getFloat(i+2,0.0f),atlasinfo.getFloat(i+3,0.0f)); 
		}
		atlasinfo.recycle();
	}
	
	protected int atlasDef;

}

