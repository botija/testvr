package com.botijasoftware.utils;

import com.botijasoftware.utils.Sprite;


public class SpriteAnimated extends Sprite{
	public SpriteAnimated(Rectanglef rectangle, TextureAtlas atlas, float animationtime, float scale, float rotation, ColorRGBAb color, boolean flip) {
		super(rectangle, atlas.mTexture, scale, rotation, color, flip);
		mAtlas = atlas;
		mAnimationTime = animationtime;
		mAcumAnimationTime = 0.0f;
		mTotalFrames = mAtlas.mNumTextures;
		mCurrentFrame = 0;
		mFrameTime = mTotalFrames / mAnimationTime;
	}

	public void Update(float time) {
		//super.Update(time);
		mAcumAnimationTime += time;
		if (mAcumAnimationTime >= mAnimationTime)
			mAcumAnimationTime -= mAnimationTime;
		
		int frame = (int) (mAcumAnimationTime /mFrameTime);
		
		if (frame != mCurrentFrame) {
			mCurrentFrame = frame;
			TextureCoords coords = mAtlas.getTextureCoords(mCurrentFrame);
			
			float st [] = { coords.s0, coords.t0, coords.s1, coords.t0, coords.s0, coords.t1, coords.s1, coords.t1 };
			//mVertexBuffer.getBuffer(1).put(st);
		}
		
	}
	

	public void setAnimation(TextureAtlas atlas, float animationtime) {
		mAtlas = atlas;
		mAnimationTime = animationtime;
		mAcumAnimationTime = 0.0f;
		mTotalFrames = mAtlas.mNumTextures;
		mCurrentFrame = 0;
		mFrameTime = mTotalFrames / mAnimationTime;

		setTexture(mAtlas.mTexture);		
	}
	

	public void setAnimation(TextureAtlas atlas, float animationtime, boolean flip) {
		
		mFlip = flip;
		setAnimation(atlas, animationtime);	
	}
	
	private TextureAtlas mAtlas;
	private float mAnimationTime;
	private float mAcumAnimationTime;
	private float mFrameTime;
	private int mTotalFrames;
	private int mCurrentFrame;
	
}