package com.botijasoftware.utils.Screens;

import javax.microedition.khronos.egl.EGLConfig;

import android.opengl.GLES20;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.botijasoftware.utils.ResourceManager;
import com.botijasoftware.utils.ScreenManagerVR;
import com.botijasoftware.utils.Timer;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;

public class ScreenVR {

	public enum ScreenState { FADINGOUT, FADINGIN, ACTIVE, HIDDEN, PARTHIDDEN, ENDED }

    protected boolean visible;
    private float fade;
    private float fadetime;
    public ScreenState state;
    private float elapsedTime;
    protected ScreenManagerVR mScreenManager;
    protected int width;
    protected int height;
    protected boolean focus = false;
    protected Timer timer;

	public ScreenVR(ScreenManagerVR screenManager) {
		mScreenManager = screenManager;
		width = mScreenManager.mWidth;
		height = mScreenManager.mHeight;
		elapsedTime = 0.0f;
		visible = true;
		timer = new Timer();
		FadeIn(0.3f);
	}

	public void Update(float time) {
		elapsedTime += time;
		fadetime -= time;

		if (state == ScreenState.ACTIVE) {
			timer.update(time);
		}

		if ((state == ScreenState.FADINGIN) && (fadetime <= 0.0f)) {
			state = ScreenState.ACTIVE;
		}
		else if  ((state == ScreenState.FADINGOUT) && (fadetime <= 0.0f)) {
			state = ScreenState.ENDED;
		}
	}

	public void onSurfaceChanged(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void onStop() {
	
	}
	
	public void onPause() {
		
	}
	
	public void onResume() {
		
	}
	
	public void onDestroy() {
		state = ScreenState.ENDED;
	}
	
	public void Draw() {
		
	}

	public void LoadContent(ResourceManager resources) {
	
	}

	public void reloadContent() {
		
	}

    public void freeContent(ResourceManager resources) {

    }
	
	public void Show() {
		visible = true;
	}

	public void Hide() {
		visible = false;
	}

	public void FadeIn(float time) {
		state = ScreenState.FADINGIN;
		fade = time;
		fadetime = time;
		if (fade <= 0.0000001f) {
			fade =  0.0000001f;
		}
	}

	public void FadeOut(float time) {
		state = ScreenState.FADINGOUT;
		fade = time;
		fadetime = time;
		if (fade <= 0.0000001f) {
			fade =  0.0000001f;
		}
	}


	public float getAlphaFade() {
		
		float alpha = 1.0f;
		
		if (state == ScreenState.FADINGIN) {
			alpha = 1.0f - (fadetime / fade);
		}
		else if (state == ScreenState.FADINGOUT) {
			alpha = (fadetime / fade);
		}
		
		return alpha;
	}
	
	public void Clear() {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
	}

	public boolean isVisible() {
		return visible;
	}
	
	public void reset() {
		state = ScreenState.ACTIVE;
	}
	
    public boolean onKeyDown(int keyCode, KeyEvent event) {

		return false;
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {

		return false;
	}
	
	 public boolean onTouchEvent(final MotionEvent event) {

		return false;
	}

    public boolean onGenericMotionEvent(MotionEvent event) {
        return false;
    }
	 
	 
	public boolean onBackPressed() {
        state = ScreenState.ENDED;
        return true;
	}


    public boolean onGetFocus() {
        setFocus(true);
        return true;
    }

    public boolean onFocusLost() {
        setFocus(false);
        return true;
    }

    public boolean hasFocus() {
        return focus;
    }


    public void setFocus(boolean focus) {
        this.focus = focus;
    }


	public void onNewFrame(HeadTransform headTransform) {

	}

	public void onDrawEye(Eye eye) {

	}

	public void onFinishFrame(Viewport viewport) {

	}


	public void onSurfaceCreated(EGLConfig eglConfig) {

	}

	public void onRendererShutdown() {

	}

    public void onCardboardTrigger() {

    }



}
