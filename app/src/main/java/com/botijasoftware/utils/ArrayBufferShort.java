package com.botijasoftware.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class ArrayBufferShort extends ArrayBuffer {
	private ShortBuffer mBuffer;
	private final static int short_size = Short.SIZE/8;

	public ArrayBufferShort(VertexBufferDefinition vbdefinition, int maxelements) {
		super(vbdefinition, maxelements);

		mBuffer = ByteBuffer.allocateDirect(
				vbdefinition.getDataSize() * vbdefinition.mSize * maxelements * short_size)
				.order(ByteOrder.nativeOrder()).asShortBuffer();
	}
	
	public ShortBuffer getBuffer() {
		return mBuffer;
	}
	
	public void put(int index, short value) {
		mBuffer.put(index, value);
	}
	
	public void put(short[] data, int offset) {
		mBuffer.put(data, offset, data.length - offset);
		mBuffer.position(0);
	}
	
	public void put(short[] data) {
		mBuffer.put(data, 0, data.length);
		mBuffer.position(0);
	}

	public void put(byte value) {
		mBuffer.put(value);
	}
	
	public void put(short value) {
		mBuffer.put( value);
	}
	
	public void put(int value) {
		mBuffer.put((short) value);
	}
	
	public void put(float value) {
		mBuffer.put((short)value);
	}
	
	public void position(int i) {
		mBuffer.position(i);
	}
	
	public int position() {
		return mBuffer.position();
	}


}