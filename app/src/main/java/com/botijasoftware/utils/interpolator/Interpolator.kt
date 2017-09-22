package com.botijasoftware.utils.interpolator

interface Interpolator {
    //public abstract Interpolator() {}
    fun interpolate(v0: Float, v1: Float, t: Float): Float
}
