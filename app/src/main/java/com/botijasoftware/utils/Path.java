package com.botijasoftware.utils;

import java.util.ArrayList;

import com.botijasoftware.utils.curves.Catmull3;

public class Path {
	
	private Catmull3 path;
	private ArrayList<Vector3> points = new ArrayList<Vector3>();
	private int pointindex = 0;
	private float pathtime = 0.0f;
	private float pathspeed = 1.0f;
	private ArrayList<Vector3> pathsimulation = new ArrayList<Vector3>();
	private Vector3 position;
	private Vector3 startpoint;
	private Vector3 direction = new Vector3(0,0,0);
	private boolean onpath = false;
	public float units_per_second = 20.0f;
	public boolean path_closed = false;
	private Vector3 scratch0 = new Vector3(0,0,0); //scratch vectors for temp operations
	private Vector3 scratch1 = new Vector3(0,0,0);
	private Vector3 scratch2 = new Vector3(0,0,0);
	private boolean simpath = true;
	
	
	
	public Path(Vector3 start) {
		
		points.add(start);
		startpoint = start;
		position = startpoint.clone();
		pointindex = 3;
		path = null;
	}
	
	public void enableSimulatePath() {
		simpath = true;
	}
	
	public void disableSimulatePath() {
		simpath = false;
	}
	
	
	private void setPathSpeed() {
		Vector3 v0 = points.get(pointindex-2);
		Vector3 v1 = points.get(pointindex-1);
		pathspeed = units_per_second / v0.distance(v1);
	}
	
	public void Update(float time) {
		
		pathtime += time * pathspeed;
		
		if (path == null) {
			if (pointindex < points.size()-1) {
				setPathSpeed();
				path = new Catmull3( points.get(pointindex-3), points.get(pointindex-2), points.get(pointindex-1), points.get(pointindex));
			}
			pathtime = 0.0f;
			onpath = true;
		}
		
		while (pathtime >= 1.0f && path!=null && onpath) {
			if (pointindex < points.size()-1) {
				pointindex++;
				setPathSpeed();
			
				path.set( points.get(pointindex-3), points.get(pointindex-2), points.get(pointindex-1), points.get(pointindex));
				pathtime -= 1.0f;
				if (pathtime<=1.0f)
					simulatePath();
			}
			else
				onpath = false;
		}
		
		if (onpath && path!=null) {
			path.interpolate(pathtime, position);
			path.interpolateTangent(pathtime, direction);
		}

	}
	
	public Vector3 getCurrentPosition() {
			return position;
	}
	
	public Vector3 getCurrentDirection() {
			return direction; 
	}
	
	public Vector3 getNextPoint() {
		if (pointindex <= (points.size() -1))
			return points.get(pointindex);
		else
			return points.get(points.size() -1);
	}
	
	public boolean isOnPath() {
		return onpath;
	}
	
	public void addPath(Vector3 v) {
		
		if (points.isEmpty())
			points.add(v);
		else {
			Vector3 p0 = points.get(points.size()-1);
			float angle = checkAngle(v);
			if (p0.distancesq(v) >= 625 || angle > 20.0f) {
				if (angle > 15)
					points.add(v);
				else {
					points.add(straight(v));
				}
				simulatePath();
			}
			
		}
	}
	
	private Vector3 straight(Vector3 v) {
		int size = points.size();
		if (size > 1) {

			scratch0.setValue(points.get(size-2));
			scratch1.setValue(points.get(size-1));
			float d = scratch0.distance(v);
			float d2 = scratch1.distance(v);
			return scratch1.mul(d2-d).clone();
		}
		return v;
	}
	
	private float checkAngle(Vector3 v) {
		int size = points.size();
		if (size > 1) {
			return getAngle(points.get(size-2),points.get(size-1),v); 
		}
		else
			return 180.0f;
	}
	
	private float getAngle(Vector3 v0, Vector3 v1, Vector3 v2) {
		scratch0.setValue(v2);
		scratch1.setValue(v2);
		scratch0.sub(v0);
		scratch1.sub(v1);
		return scratch0.dot(scratch1);		
	}
	
	private void simplifyPath() {
		
		for (int i=2; i< points.size();i++) {
			scratch0.setValue(points.get(i-2));
			scratch1.setValue(points.get(i-1));
			scratch2.setValue(points.get(i));
			float angle = getAngle(scratch0, scratch1, scratch2);
			if (angle < 10) {
				points.remove(i-1);
				i--;
			}
		}
	}
	
	public void clearPath() {
		points.clear();
		points.add(startpoint);
		path = null;
		pointindex = 3;
	}
	
	public void closePath() {
		int size = points.size();
		
		if (size > 3 ) {
			Vector3 v = points.get(size-1); //duplicate last point
			points.add(v.clone());
		}
		else {
			points.add(startpoint);
		}
		//simplifyPath();
		simulatePath();
		path_closed = true;
	}
	
	public void simulatePath() {
		if (!simpath)
			return;
		
		pathsimulation.clear();
		
		
		if (points.size() < 3)
			return;
		
		float t = 0.0f;
		float ps = 1.0f;
		Vector3 v = new Vector3(0, 0, 0);
		Catmull3 c = new Catmull3(v,v,v,v);
		
		for (int i = pointindex+1; i< points.size(); i++ ) {
			c.set(points.get(i-3), points.get(i-2), points.get(i-1), points.get(i));
			
			Vector3 v0 = points.get(i-2);
			Vector3 v1 = points.get(i-1);
			
			float d = v0.distance(v1);
			
			int steps = (int)(d/5.0f)+1;
			ps = 1.0f/steps;
			t = 0.0f;
			for (int j = 0; j < steps; j++) {
				t = j * ps;				
				c.interpolate(t,v);
				v.Z = 0;
				pathsimulation.add(v.clone()); 
			}
			
		}
		
	}
	
	public ArrayList<Vector3> getSimulatedPath() {
		return pathsimulation;
	}
	
	public void resetPath() {
		pointindex = 3;
		pathtime = 0.0f;
		pathspeed = 1.0f;	
		path = null;
		path_closed = true;
	}
	
}