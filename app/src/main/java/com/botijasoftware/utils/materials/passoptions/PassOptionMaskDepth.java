package com.botijasoftware.utils.materials.passoptions;

import android.opengl.GLES10;

import com.botijasoftware.utils.materials.Material;

public class PassOptionMaskDepth extends PassOption {

	@Override
	public void set(Material material) {
		GLES10.glDepthMask(true);
	}

	@Override
	public void unSet() {
		GLES10.glDepthMask(false);
	}

}