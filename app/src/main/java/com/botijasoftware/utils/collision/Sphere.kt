package com.botijasoftware.utils.collision

import com.botijasoftware.utils.Vector3

class Sphere {

    var X = 0f //center
    var Y = 0f
    var Z = 0f
    var radius: Float = 0.0f

    constructor(px: Float, py: Float, pz: Float, r: Float) {

        X = px
        Y = py
        Z = pz
        radius = r
    }

    constructor(position: Vector3, r: Float) {

        X = position.X
        Y = position.Y
        Z = position.Z
        radius = r
    }

    /*fun setRadius(r: Float) {
        radius = r
    }*/

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

    fun collides(x: Float, y: Float, z: Float): Boolean {
        val dx = X - x
        val dy = Y - y
        val dz = Z - z
        return dx <= radius && dx >= -radius && dy <= radius && dy >= -radius && dz <= radius && dz >= -radius
    }

    fun collides(v: Vector3): Boolean {
        return collides(v.X, v.Y, v.Z)
    }

    fun collides(r: Ray): Boolean {
        return r.collides(this)
    }

    fun collides(c: Capsule): Boolean {
        return c.collides(this)
    }

    fun collides(s: Sphere): Boolean {
        val d = center.distance(s.center)
        return d <= radius + s.radius
    }

    fun setCenter(x: Float, y: Float, z: Float) {
        X = x
        Y = y
        Z = z
    }

    var center: Vector3
        get() = Vector3(X, Y, Z)
        set(v) {
            X = v.X
            Y = v.Y
            Z = v.Z
        }

}
