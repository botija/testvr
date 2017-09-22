package com.botijasoftware.utils.animation

import com.botijasoftware.utils.Vector3

class Keyframe(var time: Float) {
    lateinit var translate: Vector3
    var angle: Float = 0.toFloat()
    lateinit var axis: Vector3
    lateinit var scale: Vector3

    fun setTranslate(x: Float, y: Float, z: Float) {
        translate = Vector3(x, y, z)

    }

    fun setScale(x: Float, y: Float, z: Float) {
        scale = Vector3(x, y, z)

    }

    fun setRotation(angle: Float, x: Float, y: Float, z: Float) {
        this.angle = angle
        axis = Vector3(x, y, z)
    }

    fun transformBone(bone: Bone) {
        bone.transmformMatrix(translate, axis, (angle * Math.PI / 180.0f).toFloat(), scale)
    }

}
