package com.botijasoftware.utils

import android.app.Activity
import android.content.SharedPreferences
import android.media.AudioManager
import android.os.Bundle
import android.view.Window
import android.view.WindowManager

import com.botijasoftware.utils.ResourceManager
import com.botijasoftware.utils.ScreenManager


class GameActivity : Activity() {


    protected lateinit var mScreenManager: ScreenManager
    protected lateinit var mResourceManager: ResourceManager
    protected lateinit var mSharedPreferences: SharedPreferences

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val mSharedPreferences = getPreferences(0)
        mResourceManager = ResourceManager(applicationContext)

        mScreenManager = ScreenManager(this, mResourceManager, mSharedPreferences)

        volumeControlStream = AudioManager.STREAM_MUSIC

        setContentView(mScreenManager)


    }


    override fun onBackPressed() {

        if (!mScreenManager.onBackPressed()) {
            super.onBackPressed()
        }
    }


    public override fun onResume() {
        super.onResume()
        mScreenManager.onResume()
    }


    public override fun onPause() {
        super.onPause()
        mScreenManager.onPause()
    }

    public override fun onDestroy() {
        super.onDestroy()
        mScreenManager.onDestroy()
    }

    public override fun onStop() {
        super.onStop()
        mScreenManager.onStop()
    }



}  
