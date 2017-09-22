package com.botijasoftware.utils

class Vector2 {

    constructor() {
        Y = 0.0f
        X = Y
    }

    constructor(`val`: Float) {
        X = `val`
        Y = `val`
    }

    constructor(valx: Float, valy: Float) {
        X = valx
        Y = valy
    }

    fun setValue(`val`: Float) {
        X = `val`
        Y = `val`
    }

    fun setValue(valx: Float, valy: Float) {
        X = valx
        Y = valy
    }

    fun setValue(v: Vector2) {
        X = v.X
        Y = v.Y
    }

    fun clone(): Vector2 {
        return Vector2(X, Y)
    }

    fun normalize(): Vector2 {
        val len = length()
        if (len != 0.0f) {
            X /= len
            Y /= len
        }
        return this
    }

    fun add(v: Vector2): Vector2 {
        X += v.X
        Y += v.Y
        return this
    }

    fun add(`val`: Float): Vector2 {
        X += `val`
        Y += `val`
        return this
    }

    fun sub(v: Vector2): Vector2 {
        X -= v.X
        Y -= v.Y
        return this
    }

    fun sub(`val`: Float): Vector2 {
        X -= `val`
        Y -= `val`
        return this
    }

    fun mul(v: Vector2): Vector2 {
        X *= v.X
        Y *= v.Y
        return this
    }

    fun mul(`val`: Float): Vector2 {
        X *= `val`
        Y *= `val`
        return this
    }

    operator fun div(v: Vector2): Vector2 {
        if (v.X != 0.0f && v.Y != 0.0f) {
            X /= v.X
            Y /= v.Y
        }
        return this
    }

    operator fun div(`val`: Float): Vector2 {
        if (`val` != 0.0f) {
            val inv = 1.0f / `val`
            X *= inv
            Y *= inv
        }
        return this
    }

    fun lengthsq(): Float {
        return X * X + Y * Y
    }

    fun length(): Float {
        return Math.sqrt((X * X + Y * Y).toDouble()).toFloat()
    }

    val isZero: Boolean
        get() = X <= SIGMA && X >= -SIGMA && Y <= SIGMA && Y >= -SIGMA

    fun equals(`val`: Float): Boolean {
        val val_plus = `val` + SIGMA
        val val_minus = `val` - SIGMA
        return X in val_minus..val_plus && Y <= val_plus && Y >= val_minus
    }

    fun equals(v: Vector2): Boolean {
        return X <= v.X + SIGMA && X >= v.X - SIGMA && Y <= v.Y + SIGMA && Y >= v.Y - SIGMA
    }

    fun distancesq(v: Vector2): Float {
        return (X - v.X) * (X - v.X) + (Y - v.Y) * (Y - v.Y)
    }

    fun distance(v: Vector2): Float {
        return Math.sqrt(((X - v.X) * (X - v.X) + (Y - v.Y) * (Y - v.Y)).toDouble()).toFloat()
    }

    fun dot(v: Vector2): Float {
        return X * v.X + Y * v.Y
    }

    fun reflect(v: Vector2): Vector2 {
        //i - (2 * n * dot(i, n))
        sub(v.mul(2.0f * dot(v)))
        return this
    }

    fun lerp(v: Vector2, factor: Float): Vector2 {
        if (factor <= 0.0f) {
            return this
        } else if (factor >= 1.0f) {
            X = v.X
            Y = v.Y
            return this
        }

        X += factor * (v.X - X)
        Y += factor * (v.Y - Y)

        return this
    }

    fun zero(): Vector2 {
        X = 0.0f
        Y = 0.0f
        return this
    }

    var X: Float = 0.toFloat()
    var Y: Float = 0.toFloat()

    companion object {

        fun lerp(v: Vector2, u: Vector2, factor: Float): Vector2 {
            if (factor <= 0.0f) {
                return v.clone()
            } else if (factor >= 1.0f) {
                return u.clone()
            }

            val x = v.X + factor * (u.X - v.X)
            val y = v.Y + factor * (u.Y - v.Y)

            return Vector2(x, y)
        }

        private val SIGMA = 0.000001f
        val ZERO = Vector2(0.0f, 0.0f)
        val ONE = Vector2(1.0f, 1.0f)
        val RIGHT = Vector2(1.0f, 0.0f)
        val UP = Vector2(0.0f, 1.0f)
    }

}
