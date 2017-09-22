package com.botijasoftware.utils

class ColorRGBA {

    constructor() {
        A = 0.0f
        B = A
        G = B
        R = G
    }

    constructor(c: Float) {
        R = c
        G = c
        B = c
        A = c
    }

    constructor(r: Float, g: Float, b: Float, a: Float) {
        R = r
        G = g
        B = b
        A = a
    }

    constructor(c: ColorRGBA) {
        R = c.R
        G = c.G
        B = c.B
        A = c.A
    }

    fun setValue(c: Float) {
        R = c
        G = c
        B = c
        A = c
    }

    fun setValue(r: Float, g: Float, b: Float, a: Float) {
        R = r
        G = g
        B = b
        A = a
    }

    fun clone(): ColorRGBA {
        return ColorRGBA(R, G, B, A)
    }

    fun saturate() {
        var m = R + G + B / 3.0f
        if (m != 0.0f) {
            m = 1.0f / m
            R *= m
            G *= m
            B *= m
        }
    }

    var R: Float = 0.toFloat()
    var G: Float = 0.toFloat()
    var B: Float = 0.toFloat()
    var A: Float = 0.toFloat()

    companion object {


        val BLACK = ColorRGBA(0.0f, 0.0f, 0.0f, 1.0f)
        val WHITE = ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f)
        val RED = ColorRGBA(1.0f, 0.0f, 0.0f, 1.0f)
        val LIME = ColorRGBA(0.0f, 1.0f, 0.0f, 1.0f)
        val BLUE = ColorRGBA(0.0f, 0.0f, 1.0f, 1.0f)
        val YELLOW = ColorRGBA(1.0f, 1.0f, 0.0f, 1.0f)
        val CYAN = ColorRGBA(0.0f, 1.0f, 1.0f, 1.0f)
        val MAGENTA = ColorRGBA(1.0f, 0.0f, 1.0f, 1.0f)
        val SILVER = ColorRGBA(0.75f, 0.75f, 0.75f, 1.0f)
        val GRAY = ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f)
        val MAROON = ColorRGBA(0.5f, 0.0f, 0.0f, 1.0f)
        val OLIVE = ColorRGBA(0.5f, 0.5f, 0.0f, 1.0f)
        val GREEN = ColorRGBA(0.0f, 0.5f, 0.0f, 1.0f)
        val PURPLE = ColorRGBA(0.5f, 0.0f, 0.5f, 1.0f)
        val TEAL = ColorRGBA(0.0f, 0.5f, 0.5f, 1.0f)
        val NAVY = ColorRGBA(0.0f, 0.0f, 0.5f, 1.0f)
        val CORNFLOWERBLUE = ColorRGBA(0.39f, 0.58f, 0.93f, 1.0f)
    }
}

