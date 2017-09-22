package com.botijasoftware.utils

import java.util.ArrayList

import com.botijasoftware.utils.renderer.Renderer

class OrthoCamera(private val mWidth: Int, private val mHeight: Int, private val mViewRect: Rectangle) {

    var mZoom = 1.0f
    private var mFinalZoom = 1.0f
    private var mZoomSpeed = 0.0f
    private var mZoomTime = 0.0f
    private var mZoomElapsedTime = 0.0f
    var mLookAt: Vector2
    var mSpeed: Vector2
    private val mFinalLookAt: Vector2
    private var mLookAtTime = 0.0f
    private var mLookAtElapsedTime = 0.0f
    private val mHalfWidth: Int
    private val mHalfHeight: Int
    private var mTarget: Vector2? = null
    private var mPath: ArrayList<Vector2>? = null
    private var mStartPath: Vector2? = null
    private var mFollowingPath = false
    private var mPathIndex = 0

    init {
        mHalfWidth = mWidth / 2
        mHalfHeight = mHeight / 2
        mLookAt = Vector2((mViewRect.X + mHalfWidth).toFloat(), (mViewRect.Y + mHalfHeight).toFloat())
        mSpeed = Vector2(0.0f, 0.0f)
        mFinalLookAt = Vector2((mViewRect.X + mHalfWidth).toFloat(), (mViewRect.Y + mHalfHeight).toFloat())
        mZoom = 1.0f
        mFinalZoom = 1.0f
        mZoomSpeed = 0.0f
        mZoomTime = 0.0f
        mZoomElapsedTime = 0.0f
        mLookAtTime = 0.0f
        mLookAtElapsedTime = 0.0f
        mTarget = null
        mPath = null
        mStartPath = Vector2(0.0f, 0.0f)
        mFollowingPath = false
    }

    fun reset() {
        mLookAt.setValue((mViewRect.X + mHalfWidth).toFloat(), (mViewRect.Y + mHalfHeight).toFloat())
        mSpeed.setValue(0.0f)
        mFinalLookAt.setValue((mViewRect.X + mHalfWidth).toFloat(), (mViewRect.Y + mHalfHeight).toFloat())
        mZoom = 1.0f
        mFinalZoom = 1.0f
        mZoomSpeed = 0.0f
        mZoomTime = 0.0f
        mZoomElapsedTime = 0.0f
        mLookAtTime = 0.0f
        mLookAtElapsedTime = 0.0f
        mTarget = null
    }

    fun Set() {
        //GLES10.glPushMatrix();
        //GLES10.glLoadIdentity();
        //GLES10.glScalef(mZoom, mZoom, 0.0f);
        //GLES10.glTranslatef(-mViewRect.X-mLookAt.X+mHalfWidth, -mViewRect.Y-mLookAt.Y+mHalfHeight, 0.0f);
        //GLES10.glPopMatrix();
        Renderer.pushModelViewMatrix()
        Renderer.modelview.loadIdentity()
        Renderer.modelview.scale(mZoom, mZoom, 0.0f)
        Renderer.modelview.translate(-mViewRect.X - mLookAt.X + mHalfWidth, -mViewRect.Y - mLookAt.Y + mHalfHeight, 0.0f)
    }

    fun UnSet() {

        Renderer.popModelViewMatrix()
        //GLES10.glPopMatrix();
    }

    fun setZoom(zoom: Float) {
        mZoom = zoom
        mFinalZoom = zoom
        mZoomSpeed = 0.0f
    }

    fun setZoom(zoom: Float, time: Float) {
        mFinalZoom = zoom
        if (time > 0.0f) {
            mZoomTime = time
            mZoomElapsedTime = 0.0f
            mZoomSpeed = (mFinalZoom - mZoom) / time
        } else
            setZoom(zoom)

    }

    fun lookAt(x: Float, y: Float) {

        if (x < mViewRect.X + mHalfWidth) {
            mFinalLookAt.X = (mViewRect.X + mHalfWidth).toFloat()
        } else
            mFinalLookAt.X = x

        if (x > mViewRect.X + mViewRect.width - mHalfWidth)
            mFinalLookAt.X = (mViewRect.X + mViewRect.width - mHalfWidth).toFloat()


        if (y <= mViewRect.Y + mHalfHeight) {
            mFinalLookAt.Y = (mViewRect.Y + mHalfHeight).toFloat()
        } else
            mFinalLookAt.Y = y

        if (y > mViewRect.Y + mViewRect.height - mHalfHeight)
            mFinalLookAt.Y = (mViewRect.Y + mViewRect.height - mHalfHeight).toFloat()

        mSpeed.X = 0.0f
        mSpeed.Y = 0.0f
        mLookAt.X = mFinalLookAt.X
        mLookAt.Y = mFinalLookAt.Y
    }

    fun lookAt(p: Vector2) {
        lookAt(p.X, p.Y)
    }

    fun lookAt(x: Float, y: Float, time: Float) {
        if (time != 0.0f) {
            mLookAtTime = time
            mLookAtElapsedTime = 0.0f

            when {
                x < mViewRect.X + mHalfWidth -> mFinalLookAt.X = (mViewRect.X + mHalfWidth).toFloat()
                x > mViewRect.X + mViewRect.width - mHalfWidth -> mFinalLookAt.X = (mViewRect.X + mViewRect.width - mHalfWidth).toFloat()
                else -> mFinalLookAt.X = x
            }


            if (y < mViewRect.Y + mHalfHeight)
                mFinalLookAt.Y = (mViewRect.Y + mHalfHeight).toFloat()
            else if (y > mViewRect.Y + mViewRect.height - mHalfHeight)
                mFinalLookAt.Y = (mViewRect.Y + mViewRect.height - mHalfHeight).toFloat()
            else
                mFinalLookAt.Y = y

            mSpeed.X = (mFinalLookAt.X - mLookAt.X) / time
            mSpeed.Y = (mFinalLookAt.Y - mLookAt.Y) / time
        } else
            lookAt(x, y)
    }

    fun lookAt(p: Vector2, time: Float) {
        lookAt(p.X, p.Y, time)
    }

    fun move(x: Float, y: Float, time: Float) {
        lookAt(mLookAt.X + x, mLookAt.Y + y, time)
    }

    fun move(x: Float, y: Float) {
        lookAt(mLookAt.X + x, mLookAt.Y + y)
    }

    fun move(p: Vector2, time: Float) {
        lookAt(mLookAt.X + p.X, mLookAt.Y + p.Y, time)
    }

    fun move(p: Vector2) {
        lookAt(mLookAt.X + p.X, mLookAt.Y + p.Y)
    }

    fun followTarget(target: Vector2) {
        mTarget = target //store reference to vector, its updated automatically (by its object)
    }

    fun freeTarget() {
        mTarget = null
    }

    fun fromScreen(position: Vector2): Vector2 {
        return Vector2(mLookAt.X + position.X - mHalfWidth, mLookAt.Y + position.Y - mHalfHeight)
    }

    fun fromScreen(x: Float, y: Float): Vector2 {
        return Vector2(mLookAt.X + x - mHalfWidth, mLookAt.Y + y - mHalfHeight)
    }

    fun Update(time: Float) {

        if (mFollowingPath) {

            var ep = mPath!![mPathIndex]

            //prevent camera from surpassing its end destination
            if (mSpeed.X > 0.0f && mLookAt.X > ep.X || mSpeed.X < 0.0f && mLookAt.X < ep.X)
                mLookAt.X = ep.X

            if (mSpeed.Y > 0.0f && mLookAt.Y > ep.Y || mSpeed.Y < 0.0f && mLookAt.Y < ep.Y)
                mLookAt.Y = ep.Y

            if (mLookAt.equals(mFinalLookAt)) {
                mPathIndex++

                if (mPathIndex >= mPath!!.size) {
                    mFollowingPath = false
                } else {
                    ep = mPath!![mPathIndex].add(mStartPath!!)
                    val t = ep.distance(mLookAt)
                    lookAt(ep, t / 100.0f) // 100px/s
                }
            }
        }

        if (mTarget != null) {
            lookAt(mTarget!!)
            mZoomTime = 0.0f
            mLookAtTime = 0.0f
            return
        }

        if (mZoomElapsedTime < mZoomTime) {
            mZoomElapsedTime += time
            mZoom += mZoomSpeed * time
        } else {
            mZoom = mFinalZoom
            mZoomTime = 0.0f
        }

        if (mLookAtElapsedTime < mLookAtTime) {
            mLookAt.X += mSpeed.X * time
            mLookAt.Y += mSpeed.Y * time
            mLookAtElapsedTime += time

            //prevent camera from surpassing its end destination
            if (mSpeed.X > 0.0f && mLookAt.X > mFinalLookAt.X || mSpeed.X < 0.0f && mLookAt.X < mFinalLookAt.X)
                mLookAt.X = mFinalLookAt.X

            if (mSpeed.Y > 0.0f && mLookAt.Y > mFinalLookAt.Y || mSpeed.Y < 0.0f && mLookAt.Y < mFinalLookAt.Y)
                mLookAt.Y = mFinalLookAt.Y

        } else {
            mLookAt.X = mFinalLookAt.X
            mLookAt.Y = mFinalLookAt.Y
            mLookAtTime = 0.0f
        }

    }

    fun followPath(startPoint: Vector2, path: ArrayList<Vector2>) {
        mStartPath = startPoint
        mPath = path
        mFollowingPath = true
        mPathIndex = 0
        lookAt(mStartPath!!.X, mStartPath!!.Y)
    }

    fun hasFinishedPath(): Boolean {
        return mFollowingPath
    }

}