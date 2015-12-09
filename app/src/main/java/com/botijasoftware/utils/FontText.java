package com.botijasoftware.utils;

import javax.microedition.khronos.opengles.GL10;

public class FontText {

    private int textLen;
    private String oldText;
    private Font mFont;
    private static final int MAX_CHARS = 32;
    private SpriteBatch spritebatch;
    private Sprite tmpSprite;
    private float fontsize;
    private float fontsizesep;



	public FontText(Font font)
	{
	    mFont = font;
		textLen = 0;


	}
	
	public void LoadContent(GL10 gl, ResourceManager resources) {
	
		float x = 0.0f;
		float y = 0.0f;
		fontsize = (float) mFont.getFontSize();// * mLayout.getHorizontal(1);// * fontsize * 0.2f;
        fontsizesep = fontsize * 3/7;

        spritebatch = new SpriteBatch(MAX_CHARS);
        tmpSprite = new Sprite(new Rectanglef(0,0,fontsize,fontsize), mFont.getTexture());
	}

    public void Draw(GL10 gl, float x, float y, String text) {
        Draw(gl, x, y, text, null);
    }

	
	public void Draw(GL10 gl, float x, float y, String text, ColorRGBAb color) {

		spritebatch.begin(gl);


        textLen = text.length();

        spritebatch.begin(gl);

        if (textLen > MAX_CHARS)
            textLen = MAX_CHARS;

        if (color != null)
            tmpSprite.setColor(color);

        int ascii = 0;

        for (int i = 0; i< textLen; i++) {

            ascii = text.codePointAt(i);
            tmpSprite.setTexture(mFont.getTexture(ascii));
            tmpSprite.getRectangle().setPosition(x+ i*fontsizesep , y);
            spritebatch.DrawSprite(gl, tmpSprite);

        }
        spritebatch.end(gl);

	}

    public Vector2 getTextSize(String text) {
        return new Vector2(fontsizesep*text.length(), fontsize);
    }


}
