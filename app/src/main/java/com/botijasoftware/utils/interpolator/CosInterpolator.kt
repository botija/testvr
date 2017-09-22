package com.botijasoftware.utils.interpolator

class CosInterpolator : Interpolator {
    //public abstract Interpolator() {}
    override fun interpolate(v0: Float, v1: Float, t: Float): Float {
        val a = (1 - Math.cos((t * Math.PI.toFloat()).toDouble()).toFloat()) / 2.0f
        return v0 + a * (v1 - v0)
    }
}
