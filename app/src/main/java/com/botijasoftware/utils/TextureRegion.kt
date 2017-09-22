package com.botijasoftware.utils


class TextureRegion(private val original: Texture, coords: TextureCoords) : Texture(original.id, original.width, original.height) {

    init {
        textureCoords = coords
    }

    override var id: Int
        get() = original.id
        set(value: Int) {
            super.id = value
        }

    fun clone(): TextureRegion {
        return TextureRegion(original, textureCoords)
    }

}
