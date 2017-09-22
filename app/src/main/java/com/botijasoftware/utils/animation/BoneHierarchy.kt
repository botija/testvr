package com.botijasoftware.utils.animation

import java.util.ArrayList

class BoneHierarchy {

    inner class BoneNode(var bone: Bone) {

        lateinit var parent: BoneNode
        var children = ArrayList<BoneNode>()

        fun addChilren(bone: Bone) {

            val node = BoneNode(bone)
            node.parent = this
            children.add(node)
        }

    }

    var root: BoneNode? = null

    fun findBone(bonename: String): Bone? {

        return null
    }
}
