package com.botijasoftware.utils

import android.opengl.GLES20

class TextureOptions {


    constructor(min: Int, max: Int, s: Int, t: Int) : this(true, min, max, s, t) {}

    constructor(usemipmap: Boolean, min: Int, max: Int, s: Int, t: Int) {
        mipmap = usemipmap
        minFilter = min
        maxFilter = max
        wraps = s
        wrapt = t
        //mode = GLES20.GL_MODULATE;
    }

    constructor() {
        minFilter = default_options.minFilter
        maxFilter = default_options.maxFilter
        wraps = default_options.wraps
        wrapt = default_options.wrapt
        mipmap = false
    }

    fun apply() {
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, minFilter.toFloat())
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, maxFilter.toFloat())
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, wraps.toFloat())
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, wrapt.toFloat())
        //GLES20.glTexEnvf(GLES20.GL_TEXTURE_ENV, GLES20.GL_TEXTURE_ENV_MODE, mode);
    }

    var minFilter: Int = 0
    var maxFilter: Int = 0
    var wraps: Int = 0
    var wrapt: Int = 0
    var mipmap: Boolean = false

    companion object {
        //public int mode;

        val nearest_clamp = TextureOptions(GLES20.GL_NEAREST, GLES20.GL_NEAREST, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE)
        val nearest_repeat = TextureOptions(GLES20.GL_NEAREST, GLES20.GL_NEAREST, GLES20.GL_REPEAT, GLES20.GL_REPEAT)
        val linear_clamp = TextureOptions(GLES20.GL_LINEAR, GLES20.GL_LINEAR, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE)
        val linear_repeat = TextureOptions(GLES20.GL_LINEAR, GLES20.GL_LINEAR, GLES20.GL_REPEAT, GLES20.GL_REPEAT)
        val bilinear_clamp = TextureOptions(GLES20.GL_LINEAR_MIPMAP_NEAREST, GLES20.GL_LINEAR_MIPMAP_NEAREST, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE)
        val bilinear_repeat = TextureOptions(GLES20.GL_LINEAR_MIPMAP_NEAREST, GLES20.GL_LINEAR_MIPMAP_NEAREST, GLES20.GL_REPEAT, GLES20.GL_REPEAT)
        val trilinear_clamp = TextureOptions(GLES20.GL_LINEAR_MIPMAP_LINEAR, GLES20.GL_LINEAR_MIPMAP_LINEAR, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE)
        val trilinear_repeat = TextureOptions(GLES20.GL_LINEAR_MIPMAP_LINEAR, GLES20.GL_LINEAR_MIPMAP_LINEAR, GLES20.GL_REPEAT, GLES20.GL_REPEAT)
        val nearest_clamp_nomipmap = TextureOptions(false, GLES20.GL_NEAREST, GLES20.GL_NEAREST, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE)
        val nearest_repeat_nomipmap = TextureOptions(false, GLES20.GL_NEAREST, GLES20.GL_NEAREST, GLES20.GL_REPEAT, GLES20.GL_REPEAT)
        val linear_clamp_nomipmap = TextureOptions(false, GLES20.GL_LINEAR, GLES20.GL_LINEAR, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE)
        val linear_repeat_nomipmap = TextureOptions(false, GLES20.GL_LINEAR, GLES20.GL_LINEAR, GLES20.GL_REPEAT, GLES20.GL_REPEAT)
        val bilinear_clamp_nomipmap = TextureOptions(false, GLES20.GL_LINEAR_MIPMAP_NEAREST, GLES20.GL_LINEAR_MIPMAP_NEAREST, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE)
        val bilinear_repeat_nomipmap = TextureOptions(false, GLES20.GL_LINEAR_MIPMAP_NEAREST, GLES20.GL_LINEAR_MIPMAP_NEAREST, GLES20.GL_REPEAT, GLES20.GL_REPEAT)
        val trilinear_clamp_nomipmap = TextureOptions(false, GLES20.GL_LINEAR_MIPMAP_LINEAR, GLES20.GL_LINEAR_MIPMAP_LINEAR, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE)
        val trilinear_repeat_nomipmap = TextureOptions(false, GLES20.GL_LINEAR_MIPMAP_LINEAR, GLES20.GL_LINEAR_MIPMAP_LINEAR, GLES20.GL_REPEAT, GLES20.GL_REPEAT)
        val default_options = linear_repeat
        val default_options_nomipmap = linear_repeat_nomipmap
    }
}