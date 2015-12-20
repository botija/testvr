package com.botijasoftware.utils;

import com.botijasoftware.utils.renderer.Renderer;

public class BillboardSpherical extends Billboard{
	
	public BillboardSpherical(Texture texture, Vector3 position, Vector2 size) {
		super (texture, position, size, false);
	}
	
	public BillboardSpherical(Texture texture, Vector3 position, Vector2 size, boolean flip) {
		super (texture, position, size, flip);
	}

	@Override
	public void Update(float time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void LoadContent(ResourceManager resources) {
		// TODO Auto-generated method stub
		
	}

	/*@Override
	public void Draw(GL10 gl) {
		
		//GLES10.glPushMatrix();
		MaterialRenderer.pushModelViewMatrix();
		
		Transform();
		
		GLES10.glBindTexture(GLES10.GL_TEXTURE_2D, mTexture.getID() );
		MaterialRenderer.loadModelViewMatrix();
		mVertexBuffer.Draw(gl, mIndexBuffer);
		MaterialRenderer.popModelViewMatrix();
		MaterialRenderer.modelview.loadMatrix();
		//GLES10.glPopMatrix();
		
	};*/
	
	@Override
	public void Transform() {
		
		//GLES10.glTranslatef(position.X, position.Y, position.Z);
		Renderer.modelview.translate(position.X, position.Y, position.Z);
		float[] matrix = Renderer.modelview.matrix;
		
		//GLES11.glGetFloatv(GLES11.GL_MODELVIEW_MATRIX, matrix, 0);
		matrix[0] = 1.0f;
		matrix[1] = 0.0f;
		matrix[2] = 0.0f;
		
		matrix[4] = 0.0f;
		matrix[5] = 1.0f;
		matrix[6] = 0.0f;
		
		matrix[8] = 0.0f;
		matrix[9] = 0.0f;
		matrix[10] = 1.0f;
		
		//GLES11.glLoadMatrixf(matrix, 0);
		//GLES10.glRotatef(rotation, 0, 0, 1);
		//GLES10.glScalef(scale.X, scale.Y, scale.Z);
		Renderer.modelview.rotate(rotation, 0, 0, 1);
		Renderer.modelview.scale(scale.X, scale.Y, scale.Z);
		
	}


}