package com.botijasoftware.utils;

import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

public abstract class RenderTarget {
	public int width;
	public int height;
	
	public RenderTarget(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public abstract void init();
	public abstract void setTarget();
	public abstract void clear();
	public abstract void initRender();
	public abstract void endRender();
	public Texture getTexture() {
		return null;
	}
	
	public static boolean checkFBO() {
		return checkExtension("GL_OES_framebuffer_object");
	}
	
    private static boolean checkExtension(String extension) {
        String extensions = " " + GLES20.glGetString(GLES20.GL_EXTENSIONS) + " ";
        return extensions.contains(" " + extension + " ");
    }

	
}