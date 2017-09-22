package com.botijasoftware.utils

import javax.microedition.khronos.opengles.GL10

import android.opengl.GLES10

class Light(private val mLightID: Int) {

    private val mPosition: FloatArray
    private val mAmbient: FloatArray
    private val mDifuse: FloatArray
    private val mSpecular: FloatArray
    private var mLinearAttenuation: Float = 0.0f
    private var mConstantAttenuation: Float = 0.0f
    private var mQuadricAttenuation: Float = 0.0f

    init {
        mPosition = FloatArray(4)
        mAmbient = FloatArray(4)
        mDifuse = FloatArray(4)
        mSpecular = FloatArray(4)
    }

    fun enable(gl: GL10) {
        GLES10.glEnable(mLightID)
    }

    fun disable(gl: GL10) {
        GLES10.glDisable(mLightID)
    }

    fun setAmbient(gl: GL10, r: Float, g: Float, b: Float, a: Float) {
        mAmbient[0] = r
        mAmbient[1] = g
        mAmbient[2] = b
        mAmbient[3] = a

        GLES10.glLightfv(mLightID, GLES10.GL_AMBIENT, mAmbient, 0)
    }

    fun setSpecular(gl: GL10, r: Float, g: Float, b: Float, a: Float) {
        mSpecular[0] = r
        mSpecular[1] = g
        mSpecular[2] = b
        mSpecular[3] = a

        GLES10.glLightfv(mLightID, GLES10.GL_SPECULAR, mSpecular, 0)
    }

    fun setDifuse(gl: GL10, r: Float, g: Float, b: Float, a: Float) {
        mDifuse[0] = r
        mDifuse[1] = g
        mDifuse[2] = b
        mDifuse[3] = a

        GLES10.glLightfv(mLightID, GLES10.GL_DIFFUSE, mDifuse, 0)
    }

    fun setPosition(gl: GL10, x: Float, y: Float, z: Float) {
        mPosition[0] = x
        mPosition[1] = y
        mPosition[2] = z
        mPosition[3] = 0.0f

        GLES10.glLightfv(mLightID, GLES10.GL_POSITION, mPosition, 0)
    }

    fun setDirection(gl: GL10, x: Float, y: Float, z: Float) {
        mPosition[0] = x
        mPosition[1] = y
        mPosition[2] = z
        mPosition[3] = 1.0f

        GLES10.glLightfv(mLightID, GLES10.GL_POSITION, mPosition, 0)
    }

    fun setLConstantAttenuation(gl: GL10, attenuation: Float) {
        mConstantAttenuation = attenuation
        GLES10.glLightf(mLightID, GLES10.GL_CONSTANT_ATTENUATION, mConstantAttenuation)
    }

    fun setLinearAttenuation(gl: GL10, attenuation: Float) {
        mLinearAttenuation = attenuation
        GLES10.glLightf(mLightID, GLES10.GL_LINEAR_ATTENUATION, mLinearAttenuation)
    }

    fun setQuadraticAttenuation(gl: GL10, attenuation: Float) {
        mQuadricAttenuation = attenuation
        GLES10.glLightf(mLightID, GLES10.GL_QUADRATIC_ATTENUATION, mQuadricAttenuation)
    }

}
	