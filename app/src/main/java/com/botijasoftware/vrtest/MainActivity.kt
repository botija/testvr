package com.botijasoftware.vrtest

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle

import com.botijasoftware.utils.GameActivity
import com.botijasoftware.utils.GameActivityVR

class MainActivity : GameActivityVR() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mScreenManager.addScreen(MainScreen(mScreenManager))

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

    }

    override fun onBackPressed() {

        if (!mScreenManager.onBackPressed()) {
            AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Quit game?")
                    .setMessage("Are you sure you want to quit?")
                    .setPositiveButton("Yes") { dialog, which -> finish() }
                    .setNegativeButton("No", null)
                    .show()
        }
    }


}
