package com.botijasoftware.utils

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class ArrayBufferFloat(vbdefinition: VertexBufferDefinition, maxelements: Int) : ArrayBuffer(vbdefinition, maxelements) {
    override val buffer: FloatBuffer

    init {

        buffer = ByteBuffer.allocateDirect(
                vbdefinition.dataSize * vbdefinition.mSize * maxelements * float_size)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
    }

    override fun put(index: Int, value: Float) {
        buffer.put(index, value)
    }

    override fun put(data: FloatArray, offset: Int) {
        buffer.put(data, offset, data.size - offset)
        buffer.position(0)
    }

    override fun put(data: FloatArray) {
        buffer.put(data, 0, data.size)
        buffer.position(0)
    }

    override fun put(value: Byte) {
        buffer.put(value.toFloat())
    }

    override fun put(value: Short) {
        buffer.put(value.toFloat())
    }

    override fun put(value: Int) {
        buffer.put(value.toFloat())
    }

    override fun put(value: Float) {
        buffer.put(value)
    }

    override fun position(i: Int) {
        buffer.position(i)
    }

    override fun position(): Int {
        return buffer.position()
    }

    companion object {
        private val float_size = java.lang.Float.SIZE / 8
    }

}