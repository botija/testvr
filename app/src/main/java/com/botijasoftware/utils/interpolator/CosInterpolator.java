package com.botijasoftware.utils.interpolator;

public class CosInterpolator implements Interpolator {
	//public abstract Interpolator() {}
	public float interpolate(float v0, float v1, float t) {
		float a = (1-(float)Math.cos(t*(float)Math.PI))/2.0f;
		return v0 + a*(v1 - v0);
	}
}
