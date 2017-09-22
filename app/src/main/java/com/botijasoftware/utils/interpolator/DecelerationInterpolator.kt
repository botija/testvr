package com.botijasoftware.utils.interpolator

class DecelerationInterpolator : Interpolator {
    //public abstract Interpolator() {}
    override fun interpolate(v0: Float, v1: Float, t: Float): Float {
        val a = 1 - (1 - t) * (1 - t)
        return v0 + a * (v1 - v0)
    }
}
