package com.botijasoftware.utils.animation;

import java.util.ArrayList;


public class Track {
	
	public Bone bone;
	public ArrayList<Keyframe> keyframes = new ArrayList<Keyframe>();

	public Track(Bone bone) {
		this.bone = bone;

	}
	
	public void transformBone(Keyframe keyframe) {
		keyframe.transformBone(bone);
	}
}


