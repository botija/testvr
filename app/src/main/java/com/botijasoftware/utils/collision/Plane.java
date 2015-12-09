package com.botijasoftware.utils.collision;

import com.botijasoftware.utils.Vector3;

public class Plane {
	
	
	public Plane(Vector3 n, float d) {
		mNormal = new Vector3(n.X, n.Y, n.Z);
		mDistance = d;
	}
	
	public Plane(float a, float b, float c, float d) {
		mNormal = new Vector3(a, b, c);
		mDistance = d;
	}
	
    public Plane(Vector3 p0, Vector3 p1, Vector3 p2) {
        Vector3 v0 = new Vector3(p1.X - p0.X, p1.Y - p0.Y, p1.Z - p0.Z);
        Vector3 v1 = new Vector3(p2.X - p0.X, p2.Y - p0.Y, p2.Z - p0.Z);
        mNormal = v0.cross( v1 );
        mDistance = mNormal.length();
        mNormal.normalize();
    }

	public void normalize() {
        float invlen = 1.0f / mNormal.length();
        mNormal.mul(invlen);
        mDistance *= invlen;
	}
	
	public Vector3 getNormal() {
		return mNormal;
	}
	
	public float getDistance() {
		return mDistance;
	}
	
    public float getDistance(Vector3 point) {
        return mNormal.dot(point) + mDistance;
    }

    float getDistance2(Vector3 point)  {
        float d = getDistance(point);
        return d * d;
	}
	
    public int getSide(Vector3 point) {
        float d = getDistance(point);
        if (d < 0.0f) return SIDE_NEGATIVE;
        else if (d > 0.0f) return SIDE_POSITIVE;
        else return SIDE_INSIDE;
    }

    public boolean inHalfSpace(Vector3 point) {
        return ( getDistance(point) >= 0.0f );
    }
	
	public Vector3 mNormal;
	public float mDistance;
	public final static int SIDE_INSIDE = 0;
	public final static int SIDE_POSITIVE = 1;
	public final static int SIDE_NEGATIVE = 2;

}


        
