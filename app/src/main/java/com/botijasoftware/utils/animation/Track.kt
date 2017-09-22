package com.botijasoftware.utils.animation

import java.util.ArrayList


class Track(var bone: Bone) {
    var keyframes = ArrayList<Keyframe>()

    fun transformBone(keyframe: Keyframe) {
        keyframe.transformBone(bone)
    }
}


