package com.botijasoftware.utils

import java.nio.Buffer

abstract class IndexBuffer(var type: Int, maxelements: Int) {

    var maxElements: Int = 0
    var size: Int = 0
        set(value) {
            if (value >= 0) {
                field = value
            }
            if (field > maxElements)
                field = maxElements
        }

    var format: Int = 0
    protected var elementsize: Int = 0

    init {
        this.maxElements = maxelements
        this.size = maxelements
    }


    abstract val buffer: Buffer

    open fun put(index: Int, value: Byte) {}
    open fun put(data: ByteArray) {}
    open fun put(index: Int, value: Short) {}
    open fun put(data: ShortArray) {}
    open fun put(index: Int, value: Int) {}
    open fun put(data: IntArray) {}
    open fun put(value: Byte) {}
    open fun put(value: Short) {}
    open fun put(value: Int) {}

    open fun position(i: Int) {}
    open fun position(): Int {
        return 0
    }


}