package com.botijasoftware.utils

open class Texture {

    open var id: Int = 0
    var width: Int = 0
    var height: Int = 0
    var textureCoords: TextureCoords
        protected set
    var referenceCount = 0
        protected set

    constructor(id: Int, w: Int, h: Int) {
        this.id = id
        width = w
        height = h
        textureCoords = TextureCoords()
    }

    constructor(id: Int, w: Int, h: Int, tc: TextureCoords) {
        this.id = id
        width = w
        height = h
        textureCoords = tc
    }

    /*public Texture clone() {

        return new Texture(mID, width, height, textCoords.clone());

    }*/

    fun subTexture(S0: Float, T0: Float, S1: Float, T1: Float): TextureRegion {
        return TextureRegion(this, TextureCoords(S0, T0, S1, T1))
    }

    fun addReference() {
        referenceCount++
    }

    fun removeReference() {
        referenceCount--
        if (referenceCount < 0)
            referenceCount = 0
    }

    val isReferenced: Boolean
        get() = referenceCount > 0


}