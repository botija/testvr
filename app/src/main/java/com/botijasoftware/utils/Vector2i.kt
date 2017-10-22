package com.botijasoftware.utils

class Vector2i {

    constructor() {
        X = 0
        Y = 0
    }

    constructor(value: Int) {
        X = value
        Y = value
    }

    constructor(valx: Int, valy: Int) {
        X = valx
        Y = valy
    }

    fun setValue(value: Int) {
        X = value
        Y = value
    }

    fun setValue(valx: Int, valy: Int) {
        X = valx
        Y = valy
    }

    fun setValue(v: Vector2i) {
        X = v.X
        Y = v.Y
    }

    fun clone(): Vector2i {
        return Vector2i(X, Y)
    }

    fun normalize(): Vector2i {
        val len = length()
        if (len != 0.0f) {
            X = (X / len).toInt()
            Y = (Y / len).toInt()
        }
        return this
    }

    fun add(v: Vector2i): Vector2i {
        X += v.X
        Y += v.Y
        return this
    }

    fun add(value: Int): Vector2i {
        X += value
        Y += value
        return this
    }

    fun sub(v: Vector2i): Vector2i {
        X -= v.X
        Y -= v.Y
        return this
    }

    fun sub(value: Int): Vector2i {
        X -= value
        Y -= value
        return this
    }

    fun mul(v: Vector2i): Vector2i {
        X *= v.X
        Y *= v.Y
        return this
    }

    fun mul(value: Int): Vector2i {
        X *= value
        Y *= value
        return this
    }

    operator fun div(v: Vector2i): Vector2i {
        if (v.X != 0 && v.Y != 0) {
            X /= v.X
            Y /= v.Y
        }
        return this
    }

    operator fun div(value: Int): Vector2i {
        if (value != 0) {
            val inv = 1.0f / value
            X = (X * inv).toInt()
            Y = (Y * inv).toInt()
        }
        return this
    }

    fun lengthsq(): Float {
        return (X * X + Y * Y).toFloat()
    }

    fun length(): Float {
        return Math.sqrt((X * X + Y * Y).toDouble()).toFloat()
    }

    val isZero: Boolean
        get() = X == 0 && Y == 0

    fun equals(value: Int): Boolean {
        return X == value && Y == value
    }

    fun equals(v: Vector2i): Boolean {
        return X == v.X && Y == v.Y
    }

    fun distancesq(v: Vector2i): Float {
        return ((X - v.X) * (X - v.X) + (Y - v.Y) * (Y - v.Y)).toFloat()
    }

    fun distance(v: Vector2i): Float {
        return Math.sqrt(((X - v.X) * (X - v.X) + (Y - v.Y) * (Y - v.Y)).toDouble()).toFloat()
    }

    fun dot(v: Vector2i): Float {
        return (X * v.X + Y * v.Y).toFloat()
    }

    fun reflect(v: Vector2i): Vector2i {
        //i - (2 * n * dot(i, n))
        sub(v.mul((2 * dot(v)).toInt()))
        return this
    }

    fun lerp(v: Vector2i, factor: Float): Vector2i {
        if (factor <= 0.0f) {
            return this
        } else if (factor >= 1.0f) {
            X = v.X
            Y = v.Y
            return this
        }

        X += (factor * (v.X - X)).toInt()
        Y += (factor * (v.Y - Y)).toInt()

        return this
    }

    fun zero(): Vector2i {
        X = 0
        Y = 0
        return this
    }

    var X: Int = 0
    var Y: Int = 0

    companion object {


        fun lerp(v: Vector2i, u: Vector2i, factor: Float): Vector2i {
            if (factor <= 0.0f) {
                return v.clone()
            } else if (factor >= 1.0f) {
                return u.clone()
            }

            val x = v.X + (factor * (u.X - v.X)).toInt()
            val y = v.Y + (factor * (u.Y - v.Y)).toInt()

            return Vector2i(x, y)
        }

        val ZERO = Vector2i(0, 0)
        val ONE = Vector2i(1, 1)
        val RIGHT = Vector2i(1, 0)
        val UP = Vector2i(0, 1)
    }

}
