package com.botijasoftware.utils

class ColorRGBAb {

    constructor() {
        A = 0
        B = 0
        G = 0
        R = 0
    }

    constructor(c: Byte) {
        R = c
        G = c
        B = c
        A = 255.toByte()
    }

    constructor(r: Byte, g: Byte, b: Byte, a: Byte) {
        R = r
        G = g
        B = b
        A = a
    }

    constructor(c: ColorRGBAb) {
        R = c.R
        G = c.G
        B = c.B
        A = c.A
    }

    fun setValue(c: Byte) {
        R = c
        G = c
        B = c
        A = 255.toByte()
    }

    fun setValue(r: Byte, g: Byte, b: Byte, a: Byte) {
        R = r
        G = g
        B = b
        A = a
    }


    fun clone(): ColorRGBAb {
        return ColorRGBAb(R, G, B, A)
    }

    fun saturate() {
        var m = R.toFloat() + G.toFloat() + B / 3.0f
        if (m != 0.0f) {
            m = 1.0f / m
            R = (R * m).toByte()
            G = (G * m).toByte()
            B = (B * m).toByte()
        }
    }

    var R: Byte = 0
    var G: Byte = 0
    var B: Byte = 0
    var A: Byte = 0

    companion object {

        val BLACK = ColorRGBAb(0.toByte(), 0.toByte(), 0.toByte(), 255.toByte())
        val WHITE = ColorRGBAb(255.toByte(), 255.toByte(), 255.toByte(), 255.toByte())
        val RED = ColorRGBAb(255.toByte(), 0.toByte(), 0.toByte(), 255.toByte())
        val LIME = ColorRGBAb(0.toByte(), 255.toByte(), 0.toByte(), 255.toByte())
        val BLUE = ColorRGBAb(0.toByte(), 0.toByte(), 255.toByte(), 255.toByte())
        val YELLOW = ColorRGBAb(255.toByte(), 255.toByte(), 0.toByte(), 255.toByte())
        val CYAN = ColorRGBAb(0.toByte(), 255.toByte(), 255.toByte(), 255.toByte())
        val MAGENTA = ColorRGBAb(255.toByte(), 0.toByte(), 255.toByte(), 255.toByte())
        val SILVER = ColorRGBAb(192.toByte(), 192.toByte(), 192.toByte(), 255.toByte())
        val GRAY = ColorRGBAb(128.toByte(), 128.toByte(), 128.toByte(), 255.toByte())
        val MAROON = ColorRGBAb(128.toByte(), 0.toByte(), 0.toByte(), 255.toByte())
        val OLIVE = ColorRGBAb(128.toByte(), 128.toByte(), 0.toByte(), 255.toByte())
        val GREEN = ColorRGBAb(0.toByte(), 128.toByte(), 0.toByte(), 255.toByte())
        val PURPLE = ColorRGBAb(128.toByte(), 0.toByte(), 128.toByte(), 255.toByte())
        val TEAL = ColorRGBAb(0.toByte(), 128.toByte(), 128.toByte(), 255.toByte())
        val NAVY = ColorRGBAb(0.toByte(), 0.toByte(), 128.toByte(), 255.toByte())
        val CORNFLOWERBLUE = ColorRGBAb(100.toByte(), 149.toByte(), 237.toByte(), 255.toByte())
    }

}
