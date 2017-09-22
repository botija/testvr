package com.botijasoftware.utils

import com.botijasoftware.utils.renderer.Renderer
import android.opengl.GLES20

class SkyBox(private val position: Vector3, private val size: Float, texture: Texture) : Renderable {
    private val mVertexBuffer: VertexBuffer
    private val mIndexBuffer: IndexBuffer
    // private Texture[] textures = new Texture[6];
    internal var tmptex = FloatArray(24 * 2)
    private var mTexture: Texture? = null

    init {

        val vbd = VertexBufferDeclaration()
        vbd.add(VertexBufferDefinition(VertexBufferDefinition.FLOAT,
                VertexBufferDefinition.DEF_VERTEX, 3,
                VertexBufferDefinition.ACCESS_DYNAMIC))
        vbd.add(VertexBufferDefinition(VertexBufferDefinition.FLOAT,
                VertexBufferDefinition.DEF_TEXTURE_COORD, 2,
                VertexBufferDefinition.ACCESS_DYNAMIC))

        mVertexBuffer = VertexBuffer(24, vbd)
        mIndexBuffer = IndexBufferShort(GLES20.GL_TRIANGLES, 36)

        mIndexBuffer.put(data)
        changeTexture(texture)
        createVertex()
    }

    fun changeTexture(texture: Texture) {
        mTexture = texture

        val coords = mTexture!!.textureCoords
        var i: Int = 0
        while (i < 24 * 2) {
            tmptex[i] = coords.s0
            tmptex[i + 1] = coords.t0
            tmptex[i + 2] = coords.s1
            tmptex[i + 3] = coords.t0
            tmptex[i + 4] = coords.s0
            tmptex[i + 5] = coords.t1
            tmptex[i + 6] = coords.s1
            tmptex[i + 7] = coords.t1
            i += 8
        }

        mVertexBuffer.getBuffer(1)!!.put(tmptex)
    }

    fun createVertex() {

        val data = floatArrayOf(-size, +size, -size, -size, +size, +size, -size, -size, +size, -size, -size, -size,

                +size, +size, -size, +size, -size, -size, +size, -size, +size, +size, +size, +size,

                -size, +size, -size, +size, +size, -size, +size, +size, +size, -size, +size, +size,

                -size, +size, +size, +size, +size, +size, +size, -size, +size, -size, -size, +size,

                -size, -size, +size, +size, -size, +size, +size, -size, -size, -size, -size, -size,

                +size, +size, -size, -size, +size, -size, -size, -size, -size, +size, -size, -size)

        mVertexBuffer.getBuffer(0)!!.put(data)

    }

    override fun Draw() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture!!.id)
        GLES20.glDisable(GLES20.GL_DEPTH_TEST)
        GLES20.glDisable(GLES20.GL_CULL_FACE)
        //GLES20.glDisable(GLES20.GL_LIGHTING);
        //GLES10.glPushMatrix();
        //GLES10.glTranslatef(position.X, position.Y, position.Z);
        Renderer.pushModelViewMatrix()
        Renderer.modelview.translate(position.X, position.Y, position.Z)
        Renderer.loadModelViewMatrix()
        mVertexBuffer.Draw(mIndexBuffer)
        //GLES10.glPopMatrix();
        Renderer.popModelViewMatrix()
        Renderer.loadModelViewMatrix()
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glEnable(GLES20.GL_CULL_FACE)
        //GLES20.glEnable(GLES20.GL_LIGHTING);
    }

    override fun Update(time: Float) {

    }

    override fun LoadContent(resources: ResourceManager) {

    }

    override fun move(x: Float, y: Float) {}

    override fun scale(x: Float, y: Float) {}

    override fun freeContent(resources: ResourceManager) {
        // TODO Auto-generated method stub

    }

    companion object {
        val SKYBOX_FRONT = 0
        val SKYBOX_BACK = 1
        val SKYBOX_LEFT = 2
        val SKYBOX_RIGHT = 3
        val SKYBOX_TOP = 4
        val SKYBOX_BOTTOM = 5
        private val data = shortArrayOf(0, 1, 2, 0, 2, 3, 4, 5, 6, 4, 6, 7, 8, 9, 10, 8, 10, 11, 12, 13, 14, 12, 14, 15, 16, 17, 18, 16, 18, 19, 20, 21, 22, 20, 22, 23)
    }


}