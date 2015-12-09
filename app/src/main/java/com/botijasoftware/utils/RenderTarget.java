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
	
	public abstract void init(GL10 gl);
	public abstract void setTarget(GL10 gl);
	public abstract void clear(GL10 gl);
	public abstract void initRender(GL10 gl);
	public abstract void endRender(GL10 gl);
	public Texture getTexture() {
		return null;
	}
	
	public static boolean checkFBO(GL10 gl) {
		return checkExtension(gl,"GL_OES_framebuffer_object");
	}
	
    private static boolean checkExtension(GL10 gl, String extension) {
        String extensions = " " + GLES20.glGetString(GLES20.GL_EXTENSIONS) + " ";
        return extensions.contains(" " + extension + " ");
    }

	
}