package com.botijasoftware.utils;

public class ColorRGBAb {
	
	public ColorRGBAb() {
        R = G = B = A = 0;
    }

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

    public ColorRGBAb(ColorRGBAb c) {
        R = c.R;
        G = c.G;
        B = c.B;
        A = c.A;
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
	public static final ColorRGBAb RED = new ColorRGBAb((byte)255,(byte)0,(byte)0, (byte)255);
	public static final ColorRGBAb LIME = new ColorRGBAb((byte)0,(byte)255,(byte)0, (byte)255);
	public static final ColorRGBAb BLUE = new ColorRGBAb((byte)0,(byte)0,(byte)255, (byte)255);
	public static final ColorRGBAb YELLOW = new ColorRGBAb((byte)255,(byte)255,(byte)0, (byte)255);
	public static final ColorRGBAb CYAN = new ColorRGBAb((byte)0,(byte)255,(byte)255, (byte)255);
	public static final ColorRGBAb MAGENTA = new ColorRGBAb((byte)255,(byte)0,(byte)255, (byte)255);
	public static final ColorRGBAb SILVER = new ColorRGBAb((byte)192,(byte)192,(byte)192, (byte)255);
	public static final ColorRGBAb GRAY = new ColorRGBAb((byte)128,(byte)128,(byte)128, (byte)255);
	public static final ColorRGBAb MAROON = new ColorRGBAb((byte)128,(byte)0,(byte)0, (byte)255);
	public static final ColorRGBAb OLIVE = new ColorRGBAb((byte)128,(byte)128,(byte)0, (byte)255);
	public static final ColorRGBAb GREEN = new ColorRGBAb((byte)0,(byte)128,(byte)0, (byte)255);
	public static final ColorRGBAb PURPLE = new ColorRGBAb((byte)128,(byte)0,(byte)128, (byte)255);
	public static final ColorRGBAb TEAL = new ColorRGBAb((byte)0,(byte)128,(byte)128, (byte)255);
	public static final ColorRGBAb NAVY = new ColorRGBAb((byte)0,(byte)0,(byte)128, (byte)255);
	public static final ColorRGBAb CORNFLOWERBLUE = new ColorRGBAb((byte)100,(byte)149,(byte)237, (byte)255);
	
}
