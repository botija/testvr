package com.botijasoftware.utils;

public class GLMatrixStack {
	public int MAX_STACK = 12;
	private GLMatrix[] stack = new GLMatrix[MAX_STACK];
	private int index;
	
	public GLMatrixStack() {
		for (int i=0; i<MAX_STACK;i++) {
			stack[i] = new GLMatrix();
		}
		index = 0;
	}
	
	public void pushMatrix(GLMatrix m) {
		if (index < MAX_STACK) {
			stack[index++].restore(m);
		}
	}
	
	public void popMatrix(GLMatrix m) {
		if (index > 0) {
			stack[--index].save(m);
		}
	}
}