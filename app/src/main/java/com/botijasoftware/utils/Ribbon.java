package com.botijasoftware.utils;

import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import com.botijasoftware.utils.renderer.Renderer;
import android.opengl.GLES20;


public class Ribbon implements Renderable {
	private VertexBuffer mVertexBuffer;
	private IndexBuffer mIndexBuffer;
	private int maxSegments;
	private Texture texture;
	private int activeSegments = 0;
	private ArrayList<Vector3> vertex = new ArrayList<Vector3>();
	private boolean needsreload = true;
	private int maxvertex;

	public Ribbon(int maxsegments, Texture texture) {
		this.maxSegments = maxsegments;
		this.texture = texture;
		maxvertex = maxSegments * 2 + 2;
	}

	public void Update(float time) {

		if (needsreload) {
			recalcVertexBuffer();
			needsreload = false;
		}

	}

	public void LoadContent(ResourceManager resources) {
		VertexBufferDeclaration vbd = new VertexBufferDeclaration();
		vbd.add(new VertexBufferDefinition(VertexBufferDefinition.FLOAT,
				VertexBufferDefinition.DEF_VERTEX, 3,
				VertexBufferDefinition.ACCESS_DYNAMIC));

		vbd.add(new VertexBufferDefinition(VertexBufferDefinition.FLOAT,
				VertexBufferDefinition.DEF_TEXTURE_COORD, 2,
				VertexBufferDefinition.ACCESS_DYNAMIC));

		//mVertexBuffer = new VertexBuffer(maxvertex, vbd);
		//mIndexBuffer = new IndexBufferShort(GLES20.GL_TRIANGLE_STRIP, maxvertex);
		mVertexBuffer = Renderer.vbManager.requestVB(vbd, maxvertex);
		mIndexBuffer = Renderer.ibManager.requestIB(GLES20.GL_TRIANGLE_STRIP, GLES20.GL_UNSIGNED_SHORT, maxvertex);

		mIndexBuffer.position(0);

		mIndexBuffer.put(0);
		mIndexBuffer.put(1);
		mIndexBuffer.put(2);
		mIndexBuffer.put(3);
		//mIndexBuffer.reset();

		for (int i = 2; i < maxSegments; i++) {
			mIndexBuffer.put(i * 2);
			mIndexBuffer.put(i * 2+1);
		}
		mIndexBuffer.position(0);

		mIndexBuffer.setSize(0);

		TextureCoords coords = texture.getTextureCoords();
		
		float stamount = (coords.s1 - coords.s0) / maxSegments;

		ArrayBuffer b = mVertexBuffer.getBuffer(1); // texture buffer
		for (int i = 0; i <maxSegments+1; i++) {
			float s = coords.s0 + i * stamount;
			int index = i * 4;
			b.put(index++, s);
			b.put(index++, coords.t0);
			b.put(index++, s);
			b.put(index, coords.t1);
		}
		b.position(0);
		
	}
	
	public void freeContent(ResourceManager resources) {
		Renderer.vbManager.freeVB(mVertexBuffer);
		Renderer.ibManager.freeIB(mIndexBuffer);
	}

	@Override
	public void scale(float x, float y) {

	}

	@Override
	public void move(float x, float y) {

	}


	private void recalcVertexBuffer() {

		mIndexBuffer.setSize(activeSegments*2);
		ArrayBuffer b = mVertexBuffer.getBuffer(0); // vertex buffer
		for (int i = 0; i < vertex.size(); i++) {
			Vector3 v = vertex.get(i);
			int index = i * 3;
			b.put(index++, v.X);
			b.put(index++, v.Y);
			b.put(index, v.Z);
		}
		b.position(0);

	}

	public void Draw() {

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture.getID());
		mVertexBuffer.Draw(mIndexBuffer);

	}
	

	public void addSegment(Vector3 v0, Vector3 v1) {

		if (activeSegments >= maxSegments) {
			vertex.remove(0);
			vertex.remove(0);
		} else {
			if (vertex.size()>1)
				activeSegments++;
		}
		
		vertex.add(v0);
		vertex.add(v1);
		needsreload = true;
	} 

}