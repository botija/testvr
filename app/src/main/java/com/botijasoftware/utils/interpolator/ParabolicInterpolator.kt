package com.botijasoftware.utils.interpolator

class ParabolicInterpolator(//public abstract Interpolator() {}
        private val amount: Float) : Interpolator {
    override fun interpolate(v0: Float, v1: Float, t: Float): Float {

        return v0 + t * (v1 - v0) + amount * Math.sin(Math.PI * t).toFloat() * (v1 - v0)

    }
}
