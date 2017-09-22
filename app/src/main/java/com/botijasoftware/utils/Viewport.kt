package com.botijasoftware.utils

import com.botijasoftware.utils.renderer.Renderer

import android.opengl.GLES20

class Viewport(private val X: Int, private val Y: Int, private val width: Int, private val height: Int) {

    fun enable() {
        GLES20.glViewport(X, Y, width, height)
        //GLES20.glLoadIdentity();
        Renderer.modelview.loadIdentity()
    }

    fun getAsArray(viewport: IntArray) {
        viewport[0] = X
        viewport[1] = Y
        viewport[2] = width
        viewport[3] = height
    }
}
