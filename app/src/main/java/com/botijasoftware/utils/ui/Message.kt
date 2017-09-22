package com.botijasoftware.utils.ui

import com.botijasoftware.utils.Rectanglef
import com.botijasoftware.utils.ResourceManager
import com.botijasoftware.utils.SpriteBatch
import com.botijasoftware.utils.Texture
import com.botijasoftware.utils.Sprite

open class Message(protected var mTexture: Texture, protected var mRectanglef: Rectanglef, protected var onScreenTime: Float) {

    protected lateinit var sprite: Sprite
    protected var elapsedTime: Float = 0.0f
    protected var alpha = 1.0f
    protected var inTime = 1.0f
    protected var outTime = 1.0f
    var isVisible = true
        protected set
    var state: Int = 0
    protected lateinit var spritebatch: SpriteBatch

    init {
        isVisible = false
        state = STATE_END
    }

    open fun Update(time: Float) {

        if (state == STATE_END)
            return

        isVisible = true
        elapsedTime += time
        if (state == STATE_IN && elapsedTime > inTime) {
            state = STATE_SHOW
            elapsedTime -= inTime
        } else if (state == STATE_SHOW && elapsedTime > onScreenTime) {
            state = STATE_OUT
            elapsedTime -= onScreenTime
        } else if (state == STATE_OUT && elapsedTime > outTime) {
            state = STATE_END
            elapsedTime -= outTime
            isVisible = false
        }
    }

    fun LoadContent(resources: ResourceManager) {
        sprite = Sprite(mRectanglef, mTexture)
        spritebatch = SpriteBatch(2)
    }

    fun reloadContent() {}

    fun changeTexture(texture: Texture) {
        mTexture = texture
    }

    fun Draw() {

        if (isVisible) {
            //GLES10.glColor4f(1.0f, 1.0f, 1.0f, alpha );
            spritebatch.begin()
            spritebatch.DrawSprite(sprite)
            spritebatch.end()
        }
    }

    fun Show() {
        state = STATE_IN
        elapsedTime = 0.0f
    }

    fun Hide() {
        state = STATE_END
        elapsedTime = 0.0f
    }


    companion object {

        val STATE_IN = 0
        val STATE_SHOW = 1
        val STATE_OUT = 2
        val STATE_END = 3
    }

}
