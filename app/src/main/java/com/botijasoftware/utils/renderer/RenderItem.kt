package com.botijasoftware.utils.renderer

import com.botijasoftware.utils.GLMatrix
import com.botijasoftware.utils.IndexBuffer
import com.botijasoftware.utils.VertexBuffer
import com.botijasoftware.utils.materials.Material

class RenderItem(var vb: VertexBuffer, var ib: IndexBuffer, mvmatrix: GLMatrix, var material: Material) {
    var mvmatrix = GLMatrix()

    init {
        this.mvmatrix.restore(mvmatrix)
    }
}