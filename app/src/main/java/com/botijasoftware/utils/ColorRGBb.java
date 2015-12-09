package com.botijasoftware.utils;

public class ColorRGBb {
	
	public ColorRGBb() {}

	public ColorRGBb(byte c) {
		R = c;
		G = c;
		B = c;
	}

	public ColorRGBb(byte r, byte g, byte b) {
		R = r;
		G = g;
		B = b;
	}

	public void setValue(byte c) {
		R = c;
		G = c;
		B = c;
	}

	public void setValue(byte r, byte g, byte b) {
		R = r;
		G = g;
		B = b;
	}


	public ColorRGBb clone() {
		return new ColorRGBb(R,G,B);
	}
	
	public void saturate() {
		float m = (R + G + B / 3.0f);
		if (m != 0.0f) {
			m = 1.0f/m;
			R = (byte) (R * m);
			G = (byte) (G * m);
			B = (byte) (B * m);
		}
	}
	
	public byte R;
	public byte G;
	public byte B;
	
	public static final ColorRGBb BLACK = new ColorRGBb((byte)0, (byte)0, (byte)0);
	public static final ColorRGBb WHITE = new ColorRGBb((byte)255, (byte)255, (byte)255);
	
}
