package com.botijasoftware.utils

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.ShortBuffer

class ArrayBufferShort(vbdefinition: VertexBufferDefinition, maxelements: Int) : ArrayBuffer(vbdefinition, maxelements) {
    override val buffer: ShortBuffer

    init {

        buffer = ByteBuffer.allocateDirect(
                vbdefinition.dataSize * vbdefinition.mSize * maxelements * short_size)
                .order(ByteOrder.nativeOrder()).asShortBuffer()
    }

    override fun put(index: Int, value: Short) {
        buffer.put(index, value)
    }

    override fun put(data: ShortArray, offset: Int) {
        buffer.put(data, offset, data.size - offset)
        buffer.position(0)
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

    override fun put(value: Float) {
        buffer.put(value.toShort())
    }

    override fun position(i: Int) {
        buffer.position(i)
    }

    override fun position(): Int {
        return buffer.position()
    }

    companion object {
        private val short_size = java.lang.Short.SIZE / 8
    }


}