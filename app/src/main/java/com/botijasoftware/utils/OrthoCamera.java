package com.botijasoftware.utils;

import java.util.ArrayList;

import com.botijasoftware.utils.renderer.Renderer;

public class OrthoCamera {
	
	public float mZoom = 1.0f;
	private float mFinalZoom = 1.0f;
	private float mZoomSpeed = 0.0f;
	private float mZoomTime = 0.0f;
	private float mZoomElapsedTime = 0.0f;
	public Vector2 mLookAt;
	public Vector2 mSpeed;
	private Vector2 mFinalLookAt;
	private float mLookAtTime = 0.0f;
	private float mLookAtElapsedTime = 0.0f;
	private int mWidth;
	private int mHeight;
	private int mHalfWidth;
	private int mHalfHeight;
	private Rectangle mViewRect;
	private Vector2 mTarget = null;
	private ArrayList<Vector2> mPath = null;
	private Vector2 mStartPath = null;
	private boolean mFollowingPath = false;
	private int mPathIndex = 0;
	
	public OrthoCamera(int width, int height, Rectangle viewRect) {
		mWidth = width;
		mHalfWidth = width / 2;
		mHeight = height;
		mHalfHeight = height / 2;
		mViewRect = viewRect;
		mLookAt = new Vector2(mViewRect.X+mHalfWidth, mViewRect.Y+mHalfHeight);
		mSpeed = new Vector2(0.0f, 0.0f);
		mFinalLookAt = new Vector2(mViewRect.X+mHalfWidth, mViewRect.Y+mHalfHeight);
		mZoom = 1.0f;
		mFinalZoom = 1.0f;
		mZoomSpeed = 0.0f;
		mZoomTime = 0.0f;
		mZoomElapsedTime = 0.0f;
		mLookAtTime = 0.0f;
		mLookAtElapsedTime = 0.0f;
		mTarget = null;
		mPath = null;
		mStartPath = new Vector2(0.0f, 0.0f);
		mFollowingPath = false;
	}
	
	public void reset() {
		mLookAt.setValue(mViewRect.X+mHalfWidth, mViewRect.Y+mHalfHeight);
		mSpeed.setValue(0.0f);
		mFinalLookAt.setValue(mViewRect.X+mHalfWidth, mViewRect.Y+mHalfHeight);
		mZoom = 1.0f;
		mFinalZoom = 1.0f;
		mZoomSpeed = 0.0f;
		mZoomTime = 0.0f;
		mZoomElapsedTime = 0.0f;
		mLookAtTime = 0.0f;
		mLookAtElapsedTime = 0.0f;
		mTarget = null;
	}
	
	public void Set() {
		//GLES10.glPushMatrix();
		//GLES10.glLoadIdentity();
		//GLES10.glScalef(mZoom, mZoom, 0.0f);
		//GLES10.glTranslatef(-mViewRect.X-mLookAt.X+mHalfWidth, -mViewRect.Y-mLookAt.Y+mHalfHeight, 0.0f);
		//GLES10.glPopMatrix();
        Renderer.pushModelViewMatrix();
        Renderer.modelview.loadIdentity();
        Renderer.modelview.scale(mZoom, mZoom, 0.0f);
        Renderer.modelview.translate(-mViewRect.X-mLookAt.X+mHalfWidth, -mViewRect.Y-mLookAt.Y+mHalfHeight, 0.0f);
    }
	
	public void UnSet() {

        Renderer.popModelViewMatrix();
        //GLES10.glPopMatrix();
	}
	
	public void setZoom(float zoom) {
		mZoom = zoom;
		mFinalZoom = zoom;
		mZoomSpeed = 0.0f;
	}
	
	public void setZoom(float zoom, float time) {
		mFinalZoom = zoom;
		if (time > 0.0f) {
			mZoomTime = time;
			mZoomElapsedTime = 0.0f;
			mZoomSpeed = (mFinalZoom - mZoom) / time;
		}
		else
			setZoom(zoom);
		
	}
	
	public void lookAt(float x, float y) {
		
		if (x < (mViewRect.X + mHalfWidth)) {
			mFinalLookAt.X = mViewRect.X + mHalfWidth;
		}
		else 				
			mFinalLookAt.X = x;
		
		if (x > (mViewRect.X + mViewRect.width - mHalfWidth))
			mFinalLookAt.X = mViewRect.X + mViewRect.width - mHalfWidth;
		
		
		if (y <= (mViewRect.Y + mHalfHeight)) {
			mFinalLookAt.Y = mViewRect.Y + mHalfHeight;
		}
		else 				
			mFinalLookAt.Y = y;
		
		if (y > (mViewRect.Y + mViewRect.height - mHalfHeight))
			mFinalLookAt.Y = mViewRect.Y + mViewRect.height - mHalfHeight;
		
		mSpeed.X = 0.0f;
		mSpeed.Y = 0.0f;
		mLookAt.X = mFinalLookAt.X;
		mLookAt.Y = mFinalLookAt.Y;		
	}
	
	public void lookAt(Vector2 p) {
		lookAt(p.X, p.Y);
	}
	
	public void lookAt(float x, float y, float time) {
		if (time != 0.0f) {
			mLookAtTime = time;
			mLookAtElapsedTime = 0.0f;

			if (x < (mViewRect.X + mHalfWidth))
				mFinalLookAt.X = mViewRect.X + mHalfWidth;
			else if (x > (mViewRect.X + mViewRect.width - mHalfWidth))
				mFinalLookAt.X = mViewRect.X + mViewRect.width - mHalfWidth;
			else 				
				mFinalLookAt.X = x;
			
			
			if (y < (mViewRect.Y + mHalfHeight))
				mFinalLookAt.Y = mViewRect.Y + mHalfHeight;
			else if (y > (mViewRect.Y + mViewRect.height - mHalfHeight))
				mFinalLookAt.Y = mViewRect.Y + mViewRect.height - mHalfHeight;
			else 
				mFinalLookAt.Y = y;
			
			mSpeed.X = (mFinalLookAt.X - mLookAt.X) / time;
			mSpeed.Y = (mFinalLookAt.Y - mLookAt.Y) / time;
		}
		else 
			lookAt(x,y);
	}
	
	public void lookAt(Vector2 p, float time) {
		lookAt(p.X, p.Y, time);
	}
	
	public void move(float x, float y, float time) {
		lookAt(mLookAt.X + x, mLookAt.Y + y, time);
	}
	
	public void move(float x, float y) {
		lookAt(mLookAt.X + x, mLookAt.Y + y); 
	}
	
	public void move(Vector2 p, float time) {
		lookAt(mLookAt.X + p.X, mLookAt.Y + p.Y , time); 
	}
	
	public void move(Vector2 p) {
		lookAt(mLookAt.X + p.X, mLookAt.Y + p.Y ); 
	}
	
	public void followTarget(Vector2 target) {
		mTarget = target; //store reference to vector, its updated automatically (by its object)
	}
	
	public void freeTarget() {
		mTarget = null;
	}
	
	public Vector2 fromScreen(Vector2 position) {
		return new Vector2(mLookAt.X + position.X - mHalfWidth, mLookAt.Y + position.Y - mHalfHeight);
	}
	
	public Vector2 fromScreen(float x, float y) {
		return new Vector2(mLookAt.X + x - mHalfWidth, mLookAt.Y + y - mHalfHeight);
	}
	
	public void Update(float time) {
		
		if (mFollowingPath) {
			
			Vector2 ep = mPath.get(mPathIndex);
			 
			//prevent camera from surpassing its end destination
			if ((mSpeed.X > 0.0f && mLookAt.X > ep.X) || (mSpeed.X < 0.0f && mLookAt.X < ep.X) ) 
				mLookAt.X = ep.X;
				
			if ((mSpeed.Y > 0.0f && mLookAt.Y > ep.Y) || (mSpeed.Y < 0.0f && mLookAt.Y < ep.Y) ) 
				mLookAt.Y = ep.Y;
			
			if (mLookAt.equals(mFinalLookAt)) {
				mPathIndex++;
			 
				if (mPathIndex >= mPath.size()) {
					mFollowingPath = false;
				}
				else {
					ep = mPath.get(mPathIndex).add(mStartPath);
					float t = ep.distance(mLookAt);
					lookAt(ep, t/100.0f); // 100px/s
				}
			}
		}
		
		if (mTarget != null) {
			lookAt(mTarget);
			mZoomTime = 0.0f;
			mLookAtTime = 0.0f;
			return;
		}
		
		if (mZoomElapsedTime < mZoomTime) {
			mZoomElapsedTime += time;
			mZoom += mZoomSpeed * time;
		}
		else {
			mZoom = mFinalZoom;
			mZoomTime = 0.0f;
		}
				
		if (mLookAtElapsedTime < mLookAtTime) {
			mLookAt.X += mSpeed.X * time;
			mLookAt.Y += mSpeed.Y * time;
			mLookAtElapsedTime += time;
			
			//prevent camera from surpassing its end destination
			if ((mSpeed.X > 0.0f && mLookAt.X > mFinalLookAt.X) || (mSpeed.X < 0.0f && mLookAt.X < mFinalLookAt.X) ) 
				mLookAt.X = mFinalLookAt.X;
			
			if ((mSpeed.Y > 0.0f && mLookAt.Y > mFinalLookAt.Y) || (mSpeed.Y < 0.0f && mLookAt.Y < mFinalLookAt.Y) ) 
				mLookAt.Y = mFinalLookAt.Y;
			
		}
		else {
			mLookAt.X = mFinalLookAt.X;
			mLookAt.Y = mFinalLookAt.Y;
			mLookAtTime = 0.0f;
		}

	}
	
	public void followPath( Vector2 startPoint, ArrayList<Vector2> path) {
		mStartPath = startPoint;
		mPath = path;
		mFollowingPath = true;
		mPathIndex = 0;
		lookAt(mStartPath.X, mStartPath.Y);
	}
	
	public boolean hasFinishedPath() {
		return mFollowingPath;
	}
	
}