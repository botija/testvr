package com.botijasoftware.utils.interpolator

class SmoothStepInterpolator : Interpolator {
    //public abstract Interpolator() {}
    override fun interpolate(v0: Float, v1: Float, t: Float): Float {
        val a = (3 - 2 * t) * t * t
        return v0 + a * (v1 - v0)
    }
}
