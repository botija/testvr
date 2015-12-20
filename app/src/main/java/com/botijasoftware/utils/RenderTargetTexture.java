package com.botijasoftware.utils;

import android.opengl.GLES20;


public class RenderTargetTexture extends RenderTarget {

	private Texture target = null;
	private int color_texture[] = new int[1];
	
	public RenderTargetTexture(int width, int height) {
		super(width, height);
	}

	public void init() {

		GLES20.glGenTextures(1, color_texture, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, color_texture[0]);

		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width,
				height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);

		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
				GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
				GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		
		target = new Texture(color_texture[0], width, height);

	}

	@Override
	public void setTarget() {

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initRender() {
		// TODO Auto-generated method stub

	}

	@Override
	public void endRender() {
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, target.getID());
		GLES20.glCopyTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, 0, 0, width, height, 0);
		//GLES20.glCopyTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 1, 1, 0, 0, width, height);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

	}

	public Texture getTexture() {
		return target;
	}

}