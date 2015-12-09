package com.botijasoftware.utils.collision;

public class Collision {
	protected Collidable obj1, obj2;
	
	public Collision(Collidable obj1, Collidable obj2) {
		this.obj1 = obj1;
		this.obj2 = obj2;
	}
}