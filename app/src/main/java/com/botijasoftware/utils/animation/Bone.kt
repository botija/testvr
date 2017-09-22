package com.botijasoftware.utils.animation

import android.opengl.Matrix

import com.botijasoftware.utils.Vector3

class Bone(var name: String, var id: Int) {

    lateinit var parent: Bone
    lateinit var position: Vector3
    var rotationangle: Float = 0.0f //in radians
    lateinit var rotationaxis: Vector3
    private val transformmatrix = FloatArray(16)

    fun setPosition(x: Float, y: Float, z: Float) {
        position = Vector3(x, y, z)

    }

    fun setRotation(angle: Float, x: Float, y: Float, z: Float) {
        rotationangle = angle
        rotationaxis = Vector3(x, y, z)
    }

    fun transmformMatrix(translate: Vector3, rotateaxis: Vector3, rotateangle: Float, scale: Vector3) {
        Matrix.setIdentityM(transformmatrix, 0)
        Matrix.rotateM(transformmatrix, 0, rotateangle, rotateaxis.X, rotateaxis.Y, rotateaxis.Z)
        Matrix.translateM(transformmatrix, 0, translate.X, translate.Y, translate.Z)
        Matrix.scaleM(transformmatrix, 0, scale.X, scale.Y, scale.Z)
    }

}
