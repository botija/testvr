package com.botijasoftware.utils

import android.opengl.GLES20

import com.botijasoftware.utils.renderer.Renderer

class VertexBufferDefinition(var mFormat: Int, var usageGL: Int, var mSize: Int, var mAccess: Int) {

    fun setUsage(usage: Int) {
        usageGL = usage
    }

    fun setAccess(access: Int) {
        mAccess = access
    }

    val dataSize: Int
        get() = sizes[mFormat]

    val formatGL: Int
        get() = formats[mFormat]

    val accessGL: Int
        get() = access[mAccess]


    fun clone(): VertexBufferDefinition {
        return VertexBufferDefinition(mFormat, usageGL, mSize, mAccess)
    }

    companion object {

        val FLOAT = 0
        val INT = 1
        val SHORT = 2
        val BYTE = 3
        val sizes = intArrayOf(java.lang.Float.SIZE / 8, Integer.SIZE / 8, java.lang.Short.SIZE / 8, java.lang.Byte.SIZE / 8)
        val formats = intArrayOf(GLES20.GL_FLOAT, GLES20.GL_SHORT, GLES20.GL_UNSIGNED_SHORT, GLES20.GL_UNSIGNED_BYTE)

        val DEF_VERTEX = 0
        val DEF_TEXTURE_COORD = 1
        val DEF_COLOR = 2
        val DEF_NORMAL = 3
        val DEF_TANGENT = 4

        //public static final int[] usage = { GLES10.GL_VERTEX_ARRAY,GLES10.GL_TEXTURE_COORD_ARRAY, GLES10.GL_COLOR_ARRAY,GLES10.GL_NORMAL_ARRAY, 0, 0 };

        val ACCESS_STATIC = 0
        val ACCESS_DYNAMIC = 1
        val access = intArrayOf(GLES20.GL_STATIC_DRAW, GLES20.GL_DYNAMIC_DRAW)

        val VBD_VERTEX = VertexBufferDefinition(FLOAT, Renderer.ATTRIBUTE_VERTEX, 3, ACCESS_DYNAMIC)
        val VBD_TEXTURE_COORD = VertexBufferDefinition(FLOAT, Renderer.ATTRIBUTE_TEXCOORDS, 2, ACCESS_DYNAMIC)
        val VBD_COLOR_UBRGBA = VertexBufferDefinition(BYTE, Renderer.ATTRIBUTE_COLOR, 4, ACCESS_DYNAMIC)
        val VBD_COLOR_UBRGB = VertexBufferDefinition(BYTE, Renderer.ATTRIBUTE_COLOR, 3, ACCESS_DYNAMIC)
        val VBD_COLOR_RGBA = VertexBufferDefinition(FLOAT, Renderer.ATTRIBUTE_COLOR, 4, ACCESS_DYNAMIC)
        val VBD_COLOR_RGB = VertexBufferDefinition(FLOAT, Renderer.ATTRIBUTE_COLOR, 3, ACCESS_DYNAMIC)
        val VBD_NORMAL = VertexBufferDefinition(FLOAT, Renderer.ATTRIBUTE_NORMAL, 3, ACCESS_DYNAMIC)
    }

}
