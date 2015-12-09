package com.botijasoftware.utils.interpolator;

public interface Interpolator {
	//public abstract Interpolator() {}
	public abstract float interpolate(float v0, float v1, float t);
}
