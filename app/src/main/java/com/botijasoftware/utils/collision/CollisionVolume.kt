package com.botijasoftware.utils.collision

import com.botijasoftware.utils.Vector3

open class CollisionVolume {
    open fun collides(c: CollisionVolume): Boolean {
        return false
    }

    open fun collides(v: Vector3): Boolean {
        return false
    }

    open fun collides(s: Sphere): Boolean {
        return false
    }

    open fun collides(c: Capsule): Boolean {
        return false
    }

    fun collides(c: Cube): Boolean {
        return false
    }

    fun collides(s: Segment): Boolean {
        return false
    }

    open fun collides(r: Ray): Boolean {
        return false
    }

    fun collides(p: Plane): Boolean {
        return false
    }

    open var position: Vector3
        get() = Vector3(0f, 0f, 0f)
        set(v) {

        }

    open var radius: Float
        get() = 0.0f
        set(r) {

        }
}