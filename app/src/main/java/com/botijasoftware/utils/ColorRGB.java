package com.botijasoftware.utils;

public class ColorRGB {
	
	public ColorRGB() {}

	public ColorRGB(float c) {
		R = c;
		G = c;
		B = c;
	}

	public ColorRGB(float r, float g, float b) {
		R = r;
		G = g;
		B = b;
	}

	public void setValue(float c) {
		R = c;
		G = c;
		B = c;
	}

	public void setValue(float r, float g, float b) {
		R = r;
		G = g;
		B = b;
	}


	public ColorRGB clone() {
		return new ColorRGB(R,G,B);
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
	
	public static final ColorRGB BLACK = new ColorRGB(0.0f, 0.0f, 0.0f);
	public static final ColorRGB WHITE = new ColorRGB(1.0f, 1.0f, 1.0f);
	
}
