package com.botijasoftware.utils

class ColorRGBb {

    constructor() {
        B = 0
        G = B
        R = G
    }

    constructor(c: Byte) {
        R = c
        G = c
        B = c
    }

    constructor(r: Byte, g: Byte, b: Byte) {
        R = r
        G = g
        B = b
    }

    constructor(c: ColorRGBb) {
        R = c.R
        G = c.G
        B = c.B
    }

    fun setValue(c: Byte) {
        R = c
        G = c
        B = c
    }

    fun setValue(r: Byte, g: Byte, b: Byte) {
        R = r
        G = g
        B = b
    }


    fun clone(): ColorRGBb {
        return ColorRGBb(R, G, B)
    }

    fun saturate() {
        /*float m = (R + G + B / 3.0f);
		if (m != 0.0f) {
			m = 1.0f/m;
			R = (byte) (R * m);
			G = (byte) (G * m);
			B = (byte) (B * m);
		}*/
    }

    var R: Byte = 0
    var G: Byte = 0
    var B: Byte = 0

    companion object {

        val BLACK = ColorRGBb(0.toByte(), 0.toByte(), 0.toByte())
        val WHITE = ColorRGBb(255.toByte(), 255.toByte(), 255.toByte())
        val RED = ColorRGBb(255.toByte(), 0.toByte(), 0.toByte())
        val LIME = ColorRGBb(0.toByte(), 255.toByte(), 0.toByte())
        val BLUE = ColorRGBb(0.toByte(), 0.toByte(), 255.toByte())
        val YELLOW = ColorRGBb(255.toByte(), 255.toByte(), 0.toByte())
        val CYAN = ColorRGBb(0.toByte(), 255.toByte(), 255.toByte())
        val MAGENTA = ColorRGBb(255.toByte(), 0.toByte(), 255.toByte())
        val SILVER = ColorRGBb(192.toByte(), 192.toByte(), 192.toByte())
        val GRAY = ColorRGBb(128.toByte(), 128.toByte(), 128.toByte())
        val MAROON = ColorRGBb(128.toByte(), 0.toByte(), 0.toByte())
        val OLIVE = ColorRGBb(128.toByte(), 128.toByte(), 0.toByte())
        val GREEN = ColorRGBb(0.toByte(), 128.toByte(), 0.toByte())
        val PURPLE = ColorRGBb(128.toByte(), 0.toByte(), 128.toByte())
        val TEAL = ColorRGBb(0.toByte(), 128.toByte(), 128.toByte())
        val NAVY = ColorRGBb(0.toByte(), 0.toByte(), 128.toByte())
        val CORNFLOWERBLUE = ColorRGBb(100.toByte(), 149.toByte(), 237.toByte())
    }

}
