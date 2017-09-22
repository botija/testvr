package com.botijasoftware.utils


import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.nio.ByteOrder
import android.opengl.GLES20

class IndexBufferInt(type: Int, maxelements: Int) : IndexBuffer(type, maxelements) {

    override val buffer: IntBuffer

    init {
        format = GLES20.GL_SHORT
        elementsize = Integer.SIZE / 8
        buffer = ByteBuffer.allocateDirect(maxelements * elementsize).order(
                ByteOrder.nativeOrder()).asIntBuffer()

    }

    override fun put(index: Int, value: Byte) {
        buffer.put(index, value.toInt())
    }

    override fun put(index: Int, value: Short) {
        buffer.put(index, value.toInt())
    }

    override fun put(index: Int, value: Int) {
        buffer.put(index, value)
    }

    override fun put(data: IntArray) {
        buffer.put(data, 0, data.size)
    }

    override fun put(value: Byte) {
        buffer.put(value.toInt())
    }

    override fun put(value: Short) {
        buffer.put(value.toInt())
    }

    override fun put(value: Int) {
        buffer.put(value)
    }

    override fun position(i: Int) {
        buffer.position(i)
    }

    override fun position(): Int {
        return buffer.position()
    }

}