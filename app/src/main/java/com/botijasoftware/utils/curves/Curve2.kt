package com.botijasoftware.utils.curves

import com.botijasoftware.utils.Vector2

abstract class Curve2(var p0: Vector2, var p1: Vector2, var p2: Vector2, var p3: Vector2) {

    var tangent0 = Vector2(0f, 0f)
    var tangent1 = Vector2(0f, 0f)

    init {
        set(p0, p1, p2, p3)
    }

    operator fun set(p0: Vector2, p1: Vector2, p2: Vector2, p3: Vector2) {
        this.p0 = p0
        this.p1 = p1
        this.p2 = p2
        this.p3 = p3
        tangent0.setValue(p2)
        tangent0.sub(p0)
        tangent0.mul(0.5f)
        tangent0.normalize()
        tangent1.setValue(p3)
        tangent1.sub(p1)
        tangent1.mul(0.5f)
        tangent1.normalize()
    }

    fun setTangent(tangent: Vector2) {
        this.tangent0.setValue(tangent)
    }


    fun interpolate(t: Float): Vector2 {
        return interpolate(t, Vector2(0f, 0f))
    }

    abstract fun interpolate(t: Float, v: Vector2): Vector2

    @JvmOverloads fun interpolateTangent(t: Float, v: Vector2 = Vector2(0f, 0f)): Vector2 {

        if (t >= 0.0f && t <= 1.0f) {
            v.X = tangent1.X * t + tangent0.X * (1 - t)
            v.Y = tangent1.Y * t + tangent0.Y * (1 - t)
        }
        return v
    }
}
