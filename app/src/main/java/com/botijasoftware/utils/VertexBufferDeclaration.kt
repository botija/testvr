package com.botijasoftware.utils

import java.util.ArrayList

class VertexBufferDeclaration {
    protected var declaration = ArrayList<VertexBufferDefinition>()

    fun add(vbd: VertexBufferDefinition): Int {
        declaration.add(vbd)
        return vbd.mSize * vbd.dataSize
    }

    val count: Int
        get() = declaration.size

    fun getDefinition(index: Int): VertexBufferDefinition? {
        if (index >= 0 && index < declaration.size) {
            return declaration[index]
        }
        return null
    }

    fun searchByUse(use: Int): VertexBufferDefinition? {
        for (i in declaration.indices) {
            val vbd = declaration[i]
            if (vbd.usageGL == use)
                return vbd
        }
        return null
    }

}