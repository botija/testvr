package com.botijasoftware.utils.animation;

import java.util.ArrayList;

public class Skeleton {

	public ArrayList<Animation> animations = new ArrayList<Animation>();
	public ArrayList<Bone> bones = new ArrayList<Bone>();
	public Bone rootBone;
	public Animation currentAnimation;
	
	public void Update(float time) {}

	public Animation findAnimation(String animationname) {
		for (int i=0;i<animations.size();i++) {
			Animation a = animations.get(i); 
			if (a.name.equalsIgnoreCase(animationname)) {
				return a;
			}
		}
		return null;
	}
	
	public Bone findBone(String bonename) {
		for (int i=0;i<bones.size();i++) {
			Bone b = bones.get(i); 
			if (b.name.equalsIgnoreCase(bonename)) {
				return b;
			}
		}
		return null;
	}
	
}