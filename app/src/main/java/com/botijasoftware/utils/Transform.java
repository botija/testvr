package com.botijasoftware.utils;

import com.botijasoftware.utils.renderer.Renderer;

import android.opengl.Matrix;

public class Transform {
	public Vector3 translation;
	public Quaternion rotation;
	public Vector3 scale;
	protected GLMatrix matrix = new GLMatrix();
    protected static GLMatrix translationmatrix = new GLMatrix();
    protected static GLMatrix rotationmatrix = new GLMatrix();
    protected static GLMatrix scalematrix = new GLMatrix();
    protected static GLMatrix scratchmatrix = new GLMatrix();
	
	
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
	
	public GLMatrix generateMatrix() {
        Matrix.translateM( translationmatrix.matrix, 0, translation.X, translation.Y, translation.Z);
        rotation.getMatrix( rotationmatrix.matrix );
        Matrix.scaleM(scalematrix.matrix, 0, scale.X, scale.Y, scale.Z);
        Matrix.multiplyMM(scalematrix.matrix, 0, scalematrix.matrix, 0, rotationmatrix.matrix, 0);
        Matrix.multiplyMM(matrix.matrix, 0, scratchmatrix.matrix, 0, translationmatrix.matrix, 0);

        return matrix;
	}

	public GLMatrix getTransformMatrix() {
		return matrix;
	}
	
}