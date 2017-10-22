package com.botijasoftware.utils

class Vector3i {

    constructor() {
        Z = 0
        Y = Z
        X = Y
    }

    constructor(value: Int) {
        X = value
        Y = value
        Z = value
    }

    constructor(valx: Int, valy: Int, valz: Int) {
        X = valx
        Y = valy
        Z = valz
    }

    fun setValue(value: Int) {
        X = value
        Y = value
        Z = value
    }

    fun setValue(valx: Int, valy: Int, valz: Int) {
        X = valx
        Y = valy
        Z = valz
    }

    fun setValue(v: Vector3i) {
        X = v.X
        Y = v.Y
        Z = v.Z
    }

    fun clone(): Vector3i {
        return Vector3i(X, Y, Z)
    }

    fun normalize(): Vector3i {
        val len = length()
        if (len != 0.0f) {
            X = (X / len).toInt()
            Y = (Y / len).toInt()
            Z = (Z / len).toInt()
        }
        return this
    }

    fun add(v: Vector3i): Vector3i {
        X += v.X
        Y += v.Y
        Z += v.Z
        return this
    }

    fun add(value: Int): Vector3i {
        X += value
        Y += value
        Z += value
        return this
    }

    fun sub(v: Vector3i): Vector3i {
        X -= v.X
        Y -= v.Y
        Z -= v.Z
        return this
    }

    fun sub(value: Int): Vector3i {
        X -= value
        Y -= value
        Z -= value
        return this
    }

    fun mul(v: Vector3i): Vector3i {
        X *= v.X
        Y *= v.Y
        Z *= v.Z
        return this
    }

    fun mul(value: Int): Vector3i {
        X *= value
        Y *= value
        Z *= value
        return this
    }

    operator fun div(v: Vector3i): Vector3i {
        if (v.X != 0 && v.Y != 0 && v.Z != 0) {
            X /= X / v.X
            Y /= Y / v.X
            Z /= Z / v.X
        }
        return this
    }

    operator fun div(value: Int): Vector3i {
        if (value != 0) {
            val inv = 1.0f / value
            X = (X * inv).toInt()
            Y = (Y * inv).toInt()
            Z = (Z * inv).toInt()
        }
        return this
    }

    fun lengthsq(): Float {
        return (X * X + Y * Y + Z * Z).toFloat()
    }

    fun length(): Float {
        return Math.sqrt((X * X + Y * Y + Z * Z).toDouble()).toFloat()
    }

    val isZero: Boolean
        get() = X == 0 && Y == 0 && Z == 0

    fun equals(value: Int): Boolean {
        return X == value && Y == value && Z == value
    }

    fun equals(v: Vector3i): Boolean {
        return X == v.X && Y == v.Y && Z == v.Z
    }

    fun distancesq(v: Vector3i): Float {
        return ((X - v.X) * (X - v.X) + (Y - v.Y) * (Y - v.Y) + (Z - v.Z) * (Z - v.Z)).toFloat()
    }

    fun distance(v: Vector3i): Float {
        return Math.sqrt(((X - v.X) * (X - v.X) + (Y - v.Y) * (Y - v.Y) + (Z - v.Z) * (Z - v.Z)).toDouble()).toFloat()
    }

    fun dot(v: Vector3i): Float {
        return (X * v.X + Y * v.Y + Z * v.Z).toFloat()
    }

    fun cross(v: Vector3i): Vector3i {
        val nx = (Y * v.Z - Z * v.Y)
        val ny = (Z * v.X - X * v.Z)
        val nz = (X * v.Y - Y * v.X)
        X = nx
        Y = ny
        Z = nz
        return this
    }

    fun reflect(v: Vector3i): Vector3i {
        //i - (2 * n * dot(i, n))
        sub(v.mul((2 * dot(v)).toInt()))
        return this
    }


    fun zero(): Vector3i {
        X = 0
        Y = 0
        Z = 0
        return this
    }

    fun lerp(v: Vector3i, factor: Float): Vector3i {
        if (factor <= 0.0f) {
            return this
        } else if (factor >= 1.0f) {
            X = v.X
            Y = v.Y
            Z = v.Z
            return this
        }

        X += (factor * (v.X - X)).toInt()
        Y += (factor * (v.Y - Y)).toInt()
        Z += (factor * (v.Z - Z)).toInt()

        return this
    }

    var X: Int = 0
    var Y: Int = 0
    var Z: Int = 0

    companion object {

        fun cross(v: Vector3i, u: Vector3i): Vector3i {
            val nx = v.Y * u.Z - v.Z * u.Y
            val ny = v.Z * u.X - v.X * u.Z
            val nz = v.X * u.Y - v.Y * u.X
            return Vector3i(nx, ny, nz)
        }

        fun orthoNormalize(n: Vector3i, u: Vector3i): Vector3i {
            n.normalize()
            val v = cross(n, u)
            v.normalize()
            return cross(v, n)
        }

        fun lerp(v: Vector3i, u: Vector3i, factor: Float): Vector3i {
            if (factor <= 0.0f) {
                return v.clone()
            } else if (factor >= 1.0f) {
                return u.clone()
            }

            val x = v.X + (factor * (u.X - v.X)).toInt()
            val y = v.Y + (factor * (u.Y - v.Y)).toInt()
            val z = v.Z + (factor * (u.Z - v.Z)).toInt()

            return Vector3i(x, y, z)
        }

        val ZERO = Vector3i(0, 0, 0)
        val ONE = Vector3i(1, 1, 1)
        val RIGHT = Vector3i(1, 0, 0)
        val UP = Vector3i(0, 1, 0)
        val FORWARD = Vector3i(0, 0, 1)
    }

}
