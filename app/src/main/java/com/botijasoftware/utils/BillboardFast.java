package com.botijasoftware.utils;

import com.botijasoftware.utils.renderer.Renderer;

public class BillboardFast extends Billboard {
	
	public BillboardFast(Texture texture, Vector3 position, Vector2 size) {
		super(texture, position, size, false);
	}
	
	public BillboardFast(Texture texture, Vector3 position, Vector2 size, boolean flip) {
		super(texture, position, size, flip);
	}

	public void Transform() {
		
		//GLES10.glTranslatef(position.X, position.Y, position.Z);
		Renderer.modelview.translate(position.X, position.Y, position.Z);
	}

}