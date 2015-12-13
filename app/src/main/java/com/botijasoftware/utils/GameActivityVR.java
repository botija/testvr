package com.botijasoftware.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.botijasoftware.utils.ResourceManager;
import com.botijasoftware.utils.ScreenManager;
import com.google.vrtoolkit.cardboard.CardboardActivity;


public class GameActivityVR extends CardboardActivity{
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
	
	protected ScreenManagerVR mScreenManager;
	protected ResourceManager mResourceManager;
	protected SharedPreferences mSharedPreferences;


}  
