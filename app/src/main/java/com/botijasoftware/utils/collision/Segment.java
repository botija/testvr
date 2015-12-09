package com.botijasoftware.utils.collision;

import com.botijasoftware.utils.Vector3;

public class Segment {
	public Vector3 p0, p1;
	private Vector3 tmp0 = new Vector3(0), tmp1 = new Vector3(0);
	private Vector3 projection = new Vector3(0);
	
	public Segment(Vector3 p0, Vector3 p1) {
		this.p0 = p0;
		this.p1 = p1;
	}
	
	public void set(Vector3 p0, Vector3 p1) {
		this.p0 = p0;
		this.p1 = p1;
	}
	
	public float distance(Vector3 p) {
		
		float l2 = p0.distancesq(p1);
		if (l2 == 0.0f) {
			return p0.distance(p);
		}
		
		tmp0.setValue(p);
		tmp0.sub(p0);
		tmp1.setValue(p1);
		tmp1.sub(p0);
		float t = tmp0.dot(tmp1) / l2;
		
		if (t <= 0.0) return p0.distance(p);       // Beyond the 'p0' end of the segment
		else if (t > 1.0) return p1.distance(p);  // Beyond the 'p1' end of the segment
		
		projection.setValue(p0);
		tmp1.mul(t);
		projection.add(tmp1);
		
		return projection.distance(p);
	}
	
	public float distancesq(Vector3 p) {
		
		float l2 = p0.distancesq(p1);
		if (l2 == 0.0f) {
			return p0.distancesq(p);
		}
		
		tmp0.setValue(p);
		tmp0.sub(p0);
		tmp1.setValue(p1);
		tmp1.sub(p0);
		float t = tmp0.dot(tmp1) / l2;
		
		if (t <= 0.0) return p0.distancesq(p);       // Beyond the 'p0' end of the segment
		else if (t > 1.0) return p1.distancesq(p);  // Beyond the 'p1' end of the segment
		
		projection.setValue(p0);
		tmp1.mul(t);
		projection.add(tmp1);
		
		return projection.distancesq(p);
	}
	
}