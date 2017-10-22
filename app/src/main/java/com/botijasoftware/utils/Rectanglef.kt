package com.botijasoftware.utils

class Rectanglef {

    constructor() {

        X = 0.0f
        Y = 0.0f
        width = 0.0f
        height = 0.0f
    }

    constructor(px: Float, py: Float, w: Float, h: Float) {

        X = px
        Y = py
        width = w
        height = h
    }

    constructor(position: Vector2, size: Vector2) {

        X = position.X
        Y = position.Y
        width = size.X
        height = size.Y
    }

    constructor(rect: Rectangle) {
        X = rect.X.toFloat()
        Y = rect.Y.toFloat()
        width = rect.width.toFloat()
        height = rect.height.toFloat()
    }

    constructor(rect: Rectanglef) {

        X = rect.X
        Y = rect.Y
        width = rect.width
        height = rect.height
    }

    fun setSize(w: Float, h: Float) {
        width = w
        height = h
    }

    fun setSize(v: Vector2) {
        width = v.X
        height = v.Y
    }

    fun setPosition(x: Float, y: Float) {
        X = x
        Y = y
    }

    fun setPosition(v: Vector2) {
        X = v.X
        Y = v.Y
    }

    fun move(incx: Float, incy: Float) {
        X += incx
        Y += incy
    }

    fun move(v: Vector2) {
        X += v.X
        Y += v.Y
    }

    fun contains(x: Float, y: Float): Boolean {
        return x >= X && x <= X + width && y >= Y && y <= Y + height
    }

    operator fun contains(v: Vector2): Boolean {
        return v.X >= X && v.X <= X + width && v.Y >= Y && v.Y <= Y + height
    }

    fun setCenter(x: Float, y: Float) {
        X = x - width / 2.0f
        Y = y - height / 2.0f
    }

    var center: Vector2
        get() = Vector2(X + width / 2.0f, Y + height / 2.0f)
        set(v) {
            X = v.X - width / 2.0f
            Y = v.Y - height / 2.0f
        }

    val topLeft: Vector2
        get() = Vector2(X, Y)

    val topRight: Vector2
        get() = Vector2(X + width, Y)

    val bottomLeft: Vector2
        get() = Vector2(X, Y - height)

    val bottomRight: Vector2
        get() = Vector2(X + width, Y - height)

    fun clone(): Rectanglef {
        return Rectanglef(X, Y, width, height)
    }

    var X: Float = 0.0f //top left
    var Y: Float = 00f
    var width: Float = 0.0f
    var height: Float = 0.0f

}
