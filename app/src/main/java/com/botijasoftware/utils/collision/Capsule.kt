package com.botijasoftware.utils.collision

import com.botijasoftware.utils.Vector3

class Capsule(p0: Vector3, p1: Vector3, var radius: Float) {

    private val segment: Segment

    init {
        segment = Segment(p0, p1)
    }

    operator fun set(p0: Vector3, p1: Vector3) {
        segment.set(p0, p1)
    }

    fun collides(p: Vector3): Boolean {
        return segment.distancesq(p) <= radius * radius
    }

    fun collides(s: Sphere): Boolean {
        return segment.distancesq(s.center) <= s.radius * s.radius + radius * radius
    }

    fun collides(r: Ray): Boolean {
        return false
    }

    fun collides(c: Capsule): Boolean {
        return false
    }

    fun collides(s: Segment): Boolean {
        return false
    }

    val p0: Vector3
        get() = segment.p0

    val p1: Vector3
        get() = segment.p1

}