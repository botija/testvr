package com.botijasoftware.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class ArrayBufferFloat extends ArrayBuffer {
	private FloatBuffer mBuffer;
	private final static int float_size = Float.SIZE/8;

	public ArrayBufferFloat(VertexBufferDefinition vbdefinition, int maxelements) {
		super(vbdefinition,maxelements);

		mBuffer = ByteBuffer.allocateDirect(
				vbdefinition.getDataSize() * vbdefinition.mSize * maxelements * float_size)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
	}
	
	public FloatBuffer getBuffer() {
		return mBuffer;
	}
	
	public void put(int index, float value) {
		mBuffer.put(index, value);
	}
	
	public void put(float[] data, int offset) {
		mBuffer.put(data, offset, data.length - offset);
		mBuffer.position(0);
	}
	
	public void put(float[] data) {
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
		mBuffer.put(value);
	}
	
	public void put(float value) {
		mBuffer.put(value);
	}
	
	public void position(int i) {
		mBuffer.position(i);
	}
	
	public int position() {
		return mBuffer.position();
	}

}