package com.botijasoftware.utils.materials.passoptions;

import android.opengl.GLES10;

import com.botijasoftware.utils.materials.Expression;
import com.botijasoftware.utils.materials.Material;

public class PassOptionTextureRotate extends PassOption {

	Expression rotation;

	public PassOptionTextureRotate(Expression rotation) {
		this.rotation = rotation;
	}

	@Override
	public void set(Material material) {
		GLES10.glMatrixMode(GLES10.GL_TEXTURE);
		GLES10.glPushMatrix();
		GLES10.glTranslatef(0.5f, 0.5f, 0.0f);
		GLES10.glRotatef(rotation.evaluate(material), 0, 0, 1.0f);
		GLES10.glTranslatef(-0.5f, -0.5f, 0.0f);
		GLES10.glMatrixMode(GLES10.GL_MODELVIEW);
	}

	@Override
	public void unSet() {
		GLES10.glMatrixMode(GLES10.GL_TEXTURE);
		GLES10.glPopMatrix();
		GLES10.glMatrixMode(GLES10.GL_MODELVIEW);
	}

}