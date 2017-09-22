package com.botijasoftware.utils.collision

import com.botijasoftware.utils.collision.CollisionVolume


interface Collidable {
    fun collides(c: CollisionVolume): Boolean
}