package com.botijasoftware.utils;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES10;

public class LRSAnimation {
	
	public ArrayList<LRSAnimationFrame> mFrames;
	private boolean mLoop;
	private LRSAnimationFrame mInterpolatedFrame;
	private int mCurrentFrame;
	private int mNextFrame;
	private float mAcumTime = 0.0f;
	private float mAnimationTime = 0.0f;
	private float mCurrentFrameTime = 0.0f;
	private float[] mMatrix;
	
	public LRSAnimation() {
		this(false);
	}
	
	public LRSAnimation(boolean loop) {
		mFrames = new ArrayList<LRSAnimationFrame>();
		mLoop = loop;
		mMatrix = new float[16];
	}
	
	public void update(float time) {
		mAcumTime += time;
		LRSAnimationFrame frame = mFrames.get(mCurrentFrame);
		
		if (frame.mTime > (mCurrentFrameTime + time)) {
			mCurrentFrame = mNextFrame;
			mNextFrame++;
			if (mNextFrame >= mFrames.size()) {
				if (mLoop)
					mNextFrame = 0;
				else
					mNextFrame = mFrames.size()-1;
			}
			mAnimationTime += frame.mTime;
			mCurrentFrameTime = mCurrentFrameTime + time - frame.mTime;	
		}
		else {
			mCurrentFrameTime += time;
		}
		
		LRSAnimationFrame startframe = mFrames.get( mCurrentFrame );
		LRSAnimationFrame endframe = mFrames.get( mNextFrame );
		
		endframe.interpolate(startframe, mInterpolatedFrame, mCurrentFrameTime);
	}
	
	public void apply(GL10 gl) {
		GLES10.glPushMatrix();
		
		GLES10.glTranslatef(mInterpolatedFrame.mTrans.X, mInterpolatedFrame.mTrans.Y, mInterpolatedFrame.mTrans.Z);
		GLES10.glTranslatef(mInterpolatedFrame.mRotCenter.X, mInterpolatedFrame.mRotCenter.Y, mInterpolatedFrame.mRotCenter.Z);
		mInterpolatedFrame.mRotation.getMatrix(mMatrix);
		GLES10.glMultMatrixf(mMatrix, 0);
		GLES10.glTranslatef(-mInterpolatedFrame.mRotCenter.X, -mInterpolatedFrame.mRotCenter.Y, -mInterpolatedFrame.mRotCenter.Z);
		GLES10.glScalef(mInterpolatedFrame.mScale.X,mInterpolatedFrame.mScale.Y, mInterpolatedFrame.mScale.Z);
		
		GLES10.glPopMatrix();
	}

	
	public class LRSAnimationFrame {
		
		public float mTime;
		public Vector3 mTrans;
		public Quaternion mRotation;
		public Vector3 mRotCenter;
		private Vector3 mScale;

		public LRSAnimationFrame(float time, Vector3 trans, Quaternion rot, Vector3 crot, Vector3 scale) {
			mTime = time;
			mTrans = trans;
			mRotation = rot;
			mRotCenter = crot;
			mScale = scale;
		}
		
		public void interpolate(LRSAnimationFrame start, LRSAnimationFrame interpolated, float time) {
			float ratio = time / mTime;
			float one_minus_ratio = 1.0f - ratio;
			
			interpolated.mTrans.X = mTrans.X * one_minus_ratio + start.mTrans.X * ratio;
			interpolated.mTrans.X = mTrans.Y * one_minus_ratio + start.mTrans.Y * ratio;
			interpolated.mTrans.X = mTrans.Z * one_minus_ratio + start.mTrans.Z * ratio;
			
			interpolated.mScale.X = mScale.X * one_minus_ratio + start.mScale.X * ratio;
			interpolated.mScale.X = mScale.Y * one_minus_ratio + start.mScale.Y * ratio;
			interpolated.mScale.X = mScale.Z * one_minus_ratio + start.mScale.Z * ratio;
			
			interpolated.mRotCenter.X = mRotCenter.X * one_minus_ratio + start.mRotCenter.X * ratio;
			interpolated.mRotCenter.X = mRotCenter.Y * one_minus_ratio + start.mRotCenter.Y * ratio;
			interpolated.mRotCenter.X = mRotCenter.Z * one_minus_ratio + start.mRotCenter.Z * ratio;
			
			interpolated.mRotation = start.mRotation.slerp(mRotation, ratio);
			
			
		}
		
	}
}