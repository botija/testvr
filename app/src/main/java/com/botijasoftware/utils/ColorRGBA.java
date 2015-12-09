package com.botijasoftware.utils;

public class ColorRGBA {
	
	public ColorRGBA() {}

	public ColorRGBA(float c) {
		R = c;
		G = c;
		B = c;
		A = c;
	}

	public ColorRGBA(float r, float g, float b, float a) {
		R = r;
		G = g;
		B = b;
		A = a;
	}

	public void setValue(float c) {
		R = c;
		G = c;
		B = c;
		A = c;
	}

	public void setValue(float r, float g, float b, float a) {
		R = r;
		G = g;
		B = b;
		A = a;
	}

	public ColorRGBA clone() {
		return new ColorRGBA(R,G,B,A);
	}	
	
	public void saturate() {
		float m = (R + G + B / 3.0f);
		if (m != 0.0f) {
			m = 1.0f/m;
			R *= m;
			G *= m;
			B *= m;
		}
	}
	
	public float R;
	public float G;
	public float B;
	public float A;

	
	public static final ColorRGBA BLACK = new ColorRGBA(0.0f, 0.0f, 0.0f, 1.0f);
	public static final ColorRGBA WHITE = new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f);
}

