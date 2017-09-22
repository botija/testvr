package com.botijasoftware.utils

import android.opengl.GLES20

class RenderTargetScreen(width: Int, height: Int) : RenderTarget(width, height) {

    override fun init() {}

    override fun setTarget() {

    }

    override fun clear() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
    }

    override fun initRender() {

    }

    override fun endRender() {

    }

}