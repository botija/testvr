package com.botijasoftware.utils;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES10;

public class Light {

	public Light(int lightid) {
		mLightID = lightid;
		mPosition = new float[4];
		mAmbient = new float[4];
		mDifuse = new float[4];
		mSpecular = new float[4];
	}
	
	public void enable(GL10 gl) {
		GLES10.glEnable(mLightID);
	}

	public void disable(GL10 gl) {
		GLES10.glDisable(mLightID);
	}

	public void setAmbient(GL10 gl, float r, float g, float b, float a) {
		mAmbient[0] = r;
		mAmbient[1] = g;
		mAmbient[2] = b;
		mAmbient[3] = a;
		
		GLES10.glLightfv(mLightID, GLES10.GL_AMBIENT, mAmbient, 0);
	}

	public void setSpecular(GL10 gl, float r, float g, float b, float a) {
		mSpecular[0] = r;
		mSpecular[1] = g;
		mSpecular[2] = b;
		mSpecular[3] = a;
		
		GLES10.glLightfv(mLightID, GLES10.GL_SPECULAR, mSpecular, 0);
	}

	public void setDifuse(GL10 gl, float r, float g, float b, float a) {
		mDifuse[0] = r;
		mDifuse[1] = g;
		mDifuse[2] = b;
		mDifuse[3] = a;
		
		GLES10.glLightfv(mLightID, GLES10.GL_DIFFUSE, mDifuse, 0);
	}

	public void setPosition(GL10 gl, float x, float y, float z) {
		mPosition[0] = x;
		mPosition[1] = y;
		mPosition[2] = z;
		mPosition[3] = 0.0f;
		
		GLES10.glLightfv(mLightID, GLES10.GL_POSITION, mPosition, 0);
	}

	public void setDirection(GL10 gl, float x, float y, float z) {
		mPosition[0] = x;
		mPosition[1] = y;
		mPosition[2] = z;
		mPosition[3] = 1.0f;
		
		GLES10.glLightfv(mLightID, GLES10.GL_POSITION, mPosition, 0);
	}

	public void setLConstantAttenuation(GL10 gl, float attenuation) {
		mConstantAttenuation = attenuation;
		GLES10.glLightf(mLightID, GLES10.GL_CONSTANT_ATTENUATION, mConstantAttenuation);
	}

	public void setLinearAttenuation(GL10 gl, float attenuation) {
		mLinearAttenuation = attenuation;
		GLES10.glLightf(mLightID, GLES10.GL_LINEAR_ATTENUATION, mLinearAttenuation);
	}

	public void setQuadraticAttenuation(GL10 gl, float attenuation) {
		mQuadricAttenuation = attenuation;
		GLES10.glLightf(mLightID, GLES10.GL_QUADRATIC_ATTENUATION, mQuadricAttenuation);
	}


	private int mLightID;
	private float mPosition[];
	private float mAmbient[];
	private float mDifuse[];
	private float mSpecular[];
	private float mLinearAttenuation;
	private float mConstantAttenuation;
	private float mQuadricAttenuation;
	
}
	