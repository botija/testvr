package com.botijasoftware.utils;


public class SimpleTextureAtlas extends Atlas {
	
	public SimpleTextureAtlas(Texture texture, int columns, int rows, int count ) {
		super(texture);
		ncols = columns;
		nrows = rows;
		mNumTextures = count;
		textX = (1.0f / ncols);
		textY = (1.0f / nrows);
		
		mCoords = new TextureCoords[mNumTextures];
		
		for (int i = 0; i < mNumTextures; i++) {
			int row = i / ncols;
			int column = i - (row * ncols);
			mCoords[i] = new TextureCoords(textX * column,textY * (row+1),textX * (column+1),textY * row );
		}
		
	}
	
	protected int ncols;
	protected int nrows;
	protected float textX;
	protected float textY;
}
