package com.botijasoftware.utils


class SimpleTextureAtlas(texture: Texture, protected var ncols: Int, protected var nrows: Int, count: Int) : Atlas(texture) {

    protected var textX: Float = 0.toFloat()
    protected var textY: Float = 0.toFloat()

    init {
        textureCount = count
        textX = 1.0f / ncols
        textY = 1.0f / nrows

        mCoords = Array<TextureCoords>(textureCount, { i -> val row = i / ncols; val column = i - row * ncols; TextureCoords(textX * column, textY * (row + 1), textX * (column + 1), textY * row)})

        /*for (i in 0..textureCount - 1) {
            val row = i / ncols
            val column = i - row * ncols
            mCoords[i] = TextureCoords(textX * column, textY * (row + 1), textX * (column + 1), textY * row)
        }*/

    }

}
