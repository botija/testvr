package com.botijasoftware.ParticleSystem

import java.util.ArrayList

import javax.microedition.khronos.opengles.GL10

import android.opengl.GLES10

import com.botijasoftware.utils.TextureCoords
import com.botijasoftware.utils.VertexBuffer

class ParticleRenderer(protected var mEmitter: ParticleEmitter) {
    protected var mVertexBuffer: VertexBuffer
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

        mVertexBuffer = VertexBuffer(4, 6, mColor)

        mVertexBuffer.setIndexData(0.toShort(), 0.toShort())
        mVertexBuffer.setIndexData(1.toShort(), 1.toShort())
        mVertexBuffer.setIndexData(2.toShort(), 2.toShort())
        mVertexBuffer.setIndexData(3.toShort(), 1.toShort())
        mVertexBuffer.setIndexData(4.toShort(), 2.toShort())
        mVertexBuffer.setIndexData(5.toShort(), 3.toShort())

        //mVertexBuffer.setVertex((short)0, 0, 0, -10);
        mVertexBuffer.setTextCoord(0.toShort(), 0, 0)
        //mVertexBuffer.setVertex((short)1, 10, 0, -10);
        mVertexBuffer.setTextCoord(1.toShort(), 1, 0)
        //mVertexBuffer.setVertex((short)2, 0, 10, -10);
        mVertexBuffer.setTextCoord(2.toShort(), 0, 1)
        //mVertexBuffer.setVertex((short)3, 10, 10, -10);
        mVertexBuffer.setTextCoord(3.toShort(), 1, 1)

        if (mColor) {
            mVertexBuffer.setColor(0.toShort(), 1.0f, 1.0f, 1.0f, 1.0f)
            mVertexBuffer.setColor(1.toShort(), 1.0f, 1.0f, 1.0f, 1.0f)
            mVertexBuffer.setColor(2.toShort(), 1.0f, 1.0f, 1.0f, 1.0f)
            mVertexBuffer.setColor(3.toShort(), 1.0f, 1.0f, 1.0f, 1.0f)
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

        mVertexBuffer.setVertex(0.toShort(), -x, -y, 0.0f)
        mVertexBuffer.setVertex(1.toShort(), x, -y, 0.0f)
        mVertexBuffer.setVertex(2.toShort(), -x, y, 0.0f)
        mVertexBuffer.setVertex(3.toShort(), x, y, 0.0f)

        if (mAnimation) {
            mVertexBuffer.setTextCoord(0.toShort(), coords.s0, coords.t0)
            mVertexBuffer.setTextCoord(1.toShort(), coords.s1, coords.t0)
            mVertexBuffer.setTextCoord(2.toShort(), coords.s0, coords.t1)
            mVertexBuffer.setTextCoord(3.toShort(), coords.s1, coords.t1)
        }

        if (mColor) {
            val r = p.color.R
            val g = p.color.G
            val b = p.color.B
            val a = p.color.A
            mVertexBuffer.setColor(0.toShort(), r, g, b, a)
            mVertexBuffer.setColor(1.toShort(), r, g, b, a)
            mVertexBuffer.setColor(2.toShort(), r, g, b, a)
            mVertexBuffer.setColor(3.toShort(), r, g, b, a)
        }

        GLES10.glPushMatrix()
        //GLES10.glLoadIdentity();
        GLES10.glTranslatef(p.position.X, p.position.Y, p.position.Z)

        if (mRotation) {
            GLES10.glRotatef(p.rotation.X, 1.0f, 0f, 0f)
            GLES10.glRotatef(p.rotation.Y, 0f, 1.0f, 0f)
            GLES10.glRotatef(p.rotation.Z, 0f, 0f, 1.0f)
        }

        mVertexBuffer.fastDraw(gl, 6)
        GLES10.glPopMatrix()
    }
}

