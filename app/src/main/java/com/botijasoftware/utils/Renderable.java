package com.botijasoftware.utils;

public interface Renderable {

	
	void Update(float time);
	void LoadContent(ResourceManager resources);
	void Draw();
	void freeContent(ResourceManager resources);
	void scale(float x, float y);
	void move(float x, float y);
	
}