package com.botijasoftware.utils.materials

import java.util.ArrayList

import com.botijasoftware.utils.IndexBuffer
import com.botijasoftware.utils.Mesh
import com.botijasoftware.utils.Model
import com.botijasoftware.utils.VertexBuffer
import com.botijasoftware.utils.renderer.RenderItem

object MaterialRenderer {

    fun renderVertexBuffer(material: Material?, vb: VertexBuffer, ib: IndexBuffer) {
        if (material == null || !material.isLoaded)
            return

        material.set()
        for (i in 0..material.passCount() - 1) {
            val pass = material.mPass[i]
            if (pass != null && pass.set()) {
                vb.Draw(ib)
                pass.unSet()
            }
        }
        material.unSet()
    }

    fun renderModel(model: Model) {

        val size = model.mMesh.size
        for (i in 0..size - 1) {
            val m = model.mMesh[i]
            renderVertexBuffer(m.mMaterial, m.mVertexBuffer, m.mIndexBuffer)
            //Renderer.toRender(m.mVertexBuffer, m.mIndexBuffer, Renderer.modelview, m.mMaterial);
        }
    }

    fun renderModel(model: Model, material: Material) {

        val size = model.mMesh.size
        for (i in 0..size - 1) {
            val m = model.mMesh[i]
            renderVertexBuffer(material, m.mVertexBuffer, m.mIndexBuffer)
            //Renderer.toRender(m.mVertexBuffer, m.mIndexBuffer, Renderer.modelview, m.mMaterial);
        }
    }


    fun renderList(material: Material?, list: ArrayList<RenderItem>) {
        if (material == null || !material.isLoaded)
            return

        material.set()
        val size = list.size
        for (i in 0..material.passCount() - 1) {
            val pass = material.mPass[i]
            if (pass != null && pass.set()) {
                for (j in 0..size - 1) {
                    val ri = list[j]
                    ri.vb.Draw(ri.ib)
                }
                pass.unSet()
            }
        }
        material.unSet()
    }


}