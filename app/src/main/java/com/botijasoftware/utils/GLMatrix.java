package com.botijasoftware.utils;

import android.opengl.Matrix;

public class GLMatrix {
	
	public float matrix[] = new float[16];
	private float vector[] = new float[4];
	private float vectorout[] = new float[4];
	
	public void rotate(float angle, float x, float y, float z) {
		Matrix.rotateM(matrix, 0, angle, x, y, z);
	}
	
	public void translate(float x, float y, float z) {
		Matrix.translateM(matrix, 0, x, y, z);
	}
	
	public void scale(float x, float y, float z) {
		Matrix.scaleM(matrix, 0, x, y, z);
	}
	
	public void setLookAt(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {
			Matrix.setLookAtM(matrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }
	
	public void save(GLMatrix m) {
		System.arraycopy(matrix, 0, m.matrix, 0, 16);
	}
	
	public void restore(GLMatrix m) {
		System.arraycopy(m.matrix, 0, matrix, 0, 16);
	}
	
	public void loadIdentity() {
		Matrix.setIdentityM(matrix, 0);
	}
	
	public void multMatrix(GLMatrix m1, GLMatrix m2) {
		Matrix.multiplyMM(matrix, 0, m1.matrix, 0, m2.matrix, 0);
	}
	
	public void invert(GLMatrix m) {
		Matrix.invertM(matrix, 0, m.matrix, 0);
	}
	
	public void loadMatrix() {

        //GLES10.glLoadMatrixf(matrix, 0);

	}
	
	public Vector3 transform(Vector3 v) {
		vector[0] = v.X;
		vector[1] = v.Y;
		vector[2] = v.Z;
		vector[3] = 1;
		Matrix.multiplyMV(vectorout, 0, matrix, 0, vector, 0);
		Vector3 out = new Vector3(vectorout[0], vectorout[1], vectorout[2]);
		out.div(vectorout[3]);
		return out;
	}
}