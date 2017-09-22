package com.botijasoftware.utils

import com.botijasoftware.utils.Sprite


class SpriteAnimated(rectangle: Rectanglef, private var mAtlas: TextureAtlas, private var mAnimationTime: Float, scale: Float, rotation: Float, color: ColorRGBAb, flip: Boolean) : Sprite(rectangle, mAtlas.mTexture, scale, rotation, color, flip) {

    private var mAcumAnimationTime: Float = 0.0f
    private var mFrameTime: Float = 0.0f
    private var mTotalFrames: Int = 0
    private var mCurrentFrame: Int = 0

    init {
        mAcumAnimationTime = 0.0f
        mTotalFrames = mAtlas.textureCount
        mCurrentFrame = 0
        mFrameTime = mTotalFrames / mAnimationTime
    }

    fun Update(time: Float) {
        //super.Update(time);
        mAcumAnimationTime += time
        if (mAcumAnimationTime >= mAnimationTime)
            mAcumAnimationTime -= mAnimationTime

        val frame = (mAcumAnimationTime / mFrameTime).toInt()

        if (frame != mCurrentFrame) {
            mCurrentFrame = frame
            val coords = mAtlas.getTextureCoords(mCurrentFrame)

            //float st [] = { coords.s0, coords.t0, coords.s1, coords.t0, coords.s0, coords.t1, coords.s1, coords.t1 };
            //mVertexBuffer.getBuffer(1).put(st);
        }

    }


    fun setAnimation(atlas: TextureAtlas, animationtime: Float) {
        mAtlas = atlas
        mAnimationTime = animationtime
        mAcumAnimationTime = 0.0f
        mTotalFrames = mAtlas.textureCount
        mCurrentFrame = 0
        mFrameTime = mTotalFrames / mAnimationTime

        texture = mAtlas.mTexture
    }


    fun setAnimation(atlas: TextureAtlas, animationtime: Float, flip: Boolean) {

        mFlip = flip
        setAnimation(atlas, animationtime)
    }


}