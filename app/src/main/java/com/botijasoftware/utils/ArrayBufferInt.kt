package com.botijasoftware.utils

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.IntBuffer

class ArrayBufferInt(vbdefinition: VertexBufferDefinition, maxelements: Int) : ArrayBuffer(vbdefinition, maxelements) {
    override val buffer: IntBuffer


    init {

        buffer = ByteBuffer.allocateDirect(
                vbdefinition.dataSize * vbdefinition.mSize * maxelements * int_size)
                .order(ByteOrder.nativeOrder()).asIntBuffer()
    }

    override fun put(index: Int, value: Int) {
        buffer.put(index, value)
    }

    override fun put(data: IntArray, offset: Int) {
        buffer.put(data, offset, data.size - offset)
        buffer.position(0)
    }

    override fun put(data: IntArray) {
        buffer.put(data, 0, data.size)
        buffer.position(0)
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

    override fun put(value: Float) {
        buffer.put(value.toInt())
    }

    override fun position(i: Int) {
        buffer.position(i)
    }

    override fun position(): Int {
        return buffer.position()
    }

    companion object {
        private val int_size = Integer.SIZE / 8
    }

}