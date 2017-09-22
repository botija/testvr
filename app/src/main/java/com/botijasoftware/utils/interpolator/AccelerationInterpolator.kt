package com.botijasoftware.utils.interpolator

class AccelerationInterpolator : Interpolator {
    //public abstract Interpolator() {}
    override fun interpolate(v0: Float, v1: Float, t: Float): Float {
        val a = t * t
        return v0 + a * (v1 - v0)
    }
}
