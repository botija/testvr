package com.botijasoftware.utils.ui;

import com.botijasoftware.utils.*;


public class Button {
	
	public Button(Rectangle rectangle, Texture imageUp, Texture imageDown) {
	
		rect = rectangle; 
		isPushed = false;
		textureUp = imageUp;
		textureDown = imageDown;
		mVisible = true;
		mSprite = new Sprite( new Rectanglef(rect) , textureUp );
	}
	
	
	
	public void LoadContent(ResourceManager resources) {
        spritebatch = new SpriteBatch(2);
	}
	
	public void Draw() {
		
		if (mVisible) {
            spritebatch.begin();
			spritebatch.DrawSprite(mSprite);
            spritebatch.end();
        }
	}
	
	public boolean isPressed() {
		return isPushed;
	}
	
	public void setPressed() {
		isPushed = true;
		updateImage();
	}
	
	public void setUnpressed() {
		isPushed = false;
		updateImage();
	}
	
	public boolean checkInput(int x, int y) {
		
		if (rect.contains(x, y)) {
			isPushed = !isPushed;
			updateImage();
		}
		return isPushed;
	}
	
	public boolean checkInputNoUnpush(int x, int y) {
		
		if (rect.contains(x, y)) {
			isPushed = true;
			updateImage();
		}
		return isPushed;
	}
	
	private void updateImage() {
		if (isPushed) {
			mSprite.setTexture(textureDown);
		}
		else {
			mSprite.setTexture(textureUp);
		}
	}
	
	public void reloadImages(Texture imageUp, Texture imageDown) {
		textureUp = imageUp;
		textureDown = imageDown;
	}
	
	public void hide() {
		mVisible = false;
	}
	
	public void show() {
		mVisible = true;
	}
	
	public void setVisible(boolean v) {
		mVisible = v;
	}
	
	public boolean isVisible() {
		return mVisible;
	}
	
	public void unPush() {
		isPushed = false;
	}
	
	public void scrollBy(int xScroll, int yScroll) {
		float x = mSprite.getRectangle().X + xScroll;
		float y = mSprite.getRectangle().Y + yScroll;
		mSprite.setPosition(x, y);
		rect.setPosition((int)x, (int)y);
	}
	
	public void moveTo(int x, int y) {
		mSprite.setPosition(x, y);
		rect.setPosition(x, y);
	}
	
	public void moveToX(int x) {
		float y = mSprite.getRectangle().Y;
		mSprite.setPosition(x, y);
		rect.setPosition(x, (int)y);
	}
	
	public void moveToY(int y) {
		float x = mSprite.getRectangle().X;
		mSprite.setPosition(x, y);
		rect.setPosition((int)x, y);
	}
		
	private Rectangle rect;
	private boolean isPushed;
	protected Sprite mSprite;
	protected Texture textureUp;
	protected Texture textureDown;
	protected boolean mVisible;
    protected SpriteBatch spritebatch;



}