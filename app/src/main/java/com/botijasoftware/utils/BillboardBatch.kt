package com.botijasoftware.utils


import android.opengl.GLES20

import com.botijasoftware.utils.renderer.Renderer

class BillboardBatch @JvmOverloads constructor(nbillboards: Int = MAXBILLBOARDS) {

    protected var mVertexBuffer: VertexBuffer
    protected var mIndexBuffer: IndexBuffer
    protected var mTexture: Texture? = null
    protected var mVisible = true
    private var count = 0
    private var maxbillboards = 0

    init {
        maxbillboards = nbillboards
        val vbd = VertexBufferDeclaration()
        vbd.add(VertexBufferDefinition(VertexBufferDefinition.FLOAT, VertexBufferDefinition.DEF_VERTEX, 3, VertexBufferDefinition.ACCESS_DYNAMIC))
        vbd.add(VertexBufferDefinition(VertexBufferDefinition.FLOAT, VertexBufferDefinition.DEF_TEXTURE_COORD, 2, VertexBufferDefinition.ACCESS_DYNAMIC))
        vbd.add(VertexBufferDefinition(VertexBufferDefinition.BYTE, VertexBufferDefinition.DEF_COLOR, 4, VertexBufferDefinition.ACCESS_DYNAMIC))

        mVertexBuffer = Renderer.vbManager.requestVB(vbd, 4 * nbillboards)
        mIndexBuffer = Renderer.ibManager.requestIB(GLES20.GL_TRIANGLES, GLES20.GL_UNSIGNED_SHORT, 6 * nbillboards)!!


        var v = 0
        var i = 0
        while (v < nbillboards * 4) {
            mIndexBuffer.put(i, v)
            mIndexBuffer.put(i + 1, v + 1)
            mIndexBuffer.put(i + 2, v + 2)
            mIndexBuffer.put(i + 3, v + 1)
            mIndexBuffer.put(i + 4, v + 2)
            mIndexBuffer.put(i + 5, v + 3)
            v += 4
            i += 6
        }
    }


    private fun drawVB() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture!!.id)

        Renderer.pushModelViewMatrix()
        Renderer.modelview.loadIdentity()

        //Renderer.loadModelViewMatrix();
        mIndexBuffer.size = (count * 6) // two triangles per sprite
        mVertexBuffer.Draw(mIndexBuffer)

        Renderer.popModelViewMatrix()
        //Renderer.modelview.loadMatrix();

    }


    private fun putBillboardOnVB(billboard: Billboard) {
        /* public Vector3 position;
        protected Vector2 size;
        public Vector3 scale = new Vector3(1,1,1);
        protected float rotation;
        protected Vector3 rotation_axis; */


    }

    fun Draw() {


    }

    fun end() {

        if (count > 0) {
            drawVB()
            count = 0
            mTexture = null
        }
        //disable sprite shader
        //clean vb
    }

    fun begin() {
        count = 0
        mTexture = null
        //enable sprite shader
        //prepare vb
    }

    companion object {
        private val MAXBILLBOARDS = 128
        private val PVERTEX = 0
        private val PTEXTCOORD = 1
        private val PCOLOR = 2
    }

}