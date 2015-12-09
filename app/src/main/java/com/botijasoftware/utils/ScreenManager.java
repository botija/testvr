package com.botijasoftware.utils;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.SharedPreferences;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.botijasoftware.utils.Screens.Screen;
import com.botijasoftware.utils.Layout;
import com.botijasoftware.utils.ResourceManager;
import com.botijasoftware.utils.SoundSystem;


public class ScreenManager extends GLSurfaceView implements GLSurfaceView.Renderer {

	protected ArrayList<Screen> Screens;
	protected ArrayList<Screen> ScreensToUpdate;
	protected ArrayList<Screen> ScreensToBackup;
    protected ArrayList<Screen> ScreensOnFocus;
	protected Screen mStartScreen = null;
	public static ResourceManager mResourceManager;
	public static SoundSystem mSoundSystem;
	protected Layout mLayout;
	public GlobalOptions mGlobalOptions;
	protected GL10 glContext = null;
	public int mWidth;
	public int mHeight;
	protected boolean isRunning = false;
	public boolean glNeedsReload = false;
    public boolean shouldExecuteOnResume  = false;
	protected final static int targetFPS = 60;
	protected final static long minFrameTime = 1000 / targetFPS;
	protected long lastUpdateTime;
	protected float mFPS = 0.0f;
	protected long oldTime;
	protected long Time;
	protected Activity mActivity;
    protected Screen main = null;

	public ScreenManager( Activity activity, ResourceManager resourceManager, SharedPreferences preferences) {
		super(resourceManager.getContext());

        setEGLContextClientVersion(2);
		setRenderer(this);

		mActivity = activity;
		mResourceManager = resourceManager;
		mLayout = new Layout(0,0);
		mGlobalOptions = new GlobalOptions(preferences);
		
		mSoundSystem = new SoundSystem( mResourceManager.getAudioManager(), mResourceManager.getSoundPool() );
		
		Screens = new ArrayList<Screen>();
		ScreensToUpdate = new ArrayList<Screen>();
		ScreensToBackup = new ArrayList<Screen>();
        ScreensOnFocus = new ArrayList<Screen>();
			
	}

	public void setStartScreen(Screen startscreen) {
		mStartScreen = startscreen;
	}
	

	public void Run() {
		//addScreen( new LoadingScreen(this, new MainScreen( this ) ) );
		if (mStartScreen != null) {
			addScreen(mStartScreen);
			isRunning = true;
		}
	}
	

	public void onSurfaceCreated(GL10 gl, EGLConfig config) 
	{
		glContext = gl;
		//GLES20.glHint(GLES20.GL_PERSPECTIVE_CORRECTION_HINT, GLES20.GL_FASTEST);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDisable(GLES20.GL_DITHER);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		//GLES20.glEnable(GLES20.GL_LIGHTING);
		//GLES20.glDisable(GLES20.GL_COLOR_MATERIAL);
		//GLES20.glEnable(GLES20.GL_MULTISAMPLE);
		//GLES20.glShadeModel(GLES20.GL_SMOOTH);
		GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		//GLES20.glDisable(GLES20.GL_BLEND);
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		GLES20.glClearColor( 0.3921f, 0.5843f, 0.9294f, 1.0f ); //CornFlowerBlue
		//GLES20.glClearColor( 0.0f, 0.0f, 0.0f, 1.0f ); // Black
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
		lastUpdateTime = 0;
		
		Time = SystemClock.uptimeMillis();
		oldTime = 0L; 
		
	}

	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		glContext = gl;
		setSize(width, height);
		GLES20.glViewport(0, 0, width, height);
		//GLES20.glMatrixMode(GLES20.GL_PROJECTION);
		//GLES20.glLoadIdentity();
		//GLES20.glOrthof(0, width, 0, height, 0, 1);
		//GLU.gluPerspective(gl, 75.0f, (float)width/(float)height, 1.0f, 500.0f);
		//GLES20.glMatrixMode(GLES20.GL_MODELVIEW);
		//GLES20.glLoadIdentity();
		

		//GLES20.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
		
		int size = Screens.size();
		for (int i = 0; i < size; i++) {
			Screen scr = Screens.get(i);
			scr.onSurfaceChanged(gl, width, height);
		}
		
		if (!isRunning) {
			Run();
		}
		
		lastUpdateTime = 0;

	}

	public void onDrawFrame(GL10 gl) 
	{ 

        if (mActivity.isFinishing())
            return;

		Time = SystemClock.uptimeMillis();
		if (oldTime == 0L)
			oldTime = Time;
		Update((Time - oldTime) / 1000.0f); //Convert elapsed milliseconds to seconds
		
		if (hasFinished()) {
			//Thread.currentThread().destroy();
			mActivity.finish();
			return;
			//this.finish();	
		}
				
		oldTime = Time;
		
		if (glNeedsReload) {
			Clear(gl);
			glContext = gl;
			mResourceManager.reloadAllTextures(glContext);
            mResourceManager.reloadAllShaders(gl);
			
			int size = Screens.size();
			for (int i = 0; i < size; i++) {
				Screen scr = Screens.get(i);
				scr.reloadContent(gl);
			}
			
			glNeedsReload = false;
		}
		
		
		long ms = SystemClock.uptimeMillis();
		long t = lastUpdateTime + minFrameTime - ms;
		
		if (t > 1) {
			try {

				Thread.sleep(t);
				//wait(lastUpdateTime + minFrameTime - ms);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		
			ms = SystemClock.uptimeMillis();
			float tframe = (ms - lastUpdateTime)/1000.0f;
			mFPS = 1.0f/tframe;
			
			lastUpdateTime = ms;
			Clear(gl);

            if (main != null) {
                main.Draw(gl);
            }
            else {
                int size = Screens.size() - 1;
                for (int i = size; i >= 0; i--) {
                    Screen scr = Screens.get(i);
                    scr.Draw(gl);
                }
            }
		
			GLES20.glFlush();
			GLES20.glFinish();
		//}
	}

	public void onStop() {
	
		int size = Screens.size();
		for (int i = 0; i < size; i++) {
			Screen scr = Screens.get(i);
			scr.onStop();
		}
	}
	
	public void onPause() {

		super.onPause();
		int size = Screens.size();
		for (int i = 0; i < size; i++) {
			Screen scr = Screens.get(i);
			scr.onPause();
		}
		oldTime = 0L;
	}
	
	public void onDestroy() {

		int size = Screens.size();
		for (int i = 0; i < size; i++) {
			Screen scr = Screens.get(i);
			scr.onDestroy();
		}

		mResourceManager.unloadAllContent(glContext);
		oldTime = 0L;
	}
	
	public void onResume() {

        if(shouldExecuteOnResume){
            super.onResume();
            glNeedsReload = true;
            int size = Screens.size();
            for (int i = 0; i < size; i++) {
                Screen scr = Screens.get(i);
                scr.onResume();
            }
            oldTime = 0L;

        } else{
            shouldExecuteOnResume = true;
        }
	}
	
	public void addScreenOnTop(Screen newScreen) {
		newScreen.LoadContent(glContext, mResourceManager);
		Screens.add(0,newScreen);
		//System.gc();
	}

	public void addScreen(Screen newScreen) {
		newScreen.LoadContent(glContext, mResourceManager);
		Screens.add(newScreen);
		//System.gc();
	}

	public void removeScreen(Screen screen) {
		screen.freeContent(glContext, mResourceManager);
		Screens.remove(screen);
		System.gc();
	}
	
	public int Count() {
		return Screens.size();
	}
	
	public Screen backupScreen(Screen screen) {

		ScreensToBackup.add(screen);
		return screen;
	}
	
	public Screen recoverScreen(Screen screen) {
	
		if (screen != null && ScreensToBackup.contains(screen)) {
            ScreensToBackup.remove(screen);
            Screens.add(screen);
			return screen;
		}
		return null;
	}
	
	public boolean hasFinished() {
		return (Screens.isEmpty());
	}
	
	public void Update(float time) {

        if (main != null) {
            main.Update(time);
        }
        else {
            int size = Screens.size();
            for (int i = 0; i < size; i++) {
                ScreensToUpdate.add(Screens.get(i));
            }

            for (int i = 0; i < size; i++) {
                Screen scr = ScreensToUpdate.get(i);
                scr.Update(time);
            }
            ScreensToUpdate.clear();

            checkFocus();
        }
				
	}
	
	public void Clear(GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		//GLES20.glMatrixMode(GLES20.GL_MODELVIEW);
		//GLES20.glPushMatrix();
		//GLES20.glLoadIdentity();
	}

	public void setSize(int width, int height) {
		mWidth = width;
		mHeight = height;
		mLayout.setSize(width,height);
	}
	
	
	public Layout getLayout() {
		return mLayout;
	}
	
	public float getFPS() {
		return mFPS;
	}	
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

		int size = Screens.size();
		for (int i = 0; i < size; i++) {			
			if (Screens.get(i).onKeyDown(keyCode, event))
				return true;
		}
		return false;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {

		int size = Screens.size();
		for (int i = 0; i < size; i++) {
			if (Screens.get(i).onKeyUp(keyCode, event))
				return true;
		}
		return false;
	}

	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {

		int size = Screens.size();
		for (int i = 0; i < size; i++) {
			if (Screens.get(i).onGenericMotionEvent(event))
				return true;
		}
		return false;
	}
	
	@Override
	 public boolean onTouchEvent(final MotionEvent event) {

		int size = Screens.size();
		for (int i = 0; i < size; i++) {			
			if (Screens.get(i).onTouchEvent( event))
				return true;
		}
		return false;
	}
	
	
	public boolean onBackPressed() {
		
		int size = Screens.size();
		for (int i = 0; i < size; i++) {			
			if (Screens.get(i).onBackPressed())
				return true;
		}
		return false;
	}

    private void addToFocusList(Screen screen) {
        if (!ScreensOnFocus.contains(screen)) {
            ScreensOnFocus.add(screen);
            screen.onGetFocus();
        }
    }

    private void removeFromFocusList(Screen screen) {
        if (ScreensOnFocus.contains(screen)) {
            ScreensOnFocus.remove(screen);
            screen.onFocusLost();
        }
    }

    private void checkFocus() {
        int size = Screens.size();
        boolean focus = true;
        Screen scr;
        for (int i = 0; i < size; i++) {
            scr = Screens.get(i);
            //scr.setFocus(focus);
            if (focus) {
                addToFocusList(scr);
                focus = false;
            }
            else {
                removeFromFocusList(scr);
            }

        }
    }

    public void makeMainScreen(Screen scr) {
        this.main = scr;
    }

    public Screen getMainScreen() {
        return main;
    }

    public void clearMainScreen() {
        main = null;
    }
	
}
