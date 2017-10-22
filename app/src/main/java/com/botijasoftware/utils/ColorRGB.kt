package com.botijasoftware.utils

class ColorRGB {

    constructor() {
        B = 0.0f
        G = 0.0f
        R = 0.0f
    }

    constructor(c: Float) {
        R = c
        G = c
        B = c
    }

    constructor(r: Float, g: Float, b: Float) {
        R = r
        G = g
        B = b
    }

    constructor(c: ColorRGB) {
        R = c.R
        G = c.G
        B = c.B
    }

    fun setValue(c: Float) {
        R = c
        G = c
        B = c
    }

    fun setValue(r: Float, g: Float, b: Float) {
        R = r
        G = g
        B = b
    }


    fun clone(): ColorRGB {
        return ColorRGB(R, G, B)
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

    fun toHSV(): ColorHSV {
        val hsv = ColorHSV(0f)
        val max = Math.max(R, Math.max(G, B))
        val min = Math.min(R, Math.min(G, B))

        val delta = max - min

        var h = 0.0f
        if (delta != 0f) {
            when (max) {
                R -> h = 60 * ((G - B) / delta % 6)
                G -> h = 60 * ((B - R) / delta + 2)
                B -> h = 60 * ((R - G) / delta + 4)
            }

        }

        var s = 0.0f
        if (max != 0.0f) {
            s = delta / max
        }

        hsv.H = h
        hsv.S = s
        hsv.V = max
        return hsv
    }

    var R: Float = 0.0f
    var G: Float = 0.0f
    var B: Float = 0.0f

    companion object {

        val BLACK = ColorRGB(0.0f, 0.0f, 0.0f)
        val WHITE = ColorRGB(1.0f, 1.0f, 1.0f)
        val RED = ColorRGB(1.0f, 0.0f, 0.0f)
        val LIME = ColorRGB(0.0f, 1.0f, 0.0f)
        val BLUE = ColorRGB(0.0f, 0.0f, 1.0f)
        val YELLOW = ColorRGB(1.0f, 1.0f, 0.0f)
        val CYAN = ColorRGB(0.0f, 1.0f, 1.0f)
        val MAGENTA = ColorRGB(1.0f, 0.0f, 1.0f)
        val SILVER = ColorRGB(0.75f, 0.75f, 0.75f)
        val GRAY = ColorRGB(0.5f, 0.5f, 0.5f)
        val MAROON = ColorRGB(0.5f, 0.0f, 0.0f)
        val OLIVE = ColorRGB(0.5f, 0.5f, 0.0f)
        val GREEN = ColorRGB(0.0f, 0.5f, 0.0f)
        val PURPLE = ColorRGB(0.5f, 0.0f, 0.5f)
        val TEAL = ColorRGB(0.0f, 0.5f, 0.5f)
        val NAVY = ColorRGB(0.0f, 0.0f, 0.5f)
        val CORNFLOWERBLUE = ColorRGB(0.39f, 0.58f, 0.93f)
    }

}
