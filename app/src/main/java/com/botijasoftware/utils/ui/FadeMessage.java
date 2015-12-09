package com.botijasoftware.utils.ui;

import com.botijasoftware.utils.Rectanglef;
import com.botijasoftware.utils.Texture;


public class FadeMessage extends Message {
	public FadeMessage(Texture texture, Rectanglef rect, float time) {
		super(texture, rect, time);
	}

	public void Update(float time) {	
		super.Update(time);
		
		if (state == STATE_IN) {
			alpha = elapsedTime / inTime;
		}
		else if (state == STATE_SHOW) {
			alpha = 1.0f;
		}
		else if (state == STATE_OUT) {
			alpha = 1.0f - (elapsedTime / outTime);
		}
	}
	
}
