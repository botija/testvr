package com.botijasoftware.ParticleSystem

import java.util.ArrayList

import javax.microedition.khronos.opengles.GL10

import android.opengl.GLES10
import android.opengl.GLES20
import com.botijasoftware.utils.*
import com.botijasoftware.utils.renderer.Renderer

class ParticleRenderer(protected var mEmitter: ParticleEmitter) {
    lateinit protected var mVertexBuffer: VertexBuffer
    lateinit protected var mIndexBuffer: IndexBuffer
    var usedsprites: Int = 0
    private var count = 0

    protected lateinit var mTexture: Texture
    protected var mParticles: ParticlePool? = null
    protected var maxParticles: Int = 0
    protected var mColor: Boolean = false
    protected var mAnimation: Boolean = false
    protected var mRotation: Boolean = false
    protected var mTrail: Boolean = false
    protected var mOnDead: Boolean = false
    protected var mChild: Boolean = false

    init {
        mParticles = mEmitter.particlePool
        maxParticles = mEmitter.size
        mColor = mEmitter.mUseColor
        mAnimation = mEmitter.mUseAnimation
        mRotation = mEmitter.mUseRotation
        mTrail = mEmitter.mUseTrail
        mOnDead = mEmitter.mUseOnDead
        mChild = mEmitter.mUseChild

        val vbd = VertexBufferDeclaration()
        vbd.add(VertexBufferDefinition(VertexBufferDefinition.FLOAT, VertexBufferDefinition.DEF_VERTEX, 3, VertexBufferDefinition.ACCESS_DYNAMIC))
        vbd.add(VertexBufferDefinition(VertexBufferDefinition.FLOAT, VertexBufferDefinition.DEF_TEXTURE_COORD, 2, VertexBufferDefinition.ACCESS_DYNAMIC))

        if (mColor) {
            vbd.add(VertexBufferDefinition(VertexBufferDefinition.BYTE, VertexBufferDefinition.DEF_COLOR, 4, VertexBufferDefinition.ACCESS_DYNAMIC))
        }

        mVertexBuffer = Renderer.vbManager.requestVB(vbd, 4 * MAXPARTICLES)
        mIndexBuffer = Renderer.ibManager.requestIB(GLES20.GL_TRIANGLES, GLES20.GL_UNSIGNED_SHORT, 6 * MAXPARTICLES)!!


        var v = 0
        var i = 0
        while (v < MAXPARTICLES * 4) {
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

    fun Update(time: Float) {}
    fun Draw(gl: GL10) {

        GLES10.glBindTexture(GLES10.GL_TEXTURE_2D, mEmitter.texture!!.textureID)
        GLES10.glBlendFunc(mEmitter.mSrcBlendMode, mEmitter.mDstBlendMode)

        val particles = mParticles!!.activeParticles
        val size = particles.size

        GLES10.glEnableClientState(GLES10.GL_VERTEX_ARRAY)
        GLES10.glEnableClientState(GLES10.GL_TEXTURE_COORD_ARRAY)
        if (mEmitter.mUseColor) {
            GLES10.glEnableClientState(GLES10.GL_COLOR_ARRAY)
        }

        for (i in 0 until size) {
            val p = particles[i]
            drawParticle(gl, p, mEmitter)
        }

        if (mEmitter.mUseColor) {
            GLES10.glDisableClientState(GLES10.GL_COLOR_ARRAY)
        }
        GLES10.glDisableClientState(GLES10.GL_TEXTURE_COORD_ARRAY)
        GLES10.glDisableClientState(GLES10.GL_VERTEX_ARRAY)

        if (mTrail) {
            val emitter = mEmitter.trailEmitter

            emitter?.Draw(gl)
        }

        if (mOnDead) {
            val emitter = mEmitter.onDeadEmitter

            emitter?.Draw(gl)
        }

        if (mChild) {
            val emitter = mEmitter.childEmitter

            emitter?.Draw(gl)
        }

        //restore default blend modes
        GLES10.glBlendFunc(GLES10.GL_SRC_ALPHA, GLES10.GL_ONE_MINUS_SRC_ALPHA)

    }

    private fun drawParticle(gl: GL10, p: Particle, emitter: ParticleEmitter) {

        val coords = emitter.texture!!.getTextureCoords(p.animation.currentFrame)

        val x = p.size.X / 2.0f
        val y = p.size.Y / 2.0f

        val data = floatArrayOf(-x , -y , 0.0f, x , -y , 0.0f, -x, y , 0.0f, x, y, 0.0f)
        var vb: ArrayBuffer = mVertexBuffer.getBuffer(PVERTEX)!!
        vb.position(0 )
        vb.put(data)
        vb.position(0)


        if (mAnimation) {
            //val coords = mTexture.textureCoords

            /*if (flip) {
                coords.flipVertical()
            }*/

            val st = floatArrayOf(coords.s0, coords.t0, coords.s1, coords.t0, coords.s0, coords.t1, coords.s1, coords.t1)
            vb = mVertexBuffer.getBuffer(PTEXTCOORD)!!
            vb.position(0)
            vb.put(st)
            vb.position(0)
        }

        if (mColor) {
            val r = p.color.R.toByte()
            val g = p.color.G.toByte()
            val b = p.color.B.toByte()
            val a = p.color.A.toByte()

            val cdata = byteArrayOf(r, g, b, a, r, g, b, a, r, g, b, a, r, g, b, a)

            vb = mVertexBuffer.getBuffer(PCOLOR)!!
            vb.position(0)
            vb.put(cdata)
            vb.position(0)
        }



        //GLES10.glTranslatef(p.position.X, p.position.Y, p.position.Z)

        //if (mRotation) {
        //    GLES10.glRotatef(p.rotation.X, 1.0f, 0f, 0f)
        //    GLES10.glRotatef(p.rotation.Y, 0f, 1.0f, 0f)
        //    GLES10.glRotatef(p.rotation.Z, 0f, 0f, 1.0f)
        //}

        Renderer.BindTexture(Renderer.TEXTURE0, emitter.texture!!.textureID)

        Renderer.pushModelViewMatrix()
        Renderer.modelview.loadIdentity()

        Renderer.loadModelViewMatrix()
        mIndexBuffer.size = count * 6 // two triangles per sprite
        mVertexBuffer.Draw(mIndexBuffer)

        Renderer.popModelViewMatrix()
    }


    private fun putParticleOnVB(p: Particle, scale: Vector2, rotation: Float, color: ColorRGBAb) {

        usedsprites++

        val x = p.position.X;
        val y = p.position.Y;
        val z = p.position.Z;
        val w = p.size.X / 2.0f
        val h = p.size.Y / 2.0f

        val hw = w * scale.X
        val hh = h * scale.Y

        // |cos -sin| * [x y]  --> xr = x * cos - y * sin
        // |sin  cos|              yr = x * sin + y * cos


        val sin = Math.sin(rotation.toDouble()).toFloat()
        val cos = Math.cos(rotation.toDouble()).toFloat()

        val x0 = -hw
        val x1 = +hw
        val y0 = -hh
        val y1 = +hh

        val data = floatArrayOf(x + x0 * cos - y0 * sin, y + x0 * sin + y0 * cos, 0.0f, x + x1 * cos - y0 * sin, y + x1 * sin + y0 * cos, 0.0f, x + x0 * cos - y1 * sin, y + x0 * sin + y1 * cos, 0.0f, x + x1 * cos - y1 * sin, y + x1 * sin + y1 * cos, 0.0f)


        //float data [] = {x0, y0, 0.0f , x1, y0, 0.0f, x0, y1, 0.0f, x1, y1, 0.0f};


        var vb: ArrayBuffer = mVertexBuffer.getBuffer(PVERTEX)!!
        vb.position(count * 12)
        vb.put(data)
        vb.position(0)

        val coords = mTexture.textureCoords

        /*if (flip) {
            coords.flipVertical()
        }*/

        val st = floatArrayOf(coords.s0, coords.t0, coords.s1, coords.t0, coords.s0, coords.t1, coords.s1, coords.t1)
        vb = mVertexBuffer.getBuffer(PTEXTCOORD)!!
        vb.position(count * 8)
        vb.put(st)
        vb.position(0)

        val r = color.R
        val g = color.G
        val b = color.B
        val a = color.A

        val cdata = byteArrayOf(r, g, b, a, r, g, b, a, r, g, b, a, r, g, b, a)

        //mVertexBuffer.getBuffer(2).put(cdata);
        vb = mVertexBuffer.getBuffer(PCOLOR)!!
        vb.position(count * 16)
        vb.put(cdata)
        vb.position(0)

        count++
    }


    companion object {
        private val MAXPARTICLES = 1
        private val PVERTEX = 0
        private val PTEXTCOORD = 1
        private val PCOLOR = 2
    }

}

