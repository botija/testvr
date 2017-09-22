package com.botijasoftware.utils.curves

import com.botijasoftware.utils.Vector2

class Bezier2(p0: Vector2, p1: Vector2, p2: Vector2, p3: Vector2) : Curve2(p0, p1, p2, p3) {

    override fun interpolate(t: Float, v: Vector2): Vector2 {

        if (t in 0.0f..1.0f) {

            v.X = p0.X + (3 * -p0.X + 3 * p1.X) * t + (3 * p0.X - 6 * p1.X + 3 * p2.X) * t * t + (-p0.X + 3 * p1.X - 3 * p2.X + p3.X) * t * t * t
            v.Y = p0.Y + (3 * -p0.Y + 3 * p1.Y) * t + (3 * p0.Y - 6 * p1.Y + 3 * p2.Y) * t * t + (-p0.Y + 3 * p1.Y - 3 * p2.Y + p3.Y) * t * t * t

        }
        return v
    }

}