package com.botijasoftware.utils.ui;

import javax.microedition.khronos.opengles.GL10;

import com.botijasoftware.utils.Rectanglef;
import com.botijasoftware.utils.ResourceManager;
import com.botijasoftware.utils.SpriteBatch;
import com.botijasoftware.utils.Texture;
import com.botijasoftware.utils.Sprite;

public class TickButton {
	
	public TickButton(Rectanglef rectangle, Texture imgUnticked, Texture imgTicked, boolean val) {
		rect = rectangle;
		value = val;
		imageUnticked = imgUnticked;
		imageTicked = imgTicked;
		tickedSprite = new Sprite(rectangle, imageTicked);
		untickedSprite =  new Sprite(rectangle, imageUnticked);
		
	}
	
	public TickButton(Rectanglef rectangle, Texture imgUnticked, Texture imgTicked) {
	
		this(rectangle, imgUnticked, imgTicked, false);
	}
	

	public void LoadContent(GL10 gl, ResourceManager resources) {
        spritebatch = new SpriteBatch(2);
	}
	
	public void Draw(GL10 gl) {		

        spritebatch.begin(gl);
		if (value) {
            spritebatch.DrawSprite(gl, tickedSprite);
		}
		else {
            spritebatch.DrawSprite(gl, untickedSprite);
		}
        spritebatch.end(gl);
	}
	

	public void setValue( boolean val) {
		value = val;
	}
	
	public boolean isTicked() {
		return value;
	}
	
	public void setTicked() {
		value = true;
	}
	
	public void setUnticked() {
		value = false;
	}
	
	public boolean checkInput(int x, int y) {
		
		if (rect.contains(x, y)) {
			value = !value;
			return true;
		}
		else
			return false;
	}
	
	public void reloadImages(Texture imgUnTicked, Texture imgTicked) {
		imageTicked = imgTicked;
		imageUnticked = imgUnTicked;
		untickedSprite.setTexture(imageUnticked);
		tickedSprite.setTexture(imageTicked);
	}
	
	private Rectanglef rect;
	protected Sprite untickedSprite;
	protected Sprite tickedSprite;
	protected Texture imageTicked;
	protected Texture imageUnticked;
	protected boolean value;
    protected SpriteBatch spritebatch;

}