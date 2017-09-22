package com.botijasoftware.utils


import android.content.SharedPreferences

class GlobalOptions internal constructor(protected var mSharedPreferences: SharedPreferences) {

    protected var mSharedPreferenceEditor: SharedPreferences.Editor

    init {
        mSharedPreferenceEditor = mSharedPreferences.edit()
        loadPreferences()
    }


    fun loadPreferences() {}

    fun savePreferences() {}


}
