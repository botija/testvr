package com.botijasoftware.utils;


public class TextureRegion extends Texture {

    private Texture original;

    public TextureRegion(Texture texture, TextureCoords coords) {
        super(texture.getID(), texture.width, texture.height);
        textCoords = coords;
        this.original = texture;
    }

    @Override
    public int getID() {
        return original.getID();
    }

    @Override
    public TextureRegion clone() {
        return new TextureRegion(original, textCoords);
    }

}
