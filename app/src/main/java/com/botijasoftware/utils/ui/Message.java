package com.botijasoftware.utils.ui;

import javax.microedition.khronos.opengles.GL10;
import com.botijasoftware.utils.Rectanglef;
import com.botijasoftware.utils.ResourceManager;
import com.botijasoftware.utils.SpriteBatch;
import com.botijasoftware.utils.Texture;
import com.botijasoftware.utils.Sprite;

public class Message {

	public Message(Texture texture, Rectanglef rect, float time) {
		mTexture = texture;
		mRectanglef = rect;
		onScreenTime = time;
		visible = false;
		setState(STATE_END);
	}
	
	public void Update(float time) {
	
		if (state == STATE_END)
			return;

		visible = true;			
		elapsedTime += time;
		if (state == STATE_IN && elapsedTime > inTime) {
			setState(STATE_SHOW);
			elapsedTime -= inTime;
		}
		else if (state == STATE_SHOW && elapsedTime > onScreenTime) {
			setState(STATE_OUT);
			elapsedTime -= onScreenTime;
		}
		else if (state == STATE_OUT && elapsedTime > outTime) {
			setState(STATE_END);
			elapsedTime -= outTime;
			visible = false;
		}
	}
	
	public void LoadContent(GL10 gl, ResourceManager resources) {
		sprite = new Sprite( mRectanglef, mTexture);
        spritebatch = new SpriteBatch(2);
	}
	
	public void reloadContent(GL10 gl) {
	}
	
	public void changeTexture(Texture texture) {
		mTexture = texture;
	}
	
	public void Draw(GL10 gl) {
		
		if (isVisible()) {
			//GLES10.glColor4f(1.0f, 1.0f, 1.0f, alpha );
            spritebatch.begin(gl);
            spritebatch.DrawSprite(gl, sprite);
            spritebatch.end(gl);
		}	
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void Show() {
		setState(STATE_IN);
		elapsedTime = 0.0f;
	}
	
	public void Hide() {
		setState(STATE_END);
		elapsedTime = 0.0f;
	}
	
	public int getState() {
		return state;
	}
	
	public void setState(int newState) {
		state = newState;
	}
	
	protected Texture mTexture;
	protected Rectanglef mRectanglef;
	protected Sprite sprite;
	protected float onScreenTime;
	protected float elapsedTime;
	protected float alpha = 1.0f;
	protected float inTime = 1.0f;
	protected float outTime = 1.0f;
	protected boolean visible = true;
	protected int state;
    protected SpriteBatch spritebatch;
	
	public final static int STATE_IN = 0;
	public final static int STATE_SHOW = 1;
	public final static int STATE_OUT = 2;
	public final static int STATE_END = 3;
	
}
