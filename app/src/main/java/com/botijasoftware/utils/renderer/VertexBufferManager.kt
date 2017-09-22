package com.botijasoftware.utils.renderer

import java.util.ArrayList
import com.botijasoftware.utils.VertexBuffer
import com.botijasoftware.utils.VertexBufferDeclaration
import com.botijasoftware.utils.VertexBufferDefinition

class VertexBufferManager {

    private val onUse = ArrayList<VertexBuffer>()
    private val spare = ArrayList<VertexBuffer>()

    fun requestVB(vbd: VertexBufferDeclaration, size: Int): VertexBuffer {
        var vb = searchSpareVB(vbd, size)
        if (vb != null) {
            return vb
        } else {
            vb = VertexBuffer(size, vbd)
            onUse.add(vb)
            return vb
        }
    }

    fun searchSpareVB(vbd: VertexBufferDeclaration, size: Int): VertexBuffer? {
        var found: Boolean
        for (i in spare.indices) {
            val vb = spare[i]
            if (vb.elementCount == size) { //same size
                val dec = vb.declaration
                if (dec.count == vbd.count) { //same number of elements
                    found = true
                    var j = 0
                    while (j < vbd.count && found) { //check each element
                        val def1 = dec.getDefinition(j)
                        val def2 = vbd.getDefinition(j)
                        if (def1!!.mAccess != def2!!.mAccess || def1.mFormat != def2.mFormat || def1.mSize != def2.mSize || def1.usageGL != def2.usageGL) {
                            found = false
                        }
                        j++
                    }
                    if (found) {
                        spare.remove(vb)
                        onUse.add(vb)
                        return vb
                    }
                }
            }
        }
        return null
    }

    fun freeVB(vb: VertexBuffer) {
        if (onUse.contains(vb)) {
            onUse.remove(vb)
            spare.add(vb)
        }
    }


}