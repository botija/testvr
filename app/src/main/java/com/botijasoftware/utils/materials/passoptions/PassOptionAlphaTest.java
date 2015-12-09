package com.botijasoftware.utils.materials.passoptions;

import android.opengl.GLES10;

import com.botijasoftware.utils.materials.Expression;
import com.botijasoftware.utils.materials.Material;

public class PassOptionAlphaTest extends PassOption {

	Expression alphatest;

	public PassOptionAlphaTest(Expression alphatest) {
		this.alphatest = alphatest;
	}

	@Override
	public void set(Material material) {
		GLES10.glAlphaFunc(GLES10.GL_GREATER, alphatest.evaluate(material));
		GLES10.glEnable(GLES10.GL_ALPHA_TEST);
	}

	@Override
	public void unSet() {
		GLES10.glDisable(GLES10.GL_ALPHA_TEST);
	}

}