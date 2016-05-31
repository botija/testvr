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

	public ColorHSV toHSV() {
		ColorHSV hsv = new ColorHSV(0);
		float max = Math.max(R, Math.max(G,B));
		float min = Math.min(R, Math.min(G,B));

		float delta = max - min;

		float h = 0.0f;
		if (delta != 0) {
            if (max == R) {
                h = 60 * (((G - B) / delta) % 6);
            }
            else if (max == G) {
                h = 60 * (((B - R) / delta) + 2);
            }
            else if (max == B) {
                h = 60 * (((R - G) / delta) + 4);
            }

		}

		float s = 0.0f;
		if (max != 0.0f) {
			s = delta / max;
		}

		hsv.S = s;
		hsv.V = max;
		return hsv;
	}
	
	public float R;
	public float G;
	public float B;
	
	public static final ColorRGB BLACK = new ColorRGB(0.0f, 0.0f, 0.0f);
	public static final ColorRGB WHITE = new ColorRGB(1.0f, 1.0f, 1.0f);
    public static final ColorRGB RED = new ColorRGB(1.0f, 0.0f, 0.0f);
    public static final ColorRGB LIME = new ColorRGB(0.0f, 1.0f, 0.0f);
    public static final ColorRGB BLUE = new ColorRGB(0.0f, 0.0f, 1.0f);
    public static final ColorRGB YELLOW = new ColorRGB(1.0f, 1.0f, 0.0f);
    public static final ColorRGB CYAN = new ColorRGB(0.0f, 1.0f, 1.0f);
    public static final ColorRGB MAGENTA = new ColorRGB(1.0f, 0.0f, 1.0f);
    public static final ColorRGB SILVER = new ColorRGB(0.75f, 0.75f, 0.75f);
    public static final ColorRGB GRAY = new ColorRGB(0.5f, 0.5f, 0.5f);
    public static final ColorRGB MAROON = new ColorRGB(0.5f, 0.0f, 0.0f);
    public static final ColorRGB OLIVE = new ColorRGB(0.5f, 0.5f, 0.0f);
    public static final ColorRGB GREEN = new ColorRGB(0.0f, 0.5f, 0.0f);
    public static final ColorRGB PURPLE = new ColorRGB(0.5f, 0.0f, 0.5f);
    public static final ColorRGB TEAL = new ColorRGB(0.0f, 0.5f, 0.5f);
    public static final ColorRGB NAVY = new ColorRGB(0.0f, 0.0f, 0.5f);
	
}
