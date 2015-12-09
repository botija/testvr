package com.botijasoftware.utils.interpolator;

public class LinearInterpolator implements Interpolator {
	//public abstract Interpolator() {}
	public float interpolate(float v0, float v1, float t) {
		return v0 + t*(v1 - v0);
	}
}
