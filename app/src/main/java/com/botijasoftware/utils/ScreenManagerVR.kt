package com.botijasoftware.utils

import java.util.ArrayList


import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.egl.EGLDisplay

import android.app.Activity
import android.content.SharedPreferences
import android.opengl.GLES20
import android.os.SystemClock
import android.view.KeyEvent
import android.view.MotionEvent

import com.botijasoftware.utils.Screens.Screen
import com.botijasoftware.utils.Screens.ScreenVR
import com.google.vr.sdk.base.*
import com.google.vr.sdk.base.Viewport
import javax.microedition.khronos.egl.EGL10


class ScreenManagerVR(protected var mActivity: Activity, resourceManager: ResourceManager, preferences: SharedPreferences) : GvrView(resourceManager.context), GvrView.StereoRenderer {

    protected var Screens: ArrayList<ScreenVR>
    protected var ScreensToUpdate: ArrayList<ScreenVR>
    protected var ScreensToBackup: ArrayList<ScreenVR>
    protected var ScreensOnFocus: ArrayList<ScreenVR>
    protected var mStartScreen: ScreenVR? = null
    var layout: Layout
        protected set
    var mGlobalOptions: GlobalOptions
    var mWidth: Int = 0
    var mHeight: Int = 0
    protected var isRunning = false
    var glNeedsReload = false
    var shouldExecuteOnResume = false
    protected var lastUpdateTime: Long = 0
    var fps = 0.0f
        protected set
    protected var oldTime: Long = 0
    protected var Time: Long = 0
    var mainScreen: Screen? = null
        protected set


    init {

        /*        setEGLContextFactory(new EGLContextFactory() {

            @Override
            public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context) {
                egl.eglDestroyContext(display, context);
            }

            @Override
            public EGLContext createContext(final EGL10 egl, final EGLDisplay display, final EGLConfig eglConfig) {

                EGLContext renderContext = EGL14.eglCreateContext(display, eglConfig, EGL14.EGL_NO_CONTEXT, null);

                mResourceLoader = new ResourceLoader(mResourceManager, (android.opengl.EGL14) egl, (android.opengl.EGLContext) renderContext, (android.opengl.EGLDisplay) display, eglConfig, getContext());

                return renderContext;
            }
        });*/

        setEGLContextClientVersion(2)
        //setRestoreGLStateEnabled(false);
        setTransitionViewEnabled(true)
        enableCardboardTriggerEmulation()
        if (setAsyncReprojectionEnabled(true)) {
            // Async reprojection decouples the app framerate from the display framerate,
            // allowing immersive interaction even at the throttled clockrates set by
            // sustained performance mode.
            AndroidCompat.setSustainedPerformanceMode(mActivity, true)
        }
        setRenderer(this)
        mResourceManager = resourceManager
        layout = Layout(0, 0)
        mGlobalOptions = GlobalOptions(preferences)

        mSoundSystem = SoundSystem(mResourceManager.audioManager, mResourceManager.soundPool)

        Screens = ArrayList<ScreenVR>()
        ScreensToUpdate = ArrayList<ScreenVR>()
        ScreensToBackup = ArrayList<ScreenVR>()
        ScreensOnFocus = ArrayList<ScreenVR>()

    }

    fun setStartScreen(startscreen: ScreenVR) {
        mStartScreen = startscreen
    }


    fun Run() {
        //addScreen( new LoadingScreen(this, new MainScreen( this ) ) );
        if (mStartScreen != null) {
            addScreen(mStartScreen!!)
            isRunning = true
        }
    }


    fun onSurfaceCreated(gl: EGL10, config: EGLConfig) {
        //GLES20.glHint(GLES20.GL_PERSPECTIVE_CORRECTION_HINT, GLES20.GL_FASTEST);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDisable(GLES20.GL_DITHER)
        GLES20.glEnable(GLES20.GL_CULL_FACE)
        //GLES20.glEnable(GLES20.GL_LIGHTING);
        //GLES20.glDisable(GLES20.GL_COLOR_MATERIAL);
        //GLES20.glEnable(GLES20.GL_MULTISAMPLE);
        //GLES20.glShadeModel(GLES20.GL_SMOOTH);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D)
        //GLES20.glDisable(GLES20.GL_BLEND);
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)

        GLES20.glClearColor(0.3921f, 0.5843f, 0.9294f, 1.0f) //CornFlowerBlue
        //GLES20.glClearColor( 0.0f, 0.0f, 0.0f, 1.0f ); // Black
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        lastUpdateTime = 0

        Time = SystemClock.uptimeMillis()
        oldTime = 0L

    }

    fun onSurfaceChanged(gl: EGL10, width: Int, height: Int) {
        setSize(width, height)
        GLES20.glViewport(0, 0, width, height)
        //GLES20.glMatrixMode(GLES20.GL_PROJECTION);
        //GLES20.glLoadIdentity();
        //GLES20.glOrthof(0, width, 0, height, 0, 1);
        //GLU.gluPerspective(gl, 75.0f, (float)width/(float)height, 1.0f, 500.0f);
        //GLES20.glMatrixMode(GLES20.GL_MODELVIEW);
        //GLES20.glLoadIdentity();


        //GLES20.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);

        val size = Screens.size
        for (i in 0..size - 1) {
            val scr = Screens[i]
            scr.onSurfaceChanged(width, height)
        }

        if (!isRunning) {
            Run()
        }

        lastUpdateTime = 0

    }

    fun onDrawFrame(gl: EGL10) {

        if (mActivity.isFinishing)
            return

        Time = SystemClock.uptimeMillis()
        if (oldTime == 0L)
            oldTime = Time
        Update((Time - oldTime) / 1000.0f) //Convert elapsed milliseconds to seconds

        if (hasFinished()) {
            //Thread.currentThread().destroy();
            mActivity.finish()
            return
            //this.finish();
        }

        oldTime = Time

        if (glNeedsReload) {
            Clear()
            mResourceManager.reloadAllTextures()
            mResourceManager.reloadAllShaders()

            val size = Screens.size
            for (i in 0..size - 1) {
                val scr = Screens[i]
                scr.reloadContent()
            }

            glNeedsReload = false
        }


        var ms = SystemClock.uptimeMillis()
        val t = lastUpdateTime + minFrameTime - ms

        if (t > 1) {
            try {

                Thread.sleep(t)
                //wait(lastUpdateTime + minFrameTime - ms);
            } catch (e: InterruptedException) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }

        }

        ms = SystemClock.uptimeMillis()
        val tframe = (ms - lastUpdateTime) / 1000.0f
        fps = 1.0f / tframe

        lastUpdateTime = ms
        Clear()

        if (mainScreen != null) {
            mainScreen!!.Draw()
        } else {
            val size = Screens.size - 1
            for (i in size downTo 0) {
                val scr = Screens[i]
                scr.Draw()
            }
        }

        GLES20.glFlush()
        GLES20.glFinish()
        //}
    }

    fun onStop() {

        val size = Screens.size
        for (i in 0..size - 1) {
            val scr = Screens[i]
            scr.onStop()
        }
    }

    override fun onPause() {

        super.onPause()
        val size = Screens.size
        for (i in 0..size - 1) {
            val scr = Screens[i]
            scr.onPause()
        }
        oldTime = 0L
    }

    fun onDestroy() {

        val size = Screens.size
        for (i in 0..size - 1) {
            val scr = Screens[i]
            scr.onDestroy()
        }

        mResourceManager.unloadAllContent()
        oldTime = 0L
    }

    override fun onResume() {

        if (shouldExecuteOnResume) {
            super.onResume()
            glNeedsReload = true
            val size = Screens.size
            for (i in 0..size - 1) {
                val scr = Screens[i]
                scr.onResume()
            }
            oldTime = 0L

        } else {
            shouldExecuteOnResume = true
        }
    }

    fun addScreenOnTop(newScreen: ScreenVR) {
        newScreen.LoadContent(mResourceManager)
        Screens.add(0, newScreen)
        //System.gc();
    }

    fun addScreen(newScreen: ScreenVR) {
        newScreen.LoadContent(mResourceManager)
        Screens.add(newScreen)
        //System.gc();
    }

    fun removeScreen(screen: ScreenVR) {
        screen.freeContent(mResourceManager)
        Screens.remove(screen)
        System.gc()
    }

    fun Count(): Int {
        return Screens.size
    }

    fun backupScreen(screen: ScreenVR): ScreenVR {

        ScreensToBackup.add(screen)
        return screen
    }

    fun recoverScreen(screen: ScreenVR?): ScreenVR? {

        if (screen != null && ScreensToBackup.contains(screen)) {
            ScreensToBackup.remove(screen)
            Screens.add(screen)
            return screen
        }
        return null
    }

    fun hasFinished(): Boolean {
        return Screens.isEmpty()
    }

    fun Update(time: Float) {

        if (mainScreen != null) {
            mainScreen!!.Update(time)
        } else {
            val size = Screens.size
            for (i in 0..size - 1) {
                ScreensToUpdate.add(Screens[i])
            }

            for (i in 0..size - 1) {
                val scr = ScreensToUpdate[i]
                scr.Update(time)
            }
            ScreensToUpdate.clear()

            checkFocus()
        }

    }

    fun Clear() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
    }

    fun setSize(width: Int, height: Int) {
        mWidth = width
        mHeight = height
        layout.setSize(width, height)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        val size = Screens.size
        for (i in 0..size - 1) {
            if (Screens[i].onKeyDown(keyCode, event))
                return true
        }
        return false
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {

        val size = Screens.size
        for (i in 0..size - 1) {
            if (Screens[i].onKeyUp(keyCode, event))
                return true
        }
        return false
    }

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {

        val size = Screens.size
        for (i in 0..size - 1) {
            if (Screens[i].onGenericMotionEvent(event))
                return true
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val size = Screens.size
        for (i in 0..size - 1) {
            if (Screens[i].onTouchEvent(event))
                return true
        }
        return false
    }


    fun onBackPressed(): Boolean {

        val size = Screens.size
        for (i in 0..size - 1) {
            if (Screens[i].onBackPressed())
                return true
        }
        return false
    }

    private fun addToFocusList(screen: ScreenVR) {
        if (!ScreensOnFocus.contains(screen)) {
            ScreensOnFocus.add(screen)
            screen.onGetFocus()
        }
    }

    private fun removeFromFocusList(screen: ScreenVR) {
        if (ScreensOnFocus.contains(screen)) {
            ScreensOnFocus.remove(screen)
            screen.onFocusLost()
        }
    }

    private fun checkFocus() {
        val size = Screens.size
        var focus = true
        var scr: ScreenVR
        for (i in 0..size - 1) {
            scr = Screens[i]
            //scr.setFocus(focus);
            if (focus) {
                addToFocusList(scr)
                focus = false
            } else {
                removeFromFocusList(scr)
            }

        }
    }

    fun makeMainScreen(scr: Screen) {
        this.mainScreen = scr
    }

    fun clearMainScreen() {
        mainScreen = null
    }

    override fun onNewFrame(headTransform: HeadTransform) {

        if (mActivity.isFinishing)
            return

        Time = SystemClock.uptimeMillis()
        if (oldTime == 0L)
            oldTime = Time
        Update((Time - oldTime) / 1000.0f) //Convert elapsed milliseconds to seconds

        if (hasFinished()) {
            //Thread.currentThread().destroy();
            mActivity.finish()
            return
            //this.finish();
        }

        oldTime = Time

        if (glNeedsReload) {
            Clear()

            mResourceManager.reloadAllTextures()
            mResourceManager.reloadAllShaders()

            val size = Screens.size
            for (i in 0..size - 1) {
                val scr = Screens[i]
                scr.reloadContent()
            }

            glNeedsReload = false
        }


        var ms = SystemClock.uptimeMillis()
        val t = lastUpdateTime + minFrameTime - ms

        if (t > 1) {
            try {

                Thread.sleep(t)
                //wait(lastUpdateTime + minFrameTime - ms);
            } catch (e: InterruptedException) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }

        }

        ms = SystemClock.uptimeMillis()
        val tframe = (ms - lastUpdateTime) / 1000.0f
        fps = 1.0f / tframe

        lastUpdateTime = ms


        val size = Screens.size
        for (i in 0..size - 1) {
            val scr = Screens[i]
            scr.onNewFrame(headTransform)
        }
    }

    override fun onDrawEye(eye: Eye) {
        val size = Screens.size
        for (i in 0..size - 1) {
            val scr = Screens[i]
            scr.onDrawEye(eye)
        }
    }

    override fun onFinishFrame(viewport: Viewport) {
        val size = Screens.size
        for (i in 0..size - 1) {
            val scr = Screens[i]
            scr.onFinishFrame(viewport)
        }
    }


    override fun onSurfaceChanged(width: Int, height: Int) {
        setSize(width, height)
        GLES20.glViewport(0, 0, width, height)

        val size = Screens.size
        for (i in 0..size - 1) {
            val scr = Screens[i]
            scr.onSurfaceChanged(width, height)
        }
    }


    override fun onSurfaceCreated(eglConfig: EGLConfig) {

        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDisable(GLES20.GL_DITHER)
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        GLES20.glEnable(GLES20.GL_TEXTURE_2D)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)

        GLES20.glClearColor(0.3921f, 0.5843f, 0.9294f, 1.0f) //CornFlowerBlue

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        lastUpdateTime = 0

        Time = SystemClock.uptimeMillis()
        oldTime = 0L

        val size = Screens.size
        for (i in 0..size - 1) {
            val scr = Screens[i]
            scr.onSurfaceCreated(eglConfig)
        }
    }

    override fun onRendererShutdown() {
        val size = Screens.size
        for (i in 0..size - 1) {
            val scr = Screens[i]
            scr.onRendererShutdown()
        }
    }

    /**
     * Called when the Cardboard trigger is pulled.
     */

    fun onCardboardTrigger() {
        val size = Screens.size
        for (i in 0..size - 1) {
            Screens[i].onCardboardTrigger()
        }
    }

    companion object {
        lateinit var mResourceManager: ResourceManager
        lateinit var mSoundSystem: SoundSystem
        protected val targetFPS = 60
        protected val minFrameTime = (1000 / targetFPS).toLong()
    }
}
