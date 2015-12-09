package com.botijasoftware.utils;


import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ByteOrder;
import android.opengl.GLES20;

public class IndexBufferInt extends IndexBuffer {
	
	private IntBuffer mBuffer;

	public IndexBufferInt(int type, int maxelements) {
		super(type, maxelements);
		format = GLES20.GL_SHORT;
		elementsize = Integer.SIZE/8;
		mBuffer = ByteBuffer.allocateDirect(maxelements*elementsize).order(
				ByteOrder.nativeOrder()).asIntBuffer();

	}

	public IntBuffer getBuffer() {
		return mBuffer;
	}

	public void put(int index, byte value) {
		mBuffer.put(index, value);
	}
	
	public void put(int index, short value) {
		mBuffer.put(index, value);
	}
	
	public void put(int index, int value) {
		mBuffer.put(index, value);
	}
	
	public void put(int[] data) {
		mBuffer.put(data, 0, data.length);
	}
	
	public void put(byte value) {
		mBuffer.put(value);
	}
	
	public void put(short value) {
		mBuffer.put(value);
	}
	
	public void put(int value) {
		mBuffer.put( value);
	}
	
	public void position(int i) {
		mBuffer.position(i);
	}
	
	public int position() {
		return mBuffer.position();
	}

}