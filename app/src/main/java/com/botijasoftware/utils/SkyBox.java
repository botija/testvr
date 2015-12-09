package com.botijasoftware.utils;

import javax.microedition.khronos.opengles.GL10;
import com.botijasoftware.utils.renderer.Renderer;
import android.opengl.GLES20;

public class SkyBox implements Renderable {

	private Vector3 position;
	private VertexBuffer mVertexBuffer;
	private IndexBuffer mIndexBuffer;
	private float size;
	public final static int SKYBOX_FRONT = 0;
	public final static int SKYBOX_BACK = 1;
	public final static int SKYBOX_LEFT = 2;
	public final static int SKYBOX_RIGHT = 3;
	public final static int SKYBOX_TOP = 4;
	public final static int SKYBOX_BOTTOM = 5;
	// private Texture[] textures = new Texture[6];
	float tmptex[] = new float[24 * 2];
	private Texture mTexture;
	private final static short data[] = { 0, 1, 2, 0, 2, 3, 4, 5, 6, 4, 6, 7,
			8, 9, 10, 8, 10, 11, 12, 13, 14, 12, 14, 15, 16, 17, 18, 16, 18,
			19, 20, 21, 22, 20, 22, 23 };

	public SkyBox(Vector3 position, float size, Texture texture) {

		this.size = size;
		this.position = position;

		VertexBufferDeclaration vbd = new VertexBufferDeclaration();
		vbd.add(new VertexBufferDefinition(VertexBufferDefinition.FLOAT,
				VertexBufferDefinition.DEF_VERTEX, 3,
				VertexBufferDefinition.ACCESS_DYNAMIC));
		vbd.add(new VertexBufferDefinition(VertexBufferDefinition.FLOAT,
				VertexBufferDefinition.DEF_TEXTURE_COORD, 2,
				VertexBufferDefinition.ACCESS_DYNAMIC));

		mVertexBuffer = new VertexBuffer(24, vbd);
		mIndexBuffer = new IndexBufferShort(GLES20.GL_TRIANGLES, 36);

		mIndexBuffer.put(data);
		changeTexture(texture);
		createVertex();
	}

	public void changeTexture(Texture texture) {
		mTexture = texture;

		TextureCoords coords = mTexture.getTextureCoords();
		for (short i = 0; i < 24 * 2; i += 8) {
			tmptex[i] = coords.s0;
			tmptex[i + 1] = coords.t0;
			tmptex[i + 2] = coords.s1;
			tmptex[i + 3] = coords.t0;
			tmptex[i + 4] = coords.s0;
			tmptex[i + 5] = coords.t1;
			tmptex[i + 6] = coords.s1;
			tmptex[i + 7] = coords.t1;
		}
		
		mVertexBuffer.getBuffer(1).put(tmptex);
	}

	public void createVertex() {

		float data[] = { -size, +size, -size, -size, +size, +size, -size,
				-size, +size, -size, -size, -size,

				+size, +size, -size, +size, -size, -size, +size, -size, +size,
				+size, +size, +size,

				-size, +size, -size, +size, +size, -size, +size, +size, +size,
				-size, +size, +size,

				-size, +size, +size, +size, +size, +size, +size, -size, +size,
				-size, -size, +size,

				-size, -size, +size, +size, -size, +size, +size, -size, -size,
				-size, -size, -size,

				+size, +size, -size, -size, +size, -size, -size, -size, -size,
				+size, -size, -size };

		mVertexBuffer.getBuffer(0).put(data);

	}

	public void Draw(GL10 gl) {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture.getID());
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glDisable(GLES20.GL_CULL_FACE);
        //GLES20.glDisable(GLES20.GL_LIGHTING);
		//GLES10.glPushMatrix();
		//GLES10.glTranslatef(position.X, position.Y, position.Z);
		Renderer.pushModelViewMatrix();		
		Renderer.modelview.translate(position.X, position.Y, position.Z);
		Renderer.loadModelViewMatrix();
		mVertexBuffer.Draw(gl, mIndexBuffer);
		//GLES10.glPopMatrix();
		Renderer.popModelViewMatrix();
		Renderer.loadModelViewMatrix();
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        //GLES20.glEnable(GLES20.GL_LIGHTING);
	}

	public void Update(float time) {

	}

	public void LoadContent(GL10 gl, ResourceManager resources) {

	}

	public void move(float x, float y) {}

	public void scale(float x, float y) {}

	public void freeContent(GL10 gl, ResourceManager resources) {
		// TODO Auto-generated method stub
		
	}


}