package com.botijasoftware.utils.collision

import com.botijasoftware.utils.Vector3

class Cube {

    constructor(px: Float, py: Float, pz: Float, w: Float, h: Float, d: Float) {

        X = px
        Y = py
        Z = pz
        width = w
        height = h
        depth = d
    }

    constructor(position: Vector3, size: Vector3) {

        X = position.X
        Y = position.Y
        Z = position.Z
        width = size.X
        height = size.Y
        depth = size.Z
    }

    fun setSize(w: Float, h: Float, d: Float) {
        width = w
        height = h
        depth = d
    }

    fun setSize(v: Vector3) {
        width = v.X
        height = v.Y
        depth = v.Z
    }

    fun setPosition(x: Float, y: Float, z: Float) {
        X = x
        Y = y
        Z = z
    }

    fun setPosition(v: Vector3) {
        X = v.X
        Y = v.Y
        Z = v.Z
    }

    fun move(incx: Float, incy: Float, incz: Float) {
        X += incx
        Y += incy
        Z += incz
    }

    fun move(v: Vector3) {
        X += v.X
        Y += v.Y
        Z += v.Z
    }

    fun contains(x: Float, y: Float, z: Float): Boolean {
        return x >= X && x <= X + width && y >= Y && y <= Y + height && z >= Z && z <= Z + depth
    }

    operator fun contains(v: Vector3): Boolean {
        return v.X.toInt() >= X && v.X <= X + width && v.Y >= Y && v.Y <= Y + height && v.Z >= Z && v.Z <= Z + depth
    }

    fun setCenter(x: Float, y: Float, z: Float) {
        X = x - width / 2.0f
        Y = y + height / 2.0f
        Z = z + depth / 2.0f
    }

    var center: Vector3
        get() = Vector3(X + width / 2.0f, Y - height / 2.0f, Z - depth / 2.0f)
        set(v) {
            X = v.X - width / 2.0f
            Y = v.Y + height / 2.0f
            Z = v.Z + depth / 2.0f
        }


    var X = 0f //top left
    var Y = 0f
    var Z = 0f
    var width = 0f
    var height = 0f
    var depth = 0f

}
