package com.botijasoftware.utils.materials.passoptions;

import android.opengl.GLES10;

import com.botijasoftware.utils.materials.Expression;
import com.botijasoftware.utils.materials.Material;

public class PassOptionPolygonOffset extends PassOption {

	Expression offset;

	public PassOptionPolygonOffset(Expression offset) {
		this.offset = offset;
	}

	@Override
	public void set(Material material) {
		GLES10.glPolygonOffset(0.0f, offset.evaluate(material));
		GLES10.glEnable(GLES10.GL_POLYGON_OFFSET_FILL);
	}

	@Override
	public void unSet() {
		GLES10.glDisable(GLES10.GL_POLYGON_OFFSET_FILL);
	}

}