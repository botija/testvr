package com.botijasoftware.utils.collision;

import com.botijasoftware.utils.Vector3;

public class Ray {
	
	public Vector3 mDirection;
	public Vector3 mOrigin;

	
	public Ray() {}

	public Ray(Vector3 origin, Vector3 direction) {
		mOrigin = origin;
		mDirection = direction;
	}

	public  void RayFromPoints(Vector3 v0, Vector3 v1) {
		mOrigin = v0;
		mDirection = v1.clone().sub(v0);
		mDirection.normalize();
	}
	
	public boolean collides(Sphere s) {
		
		
		float a = mDirection.dot(mDirection);
	    float b = 2 * mDirection.dot(mOrigin);
	    float c = mOrigin.dot(mOrigin) - (s.radius* s.radius);

	    
	    float disc = b * b - 4 * a * c;
	    
	    if (disc < 0)
	        return false;
	    
	    float distSqrt = (float)Math.sqrt(disc);
	    float q;
	    if (b < 0)
	        q = (-b - distSqrt)/2.0f;
	    else
	        q = (-b + distSqrt)/2.0f;

	    // compute t0 and t1
	    float t0 = q / a;
	    float t1 = c / q;

	    // make sure t0 is smaller than t1
	    if (t0 > t1)
	    {
	        // if t0 is bigger than t1 swap them around
	        float temp = t0;
	        t0 = t1;
	        t1 = temp;
	    }

	    // if t1 is less than zero, the object is in the ray's negative direction
	    // and consequently the ray misses the sphere
		return t1 >= 0;

	}
}