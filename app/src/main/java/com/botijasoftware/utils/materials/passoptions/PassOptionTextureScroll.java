package com.botijasoftware.utils.materials.passoptions;

import android.opengl.GLES10;

import com.botijasoftware.utils.materials.Expression;
import com.botijasoftware.utils.materials.Material;

public class PassOptionTextureScroll extends PassOption {

	Expression scrollx;
	Expression scrolly;

	public PassOptionTextureScroll(Expression scrollx, Expression scrolly) {
		this.scrollx = scrollx;
		this.scrolly = scrolly;
	}

	@Override
	public void set(Material material) {
		GLES10.glMatrixMode(GLES10.GL_TEXTURE);
		GLES10.glPushMatrix();
		GLES10.glTranslatef(scrollx.evaluate(material), scrolly.evaluate(material), 0.0f);
	}

	@Override
	public void unSet() {
		GLES10.glMatrixMode(GLES10.GL_TEXTURE);
		GLES10.glPopMatrix();
		GLES10.glMatrixMode(GLES10.GL_MODELVIEW);
	}

}