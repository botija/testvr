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
		/*float m = (R + G + B / 3.0f);
		if (m != 0.0f) {
			m = 1.0f/m;
			R = (byte) (R * m);
			G = (byte) (G * m);
			B = (byte) (B * m);
		}*/
	}
	
	public byte R;
	public byte G;
	public byte B;
	
	public static final ColorRGBb BLACK = new ColorRGBb((byte)0, (byte)0, (byte)0);
	public static final ColorRGBb WHITE = new ColorRGBb((byte)255, (byte)255, (byte)255);
	public static final ColorRGBb RED = new ColorRGBb((byte)255,(byte)0,(byte)0);
	public static final ColorRGBb LIME = new ColorRGBb((byte)0,(byte)255,(byte)0);
	public static final ColorRGBb BLUE = new ColorRGBb((byte)0,(byte)0,(byte)255);
	public static final ColorRGBb YELLOW = new ColorRGBb((byte)255,(byte)255,(byte)0);
	public static final ColorRGBb CYAN = new ColorRGBb((byte)0,(byte)255,(byte)255);
	public static final ColorRGBb MAGENTA = new ColorRGBb((byte)255,(byte)0,(byte)255);
	public static final ColorRGBb SILVER = new ColorRGBb((byte)192,(byte)192,(byte)192);
	public static final ColorRGBb GRAY = new ColorRGBb((byte)128,(byte)128,(byte)128);
	public static final ColorRGBb MAROON = new ColorRGBb((byte)128,(byte)0,(byte)0);
	public static final ColorRGBb OLIVE = new ColorRGBb((byte)128,(byte)128,(byte)0);
	public static final ColorRGBb GREEN = new ColorRGBb((byte)0,(byte)128,(byte)0);
	public static final ColorRGBb PURPLE = new ColorRGBb((byte)128,(byte)0,(byte)128);
	public static final ColorRGBb TEAL = new ColorRGBb((byte)0,(byte)128,(byte)128);
	public static final ColorRGBb NAVY = new ColorRGBb((byte)0,(byte)0,(byte)128);
	
}
