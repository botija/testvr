package com.botijasoftware.utils

import java.nio.ByteBuffer
import java.nio.ByteOrder

class ArrayBufferByte(vbdefinition: VertexBufferDefinition, maxelements: Int) : ArrayBuffer(vbdefinition, maxelements) {
    override val buffer: ByteBuffer

    init {

        buffer = ByteBuffer.allocateDirect(
                vbdefinition.dataSize * vbdefinition.mSize * maxelements)
                .order(ByteOrder.nativeOrder())
    }

    override fun put(index: Int, value: Byte) {
        buffer.put(index, value)
    }

    override fun put(data: ByteArray, offset: Int) {
        buffer.put(data, offset, data.size - offset)
        buffer.position(0)
    }

    override fun put(data: ByteArray) {
        buffer.put(data, 0, data.size)
        buffer.position(0)
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

    override fun put(value: Float) {
        buffer.put(value.toByte())
    }

    override fun position(i: Int) {
        buffer.position(i)
    }

    override fun position(): Int {
        return buffer.position()
    }

}