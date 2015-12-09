package com.botijasoftware.utils.materials.passoptions;

import android.opengl.GLES10;

import com.botijasoftware.utils.materials.Expression;
import com.botijasoftware.utils.materials.Material;

public class PassOptionTextureScale extends PassOption {

	Expression scalex;
	Expression scaley;

	public PassOptionTextureScale(Expression scalex, Expression scaley) {
		this.scalex = scalex;
		this.scaley = scaley;
	}

	@Override
	public void set(Material material) {
		GLES10.glMatrixMode(GLES10.GL_TEXTURE);
		GLES10.glPushMatrix();
		GLES10.glScalef(scalex.evaluate(material), scaley.evaluate(material), 0.0f);
		GLES10.glMatrixMode(GLES10.GL_MODELVIEW);
	}

	@Override
	public void unSet() {
		GLES10.glMatrixMode(GLES10.GL_TEXTURE);
		GLES10.glPopMatrix();
		GLES10.glMatrixMode(GLES10.GL_MODELVIEW);
	}

}