package com.botijasoftware.utils


class Font(private val fontTextureID: Int, val fontSize: Int, private val mLayout: Layout) {
    lateinit var texture: Texture
    private lateinit var atlas: SimpleTextureAtlas

    init {
        //texture = null
        //atlas = null
    }

    fun LoadContent(resources: ResourceManager) {
        val texture = resources.loadTexture(fontTextureID)
        atlas = SimpleTextureAtlas(texture, 16, 16, 256)
    }

    fun getTexture(index: Int): Texture {
        return atlas.getAsTexture(index)
    }

    fun getTextureCoords(index: Int): TextureCoords {
        return atlas.getTextureCoords(index)

    }

}
