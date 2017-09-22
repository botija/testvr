package com.botijasoftware.utils.animation

import java.util.ArrayList

class Skeleton {

    var animations = ArrayList<Animation>()
    var bones = ArrayList<Bone>()
    var rootBone: Bone? = null
    var currentAnimation: Animation? = null

    fun Update(time: Float) {}

    fun findAnimation(animationname: String): Animation? {
        for (i in animations.indices) {
            val a = animations[i]
            if (a.name.equals(animationname, ignoreCase = true)) {
                return a
            }
        }
        return null
    }

    fun findBone(bonename: String): Bone? {
        for (i in bones.indices) {
            val b = bones[i]
            if (b.name.equals(bonename, ignoreCase = true)) {
                return b
            }
        }
        return null
    }

}