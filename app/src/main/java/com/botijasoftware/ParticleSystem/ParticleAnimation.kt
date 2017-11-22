package com.botijasoftware.ParticleSystem


class ParticleAnimation @JvmOverloads constructor(protected var mFrames: Int = 0, protected var mStartFrame: Int = 0, protected var mFrameTime: Float = 0.0f, protected var mLoop: Boolean = false) {

    protected var mElapsedTime: Float = 0.toFloat()
    protected var mThisFrameTime: Float = 0.toFloat()
    var currentFrame: Int = 0
        protected set

    init {
        mElapsedTime = 0.0f
        mThisFrameTime = 0.0f
        currentFrame = mStartFrame
    }

    fun reset() {
        mElapsedTime = 0.0f
        mThisFrameTime = 0.0f
        currentFrame = mStartFrame
    }

    fun reset(frames: Int, startframe: Int, frametime: Float, loop: Boolean) {
        mFrames = frames
        mStartFrame = startframe
        mLoop = loop
        mElapsedTime = 0.0f
        mFrameTime = frametime
        mThisFrameTime = 0.0f
        currentFrame = mStartFrame
    }

    fun Update(time: Float) {
        mElapsedTime += time
        mThisFrameTime += time
        if (mThisFrameTime >= mFrameTime) {
            mThisFrameTime -= mFrameTime
            if (currentFrame < mFrames - 1) {
                currentFrame++
            } else if (mLoop) {
                currentFrame = mStartFrame
            }
        }
    }
}
