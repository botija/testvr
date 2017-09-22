package com.botijasoftware.utils.interpolator

class LinearInterpolator : Interpolator {
    //public abstract Interpolator() {}
    override fun interpolate(v0: Float, v1: Float, t: Float): Float {
        return v0 + t * (v1 - v0)
    }
}
