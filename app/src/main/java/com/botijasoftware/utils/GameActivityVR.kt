package com.botijasoftware.utils

import android.content.SharedPreferences
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager
import com.google.vr.sdk.base.GvrActivity


open class GameActivityVR : GvrActivity() {
    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val mSharedPreferences = getPreferences(0)
        mResourceManager = ResourceManager(this)

        mScreenManager = ScreenManagerVR(this, mResourceManager, mSharedPreferences)

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


    override fun onCardboardTrigger() {
        mScreenManager.onCardboardTrigger()
    }


    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        if (!mScreenManager.onGenericMotionEvent(event)) {
            return super.onGenericMotionEvent(event)
        }
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!mScreenManager.onTouchEvent(event)) {
            return super.onTouchEvent(event)
        }
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (!mScreenManager.onKeyDown(keyCode, event!!)) {
            return super.onKeyDown(keyCode, event)
        }
        return true
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {

        if (!mScreenManager.onKeyUp(keyCode, event!!)) {
            return super.onKeyUp(keyCode, event)
        }
        return true
    }

    protected lateinit var mScreenManager: ScreenManagerVR
    protected lateinit var mResourceManager: ResourceManager
    protected lateinit var mSharedPreferences: SharedPreferences

}  
