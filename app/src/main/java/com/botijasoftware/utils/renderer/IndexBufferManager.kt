package com.botijasoftware.utils.renderer

import java.util.ArrayList

import android.opengl.GLES20

import com.botijasoftware.utils.IndexBuffer
import com.botijasoftware.utils.IndexBufferByte
import com.botijasoftware.utils.IndexBufferInt
import com.botijasoftware.utils.IndexBufferShort

class IndexBufferManager {

    private val onUse = ArrayList<IndexBuffer>()
    private val spare = ArrayList<IndexBuffer>()

    fun requestIB(type: Int, format: Int, size: Int): IndexBuffer? {
        var ib = searchSpareIB(type, format, size)
        if (ib != null) {
            return ib
        } else {
            when (format) {
                GLES20.GL_BYTE -> ib = IndexBufferByte(type, size)
                GLES20.GL_UNSIGNED_SHORT -> ib = IndexBufferShort(type, size)
                GLES20.GL_SHORT -> ib = IndexBufferInt(type, size)
                else -> ib = null
            }
            if (ib != null)
                onUse.add(ib)
            return ib
        }
    }

    fun searchSpareIB(type: Int, format: Int, size: Int): IndexBuffer? {

        for (i in spare.indices) {
            val ib = spare[i]
            if (ib.maxElements == size && ib.type == type && ib.format == format) {
                spare.removeAt(i)
                onUse.add(ib)
                return ib
            }
        }
        return null
    }

    fun freeIB(ib: IndexBuffer) {
        if (onUse.contains(ib)) {
            ib.position(0)
            onUse.remove(ib)
            spare.add(ib)
        }
    }


}