package com.botijasoftware.utils

import com.botijasoftware.utils.renderer.Renderer

import android.opengl.Matrix

class Transform {
    var translation: Vector3
    var rotation: Quaternion
    var scale: Vector3
    var transformMatrix = GLMatrix()
        protected set


    constructor(translation: Vector3, rotation: Quaternion, scale: Vector3) {
        this.translation = translation
        this.rotation = rotation
        this.scale = scale
    }

    constructor(translation: Vector3) {
        this.translation = translation
        this.rotation = Quaternion.ZERO.clone()
        this.scale = Vector3.ZERO.clone()
    }

    constructor() {
        this.translation = Vector3()
        this.rotation = Quaternion()
        this.scale = Vector3()
    }

    fun clone(): Transform {
        return Transform(translation.clone(), rotation.clone(), scale.clone())
    }

    fun setTransform(transform: Transform) {
        this.translation.setValue(transform.translation)
        this.rotation.setValue(transform.rotation)
        this.scale.setValue(transform.scale)
    }

    fun generateMatrix(): GLMatrix {
        Matrix.setIdentityM(translationmatrix.matrix, 0)
        Matrix.translateM(translationmatrix.matrix, 0, translation.X, translation.Y, translation.Z)
        rotation.getMatrix(rotationmatrix.matrix)
        Matrix.setIdentityM(scalematrix.matrix, 0)
        Matrix.scaleM(scalematrix.matrix, 0, scale.X, scale.Y, scale.Z)
        Matrix.multiplyMM(scratchmatrix.matrix, 0, scalematrix.matrix, 0, rotationmatrix.matrix, 0)
        Matrix.multiplyMM(transformMatrix.matrix, 0, translationmatrix.matrix, 0, scratchmatrix.matrix, 0)

        return transformMatrix
    }

    companion object {
        protected var translationmatrix = GLMatrix()
        protected var rotationmatrix = GLMatrix()
        protected var scalematrix = GLMatrix()
        protected var scratchmatrix = GLMatrix()
    }

}