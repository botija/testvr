package com.botijasoftware.vrtest;

import android.opengl.GLES20;

import com.botijasoftware.utils.ColorRGBA;
import com.botijasoftware.utils.ScreenManagerVR;
import com.botijasoftware.utils.Screens.ScreenVR;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;

import java.util.Random;


public class MainScreen extends ScreenVR {

    public ColorRGBA color = new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f);
    private Random random = new Random();

    public MainScreen(ScreenManagerVR screenManager) {
        super(screenManager);
    }

    public void Update(float time) {
        if (state == ScreenState.ACTIVE) {

        }
        else if (state == ScreenState.ENDED) {
            mScreenManager.removeScreen(this);
        }
    }

    public void onNewFrame(HeadTransform headTransform) {

    }

    public void onDrawEye(Eye eye) {

        GLES20.glClearColor(color.R, color.G, color.B, color.A);
        GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

    }

    public void onCardboardTrigger() {
        color.setValue(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1.0f);
    }



}
