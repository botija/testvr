package com.botijasoftware.utils

class GLMatrixStack {
    var MAX_STACK = 12
    private val stack = arrayOfNulls<GLMatrix>(MAX_STACK)
    private var index: Int = 0

    init {
        for (i in 0..MAX_STACK - 1) {
            stack[i] = GLMatrix()
        }
        index = 0
    }

    fun pushMatrix(m: GLMatrix) {
        if (index < MAX_STACK) {
            stack[index++]!!.restore(m)
        }
    }

    fun popMatrix(m: GLMatrix) {
        if (index > 0) {
            stack[--index]!!.save(m)
        }
    }
}