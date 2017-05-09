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

import javax.microedition.khronos.egl.EGL;

public class ResourceLoader extends  Thread {

    private EGLContext textureContext;
    private EGL egl;
    private EGLConfig eglConfig;
    private EGLDisplay display;
    private Context androidContext;
    private ResourceManager resourcemanager;

    private final Semaphore semaphore = new Semaphore(1, true);
    private boolean isLoading = false;
    private boolean running = true;

    public class TextureInfo {
        int textureName;
        TextureOptions options;

        public TextureInfo(int textureName, TextureOptions options) {
            this.textureName = textureName;
            this.options = options;
        }
    }

    public class ModelInfo {
        int  modelName;

        public ModelInfo(int modelName) {
            this.modelName = modelName;
        }
    }

    private ArrayList<TextureInfo> texturesToLoad = new ArrayList<>();
    private ArrayList<ModelInfo> modelsToLoad = new ArrayList<>();


    public ResourceLoader(ResourceManager resources, EGL egl, EGLContext renderContext, EGLDisplay display,
                          EGLConfig eglConfig, Context androidContext) {
        this.resourcemanager = resources;
        this.egl = egl;
        this.display = display;
        this.eglConfig = eglConfig;
        this.androidContext = androidContext;

        textureContext = EGL14.eglCreateContext(display, eglConfig, renderContext, null, 0);
    }

    public void run() {
        int pbufferAttribs[] = { EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_WIDTH, 1, EGL14.EGL_HEIGHT, 1, EGL14.EGL_TEXTURE_TARGET,
                EGL14.EGL_NO_TEXTURE, EGL14.EGL_TEXTURE_FORMAT, EGL14.EGL_NO_TEXTURE,
                EGL14.EGL_NONE };

        /*int iConfigs;
        EGLConfig eglConfig;
        EGL14.eglChooseConfig( display, pbufferAttribs, eglConfig, 1, iConfigs);*/
        EGLSurface localSurface = EGL14.eglCreatePbufferSurface(display, eglConfig, pbufferAttribs, 0);

        EGL14.eglMakeCurrent(display, localSurface, localSurface, textureContext);

        while (running) {

            try {
                semaphore.acquire();

                while (texturesToLoad.size() > 0) {
                    TextureInfo ti = texturesToLoad.remove(0);
                    resourcemanager.loadTexture(ti.textureName, ti.options);
                }

                while (modelsToLoad.size() > 0) {
                    ModelInfo mi = modelsToLoad.remove(0);
                    resourcemanager.loadModel(mi.modelName);
                }

                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            sleep(); // delay loading texture to demonstrate threaded loading
        }

    }

    public void loadTexture(int textureName, TextureOptions options) {

        try {
            semaphore.acquire();
            texturesToLoad.add(new TextureInfo(textureName, options));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        semaphore.release();
    }

    public void loadModel(int modelName) {

        try {
            semaphore.acquire();
            modelsToLoad.add(new ModelInfo(modelName));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        semaphore.release();
    }

    public boolean finishedLoadTextures() {

        boolean result = false;
        try {
            semaphore.acquire();
            result = texturesToLoad.isEmpty();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        semaphore.release();
        return result;
    }

    public boolean finishedLoadModels() {

        boolean result = false;
        try {
            semaphore.acquire();
            result = modelsToLoad.isEmpty();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        semaphore.release();
        return result;
    }

    public boolean finishedLoading() {

        boolean result = false;
        try {
            semaphore.acquire();
            result = texturesToLoad.isEmpty() && modelsToLoad.isEmpty();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        semaphore.release();
        return result;
    }

    public void EndThread() {
        running = false;
    }

    private void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
    }
}