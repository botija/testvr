package com.botijasoftware.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ArrayBufferByte extends ArrayBuffer {
	private ByteBuffer mBuffer;

	public ArrayBufferByte(VertexBufferDefinition vbdefinition, int maxelements) {
		super(vbdefinition, maxelements);

		mBuffer = ByteBuffer.allocateDirect(
				vbdefinition.getDataSize() * vbdefinition.mSize * maxelements)
				.order(ByteOrder.nativeOrder());
	}
	
	public ByteBuffer getBuffer() {
		return mBuffer;
	}
	
	public void put(int index, byte value) {
		mBuffer.put(index, value);
	}
	
	public void put(byte[] data, int offset) {
		mBuffer.put(data, offset, data.length - offset);
		mBuffer.position(0);
	}
	
	public void put(byte[] data) {
		mBuffer.put(data, 0, data.length);
		mBuffer.position(0);
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
	
	public void put(float value) {
		mBuffer.put((byte)value);
	}
	
	public void position(int i) {
		mBuffer.position(i);
	}
	
	public int position() {
		return mBuffer.position();
	}

}