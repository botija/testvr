package com.botijasoftware.utils.collision


import com.botijasoftware.utils.Vector3

class CollisionCapsule(p0: Vector3, p1: Vector3, radius: Float) : CollisionVolume() {
    private val capsule: Capsule

    init {
        capsule = Capsule(p0, p1, radius)
    }

    override fun collides(c: CollisionVolume): Boolean {
        return c.collides(capsule)
    }

    override fun collides(s: Sphere): Boolean {
        return capsule.collides(s)
    }

    override fun collides(v: Vector3): Boolean {
        return capsule.collides(v)
    }

    override var radius: Float
        get() = capsule.radius
        set(r) {
            capsule.radius = r
        }

    operator fun set(v0: Vector3, v1: Vector3) {
        capsule.set(v0, v1)
    }

    val p0: Vector3
        get() = capsule.p0

    val p1: Vector3
        get() = capsule.p1

}