package com.botijasoftware.utils

import java.util.ArrayList

import javax.microedition.khronos.opengles.GL10

import android.opengl.GLES10

class LRSAnimation @JvmOverloads constructor(private val mLoop: Boolean = false) {

    var mFrames: ArrayList<LRSAnimationFrame>
    protected val mInterpolatedFrame: LRSAnimationFrame? = null
    private var mCurrentFrame: Int = 0
    private var mNextFrame: Int = 0
    private var mAcumTime = 0.0f
    private var mAnimationTime = 0.0f
    private var mCurrentFrameTime = 0.0f
    private val mMatrix: FloatArray

    init {
        mFrames = ArrayList<LRSAnimationFrame>()
        mMatrix = FloatArray(16)
    }

    fun update(time: Float) {
        mAcumTime += time
        val frame = mFrames[mCurrentFrame]

        if (frame.mTime > mCurrentFrameTime + time) {
            mCurrentFrame = mNextFrame
            mNextFrame++
            if (mNextFrame >= mFrames.size) {
                if (mLoop)
                    mNextFrame = 0
                else
                    mNextFrame = mFrames.size - 1
            }
            mAnimationTime += frame.mTime
            mCurrentFrameTime = mCurrentFrameTime + time - frame.mTime
        } else {
            mCurrentFrameTime += time
        }

        val startframe = mFrames[mCurrentFrame]
        val endframe = mFrames[mNextFrame]

        endframe.interpolate(startframe, mInterpolatedFrame, mCurrentFrameTime)
    }

    fun apply(gl: GL10) {
        GLES10.glPushMatrix()

        GLES10.glTranslatef(mInterpolatedFrame!!.mTrans.X, mInterpolatedFrame.mTrans.Y, mInterpolatedFrame.mTrans.Z)
        GLES10.glTranslatef(mInterpolatedFrame.mRotCenter.X, mInterpolatedFrame.mRotCenter.Y, mInterpolatedFrame.mRotCenter.Z)
        mInterpolatedFrame.mRotation.getMatrix(mMatrix)
        GLES10.glMultMatrixf(mMatrix, 0)
        GLES10.glTranslatef(-mInterpolatedFrame.mRotCenter.X, -mInterpolatedFrame.mRotCenter.Y, -mInterpolatedFrame.mRotCenter.Z)
        GLES10.glScalef(mInterpolatedFrame.mScale.X, mInterpolatedFrame.mScale.Y, mInterpolatedFrame.mScale.Z)

        GLES10.glPopMatrix()
    }


    inner class LRSAnimationFrame(var mTime: Float, var mTrans: Vector3, var mRotation: Quaternion, var mRotCenter: Vector3, val mScale: Vector3) {

        fun interpolate(start: LRSAnimationFrame, interpolated: LRSAnimationFrame?, time: Float) {
            val ratio = time / mTime
            val one_minus_ratio = 1.0f - ratio

            interpolated!!.mTrans.X = mTrans.X * one_minus_ratio + start.mTrans.X * ratio
            interpolated!!.mTrans.X = mTrans.Y * one_minus_ratio + start.mTrans.Y * ratio
            interpolated!!.mTrans.X = mTrans.Z * one_minus_ratio + start.mTrans.Z * ratio

            interpolated!!.mScale.X = mScale.X * one_minus_ratio + start.mScale.X * ratio
            interpolated!!.mScale.X = mScale.Y * one_minus_ratio + start.mScale.Y * ratio
            interpolated!!.mScale.X = mScale.Z * one_minus_ratio + start.mScale.Z * ratio

            interpolated!!.mRotCenter.X = mRotCenter.X * one_minus_ratio + start.mRotCenter.X * ratio
            interpolated!!.mRotCenter.X = mRotCenter.Y * one_minus_ratio + start.mRotCenter.Y * ratio
            interpolated!!.mRotCenter.X = mRotCenter.Z * one_minus_ratio + start.mRotCenter.Z * ratio

            interpolated!!.mRotation = start.mRotation.slerp(mRotation, ratio)


        }

    }
}