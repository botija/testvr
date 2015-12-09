package com.botijasoftware.utils;


import android.content.SharedPreferences;

public class GlobalOptions {
	
	GlobalOptions(SharedPreferences preferences) {
		mSharedPreferences = preferences;
		mSharedPreferenceEditor = preferences.edit();
		loadPreferences();
	}


	public void loadPreferences() {
	}
	
	public void savePreferences() {
	}
	
	protected SharedPreferences mSharedPreferences;
	protected SharedPreferences.Editor mSharedPreferenceEditor;

}
