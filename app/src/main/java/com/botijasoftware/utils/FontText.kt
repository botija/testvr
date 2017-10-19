package com.botijasoftware.utils

class FontText(private val mFont: Font) {

    private var textLen: Int = 0
    private lateinit var oldText: String
    private lateinit var spritebatch: SpriteBatch
    private lateinit var tmpSprite: Sprite
    private var fontsize: Float = 0.toFloat()
    private var fontsizesep: Float = 0.toFloat()


    init {
        textLen = 0


    }

    fun LoadContent(resources: ResourceManager) {

        val x = 0.0f
        val y = 0.0f
        fontsize = mFont.fontSize.toFloat()// * mLayout.getHorizontal(1);// * fontsize * 0.2f;
        fontsizesep = fontsize * 3 / 7

        spritebatch = SpriteBatch(MAX_CHARS)
        tmpSprite = Sprite(Rectanglef(0f, 0f, fontsize, fontsize), mFont.texture)
    }


    @JvmOverloads fun Draw(x: Float, y: Float, text: String, color: ColorRGBAb? = null) {

        spritebatch.begin()


        textLen = text.length

        spritebatch.begin()

        if (textLen > MAX_CHARS)
            textLen = MAX_CHARS

        if (color != null)
            tmpSprite.setColor(color)

        var ascii: Int

        for (i in 0..textLen - 1) {

            ascii = text.codePointAt(i)
            tmpSprite.texture = mFont.getTexture(ascii)
            tmpSprite.rectangle.setPosition(x + i * fontsizesep, y)
            spritebatch.DrawSprite(tmpSprite)

        }
        spritebatch.end()

    }

    fun getTextSize(text: String): Vector2 {
        return Vector2(fontsizesep * text.length, fontsize)
    }

    companion object {
        private val MAX_CHARS = 32
    }


}
