package com.botijasoftware.utils;

import javax.microedition.khronos.opengles.GL10;
import com.botijasoftware.utils.renderer.Renderer;

import android.opengl.GLES20;

public class Viewport {
	private int X, Y, width, height;

	public Viewport(int x, int y, int w, int h) {
		X = x;
		Y = y;
		width = w;
		height = h;
	}
	
	public void enable(GL10 gl) {
		GLES20.glViewport(X, Y, width, height);
		//GLES20.glLoadIdentity();
		Renderer.modelview.loadIdentity();
	}
	
	public void getAsArray(int[] viewport) {
		viewport[0] = X;
		viewport[1] = Y;
		viewport[2] = width;
		viewport[3] = height;
	}
}
