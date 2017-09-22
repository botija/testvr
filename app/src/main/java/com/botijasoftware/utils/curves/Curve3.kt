package com.botijasoftware.utils.curves

import com.botijasoftware.utils.Vector3

abstract class Curve3(var p0: Vector3, var p1: Vector3,var  p2: Vector3, var p3: Vector3) {

    var tangent0 = Vector3(0f, 0f, 0f)
    var tangent1 = Vector3(0f, 0f, 0f)

    init {
        set(p0, p1, p2, p3)
    }

    operator fun set(p0: Vector3, p1: Vector3, p2: Vector3, p3: Vector3) {
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

    fun interpolate(t: Float): Vector3 {
        return interpolate(t, Vector3(0f, 0f, 0f))
    }

    abstract fun interpolate(t: Float, v: Vector3): Vector3

    @JvmOverloads fun interpolateTangent(t: Float, v: Vector3 = Vector3(0f, 0f, 0f)): Vector3 {

        if (t >= 0.0f && t <= 1.0f) {
            v.X = tangent1.X * t + tangent0.X * (1 - t)
            v.Y = tangent1.Y * t + tangent0.Y * (1 - t)
            v.Z = tangent1.Z * t + tangent0.Z * (1 - t)
        }
        return v
    }
}
