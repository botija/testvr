package com.botijasoftware.utils;

import android.content.Context;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ResourceLoader extends  Thread {

    private EGLContext textureContext;
    private EGL14 egl;
    private EGLConfig eglConfig;
    private EGLDisplay display;
    private Context androidContext;

    private final Semaphore semaphore = new Semaphore(1, true);
    private boolean isLoading = false;

    public class TextureInfo {
        int textureName;
        TextureOptions options;
    }

    private ArrayList<TextureInfo> texturesToLoad = new ArrayList<>();


    public ResourceLoader(EGL14 egl, EGLContext renderContext, EGLDisplay display,
                         EGLConfig eglConfig, Context androidContext) {
        this.egl = egl;
        this.display = display;
        this.eglConfig = eglConfig;
        this.androidContext = androidContext;

        textureContext = EGL14.eglCreateContext(display, eglConfig, renderContext, null, 0);
    }

    public void run() {
        int pbufferAttribs[] = { EGL14.EGL_WIDTH, 1, EGL14.EGL_HEIGHT, 1, EGL14.EGL_TEXTURE_TARGET,
                EGL14.EGL_NO_TEXTURE, EGL14.EGL_TEXTURE_FORMAT, EGL14.EGL_NO_TEXTURE,
                EGL14.EGL_NONE };

        EGLSurface localSurface = EGL14.eglCreatePbufferSurface(display, eglConfig, pbufferAttribs, 0);

        EGL14.eglMakeCurrent(display, localSurface, localSurface, textureContext);

        //sleep(); // delay loading texture to demonstrate threaded loading

    }

    public void loadTexture() {


        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        semaphore.release();
    }

    private void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
    }
}