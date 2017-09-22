package com.botijasoftware.utils


import java.nio.ByteBuffer
import java.nio.ShortBuffer
import java.nio.ByteOrder
import android.opengl.GLES20

class IndexBufferShort(type: Int, maxelements: Int) : IndexBuffer(type, maxelements) {
    override val buffer: ShortBuffer

    init {
        if (maxelements > java.lang.Short.MAX_VALUE)
            this.maxElements = java.lang.Short.MAX_VALUE.toInt()
        format = GLES20.GL_UNSIGNED_SHORT
        elementsize = java.lang.Short.SIZE / 8
        buffer = ByteBuffer.allocateDirect(maxelements * elementsize).order(
                ByteOrder.nativeOrder()).asShortBuffer()

    }

    override fun put(index: Int, value: Byte) {
        buffer.put(index, value.toShort())
    }

    override fun put(index: Int, value: Short) {
        buffer.put(index, value)
    }

    override fun put(index: Int, value: Int) {
        buffer.put(index, value.toShort())
    }

    override fun put(data: ShortArray) {
        buffer.put(data, 0, data.size)
        buffer.position(0)
    }

    override fun put(value: Byte) {
        buffer.put(value.toShort())
    }

    override fun put(value: Short) {
        buffer.put(value)
    }

    override fun put(value: Int) {
        buffer.put(value.toShort())
    }

    override fun position(i: Int) {
        buffer.position(i)
    }

    override fun position(): Int {
        return buffer.position()
    }


}