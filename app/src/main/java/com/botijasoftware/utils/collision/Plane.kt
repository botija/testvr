package com.botijasoftware.utils.collision

import com.botijasoftware.utils.Vector3

class Plane {


    constructor(n: Vector3, d: Float) {
        normal = Vector3(n.X, n.Y, n.Z)
        distance = d
    }

    constructor(a: Float, b: Float, c: Float, d: Float) {
        normal = Vector3(a, b, c)
        distance = d
    }

    constructor(p0: Vector3, p1: Vector3, p2: Vector3) {
        val v0 = Vector3(p1.X - p0.X, p1.Y - p0.Y, p1.Z - p0.Z)
        val v1 = Vector3(p2.X - p0.X, p2.Y - p0.Y, p2.Z - p0.Z)
        normal = v0.cross(v1)
        distance = normal.length()
        normal.normalize()
    }

    fun normalize() {
        val invlen = 1.0f / normal.length()
        normal.mul(invlen)
        distance *= invlen
    }

    fun getDistance(point: Vector3): Float {
        return normal.dot(point) + distance
    }

    internal fun getDistance2(point: Vector3): Float {
        val d = getDistance(point)
        return d * d
    }

    fun getSide(point: Vector3): Int {
        val d = getDistance(point)
        when {
            d < 0.0f -> return SIDE_NEGATIVE
            d > 0.0f -> return SIDE_POSITIVE
            else -> return SIDE_INSIDE
        }
    }

    fun inHalfSpace(point: Vector3): Boolean {
        return getDistance(point) >= 0.0f
    }

    var normal: Vector3
    var distance: Float = 0.toFloat()

    companion object {
        val SIDE_INSIDE = 0
        val SIDE_POSITIVE = 1
        val SIDE_NEGATIVE = 2
    }

}


        
