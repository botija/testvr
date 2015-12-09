package com.botijasoftware.utils;

public class ColorRGBAb {
	
	public ColorRGBAb() {}

	public ColorRGBAb(byte c) {
		R = c;
		G = c;
		B = c;
		A = (byte)255;
	}

	public ColorRGBAb(byte r, byte g, byte b, byte a) {
		R = r;
		G = g;
		B = b;
		A = a;
	}

	public void setValue(byte c) {
		R = c;
		G = c;
		B = c;
		A = (byte)255;
	}

	public void setValue(byte r, byte g, byte b, byte a) {
		R = r;
		G = g;
		B = b;
		A = a;
	}


	public ColorRGBAb clone() {
		return new ColorRGBAb(R,G,B,A);
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
	public byte A;
	
	public static final ColorRGBAb BLACK = new ColorRGBAb((byte)0, (byte)0, (byte)0, (byte)255);
	public static final ColorRGBAb WHITE = new ColorRGBAb((byte)255, (byte)255, (byte)255, (byte)255);
	
}
