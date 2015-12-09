package com.botijasoftware.utils.animation;

import com.botijasoftware.utils.Vector3;

public class Keyframe {

	public float time;
	public Vector3 translate;
	public float angle;
	public Vector3 axis;
	public Vector3 scale;

	public Keyframe(float time) {
		this.time = time;
	}

	public void setTranslate(float x, float y, float z) {
		translate = new Vector3(x,y,z);

	}

	public void setScale(float x, float y, float z) {
		scale = new Vector3(x,y,z);

	}

	public void setRotation(float angle, float x, float y, float z) {
		this.angle = angle;
		axis = new Vector3(x, y, z);
	}

	public void transformBone(Bone bone) {
		bone.transmformMatrix(translate, axis, (float)(angle*Math.PI/180.0f), scale); 	
	}

}
