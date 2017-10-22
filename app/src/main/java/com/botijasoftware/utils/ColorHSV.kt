package com.botijasoftware.utils

class ColorHSV {

    constructor() {
        V = 0.0f
        S = 0.0f
        H = 0.0f
    }

    constructor(c: Float) {
        H = c
        S = c
        V = c
    }

    constructor(h: Float, s: Float, v: Float) {
        H = h
        S = s
        V = v
    }

    fun setValue(c: Float) {
        H = c
        S = c
        V = c
    }

    fun setValue(h: Float, s: Float, v: Float) {
        H = h
        S = s
        V = v
    }


    fun clone(): ColorHSV {
        return ColorHSV(H, S, V)
    }

    fun saturate() {
        S = 1.0f
    }

    fun toRGB(): ColorRGB {
        val rgb = ColorRGB(0f)

        val c = V * S
        val x = c * (1 - Math.abs(H / 60.0f % 2 - 1))
        val m = V - c

        when {
            H < 60.0f -> {
                rgb.R = c
                rgb.G = x
                rgb.B = 0.0f
            }
            H < 120.0f -> {
                rgb.R = x
                rgb.G = c
                rgb.B = 0.0f
            }
            H < 180.0f -> {
                rgb.R = 0.0f
                rgb.G = c
                rgb.B = x
            }
            H < 240.0f -> {
                rgb.R = 0.0f
                rgb.G = x
                rgb.B = c
            }
            H < 300.0f -> {
                rgb.R = x
                rgb.G = 0.0f
                rgb.B = c
            }
            H < 360.0f -> {
                rgb.R = c
                rgb.G = 0.0f
                rgb.B = x
            }
        }

        rgb.R += m
        rgb.G += m
        rgb.B += m

        return rgb
    }

    fun toRGBb(): ColorRGBb {
        val rgb = toRGB()
        return ColorRGBb((rgb.R * 255).toByte(), (rgb.B * 255).toByte(), (rgb.B * 255).toByte())
    }

    var H: Float = 0.0f
    var S: Float = 0.0f
    var V: Float = 0.0f

    companion object {

        val BLACK = ColorHSV(0.0f, 0.0f, 0.0f)
        val WHITE = ColorHSV(0.0f, 0.0f, 1.0f)
        val RED = ColorHSV(0.0f, 1.0f, 1.0f)
        val LIME = ColorHSV(120.0f, 1.0f, 1.0f)
        val BLUE = ColorHSV(240.0f, 1.0f, 1.0f)
        val YELLOW = ColorHSV(60.0f, 1.0f, 1.0f)
        val CYAN = ColorHSV(180.0f, 1.0f, 1.0f)
        val MAGENTA = ColorHSV(300.0f, 1.0f, 1.0f)
        val SILVER = ColorHSV(0.0f, 0.0f, 0.75f)
        val GRAY = ColorHSV(0.0f, 0.0f, 0.5f)
        val MAROON = ColorHSV(0.0f, 1.0f, 0.5f)
        val OLIVE = ColorHSV(60.0f, 1.0f, 0.5f)
        val GREEN = ColorHSV(120.0f, 1.0f, 0.5f)
        val PURPLE = ColorHSV(300.0f, 1.0f, 0.5f)
        val TEAL = ColorHSV(180.0f, 1.0f, 0.5f)
        val NAVY = ColorHSV(240.0f, 1.0f, 0.5f)
        val CORNFLOWERBLUE = ColorHSV(219.0f, 0.58f, 0.93f)
    }

}
