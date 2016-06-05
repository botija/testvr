package com.botijasoftware.vrtest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.botijasoftware.utils.GameActivityVR;

public class MainActivity extends GameActivityVR {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScreenManager.addScreen(new MainScreen(mScreenManager));

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onBackPressed() {

        if (!mScreenManager.onBackPressed()) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Quit game?")
                    .setMessage("Are you sure you want to quit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }



}
