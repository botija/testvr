package com.botijasoftware.utils;

import com.botijasoftware.utils.renderer.Renderer;

import android.opengl.GLES20;

public class Billboard implements Renderable {
	protected static float[] matrix = new float[16];
	public Vector3 position;
	protected Vector2 size;
	public Vector3 scale = new Vector3(1,1,1);
	protected Texture mTexture;
	protected VertexBuffer mVertexBuffer;
	protected IndexBuffer mIndexBuffer;
	protected boolean flip;
	public float camdistance = 0.0f;
	protected float rotation;
	protected Vector3 rotation_axis = new Vector3(0,0,1);
	private final static short indexdata [] = {0,1,3,2};
	private float alpha = 1.0f;
	
	
	public Billboard(Texture texture, Vector3 position, Vector2 size) {
		this(texture, position, size, false);
	}
	
	public Billboard(Texture texture, Vector3 position, Vector2 size, boolean flip) {
		
		this.mTexture = texture;
		this.position = position;
		this.size = size;
		this.flip = flip;
		VertexBufferDeclaration vbd = new VertexBufferDeclaration();
		vbd.add(new VertexBufferDefinition(VertexBufferDefinition.FLOAT, VertexBufferDefinition.DEF_VERTEX,3,VertexBufferDefinition.ACCESS_DYNAMIC));
		vbd.add(new VertexBufferDefinition(VertexBufferDefinition.FLOAT, VertexBufferDefinition.DEF_TEXTURE_COORD,2,VertexBufferDefinition.ACCESS_DYNAMIC));
		
		mVertexBuffer = Renderer.vbManager.requestVB(vbd, 4);
		mIndexBuffer = Renderer.ibManager.requestIB(GLES20.GL_TRIANGLE_FAN, GLES20.GL_UNSIGNED_SHORT, 4);
		//mVertexBuffer = new VertexBuffer(4,vbd);
		//mIndexBuffer = new IndexBufferShort(GLES10.GL_TRIANGLE_FAN, 4);

		mIndexBuffer.put(indexdata);
	
		setTexture(mTexture);
		setSize(this.size);
	}

	public void Update(float time) {
		// TODO Auto-generated method stub
		
	}

	public void LoadContent(ResourceManager resources) {
		// TODO Auto-generated method stub
		
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


	public void Draw() {
		
		//GLES10.glPushMatrix();
		Renderer.pushModelViewMatrix();
		
		Transform();
		
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture.getID() );
		
		Renderer.loadModelViewMatrix();
		//GLES20.glColor4f(1.0f, 1.0f, 1.0f, alpha);
		mVertexBuffer.Draw(mIndexBuffer);
		//GLES20.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		Renderer.popModelViewMatrix();
		Renderer.loadModelViewMatrix();
		//GLES10.glPopMatrix();
		
	}

	public void Transform() {
		
		Renderer.modelview.translate(position.X, position.Y, position.Z);
		Renderer.modelview.rotate(rotation, rotation_axis.X, rotation_axis.Y, rotation_axis.Z);
		Renderer.modelview.scale(scale.X, scale.Y, scale.Z);
		//GLES10.glTranslatef(position.X, position.Y, position.Z);		
		//GLES10.glRotatef(rotation, rotation_axis.X, rotation_axis.Y, rotation_axis.Z);
		//GLES10.glScalef(scale.X, scale.Y, scale.Z);
		
	}


	public void setTexture(Texture texture) {
		mTexture = texture;
		
		TextureCoords coords = mTexture.getTextureCoords();
		
		if (flip) {
			coords.flipVertical();
		}
		
		float stdata [] = { coords.s0, coords.t0, coords.s1, coords.t0, coords.s0, coords.t1, coords.s1, coords.t1 };
		mVertexBuffer.getBuffer(1).put(stdata);
	}
	
	public void setTexture(Texture texture, boolean flip) {
		
		this.flip = flip;
		setTexture(mTexture);	
	}
	
	public void setSize(float x, float y) {
		this.size.X = x;
		this.size.Y = y;
		float hw = x / 2.0f;
		float hh = y / 2.0f;
		float vertexdata [] = { -hw, -hh, 0.0f, hw, -hh, 0.0f, -hw, hh, 0.0f, hw, hh, 0.0f};
		mVertexBuffer.getBuffer(0).put(vertexdata);
	}
	
	public void setSize(Vector2 size) {
		
		setSize(size.X, size.Y);
	}
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float calcDistance(Vector3 camera) {
		camdistance = camera.distance(position);
		return camdistance;
	}
	
	public float calcDistancesq(Vector3 camera) {
		camdistance = camera.distancesq(position);
		return camdistance;
	}
	
	public void setRotationAxis(Vector3 axis) {
		rotation_axis = axis;
	}

	public void setRotationAxis(float axis_x, float axis_y, float axis_z) {
		rotation_axis.setValue(axis_x, axis_y, axis_z);
	}
	
	public void setAlpha(float alpha) {
		this.alpha = alpha;
		if (this.alpha > 1.0f)
			this.alpha = 1.0f;
		else if (this.alpha < 0.0f)
			this.alpha = 0.0f;
	}
	
	public float getAlpha() {
		return alpha;
	}
	
}