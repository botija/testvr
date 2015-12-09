package com.botijasoftware.utils.materials.passoptions;

import android.opengl.GLES10;

import com.botijasoftware.utils.materials.Material;

public class PassOptionMaskGreen extends PassOption {

	@Override
	public void set(Material material) {
		GLES10.glColorMask(true, false, true, true);
	}

	@Override
	public void unSet() {
		GLES10.glColorMask(true, true, true, true);
	}

}