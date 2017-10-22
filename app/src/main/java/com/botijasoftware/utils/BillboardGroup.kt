package com.botijasoftware.utils

import java.util.ArrayList
import java.util.Collections
import java.util.Comparator
import com.botijasoftware.utils.renderer.Renderer
import android.opengl.GLES20

class BillboardGroup @JvmOverloads constructor(private val camera: Camera, sort: Boolean = false) : Renderable {

    private val billboards = ArrayList<Billboard>()
    private var issorted = false

    init {
        this.issorted = sort
    }


    override fun Update(time: Float) {
        if (issorted)
            sort(camera.eye)
    }

    override fun LoadContent(resources: ResourceManager) {
        // TODO Auto-generated method stub

    }

    override fun freeContent(resources: ResourceManager) {
        // TODO Auto-generated method stub

    }


    override fun Draw() {

        var textureid: Int
        val size = billboards.size
        if (size > 0) {
            val b = billboards[0]
            textureid = b.mTexture.id
            b.mVertexBuffer.enableState()
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureid)
        } else
            return

        for (i in 0..size - 1) {
            val b = billboards[i]

            if (textureid != b.mTexture.id) {
                textureid = b.mTexture.id
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureid)
            }

            //GLES20.glPushMatrix();
            Renderer.pushModelViewMatrix()
            b.Transform()

            Renderer.loadModelViewMatrix()
            b.mVertexBuffer.fastDraw(b.mIndexBuffer)
            //GLES20.glPopMatrix();
            Renderer.popModelViewMatrix()
            Renderer.loadModelViewMatrix()
        }
        if (size > 0)
            billboards[0].mVertexBuffer.disableState()

    }


    fun addBillboard(b: Billboard) {
        billboards.add(b)
    }

    fun removeBillboard(b: Billboard) {
        billboards.remove(b)
    }

    fun sort(camera: Vector3) {
        for (i in billboards.indices) {
            billboards[i].calcDistancesq(camera)
        }

        Collections.sort(billboards) { one, two ->
            when {
                one.camdistance > two.camdistance -> 1
                two.camdistance > one.camdistance -> -1
                else -> 0
            }
        }
    }

}