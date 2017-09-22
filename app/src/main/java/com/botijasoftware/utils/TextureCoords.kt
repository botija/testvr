package com.botijasoftware.utils

class TextureCoords {

    constructor() {

    }

    constructor(S0: Float, T0: Float, S1: Float, T1: Float) {
        s0 = S0
        t0 = T0
        s1 = S1
        t1 = T1
    }

    var s0 = 0.0f
    var t0 = 1.0f
    var s1 = 1.0f
    var t1 = 0.0f

    fun flipVertical() {
        val tmp = t0
        t0 = t1
        t1 = tmp
    }

    fun flipHorizontal() {
        val tmp = s0
        s0 = s1
        s1 = tmp
    }

    fun clone(): TextureCoords {
        return TextureCoords(s0, t0, s1, t1)
    }

}