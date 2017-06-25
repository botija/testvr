package com.botijasoftware.utils;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import com.google.vr.sdk.base.GvrActivity;



public class GameActivityVR extends GvrActivity {
    /** Called when the activity is first created. */
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		SharedPreferences mSharedPreferences = getPreferences(0);
		mResourceManager = new ResourceManager( this );

        mScreenManager = new ScreenManagerVR( this, mResourceManager, mSharedPreferences );

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

		setContentView(mScreenManager);


    }

    
   @Override
    public void onBackPressed() {

	    if (!mScreenManager.onBackPressed()) {
	    	super.onBackPressed();
	    }	   
    }


    @Override
    public void onResume()  {
        super.onResume();
        mScreenManager.onResume();
    }


    @Override
    public void onPause()  {
        super.onPause();
        mScreenManager.onPause();
    }

    @Override
    public void onDestroy()  {
        super.onDestroy();
        mScreenManager.onDestroy();
    }

    @Override
    public void onStop()  {
        super.onStop();
        mScreenManager.onStop();
    }


    public void onCardboardTrigger() {
        mScreenManager.onCardboardTrigger();
    }


    public boolean onGenericMotionEvent(MotionEvent event) {
        if (!mScreenManager.onGenericMotionEvent(event)) {
            return super.onGenericMotionEvent(event);
        }
        return true;
    }

    public boolean onTouchEvent(final MotionEvent event) {
        if (!mScreenManager.onTouchEvent(event)) {
            return super.onTouchEvent(event);
        };
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!mScreenManager.onKeyDown(keyCode, event)) {
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (!mScreenManager.onKeyUp(keyCode, event)) {
            return super.onKeyUp(keyCode, event);
        }
        return true;
    }
	
	protected ScreenManagerVR mScreenManager;
	protected ResourceManager mResourceManager;
	protected SharedPreferences mSharedPreferences;

}  
