package com.botijasoftware.utils.interpolator;

public class DecelerationInterpolator implements Interpolator {
	//public abstract Interpolator() {}
	public float interpolate(float v0, float v1, float t) {
		float a = 1-((1-t)*(1-t));
		return v0 + a*(v1 - v0);
	}
}
