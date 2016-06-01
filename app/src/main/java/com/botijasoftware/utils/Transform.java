package com.botijasoftware.utils;

import com.botijasoftware.utils.renderer.Renderer;

import android.opengl.Matrix;

public class Transform {
	public Vector3 translation;
	public Quaternion rotation;
	public Vector3 scale;
	protected GLMatrix matrix = new GLMatrix();
	
	
	public Transform(Vector3 translation, Quaternion rotation, Vector3 scale) {
		this.translation = translation;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	public Transform(Vector3 translation) {
		this.translation = translation;
		this.rotation = Quaternion.ZERO.clone();
		this.scale = Vector3.ZERO.clone();
	}
	
	public Transform() {
		this.translation = new Vector3();
		this.rotation = new Quaternion();
		this.scale = new Vector3();
	}
	
	public Transform clone() {
		return new Transform(translation.clone(), rotation.clone(), scale.clone() );
	}
	
	public void setTransform(Transform transform) {
		this.translation.setValue(transform.translation);
		this.rotation.setValue(transform.rotation);
		this.scale.setValue(transform.scale);
	}
	
	private void generateMatrix() {
		Matrix.setIdentityM(Renderer.scratch0.matrix, 0);
		Matrix.translateM(Renderer.scratch0.matrix, 0, translation.X, translation.Y, translation.Z);
		rotation.getMatrix(Renderer.scratch1.matrix);		
		Matrix.multiplyMM(matrix.matrix, 0, Renderer.scratch1.matrix, 0, Renderer.scratch0.matrix, 0);
		Matrix.scaleM(matrix.matrix, 0, scale.X, scale.Y, scale.Z);
	}
	
}