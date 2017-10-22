package com.botijasoftware.utils

import javax.microedition.khronos.opengles.GL10

import android.opengl.GLES10

class Light(private val mLightID: Int) {

    private val mPosition: Vector3 = Vector3(0.0f)
    private val mDirection: Vector3 = Vector3(0.0f)
    private val mAmbient: ColorRGBA = ColorRGBA(0.0f, 0.0f, 0.0f, 1.0f)
    private val mDifuse: ColorRGBA = ColorRGBA(0.0f, 0.0f, 0.0f, 1.0f)
    private val mSpecular: ColorRGBA = ColorRGBA(0.0f, 0.0f, 0.0f, 1.0f)
    private var mLinearAttenuation: Float = 0.0f
    private var mConstantAttenuation: Float = 0.0f
    private var mQuadricAttenuation: Float = 0.0f
    private var mEnabled: Boolean = true


    fun enable() {
        mEnabled = true
    }

    fun disable() {
        mEnabled = false
    }

    fun setAmbient(color: ColorRGBA ) {
        mAmbient.setValue(color.R, color.G, color.B, color.A)
    }

    fun setSpecular(color: ColorRGBA ) {
        mSpecular.setValue(color.R, color.G, color.B, color.A)

    }

    fun setDifuse(color: ColorRGBA ) {
        mDifuse.setValue(color.R, color.G, color.B, color.A)

    }

    fun setPosition(position: Vector3) {
        mPosition.setValue(position)
    }

    fun setDirection(direction: Vector3) {
        mDirection.setValue(direction)
    }

    fun setLConstantAttenuation(attenuation: Float) {
        mConstantAttenuation = attenuation
    }

    fun setLinearAttenuation(attenuation: Float) {
        mLinearAttenuation = attenuation
    }

    fun setQuadraticAttenuation(attenuation: Float) {
        mQuadricAttenuation = attenuation
    }

}
	