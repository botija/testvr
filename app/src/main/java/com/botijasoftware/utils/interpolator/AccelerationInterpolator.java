package com.botijasoftware.utils.interpolator;

public class AccelerationInterpolator implements Interpolator {
	//public abstract Interpolator() {}
	public float interpolate(float v0, float v1, float t) {
		float a = t*t;
		return v0 + a*(v1 - v0);
	}
}
