package com.botijasoftware.utils.collision

import com.botijasoftware.utils.Vector3

class CollisionSphere(center: Vector3, radius: Float) : CollisionVolume() {
    private val sphere: Sphere

    init {
        sphere = Sphere(center, radius)
    }

    override fun collides(c: CollisionVolume): Boolean {
        return c.collides(sphere)
    }

    override fun collides(s: Sphere): Boolean {
        return sphere.collides(s)
    }

    override fun collides(v: Vector3): Boolean {
        return sphere.collides(v)
    }

    override fun collides(c: Capsule): Boolean {
        return c.collides(sphere)
    }

    override fun collides(r: Ray): Boolean {
        return r.collides(sphere)
    }

    override var position: Vector3
        get() = sphere.center
        set(v) {
            sphere.center = v
        }

    override var radius: Float
        get() = sphere.radius
        set(r) { sphere.radius = r }

}