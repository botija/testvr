package com.botijasoftware.utils.ui;

import com.botijasoftware.utils.Rectanglef;
import com.botijasoftware.utils.Texture;


public class PopUpMessage extends Message {

	public PopUpMessage(Texture texture, Rectanglef rect, float time) {
		super(texture, rect, time);
		sizeincx = rect.width / inTime;
		sizeincy = rect.height / inTime;
		growRectangle = new Rectanglef(0,0,0,0);
		growRectangle.setCenter( mRectanglef.getCenter() );
	}

	public void Update(float time) {	
	
		super.Update(time);
		
		if (state == STATE_IN) {
			growRectangle.setSize( (int)(elapsedTime * sizeincx), (int)(elapsedTime * sizeincy) );
			growRectangle.setCenter( mRectanglef.getCenter() );
			sprite.setRectangle( growRectangle );
			quadChanged = true;
		}
		else if (state == STATE_SHOW) {
			if (quadChanged) {
				sprite.setRectangle( mRectanglef );
				quadChanged = false;
			}
		}
		else if (state == STATE_OUT) {
			growRectangle.setSize( (int)((outTime - elapsedTime) * sizeincx), (int)((outTime - elapsedTime) * sizeincy) );
			growRectangle.setCenter( mRectanglef.getCenter() );
			sprite.setRectangle( growRectangle );
			quadChanged = true;
		}
	}
	
	private float sizeincx;
	private float sizeincy;
	private Rectanglef growRectangle;
	private boolean quadChanged = true;
}