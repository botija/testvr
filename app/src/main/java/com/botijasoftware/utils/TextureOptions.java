package com.botijasoftware.utils;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;

public class TextureOptions {
	
	
	public TextureOptions(int min, int max, int s, int t) {
		this(true, min, max, s, t);
	}
	
	public TextureOptions(boolean usemipmap, int min, int max, int s, int t) {
		mipmap = usemipmap;
		minFilter = min;
		maxFilter = max;
		wraps = s;
		wrapt = t;
		//mode = GLES20.GL_MODULATE;
	}
	
	public TextureOptions() {
		minFilter = default_options.minFilter;
		maxFilter = default_options.maxFilter;
		wraps = default_options.wraps;
		wrapt = default_options.wrapt;
		mipmap = false;
	}
	
	public void apply(GL10 gl) {
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, minFilter);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, maxFilter);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, wraps); 
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, wrapt); 
		//GLES20.glTexEnvf(GLES20.GL_TEXTURE_ENV, GLES20.GL_TEXTURE_ENV_MODE, mode);
	}
	
	public int minFilter;
	public int maxFilter;
	public int wraps;
	public int wrapt;
	public boolean mipmap;
	//public int mode;
	
	public final static TextureOptions nearest_clamp = new TextureOptions(GLES20.GL_NEAREST,GLES20.GL_NEAREST,GLES20.GL_CLAMP_TO_EDGE,GLES20.GL_CLAMP_TO_EDGE);
	public final static TextureOptions nearest_repeat = new TextureOptions(GLES20.GL_NEAREST,GLES20.GL_NEAREST,GLES20.GL_REPEAT,GLES20.GL_REPEAT);
	public final static TextureOptions linear_clamp = new TextureOptions(GLES20.GL_LINEAR,GLES20.GL_LINEAR,GLES20.GL_CLAMP_TO_EDGE,GLES20.GL_CLAMP_TO_EDGE);
	public final static TextureOptions linear_repeat = new TextureOptions(GLES20.GL_LINEAR,GLES20.GL_LINEAR,GLES20.GL_REPEAT,GLES20.GL_REPEAT);
	public final static TextureOptions bilinear_clamp = new TextureOptions(GLES20.GL_LINEAR_MIPMAP_NEAREST,GLES20.GL_LINEAR_MIPMAP_NEAREST,GLES20.GL_CLAMP_TO_EDGE,GLES20.GL_CLAMP_TO_EDGE);
	public final static TextureOptions bilinear_repeat = new TextureOptions(GLES20.GL_LINEAR_MIPMAP_NEAREST,GLES20.GL_LINEAR_MIPMAP_NEAREST,GLES20.GL_REPEAT,GLES20.GL_REPEAT);
	public final static TextureOptions trilinear_clamp = new TextureOptions(GLES20.GL_LINEAR_MIPMAP_LINEAR ,GLES20.GL_LINEAR_MIPMAP_LINEAR,GLES20.GL_CLAMP_TO_EDGE,GLES20.GL_CLAMP_TO_EDGE);
	public final static TextureOptions trilinear_repeat = new TextureOptions(GLES20.GL_LINEAR_MIPMAP_LINEAR,GLES20.GL_LINEAR_MIPMAP_LINEAR,GLES20.GL_REPEAT,GLES20.GL_REPEAT);
	public final static TextureOptions nearest_clamp_nomipmap = new TextureOptions(false, GLES20.GL_NEAREST,GLES20.GL_NEAREST,GLES20.GL_CLAMP_TO_EDGE,GLES20.GL_CLAMP_TO_EDGE);
	public final static TextureOptions nearest_repeat_nomipmap = new TextureOptions(false, GLES20.GL_NEAREST,GLES20.GL_NEAREST,GLES20.GL_REPEAT,GLES20.GL_REPEAT);
	public final static TextureOptions linear_clamp_nomipmap = new TextureOptions(false, GLES20.GL_LINEAR,GLES20.GL_LINEAR,GLES20.GL_CLAMP_TO_EDGE,GLES20.GL_CLAMP_TO_EDGE);
	public final static TextureOptions linear_repeat_nomipmap = new TextureOptions(false, GLES20.GL_LINEAR,GLES20.GL_LINEAR,GLES20.GL_REPEAT,GLES20.GL_REPEAT);
	public final static TextureOptions bilinear_clamp_nomipmap = new TextureOptions(false, GLES20.GL_LINEAR_MIPMAP_NEAREST,GLES20.GL_LINEAR_MIPMAP_NEAREST,GLES20.GL_CLAMP_TO_EDGE,GLES20.GL_CLAMP_TO_EDGE);
	public final static TextureOptions bilinear_repeat_nomipmap = new TextureOptions(false, GLES20.GL_LINEAR_MIPMAP_NEAREST,GLES20.GL_LINEAR_MIPMAP_NEAREST,GLES20.GL_REPEAT,GLES20.GL_REPEAT);
	public final static TextureOptions trilinear_clamp_nomipmap = new TextureOptions(false, GLES20.GL_LINEAR_MIPMAP_LINEAR ,GLES20.GL_LINEAR_MIPMAP_LINEAR,GLES20.GL_CLAMP_TO_EDGE,GLES20.GL_CLAMP_TO_EDGE);
	public final static TextureOptions trilinear_repeat_nomipmap = new TextureOptions(false, GLES20.GL_LINEAR_MIPMAP_LINEAR,GLES20.GL_LINEAR_MIPMAP_LINEAR,GLES20.GL_REPEAT,GLES20.GL_REPEAT);
	public final static TextureOptions default_options = linear_repeat;
	public final static TextureOptions default_options_nomipmap = linear_repeat_nomipmap;
}