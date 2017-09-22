package com.botijasoftware.utils

open class Atlas(var mTexture: Texture) {

    protected lateinit var mCoords: Array<TextureCoords>
    var textureCount: Int = 0

    fun LoadContent(resources: ResourceManager) {}

    val textureID: Int
        get() = mTexture.id

    fun flipCoords() {

        for (i in 0..textureCount - 1) {
            mCoords[i].flipVertical()
        }
    }


    fun getTextureCoords(textLinear: Int): TextureCoords {
        return mCoords[textLinear]
    }

    fun getAsTexture(textLinear: Int): TextureRegion {
        return TextureRegion(mTexture, getTextureCoords(textLinear))
    }

}