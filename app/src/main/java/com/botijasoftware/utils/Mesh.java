package com.botijasoftware.utils;

import android.opengl.GLES20;
import com.botijasoftware.utils.materials.Material;

public class Mesh {
	
	public Mesh(String name, Texture texture, VertexBuffer vb, IndexBuffer ib) {
		mName = name;
		mVertexBuffer = vb;
		mIndexBuffer = ib;
		mTexture = texture;
	}

	public void LoadContent(ResourceManager resources) {}

	public void Draw() {
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture.getID());
		mVertexBuffer.Draw(mIndexBuffer);
	}
	
		
	public VertexBuffer mVertexBuffer;
	public IndexBuffer mIndexBuffer;	
	public Texture mTexture;
	public String mName;
	public Material mMaterial = null;
}
