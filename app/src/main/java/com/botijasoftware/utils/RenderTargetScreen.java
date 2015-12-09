package com.botijasoftware.utils;

import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLES20;

public class RenderTargetScreen extends RenderTarget {

	
	public RenderTargetScreen(int width, int height) {
		super(width, height);
	}
	
	@Override
	public void init(GL10 gl) {}

	@Override
	public void setTarget(GL10 gl) {
		
	}

	@Override
	public void clear(GL10 gl) {
		GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void initRender(GL10 gl) {
		
	}

	@Override
	public void endRender(GL10 gl) {
		
	}
	
}