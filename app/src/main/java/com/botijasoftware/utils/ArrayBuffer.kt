package com.botijasoftware.utils

import java.nio.Buffer

abstract class ArrayBuffer(protected var vbdefinition: VertexBufferDefinition, //protected Buffer mBuffer;
                           protected var maxelements: Int) {

    abstract val buffer: Buffer

    open fun put(index: Int, value: Float) {}
    open fun put(data: FloatArray, offset: Int) {}
    open fun put(data: FloatArray) {}
    open fun put(index: Int, value: Int) {}
    open fun put(data: IntArray, offset: Int) {}
    open fun put(data: IntArray) {}
    open fun put(index: Int, value: Short) {}
    open fun put(data: ShortArray, offset: Int) {}
    open fun put(data: ShortArray) {}
    open fun put(index: Int, value: Byte) {}
    open fun put(data: ByteArray, offset: Int) {}
    open fun put(data: ByteArray) {}

    open fun put(value: Byte) {}
    open fun put(value: Short) {}
    open fun put(value: Int) {}
    open fun put(value: Float) {}

    open fun position(i: Int) {}
    open fun position(): Int {
        return 0
    }

}