package com.botijasoftware.utils;

public class TextureCoords {
	
	public TextureCoords() {
		
	}
	
	public TextureCoords(float S0, float T0, float S1, float T1) {
		s0 = S0;
		t0 = T0;
		s1 = S1;
		t1 = T1;
	}
	
	public float s0 = 0.0f;
	public float t0 = 1.0f;
	public float s1 = 1.0f;
	public float t1 = 0.0f;
	
	public void flipVertical() {
		float tmp = t0;
		t0 = t1;
		t1 = tmp;
	}

    public void flipHorizontal() {
        float tmp = s0;
        s0 = s1;
        s1 = tmp;
    }

    public TextureCoords clone() {
        return new TextureCoords( s0, t0, s1, t1);
    }
	
}