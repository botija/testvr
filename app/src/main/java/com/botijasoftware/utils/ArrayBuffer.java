package com.botijasoftware.utils;

import java.nio.Buffer;

public abstract class ArrayBuffer {
	protected VertexBufferDefinition vbdefinition;
	//protected Buffer mBuffer;
	protected int maxelements;

	public ArrayBuffer(VertexBufferDefinition vbdefinition, int maxelements) {
		this.vbdefinition = vbdefinition;
		this.maxelements = maxelements;
	}
	
	public abstract Buffer getBuffer();
	
	public void put(int index, float value) {}
	public void put(float[] data, int offset) {}
	public void put(float[] data) {}
	public void put(int index, int value) {}
	public void put(int[] data, int offset) {}
	public void put(int[] data) {}
	public void put(int index, short value) {}
	public void put(short[] data, int offset) {}
	public void put(short[] data) {}
	public void put(int index, byte value) {}
	public void put(byte[] data, int offset) {}
	public void put(byte[] data) {}
	
	public void put(byte value) {}
	public void put(short value) {}
	public void put(int value) {}
	public void put(float value) {}
	
	public void position(int i) {}
	public int position() {return 0;}

}