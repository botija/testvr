package com.botijasoftware.utils

class Vector3 {

    constructor() {
        Z = 0.0f
        Y = Z
        X = Y
    }

    constructor(value: Float) {
        X = value
        Y = value
        Z = value
    }

    constructor(valx: Float, valy: Float, valz: Float) {
        X = valx
        Y = valy
        Z = valz
    }

    fun setValue(value: Float) {
        X = value
        Y = value
        Z = value
    }

    fun setValue(valx: Float, valy: Float, valz: Float) {
        X = valx
        Y = valy
        Z = valz
    }

    fun setValue(v: Vector3) {
        X = v.X
        Y = v.Y
        Z = v.Z
    }

    fun clone(): Vector3 {
        return Vector3(X, Y, Z)
    }

    fun normalize(): Vector3 {
        val len = length()
        if (len != 0.0f) {
            X /= len
            Y /= len
            Z /= len
        }
        return this
    }

    fun add(v: Vector3): Vector3 {
        X += v.X
        Y += v.Y
        Z += v.Z
        return this
    }

    fun add(value: Float): Vector3 {
        X += value
        Y += value
        Z += value
        return this
    }

    fun sub(v: Vector3): Vector3 {
        X -= v.X
        Y -= v.Y
        Z -= v.Z
        return this
    }

    fun sub(value: Float): Vector3 {
        X -= value
        Y -= value
        Z -= value
        return this
    }

    fun mul(v: Vector3): Vector3 {
        X *= v.X
        Y *= v.Y
        Z *= v.Z
        return this
    }

    fun mul(value: Float): Vector3 {
        X *= value
        Y *= value
        Z *= value
        return this
    }

    operator fun div(v: Vector3): Vector3 {
        if (v.X != 0.0f && v.Y != 0.0f && v.Z != 0.0f) {
            X /= v.X
            Y /= v.Y
            Z /= v.Z
        }
        return this
    }

    operator fun div(value: Float): Vector3 {
        if (value != 0.0f) {
            val inv = 1.0f / value
            X *= inv
            Y *= inv
            Z *= inv
        }
        return this
    }

    fun lengthsq(): Float {
        return X * X + Y * Y + Z * Z
    }

    fun length(): Float {
        return Math.sqrt((X * X + Y * Y + Z * Z).toDouble()).toFloat()
    }

    val isZero: Boolean
        get() = X <= SIGMA && X >= -SIGMA && Y <= SIGMA && Y >= -SIGMA && Z <= SIGMA && Z >= -SIGMA

    fun equals(value: Float): Boolean {
        val val_plus = value + SIGMA
        val val_minus = value - SIGMA
        return X in val_minus..val_plus && Y <= val_plus && Y >= val_minus && Z <= val_plus && Z >= val_minus
    }

    fun equals(v: Vector3): Boolean {
        return X <= v.X + SIGMA && X >= v.X - SIGMA && Y <= v.Y + SIGMA && Y >= v.Y - SIGMA && Z <= v.Z + SIGMA && Z >= v.Z - SIGMA
    }

    fun distancesq(v: Vector3): Float {
        return (X - v.X) * (X - v.X) + (Y - v.Y) * (Y - v.Y) + (Z - v.Z) * (Z - v.Z)
    }

    fun distance(v: Vector3): Float {
        return Math.sqrt(((X - v.X) * (X - v.X) + (Y - v.Y) * (Y - v.Y) + (Z - v.Z) * (Z - v.Z)).toDouble()).toFloat()
    }

    fun dot(v: Vector3): Float {
        return X * v.X + Y * v.Y + Z * v.Z
    }

    fun cross(v: Vector3): Vector3 {
        val nx = Y * v.Z - Z * v.Y
        val ny = Z * v.X - X * v.Z
        val nz = X * v.Y - Y * v.X
        X = nx
        Y = ny
        Z = nz
        return this
    }

    fun reflect(v: Vector3): Vector3 {
        //i - (2 * n * dot(i, n))
        sub(v.mul(2.0f * dot(v)))
        return this
    }


    fun lerp(v: Vector3, factor: Float): Vector3 {
        if (factor <= 0.0f) {
            return this
        } else if (factor >= 1.0f) {
            X = v.X
            Y = v.Y
            Z = v.Z
            return this
        }

        X += factor * (v.X - X)
        Y += factor * (v.Y - Y)
        Z += factor * (v.Z - Z)

        return this
    }


    fun zero(): Vector3 {
        X = 0.0f
        Y = 0.0f
        Z = 0.0f
        return this
    }

    var X: Float = 0.0f
    var Y: Float = 0.0f
    var Z: Float = 0.0f

    companion object {

        fun cross(v: Vector3, u: Vector3): Vector3 {
            val nx = v.Y * u.Z - v.Z * u.Y
            val ny = v.Z * u.X - v.X * u.Z
            val nz = v.X * u.Y - v.Y * u.X
            return Vector3(nx, ny, nz)
        }

        fun orthoNormalize(n: Vector3, u: Vector3): Vector3 {
            n.normalize()
            val v = cross(n, u)
            v.normalize()
            return cross(v, n)
        }

        fun lerp(v: Vector3, u: Vector3, factor: Float): Vector3 {
            if (factor <= 0.0f) {
                return v.clone()
            } else if (factor >= 1.0f) {
                return u.clone()
            }

            val x = v.X + factor * (u.X - v.X)
            val y = v.Y + factor * (u.Y - v.Y)
            val z = v.Z + factor * (u.Z - v.Z)

            return Vector3(x, y, z)
        }

        private val SIGMA = 0.000001f
        val ZERO = Vector3(0.0f, 0.0f, 0.0f)
        val ONE = Vector3(1.0f, 1.0f, 1.0f)
        val RIGHT = Vector3(1.0f, 0.0f, 0.0f)
        val UP = Vector3(0.0f, 1.0f, 0.0f)
        val FORWARD = Vector3(0.0f, 0.0f, 1.0f)
    }
}
