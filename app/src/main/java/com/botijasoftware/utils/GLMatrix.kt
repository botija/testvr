package com.botijasoftware.utils

import android.opengl.Matrix

class GLMatrix {

    var matrix = FloatArray(16)
    private val vector = FloatArray(4)
    private val vectorout = FloatArray(4)

    fun rotate(angle: Float, x: Float, y: Float, z: Float) {
        Matrix.rotateM(matrix, 0, angle, x, y, z)
    }

    fun translate(x: Float, y: Float, z: Float) {
        Matrix.translateM(matrix, 0, x, y, z)
    }

    fun scale(x: Float, y: Float, z: Float) {
        Matrix.scaleM(matrix, 0, x, y, z)
    }

    fun setLookAt(eyeX: Float, eyeY: Float, eyeZ: Float, centerX: Float, centerY: Float, centerZ: Float, upX: Float, upY: Float, upZ: Float) {
        Matrix.setLookAtM(matrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ)
    }

    fun save(m: GLMatrix) {
        System.arraycopy(matrix, 0, m.matrix, 0, 16)
    }

    fun restore(m: GLMatrix) {
        System.arraycopy(m.matrix, 0, matrix, 0, 16)
    }

    fun loadIdentity() {
        Matrix.setIdentityM(matrix, 0)
    }

    fun multMatrix(m1: GLMatrix, m2: GLMatrix) {
        Matrix.multiplyMM(matrix, 0, m1.matrix, 0, m2.matrix, 0)
    }

    fun invert(m: GLMatrix) {
        Matrix.invertM(matrix, 0, m.matrix, 0)
    }

    fun loadMatrix() {

        //GLES10.glLoadMatrixf(matrix, 0);

    }

    fun transform(v: Vector3): Vector3 {
        vector[0] = v.X
        vector[1] = v.Y
        vector[2] = v.Z
        vector[3] = 1f
        Matrix.multiplyMV(vectorout, 0, matrix, 0, vector, 0)
        val out = Vector3(vectorout[0], vectorout[1], vectorout[2])
        out.div(vectorout[3])
        return out
    }
}