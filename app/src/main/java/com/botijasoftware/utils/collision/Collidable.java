package com.botijasoftware.utils.collision;

import com.botijasoftware.utils.collision.CollisionVolume;


public interface Collidable {
	boolean collides(CollisionVolume c);
}