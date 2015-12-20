package com.botijasoftware.utils.ui;

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
	

	public void LoadContent(ResourceManager resources) {
        spritebatch = new SpriteBatch(2);
	}
	
	public void Draw() {

        spritebatch.begin();
		if (value) {
            spritebatch.DrawSprite(tickedSprite);
		}
		else {
            spritebatch.DrawSprite(untickedSprite);
		}
        spritebatch.end();
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