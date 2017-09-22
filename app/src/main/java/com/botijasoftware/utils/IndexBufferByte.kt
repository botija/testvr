package com.botijasoftware.utils


import java.nio.ByteBuffer
import java.nio.ByteOrder
import android.opengl.GLES20

class IndexBufferByte(type: Int, maxelements: Int) : IndexBuffer(type, maxelements) {
    override val buffer: ByteBuffer


    init {
        if (maxelements > java.lang.Byte.MAX_VALUE)
            this.maxElements = java.lang.Byte.MAX_VALUE.toInt()
        format = GLES20.GL_BYTE
        elementsize = java.lang.Byte.SIZE / 8
        buffer = ByteBuffer.allocateDirect(maxelements * elementsize).order(
                ByteOrder.nativeOrder())

    }

    override fun put(index: Int, value: Byte) {
        buffer.put(index, value)
    }

    override fun put(index: Int, value: Short) {
        buffer.put(index, value.toByte())
    }

    override fun put(index: Int, value: Int) {
        buffer.put(index, value.toByte())
    }

    override fun put(data: ByteArray) {
        buffer.put(data, 0, data.size)
    }

    override fun put(value: Byte) {
        buffer.put(value)
    }

    override fun put(value: Short) {
        buffer.put(value.toByte())
    }

    override fun put(value: Int) {
        buffer.put(value.toByte())
    }

    override fun position(i: Int) {
        buffer.position(i)
    }

    override fun position(): Int {
        return buffer.position()
    }

}