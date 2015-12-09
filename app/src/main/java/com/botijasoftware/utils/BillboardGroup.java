package com.botijasoftware.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.microedition.khronos.opengles.GL10;
import com.botijasoftware.utils.renderer.Renderer;
import android.opengl.GLES20;

public class BillboardGroup implements Renderable {
	
	private ArrayList<Billboard> billboards = new ArrayList<Billboard>();
	private boolean issorted = false;
	private Camera camera;
	
	public BillboardGroup(Camera camera) {
		this(camera, false);
	}
	
	public BillboardGroup(Camera camera, boolean sort) {
		this.camera = camera;
		this.issorted = sort;
	}
	

	public void Update(float time) {
		if (issorted)
			sort(camera.getEye());
	}

	public void LoadContent(GL10 gl, ResourceManager resources) {
		// TODO Auto-generated method stub
		
	}
	
	public void freeContent(GL10 gl, ResourceManager resources) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scale(float x, float y) {

	}

	@Override
	public void move(float x, float y) {

	}


	public void Draw(GL10 gl) {
		
		int textureid = -1;
		int size = billboards.size();
		if (size > 0) {
			Billboard b = billboards.get(0); 
			textureid = b.mTexture.getID();
			b.mVertexBuffer.enableState(gl);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureid );
		}
		else
			return;

		for (int i=0; i < size; i++) {
			Billboard b = billboards.get(i);
			
			if (textureid != b.mTexture.getID()) {
				textureid = b.mTexture.getID();
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureid );
			}
			
			//GLES20.glPushMatrix();
			Renderer.pushModelViewMatrix();
			b.Transform();
		
			Renderer.loadModelViewMatrix();
			b.mVertexBuffer.fastDraw(gl, b.mIndexBuffer);
			//GLES20.glPopMatrix();
			Renderer.popModelViewMatrix();
			Renderer.loadModelViewMatrix();
		}
		if (size > 0)
			billboards.get(0).mVertexBuffer.disableState(gl);
		
	}
	
	
	public void addBillboard(Billboard b) {
		billboards.add(b);
	}
	
	public void removeBillboard(Billboard b) {
		billboards.remove(b);
	}
	
	public void sort(Vector3 camera) {
		for (int i=0; i< billboards.size(); i++) {
			billboards.get(i).calcDistancesq(camera);
		}
		
        Collections.sort(billboards, new Comparator<Billboard>(){
        	
			public int compare(Billboard one, Billboard two){
				
				if (one.camdistance > two.camdistance)
					return 1;
				else if (two.camdistance > one.camdistance)
					return -1;
				else 
					return 0;
			} 
		});
	}
	
}