package com.botijasoftware.utils.ui

import com.botijasoftware.utils.*


class Button(private val rect: Rectangle, protected var textureUp: Texture, protected var textureDown: Texture) {

    var isPressed: Boolean = false
        private set
    protected var mSprite: Sprite
    var isVisible: Boolean = false
    protected lateinit var spritebatch: SpriteBatch


    init {
        isPressed = false
        isVisible = true
        mSprite = Sprite(Rectanglef(rect), textureUp)
    }


    fun LoadContent(resources: ResourceManager) {
        spritebatch = SpriteBatch(2)
    }

    fun Draw() {

        if (isVisible) {
            spritebatch.begin()
            spritebatch.DrawSprite(mSprite)
            spritebatch.end()
        }
    }

    fun setPressed() {
        isPressed = true
        updateImage()
    }

    fun setUnpressed() {
        isPressed = false
        updateImage()
    }

    fun checkInput(x: Int, y: Int): Boolean {

        if (rect.contains(x, y)) {
            isPressed = !isPressed
            updateImage()
        }
        return isPressed
    }

    fun checkInputNoUnpush(x: Int, y: Int): Boolean {

        if (rect.contains(x, y)) {
            isPressed = true
            updateImage()
        }
        return isPressed
    }

    private fun updateImage() {
        if (isPressed) {
            mSprite.setTexture(textureDown, false)
        } else {
            mSprite.setTexture(textureUp, false)
        }
    }

    fun reloadImages(imageUp: Texture, imageDown: Texture) {
        textureUp = imageUp
        textureDown = imageDown
    }

    fun hide() {
        isVisible = false
    }

    fun show() {
        isVisible = true
    }

    fun unPush() {
        isPressed = false
    }

    fun scrollBy(xScroll: Int, yScroll: Int) {
        val x = mSprite.rectangle.X + xScroll
        val y = mSprite.rectangle.Y + yScroll
        mSprite.setPosition(x, y)
        rect.setPosition(x.toInt(), y.toInt())
    }

    fun moveTo(x: Int, y: Int) {
        mSprite.setPosition(x.toFloat(), y.toFloat())
        rect.setPosition(x, y)
    }

    fun moveToX(x: Int) {
        val y = mSprite.rectangle.Y
        mSprite.setPosition(x.toFloat(), y)
        rect.setPosition(x, y.toInt())
    }

    fun moveToY(y: Int) {
        val x = mSprite.rectangle.X
        mSprite.setPosition(x, y.toFloat())
        rect.setPosition(x.toInt(), y)
    }


}