package com.botijasoftware.utils;

import javax.microedition.khronos.opengles.GL10;

public interface Renderable {

	
	public void Update(float time);	
	public void LoadContent(GL10 gl, ResourceManager resources);
	public void Draw(GL10 gl);
	public void freeContent(GL10 gl, ResourceManager resources);
	public void scale(float x, float y);
	public void move(float x, float y);
	
}