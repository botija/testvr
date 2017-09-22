package com.botijasoftware.utils

import android.content.Context
import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.EGLContext
import android.opengl.EGLDisplay
import android.opengl.EGLSurface
import android.view.SurfaceView

import java.util.ArrayList
import java.util.concurrent.Semaphore

import javax.microedition.khronos.egl.EGL

class ResourceLoader(private val resourcemanager: ResourceManager, private val egl: EGL, renderContext: EGLContext, private val display: EGLDisplay,
                     private val eglConfig: EGLConfig, private val androidContext: Context) : Thread() {

    private val textureContext: EGLContext

    private val semaphore = Semaphore(1, true)
    private val isLoading = false
    private var running = true

    inner class TextureInfo(internal var textureName: Int, internal var options: TextureOptions)

    inner class ModelInfo(internal var modelName: Int)

    private val texturesToLoad = ArrayList<TextureInfo>()
    private val modelsToLoad = ArrayList<ModelInfo>()


    init {

        textureContext = EGL14.eglCreateContext(display, eglConfig, renderContext, null, 0)
    }

    override fun run() {
        val pbufferAttribs = intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_WIDTH, 1, EGL14.EGL_HEIGHT, 1, EGL14.EGL_TEXTURE_TARGET, EGL14.EGL_NO_TEXTURE, EGL14.EGL_TEXTURE_FORMAT, EGL14.EGL_NO_TEXTURE, EGL14.EGL_NONE)

        /*int iConfigs;
        EGLConfig eglConfig;
        EGL14.eglChooseConfig( display, pbufferAttribs, eglConfig, 1, iConfigs);*/
        val localSurface = EGL14.eglCreatePbufferSurface(display, eglConfig, pbufferAttribs, 0)

        EGL14.eglMakeCurrent(display, localSurface, localSurface, textureContext)

        while (running) {

            try {
                semaphore.acquire()

                while (texturesToLoad.size > 0) {
                    val ti = texturesToLoad.removeAt(0)
                    resourcemanager.loadTexture(ti.textureName, ti.options)
                }

                while (modelsToLoad.size > 0) {
                    val mi = modelsToLoad.removeAt(0)
                    resourcemanager.loadModel(mi.modelName)
                }

                semaphore.release()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            sleep() // delay loading texture to demonstrate threaded loading
        }

    }

    fun loadTexture(textureName: Int, options: TextureOptions) {

        try {
            semaphore.acquire()
            texturesToLoad.add(TextureInfo(textureName, options))
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        semaphore.release()
    }

    fun loadModel(modelName: Int) {

        try {
            semaphore.acquire()
            modelsToLoad.add(ModelInfo(modelName))
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        semaphore.release()
    }

    fun finishedLoadTextures(): Boolean {

        var result = false
        try {
            semaphore.acquire()
            result = texturesToLoad.isEmpty()

        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        semaphore.release()
        return result
    }

    fun finishedLoadModels(): Boolean {

        var result = false
        try {
            semaphore.acquire()
            result = modelsToLoad.isEmpty()

        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        semaphore.release()
        return result
    }

    fun finishedLoading(): Boolean {

        var result = false
        try {
            semaphore.acquire()
            result = texturesToLoad.isEmpty() && modelsToLoad.isEmpty()

        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        semaphore.release()
        return result
    }

    fun EndThread() {
        running = false
    }

    private fun sleep() {
        try {
            Thread.sleep(500)
        } catch (e: InterruptedException) {
        }

    }
}