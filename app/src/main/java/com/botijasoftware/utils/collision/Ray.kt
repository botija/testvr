package com.botijasoftware.utils.collision

import com.botijasoftware.utils.Vector3

class Ray {

    var mDirection: Vector3
    var mOrigin: Vector3


    constructor(origin: Vector3, direction: Vector3) {
        mOrigin = origin
        mDirection = direction
    }

    fun RayFromPoints(v0: Vector3, v1: Vector3) {
        mOrigin = v0
        mDirection = v1.clone().sub(v0)
        mDirection.normalize()
    }

    fun collides(s: Sphere): Boolean {


        val a = mDirection.dot(mDirection)
        val b = 2 * mDirection.dot(mOrigin)
        val c = mOrigin.dot(mOrigin) - s.radius * s.radius


        val disc = b * b - 4f * a * c

        if (disc < 0)
            return false

        val distSqrt = Math.sqrt(disc.toDouble()).toFloat()
        val q: Float
        if (b < 0)
            q = (-b - distSqrt) / 2.0f
        else
            q = (-b + distSqrt) / 2.0f

        // compute t0 and t1
        var t0 = q / a
        var t1 = c / q

        // make sure t0 is smaller than t1
        if (t0 > t1) {
            // if t0 is bigger than t1 swap them around
            val temp = t0
            t0 = t1
            t1 = temp
        }

        // if t1 is less than zero, the object is in the ray's negative direction
        // and consequently the ray misses the sphere
        return t1 >= 0

    }
}