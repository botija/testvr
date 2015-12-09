package com.botijasoftware.utils;

import java.nio.Buffer;

public abstract class IndexBuffer {

	protected int maxelements;
	protected int size;
	protected int type;
	protected int format;
	protected int elementsize;
	
	public IndexBuffer(int type, int maxelements) {
		this.type = type;
		this.maxelements = maxelements;
		this.size = maxelements;
	}
		
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		if (size >= 0) {
			this.size = size;
		}
		if (this.size > maxelements)
			this.size = maxelements;
	}
	
	public int getMaxElements() {
		return maxelements;
	}
	
	public int getType() {
		return type;
	}
	
	public int getFormat() {
		return format;
	}
	
	public abstract Buffer getBuffer();
	
	public void put(int index, byte value) {}
	public void put(byte[] data) {}
	public void put(int index, short value) {}
	public void put(short[] data) {}
	public void put(int index, int value) {}
	public void put(int[] data) {}
	public void put(byte value) {}
	public void put(short value) {}
	public void put(int value) {}
	
	public void position(int i) {}
	public int position() {return 0;}


}