package com.botijasoftware.utils.interpolator;

public class ParabolicInterpolator implements Interpolator {
	//public abstract Interpolator() {}
    private float amount;

    public ParabolicInterpolator(float amount) {
        this.amount = amount;
    }
	public float interpolate(float v0, float v1, float t) {

		return v0 + t*(v1 - v0) +  amount * (float) Math.sin(Math.PI * t)*(v1 - v0);

	}
}
