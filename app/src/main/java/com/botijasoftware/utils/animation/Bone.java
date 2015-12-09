package com.botijasoftware.utils.animation;

import android.opengl.Matrix;

import com.botijasoftware.utils.Vector3;

public class Bone {

	public Bone parent = null;
	public String name;
	public int id;
	public Vector3 position;
	public float rotationangle; //in radians
	public Vector3 rotationaxis;
	private float[] transformmatrix = new float[16];

	public Bone(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public void setPosition(float x, float y, float z) {
		position = new Vector3(x,y,z);

	}

	public void setRotation(float angle, float x, float y, float z) {
		rotationangle = angle;
		rotationaxis = new Vector3(x, y, z);
	}
	
	public void transmformMatrix(final Vector3 translate, final Vector3 rotateaxis, final float rotateangle, final Vector3 scale) {
		Matrix.setIdentityM(transformmatrix, 0);
		Matrix.rotateM(transformmatrix, 0, rotateangle, rotateaxis.X, rotateaxis.Y, rotateaxis.Z);
		Matrix.translateM(transformmatrix, 0, translate.X, translate.Y, translate.Z);
		Matrix.scaleM(transformmatrix, 0, scale.X, scale.Y, scale.Z);
	}

}
