package com.botijasoftware.utils

class Rectangle {

    constructor() {

        X = 0
        Y = 0
        width = 0
        height = 0
    }

    constructor(px: Int, py: Int, w: Int, h: Int) {

        X = px
        Y = py
        width = w
        height = h
    }

    constructor(position: Vector2, size: Vector2) {

        X = position.X.toInt()
        Y = position.Y.toInt()
        width = size.X.toInt()
        height = size.Y.toInt()
    }

    constructor(rect: Rectangle) {

        X = rect.X
        Y = rect.Y
        width = rect.width
        height = rect.height
    }

    constructor(rect: Rectanglef) {

        X = rect.X.toInt()
        Y = rect.Y.toInt()
        width = rect.width.toInt()
        height = rect.height.toInt()
    }

    fun setSize(w: Int, h: Int) {
        width = w
        height = h
    }

    fun setSize(v: Vector2) {
        width = v.X.toInt()
        height = v.Y.toInt()
    }

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
        return x >= X && x <= X + width && y >= Y && y <= Y + height
    }

    operator fun contains(v: Vector2): Boolean {
        return v.X.toInt() >= X && v.X.toInt() <= X + width && v.Y.toInt() >= Y && v.Y.toInt() <= Y + height
    }

    fun setCenter(x: Int, y: Int) {
        X = x - width / 2
        Y = y - height / 2
    }

    var center: Vector2
        get() = Vector2(X + width / 2.0f, Y + height / 2.0f)
        set(v) {
            X = v.X.toInt() - width / 2
            Y = v.Y.toInt() - height / 2
        }

    val topLeft: Vector2
        get() = Vector2(X.toFloat(), Y.toFloat())

    val topRight: Vector2
        get() = Vector2((X + width).toFloat(), Y.toFloat())

    val bottomLeft: Vector2
        get() = Vector2(X.toFloat(), (Y - height).toFloat())

    val bottomRight: Vector2
        get() = Vector2((X + width).toFloat(), (Y - height).toFloat())

    fun clone(): Rectangle {
        return Rectangle(X, Y, width, height)
    }

    var X: Int = 0 //top left
    var Y: Int = 0
    var width: Int = 0
    var height: Int = 0

}
