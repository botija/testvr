package com.botijasoftware.utils;


import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.nio.ByteOrder;
import android.opengl.GLES20;

public class IndexBufferShort extends IndexBuffer {
	private ShortBuffer mBuffer;

	public IndexBufferShort(int type, int maxelements) {
		super(type, maxelements);
		if (maxelements > Short.MAX_VALUE)
			this.maxelements = Short.MAX_VALUE;
		format = GLES20.GL_UNSIGNED_SHORT;
		elementsize = Short.SIZE/8;
		mBuffer = ByteBuffer.allocateDirect(maxelements*elementsize).order(
				ByteOrder.nativeOrder()).asShortBuffer();

	}

	public ShortBuffer getBuffer() {
		return mBuffer;
	}

	public void put(int index, byte value) {
		mBuffer.put(index, value);
	}
	
	public void put(int index, short value) {
		mBuffer.put(index,  value);
	}
	
	public void put(int index, int value) {
		mBuffer.put(index, (short)value);
	}
	
	public void put(short[] data) {
		mBuffer.put(data, 0, data.length);
		mBuffer.position(0);
	}
	
	public void put(byte value) {
		mBuffer.put(value);
	}
	
	public void put(short value) {
		mBuffer.put(value);
	}
	
	public void put(int value) {
		mBuffer.put((short) value);
	}
	
	public void position(int i) {
		mBuffer.position(i);
	}
	
	public int position() {
		return mBuffer.position();
	}


}