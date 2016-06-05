package com.botijasoftware.utils;

public class ColorRGBA {
	
	public ColorRGBA() {
        R = G = B = A = 0.0f;
    }

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

    public ColorRGBA(ColorRGBA c) {
        R = c.R;
        G = c.G;
        B = c.B;
        A = c.A;
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
	public static final ColorRGBA RED = new ColorRGBA(1.0f, 0.0f, 0.0f, 1.0f);
	public static final ColorRGBA LIME = new ColorRGBA(0.0f, 1.0f, 0.0f, 1.0f);
	public static final ColorRGBA BLUE = new ColorRGBA(0.0f, 0.0f, 1.0f, 1.0f);
	public static final ColorRGBA YELLOW = new ColorRGBA(1.0f, 1.0f, 0.0f, 1.0f);
	public static final ColorRGBA CYAN = new ColorRGBA(0.0f, 1.0f, 1.0f, 1.0f);
	public static final ColorRGBA MAGENTA = new ColorRGBA(1.0f, 0.0f, 1.0f, 1.0f);
	public static final ColorRGBA SILVER = new ColorRGBA(0.75f, 0.75f, 0.75f, 1.0f);
	public static final ColorRGBA GRAY = new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f);
	public static final ColorRGBA MAROON = new ColorRGBA(0.5f, 0.0f, 0.0f, 1.0f);
	public static final ColorRGBA OLIVE = new ColorRGBA(0.5f, 0.5f, 0.0f, 1.0f);
	public static final ColorRGBA GREEN = new ColorRGBA(0.0f, 0.5f, 0.0f, 1.0f);
	public static final ColorRGBA PURPLE = new ColorRGBA(0.5f, 0.0f, 0.5f, 1.0f);
	public static final ColorRGBA TEAL = new ColorRGBA(0.0f, 0.5f, 0.5f, 1.0f);
	public static final ColorRGBA NAVY = new ColorRGBA(0.0f, 0.0f, 0.5f, 1.0f);
	public static final ColorRGBA CORNFLOWERBLUE = new ColorRGBA(0.39f, 0.58f, 0.93f, 1.0f);
}

