package com.botijasoftware.utils

class Circle {

    var X = 0 //center
    var Y = 0
    var radius: Int = 0

    constructor(px: Int, py: Int, r: Int) {

        X = px
        Y = py
        radius = r
    }

    constructor(position: Vector2, r: Int) {

        X = position.X.toInt()
        Y = position.Y.toInt()
        radius = r
    }

   /* fun setRadius(r: Int) {
        radius = r
    }*/

    fun setPosition(x: Int, y: Int) {
        X = x
        Y = y
    }

    fun setPosition(v: Vector2) {
        X = v.X.toInt()
        Y = v.Y.toInt()
    }

    fun move(incx: Int, incy: Int) {
        X += incx
        Y += incy
    }

    fun move(v: Vector2) {
        X += v.X.toInt()
        Y += v.Y.toInt()
    }

    fun contains(x: Int, y: Int): Boolean {
        val dx = X - x
        val dy = Y - y
        return dx <= radius && dx >= -radius && dy <= radius && dy >= -radius
    }

    operator fun contains(v: Vector2): Boolean {
        return contains(v.X.toInt(), v.Y.toInt())
    }

    fun setCenter(x: Int, y: Int) {
        X = x
        Y = y
    }

    var center: Vector2
        get() = Vector2(X.toFloat(), Y.toFloat())
        set(v) {
            X = v.X.toInt()
            Y = v.Y.toInt()
        }


}
