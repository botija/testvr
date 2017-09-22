package com.botijasoftware.utils.collision

import com.botijasoftware.utils.Vector3

class CollisionPoint(override var position: Vector3) : CollisionVolume() {

    override fun collides(c: CollisionVolume): Boolean {
        return c.collides(position)
    }

    override fun collides(s: Sphere): Boolean {
        return s.collides(position)
    }


    override fun collides(c: Capsule): Boolean {
        return c.collides(position)
    }

    override fun collides(v: Vector3): Boolean {
        return v.equals(position)
    }

}