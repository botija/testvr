package com.botijasoftware.utils.ui

import com.botijasoftware.utils.Rectanglef
import com.botijasoftware.utils.ResourceManager
import com.botijasoftware.utils.SpriteBatch
import com.botijasoftware.utils.Texture
import com.botijasoftware.utils.Sprite

class TickButton @JvmOverloads constructor(private val rect: Rectanglef, protected var imageUnticked: Texture, protected var imageTicked: Texture, value: Boolean = false) {

    protected var untickedSprite: Sprite
    protected var tickedSprite: Sprite
    var isTicked: Boolean = false
        protected set
    protected lateinit var spritebatch: SpriteBatch


    init {
        isTicked = value
        tickedSprite = Sprite(rect, imageTicked)
        untickedSprite = Sprite(rect, imageUnticked)

    }


    fun LoadContent(resources: ResourceManager) {
        spritebatch = SpriteBatch(2)
    }

    fun Draw() {

        spritebatch.begin()
        if (isTicked) {
            spritebatch.DrawSprite(tickedSprite)
        } else {
            spritebatch.DrawSprite(untickedSprite)
        }
        spritebatch.end()
    }


    fun setValue(value: Boolean) {
        isTicked = value
    }

    fun setTicked() {
        isTicked = true
    }

    fun setUnticked() {
        isTicked = false
    }

    fun checkInput(x: Int, y: Int): Boolean {

        if (rect.contains(x.toFloat(), y.toFloat())) {
            isTicked = !isTicked
            return true
        } else
            return false
    }

    fun reloadImages(imgUnTicked: Texture, imgTicked: Texture) {
        imageTicked = imgTicked
        imageUnticked = imgUnTicked
        untickedSprite.setTexture(imageUnticked, false)
        tickedSprite.setTexture(imageTicked, false)
    }


}