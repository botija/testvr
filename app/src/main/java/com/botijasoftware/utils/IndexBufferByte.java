package com.botijasoftware.utils;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import android.opengl.GLES20;

public class IndexBufferByte extends IndexBuffer {
	private ByteBuffer mBuffer;
	

	public IndexBufferByte(int type, int maxelements) {
		super(type, maxelements);
		if (maxelements > Byte.MAX_VALUE)
			this.maxelements = Byte.MAX_VALUE;
		format = GLES20.GL_BYTE;
		elementsize = Byte.SIZE/8;
		mBuffer = ByteBuffer.allocateDirect(maxelements*elementsize).order(
				ByteOrder.nativeOrder());

	}

	public ByteBuffer getBuffer() {
		return mBuffer;
	}
	
	public void put(int index, byte value) {
		mBuffer.put(index, value);
	}
	
	public void put(int index, short value) {
		mBuffer.put(index, (byte) value);
	}
	
	public void put(int index, int value) {
		mBuffer.put(index, (byte)value);
	}
		
	public void put(byte[] data) {
		mBuffer.put(data, 0, data.length);
	}
	
	public void put(byte value) {
		mBuffer.put(value);
	}
	
	public void put(short value) {
		mBuffer.put((byte) value);
	}
	
	public void put(int value) {
		mBuffer.put((byte) value);
	}
	
	public void position(int i) {
		mBuffer.position(i);
	}
	
	public int position() {
		return mBuffer.position();
	}

}