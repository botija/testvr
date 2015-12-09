package com.botijasoftware.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class ArrayBufferInt extends ArrayBuffer {
	private IntBuffer mBuffer;
	private final static int int_size = Integer.SIZE/8;
	

	public ArrayBufferInt(VertexBufferDefinition vbdefinition, int maxelements) {
		super(vbdefinition, maxelements);

		mBuffer = ByteBuffer.allocateDirect(
				vbdefinition.getDataSize() * vbdefinition.mSize * maxelements * int_size)
				.order(ByteOrder.nativeOrder()).asIntBuffer();
	}
	
	public IntBuffer getBuffer() {
		return mBuffer;
	}
	
	public void put(int index, int value) {
		mBuffer.put(index, value);
	}
	
	public void put(int[] data, int offset) {
		mBuffer.put(data, offset, data.length - offset);
		mBuffer.position(0);
	}
	
	public void put(int[] data) {
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
		mBuffer.put((int)value);
	}
	
	public void position(int i) {
		mBuffer.position(i);
	}
	
	public int position() {
		return mBuffer.position();
	}

}