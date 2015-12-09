package com.botijasoftware.utils;

import android.opengl.GLES20;

import com.botijasoftware.utils.renderer.Renderer;

public class VertexBufferDefinition {

	public final static int FLOAT = 0;
	public final static int INT = 1;
	public final static int SHORT = 2;
	public final static int BYTE = 3;
	public final static int[] sizes = { Float.SIZE/8, Integer.SIZE/8, Short.SIZE/8, Byte.SIZE/8 };
	public final static int[] formats = { GLES20.GL_FLOAT, GLES20.GL_SHORT, GLES20.GL_UNSIGNED_SHORT, GLES20.GL_UNSIGNED_BYTE };
	
	public static final int DEF_VERTEX = 0;
	public static final int DEF_TEXTURE_COORD = 1;
	public static final int DEF_COLOR = 2;
	public static final int DEF_NORMAL = 3;
	public static final int DEF_TANGENT = 4;
	
	//public static final int[] usage = { GLES10.GL_VERTEX_ARRAY,GLES10.GL_TEXTURE_COORD_ARRAY, GLES10.GL_COLOR_ARRAY,GLES10.GL_NORMAL_ARRAY, 0, 0 };

	public static final int ACCESS_STATIC = 0;
	public static final int ACCESS_DYNAMIC = 1;
	public static final int[] access = { GLES20.GL_STATIC_DRAW, GLES20.GL_DYNAMIC_DRAW };
	
	public final static VertexBufferDefinition VBD_VERTEX = new VertexBufferDefinition(FLOAT, Renderer.ATTRIBUTE_VERTEX, 3, ACCESS_DYNAMIC);
	public final static VertexBufferDefinition VBD_TEXTURE_COORD = new VertexBufferDefinition(FLOAT, Renderer.ATTRIBUTE_TEXCOORDS, 2, ACCESS_DYNAMIC);
	public final static VertexBufferDefinition VBD_COLOR_UBRGBA = new VertexBufferDefinition(BYTE, Renderer.ATTRIBUTE_COLOR, 4, ACCESS_DYNAMIC);
	public final static VertexBufferDefinition VBD_COLOR_UBRGB = new VertexBufferDefinition(BYTE, Renderer.ATTRIBUTE_COLOR, 3, ACCESS_DYNAMIC);
	public final static VertexBufferDefinition VBD_COLOR_RGBA = new VertexBufferDefinition(FLOAT, Renderer.ATTRIBUTE_COLOR, 4, ACCESS_DYNAMIC);
	public final static VertexBufferDefinition VBD_COLOR_RGB = new VertexBufferDefinition(FLOAT, Renderer.ATTRIBUTE_COLOR, 3, ACCESS_DYNAMIC);
	public final static VertexBufferDefinition VBD_NORMAL = new VertexBufferDefinition(FLOAT, Renderer.ATTRIBUTE_NORMAL, 3, ACCESS_DYNAMIC);
	
	public int mFormat;
	public int mUsage;
	public int mSize;
	public int mAccess;
    
    
    public VertexBufferDefinition(int format, int usage, int size, int access) {
    	mFormat = format;
    	mUsage = usage;
    	mSize = size;
    	mAccess = access;
    }
    
    public void setUsage(int usage) {
    	mUsage = usage;
    }
    
    public void setAccess(int access) {
    	mAccess = access;
    }
    
    public int getDataSize() {
    	return sizes[mFormat];
    }
    
    public int getUsageGL() {
    	return mUsage;
    }
    
    public int getFormatGL() {
    	return formats[mFormat];
    }
    
    public int getAccessGL() {
    	return access[mAccess];
    }
   
   
    public VertexBufferDefinition clone() {
    	return new VertexBufferDefinition(mFormat, mUsage, mSize, mAccess);
    }

}
