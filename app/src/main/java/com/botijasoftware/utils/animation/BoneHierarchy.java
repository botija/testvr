package com.botijasoftware.utils.animation;

import java.util.ArrayList;

public class BoneHierarchy {

	public class BoneNode {

		public BoneNode parent;
		public Bone bone;
		public ArrayList<BoneNode> children = new ArrayList<BoneNode>();

		public BoneNode(Bone bone) {
			this.bone = bone;
		}

		public void addChilren(Bone bone) {

			BoneNode node = new BoneNode(bone);
			node.parent = this;
			children.add(node);
		}

	}

	public BoneNode root;

	public Bone findBone(String bonename) {

		return null;
	}
}
