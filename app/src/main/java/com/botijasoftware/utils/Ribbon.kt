package com.botijasoftware.utils

import java.util.ArrayList

import com.botijasoftware.utils.renderer.Renderer
import android.opengl.GLES20


class Ribbon(private val maxSegments: Int, private val texture: Texture) : Renderable {
    private var mVertexBuffer: VertexBuffer? = null
    private var mIndexBuffer: IndexBuffer? = null
    private var activeSegments = 0
    private val vertex = ArrayList<Vector3>()
    private var needsreload = true
    private val maxvertex: Int

    init {
        maxvertex = maxSegments * 2 + 2
    }

    override fun Update(time: Float) {

        if (needsreload) {
            recalcVertexBuffer()
            needsreload = false
        }

    }

    override fun LoadContent(resources: ResourceManager) {
        val vbd = VertexBufferDeclaration()
        vbd.add(VertexBufferDefinition(VertexBufferDefinition.FLOAT,
                VertexBufferDefinition.DEF_VERTEX, 3,
                VertexBufferDefinition.ACCESS_DYNAMIC))

        vbd.add(VertexBufferDefinition(VertexBufferDefinition.FLOAT,
                VertexBufferDefinition.DEF_TEXTURE_COORD, 2,
                VertexBufferDefinition.ACCESS_DYNAMIC))

        //mVertexBuffer = new VertexBuffer(maxvertex, vbd);
        //mIndexBuffer = new IndexBufferShort(GLES20.GL_TRIANGLE_STRIP, maxvertex);
        mVertexBuffer = Renderer.vbManager.requestVB(vbd, maxvertex)
        mIndexBuffer = Renderer.ibManager.requestIB(GLES20.GL_TRIANGLE_STRIP, GLES20.GL_UNSIGNED_SHORT, maxvertex)

        mIndexBuffer!!.position(0)

        mIndexBuffer!!.put(0)
        mIndexBuffer!!.put(1)
        mIndexBuffer!!.put(2)
        mIndexBuffer!!.put(3)
        //mIndexBuffer.reset();

        for (i in 2..maxSegments - 1) {
            mIndexBuffer!!.put(i * 2)
            mIndexBuffer!!.put(i * 2 + 1)
        }
        mIndexBuffer!!.position(0)

        mIndexBuffer!!.size = 0

        val coords = texture.textureCoords

        val stamount = (coords.s1 - coords.s0) / maxSegments

        val b = mVertexBuffer!!.getBuffer(1) // texture buffer
        for (i in 0..maxSegments + 1 - 1) {
            val s = coords.s0 + i * stamount
            var index = i * 4
            b!!.put(index++, s)
            b.put(index++, coords.t0)
            b.put(index++, s)
            b.put(index, coords.t1)
        }
        b!!.position(0)

    }

    override fun freeContent(resources: ResourceManager) {
        Renderer.vbManager.freeVB(mVertexBuffer!!)
        Renderer.ibManager.freeIB(mIndexBuffer!!)
    }

    override fun scale(x: Float, y: Float) {

    }

    override fun move(x: Float, y: Float) {

    }


    private fun recalcVertexBuffer() {

        mIndexBuffer!!.size = (activeSegments * 2)
        val b = mVertexBuffer!!.getBuffer(0) // vertex buffer
        for (i in vertex.indices) {
            val v = vertex[i]
            var index = i * 3
            b!!.put(index++, v.X)
            b.put(index++, v.Y)
            b.put(index, v.Z)
        }
        b!!.position(0)

    }

    override fun Draw() {

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture.id)
        mVertexBuffer!!.Draw(mIndexBuffer!!)

    }


    fun addSegment(v0: Vector3, v1: Vector3) {

        if (activeSegments >= maxSegments) {
            vertex.removeAt(0)
            vertex.removeAt(0)
        } else {
            if (vertex.size > 1)
                activeSegments++
        }

        vertex.add(v0)
        vertex.add(v1)
        needsreload = true
    }

}