package com.botijasoftware.utils;

import android.opengl.GLES20;

public class RenderTargetScreen extends RenderTarget {

	
	public RenderTargetScreen(int width, int height) {
		super(width, height);
	}
	
	@Override
	public void init() {}

	@Override
	public void setTarget() {
		
	}

	@Override
	public void clear() {
		GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void initRender() {
		
	}

	@Override
	public void endRender() {
		
	}
	
}