package com.botijasoftware.utils

import android.opengl.GLES20


class RenderTargetTexture(width: Int, height: Int) : RenderTarget(width, height) {

    override lateinit var texture: Texture
        /*set(value: Texture) {
            super.texture = value
        }*/
    private val color_texture = IntArray(1)

    override fun init() {

        GLES20.glGenTextures(1, color_texture, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, color_texture[0])

        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width,
                height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null)

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST)

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)

        texture = Texture(color_texture[0], width, height)

    }

    override fun setTarget() {

    }

    override fun clear() {
        // TODO Auto-generated method stub

    }

    override fun initRender() {
        // TODO Auto-generated method stub

    }

    override fun endRender() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture.id)
        GLES20.glCopyTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, 0, 0, width, height, 0)
        //GLES20.glCopyTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 1, 1, 0, 0, width, height);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)

    }

}