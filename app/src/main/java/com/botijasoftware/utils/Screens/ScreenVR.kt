package com.botijasoftware.utils.Screens

import javax.microedition.khronos.egl.EGLConfig

import android.opengl.GLES20
import android.view.KeyEvent
import android.view.MotionEvent
import com.botijasoftware.utils.ResourceManager
import com.botijasoftware.utils.ScreenManagerVR
import com.botijasoftware.utils.Timer
import com.google.vr.sdk.base.Eye
import com.google.vr.sdk.base.HeadTransform
import com.google.vr.sdk.base.Viewport


open class ScreenVR(protected var mScreenManager: ScreenManagerVR) {

    enum class ScreenState {
        FADINGOUT, FADINGIN, ACTIVE, HIDDEN, PARTHIDDEN, ENDED
    }

    var isVisible: Boolean = false
        protected set
    private var fade: Float = 0.toFloat()
    private var fadetime: Float = 0.toFloat()
    var state: ScreenState = ScreenState.ENDED
    private var elapsedTime: Float = 0.toFloat()
    protected var width: Int = 0
    protected var height: Int = 0
    protected var focus = false
    protected var timer: Timer

    init {
        width = mScreenManager.mWidth
        height = mScreenManager.mHeight
        elapsedTime = 0.0f
        isVisible = true
        timer = Timer()
        FadeIn(0.3f)
    }

    open fun Update(time: Float) {
        elapsedTime += time
        fadetime -= time

        if (state == ScreenState.ACTIVE) {
            timer.update(time)
        }

        if (state == ScreenState.FADINGIN && fadetime <= 0.0f) {
            state = ScreenState.ACTIVE
        } else if (state == ScreenState.FADINGOUT && fadetime <= 0.0f) {
            state = ScreenState.ENDED
        }
    }

    fun onSurfaceChanged(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    fun onStop() {

    }

    fun onPause() {

    }

    fun onResume() {

    }

    fun onDestroy() {
        state = ScreenState.ENDED
    }

    fun Draw() {

    }

    open fun LoadContent(resources: ResourceManager) {

    }

    fun reloadContent() {

    }

    fun freeContent(resources: ResourceManager) {

    }

    fun Show() {
        isVisible = true
    }

    fun Hide() {
        isVisible = false
    }

    fun FadeIn(time: Float) {
        state = ScreenState.FADINGIN
        fade = time
        fadetime = time
        if (fade <= 0.0000001f) {
            fade = 0.0000001f
        }
    }

    fun FadeOut(time: Float) {
        state = ScreenState.FADINGOUT
        fade = time
        fadetime = time
        if (fade <= 0.0000001f) {
            fade = 0.0000001f
        }
    }


    val alphaFade: Float
        get() {

            var alpha = 1.0f

            if (state == ScreenState.FADINGIN) {
                alpha = 1.0f - fadetime / fade
            } else if (state == ScreenState.FADINGOUT) {
                alpha = fadetime / fade
            }

            return alpha
        }

    fun Clear() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
    }

    fun reset() {
        state = ScreenState.ACTIVE
    }

    open fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        return false
    }

    open fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {

        return false
    }

    open fun onTouchEvent(event: MotionEvent): Boolean {

        return false
    }

    open fun onGenericMotionEvent(event: MotionEvent): Boolean {
        return false
    }


    fun onBackPressed(): Boolean {
        state = ScreenState.ENDED
        return true
    }


    fun onGetFocus(): Boolean {
        focus = true
        return true
    }

    fun onFocusLost(): Boolean {
        focus = false
        return true
    }

    fun hasFocus(): Boolean {
        return focus
    }


    /*fun setFocus(focus: Boolean) {
        this.focus = focus
    }*/


    open fun onNewFrame(headTransform: HeadTransform) {

    }

    open fun onDrawEye(eye: Eye) {

    }

    fun onFinishFrame(viewport: Viewport) {

    }


    fun onSurfaceCreated(eglConfig: EGLConfig) {

    }

    fun onRendererShutdown() {

    }

    open fun onCardboardTrigger() {

    }


}
