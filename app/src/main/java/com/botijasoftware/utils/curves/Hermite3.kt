package com.botijasoftware.utils.curves

import com.botijasoftware.utils.Vector3

class Hermite3(p0: Vector3, p1: Vector3, p2: Vector3, p3: Vector3) : Curve3(p0, p1, p2, p3) {

    override fun interpolate(t: Float, v: Vector3): Vector3 {

        if (t >= 0.0f && t <= 1.0f) {

            v.X = p1.X + p0.X * t + (-3 * p1.X + 3 * p2.X + -2 * p0.X - p3.X) * t * t + (2 * p1.X - 2 * p2.X + p0.X + p3.X) * t * t * t
            v.Y = p1.Y + p0.Y * t + (-3 * p1.Y + 3 * p2.Y + -2 * p0.Y - p3.Y) * t * t + (2 * p1.Y - 2 * p2.Y + p0.Y + p3.Y) * t * t * t
            v.Z = p1.Z + p0.Z * t + (-3 * p1.Z + 3 * p2.Z + -2 * p0.Z - p3.Z) * t * t + (2 * p1.Z - 2 * p2.Z + p0.Z + p3.Z) * t * t * t

        }
        return v
    }

}