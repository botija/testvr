package com.botijasoftware.utils;

public class ColorHSV {

    public ColorHSV() {}

    public ColorHSV(float c) {
        H = c;
        S = c;
        V = c;
    }

    public ColorHSV(float h, float s, float v) {
        H = h;
        S = s;
        V = v;
    }

    public void setValue(float c) {
        H = c;
        S = c;
        V = c;
    }

    public void setValue(float h, float s, float v) {
        H = h;
        S = s;
        V = v;
    }


    public ColorHSV clone() {
        return new ColorHSV(H, S, V);
    }

    public void saturate() {
       S = 1.0f;
    }

    public ColorRGB toRGB() {
        ColorRGB rgb = new ColorRGB(0);

        float c = V * S;
        float x = c * ( 1 - Math.abs((H / 60.0f) % 2 - 1));
        float m = V - c;

        if (H < 60.0f) {
            rgb.R = c;
            rgb.G = x;
            rgb.B = 0.0f;
        }
        else if (H < 120.0f) {
            rgb.R = x;
            rgb.G = c;
            rgb.B = 0.0f;
        }
        else if (H < 180.0f) {
            rgb.R = 0.0f;
            rgb.G = c;
            rgb.B = x;
        }
        else if (H < 240.0f) {
            rgb.R = 0.0f;
            rgb.G = x;
            rgb.B = c;
        }
        else if (H < 300.0f) {
            rgb.R = x;
            rgb.G = 0.0f;
            rgb.B = c;
        }
        else if (H < 360.0f) {
            rgb.R = c;
            rgb.G = 0.0f;
            rgb.B = x;
        }

        return rgb;
    }

    public float H;
    public float S;
    public float V;

    public static final ColorHSV BLACK = new ColorHSV(0.0f, 0.0f, 0.0f);
    public static final ColorHSV WHITE = new ColorHSV(0.0f, 0.0f, 1.0f);
    public static final ColorHSV RED = new ColorHSV(0.0f,1.0f,1.0f);
    public static final ColorHSV LIME = new ColorHSV(120.0f,1.0f,1.0f);
    public static final ColorHSV BLUE = new ColorHSV(240.0f,1.0f,1.0f);
    public static final ColorHSV YELLOW = new ColorHSV(60.0f,1.0f,1.0f);
    public static final ColorHSV CYAN = new ColorHSV(180.0f,1.0f,1.0f);
    public static final ColorHSV MAGENTA = new ColorHSV(300.0f,1.0f,1.0f);
    public static final ColorHSV SILVER = new ColorHSV(0.0f,0.0f,0.75f);
    public static final ColorHSV GRAY = new ColorHSV(0.0f,0.0f,0.5f);
    public static final ColorHSV MAROON = new ColorHSV(0.0f,1.0f,0.5f);
    public static final ColorHSV OLIVE = new ColorHSV(60.0f,1.0f,0.5f);
    public static final ColorHSV GREEN = new ColorHSV(120.0f,1.0f,0.5f);
    public static final ColorHSV PURPLE = new ColorHSV(300.0f,1.0f,0.5f);
    public static final ColorHSV TEAL = new ColorHSV(180.0f, 1.0f,0.5f);
    public static final ColorHSV NAVY = new ColorHSV(240.0f,1.0f, 0.5f);

}
