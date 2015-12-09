package com.botijasoftware.utils;

import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLU;
import com.botijasoftware.utils.collision.Ray;
import com.botijasoftware.utils.interpolator.*;
import com.botijasoftware.utils.renderer.Renderer;

public class Camera {

	private Vector3 mEye;
	private Vector3 mLookAt;
	private Vector3 mUp;
	private Viewport mViewport;
	public float mFov = 75.0f;
	public float mNearPlane = 1.0f;
	public float mFarPlane = 200.0f;
	private int viewport[] = new int[4];
	private float znear[] = new float[4];
	private float zfar[] = new float[4];
	private GLMatrix mvMatrix = new GLMatrix();
	private GLMatrix pMatrix = new GLMatrix();
	private GLMatrix invMatrix = new GLMatrix();
	private Vector3 neweye;
	private Vector3 oldeye;
	private float eyetime;
	private Interpolator eyeinterpolator;
	private Vector3 newtarget;
	private Vector3 oldtarget;
	private float targettime;
	private Interpolator targetinterpolator;
	private Vector3 newup;
	private Vector3 oldup;
	private float uptime;
	private Interpolator upinterpolator;
	private float newfov;
	private float oldfov;
	private float fovtime;
	private Interpolator fovinterpolator;


	public Camera(Viewport viewport, Vector3 eye, Vector3 lookat, Vector3 up) {
		this.mViewport = viewport;
		this.mEye = eye;
		this.mLookAt = lookat;
		this.mUp = up;
	}

	public void set(Vector3 eye, Vector3 lookat, Vector3 up) {
		this.mEye = eye;
		this.mLookAt = lookat;
		this.mUp = up;
	}

	public void setViewport(Viewport viewport) {
		this.mViewport = viewport;
	}

	public void setEye(Vector3 eye) {
		this.mEye = eye;
	}

	public void setLookAt(Vector3 lookat) {
		this.mLookAt = lookat;
	}

	public void setUpVector(Vector3 up) {
		this.mUp = up;
	}

	public void setOrtho(GL10 gl, int width, int height) {
		// GLES10.glOrthof(left, right, bottom, top, zNear, zFar)
		// GLU.gluOrtho2D(gl, 0, width, 0, height);
        // [ 0 4 8 12 ]
        // [ 1 5 9 13 ]
        // [ 2 6 10 14 ]
        // [ 3 7 11 15 ]
		//MaterialRenderer.projection.loadIdentity();
		float[] matrix = Renderer.projection.matrix;
		float left = 0.0f;
		float right = width;
		float bottom = 0.0f;
		float top = height;
		float znear = -1.0f;
		float zfar = 1.0f;
		matrix[0] = 2.0f / (right -left);
		matrix[5] = 2.0f / (top - bottom);
		matrix[10] = -2.0f / (zfar - znear);
		matrix[12] = -(right+left)/(right-left);
		matrix[13] = -(top+bottom)/(top-bottom);
		matrix[14] = -(zfar + znear)/(zfar - znear);
		matrix[15] = 1.0f;
		
		//frustum(0, width, 0, height, -1, 1);


		pMatrix.restore(Renderer.projection);
		Renderer.loadProjectionMatrix();
	}
	


	public void set(GL10 gl) {
		// GLES10.glMatrixMode(GLES10.GL_MODELVIEW);
		// GLU.gluLookAt(gl, mEye.X, mEye.Y, mEye.Z, mLookAt.X, mLookAt.Y,
		// mLookAt.Z, mUp.X, mUp.Y, mUp.Z);
		Renderer.modelview.setLookAt(mEye.X, mEye.Y, mEye.Z, mLookAt.X,
				mLookAt.Y, mLookAt.Z, mUp.X, mUp.Y, mUp.Z);
		// MaterialRenderer.loadModelViewMatrix();
		getMatrixFromStack(gl);
	}

	private void getMatrixFromStack(GL10 gl) {
		// GLES11.glGetFloatv(GLES11.GL_MODELVIEW_MATRIX, mvMatrix, 0);
		// GLES11.glGetFloatv(GLES11.GL_PROJECTION_MATRIX, pMatrix, 0);
		// GLES11.glGetIntegerv(GLES11.GL_VIEWPORT, viewport, 0);
		Renderer.modelview.save(mvMatrix);
		Renderer.projection.save(pMatrix);
		mViewport.getAsArray(viewport);
	}

	public Vector3 unproject(GL10 gl, float x, float y, float z) {

		GLU.gluUnProject(x, y, 0, mvMatrix.matrix, 0, pMatrix.matrix, 0,
				viewport, 0, znear, 0);
		GLU.gluUnProject(x, y, 1, mvMatrix.matrix, 0, pMatrix.matrix, 0,
				viewport, 0, zfar, 0);

		znear[0] /= znear[3];
		znear[1] /= znear[3];
		znear[2] /= znear[3];

		zfar[0] /= zfar[3];
		zfar[1] /= zfar[3];
		zfar[2] /= zfar[3];

		if (znear[2] == zfar[2]) // this means we have no solutions
			return new Vector3(0, 0, 0);

		float t = (znear[2] - z) / (znear[2] - zfar[2]);

		x = znear[0] + (zfar[0] - znear[0]) * t;
		y = znear[1] + (zfar[1] - znear[1]) * t;
		z = znear[2] + (zfar[2] - znear[2]) * t;

		return new Vector3(x, y, z);
	}

	public Ray getUnprojectRay(GL10 gl, float x, float y) {

		GLU.gluUnProject(x, y, 0, mvMatrix.matrix, 0, pMatrix.matrix, 0,
				viewport, 0, znear, 0);
		GLU.gluUnProject(x, y, 1, mvMatrix.matrix, 0, pMatrix.matrix, 0,
				viewport, 0, zfar, 0);

		invMatrix.invert(mvMatrix);
		// Matrix.invertM(mvMatrix, 0, invMatrix, 0);

		znear[0] /= znear[3];
		znear[1] /= znear[3];
		znear[2] /= znear[3];

		zfar[0] /= zfar[3];
		zfar[1] /= zfar[3];
		zfar[2] /= zfar[3];

		Vector3 ro = new Vector3(znear[0], zfar[1], zfar[2]);
		Vector3 rd = new Vector3(zfar[0] - znear[0], zfar[1] - znear[1], zfar[2] - znear[2]);
		rd.normalize();

		
		/* ro = new Vector3(invMatrix.matrix[12],invMatrix.matrix[13],invMatrix.matrix[14]);
		 rd = new Vector3(zfar[0] - znear[0], zfar[1] - znear[1],zfar[2] - znear[2]); 
		 rd.normalize();
		 
		
		 rd.X = rd.X * invMatrix.matrix[0] + rd.Y * invMatrix.matrix[1] + rd.Z * invMatrix.matrix[2]; 
		 rd.Y = rd.X * invMatrix.matrix[4] + rd.Y * invMatrix.matrix[5] + rd.Z * invMatrix.matrix[6];
		 rd.Z = rd.X * invMatrix.matrix[8] + rd.Y * invMatrix.matrix[9] + rd.Z * invMatrix.matrix[10]; 
		 rd.normalize(); */
		 

		Ray r = new Ray(ro, rd);
		// r.RayFromPoints(new Vector3(znear[0], znear[1], znear[2]), new
		// Vector3(zfar[0], zfar[1], zfar[2]));
		return r;
	}

	public Vector3 getEye() {
		return mEye;
	}

	// from http://www.opengl.org/wiki/GluPerspective_code
	// matrix will receive the calculated perspective matrix.
	// You would have to upload to your shader
	// or use glLoadMatrixf if you aren't using shaders.
	public void setPerspective(int width, int height) {

		// GLU.gluPerspective(gl, mFov, (float)width/(float)width, mNearPlane,
		// mFarPlane);
		float ymax, xmax;
		ymax = mNearPlane * (float) Math.tan(mFov * Math.PI / 360.0);
		// ymin = -ymax;
		// xmin = -ymax * aspectRatio;
		xmax = ymax * (float) width / (float) height;

		frustum(-xmax, xmax, -ymax, ymax, mNearPlane, mFarPlane);

		pMatrix.restore(Renderer.projection);
		Renderer.loadProjectionMatrix();
	}

	private void frustum(float left, float right, float bottom, float top,
			float znear, float zfar) {
		float temp, temp2, temp3, temp4;
		temp = 2.0f * znear;
		temp2 = right - left;
		temp3 = top - bottom;
		temp4 = zfar - znear;
		float[] matrix = Renderer.projection.matrix;
		matrix[0] = temp / temp2;
		matrix[1] = 0.0f;
		matrix[2] = 0.0f;
		matrix[3] = 0.0f;
		matrix[4] = 0.0f;
		matrix[5] = temp / temp3;
		matrix[6] = 0.0f;
		matrix[7] = 0.0f;
		matrix[8] = (right + left) / temp2;
		matrix[9] = (top + bottom) / temp3;
		matrix[10] = (-zfar - znear) / temp4;
		matrix[11] = -1.0f;
		matrix[12] = 0.0f;
		matrix[13] = 0.0f;
		matrix[14] = (-temp * zfar) / temp4;
		matrix[15] = 0.0f;
	}
	
	public void Update(float time) {
		if (eyeinterpolator != null) {
			eyetime += time;
			if (eyetime <= 1.0f) {
				mEye.X = eyeinterpolator.interpolate(oldeye.X, neweye.X, eyetime);
				mEye.Y = eyeinterpolator.interpolate(oldeye.Y, neweye.Y, eyetime);
				mEye.Z = eyeinterpolator.interpolate(oldeye.Z, neweye.Z, eyetime);
			}
			else {
				eyeinterpolator = null;
				mEye.setValue(neweye);
			}
		}
		
		if (targetinterpolator != null) {
			targettime += time;
			if (targettime <= 1.0f) {
				mLookAt.X = targetinterpolator.interpolate(oldtarget.X, newtarget.X, targettime);
				mLookAt.Y = targetinterpolator.interpolate(oldtarget.Y, newtarget.Y, targettime);
				mLookAt.Z = targetinterpolator.interpolate(oldtarget.Z, newtarget.Z, targettime);
			}
			else {
				targetinterpolator = null;
				mLookAt.setValue(newtarget);
			}
		}
		
		if (upinterpolator != null) {
			uptime += time;
			if (uptime <= 1.0f) {
				mUp.X = upinterpolator.interpolate(oldup.X, newup.X, uptime);
				mUp.Y = upinterpolator.interpolate(oldup.Y, newup.Y, uptime);
				mUp.Z = upinterpolator.interpolate(oldup.Z, newup.Z, uptime);
			}
			else {
				upinterpolator = null;
				mUp.setValue(newup);
			}
		}
		
		if (fovinterpolator != null) {
			fovtime += time;
			if (fovtime <= 1.0f) {
				mFov = fovinterpolator.interpolate(oldfov, newfov, fovtime);
			}
			else {
				fovinterpolator = null;
				mFov = newfov;
			}
		}
	}
	
	public void moveEye(Vector3 eye) {
		neweye = eye;
		oldeye = mEye.clone();
		eyeinterpolator = new CosInterpolator();
		eyetime = 0.0f;		
	}
	
	public void moveTarget(Vector3 target) {
		newtarget = target;
		oldtarget = mLookAt.clone();
		targetinterpolator = new CosInterpolator();
		targettime = 0.0f;
	}
	
	public void moveUp(Vector3 up) {
		newup = up;
		oldup = mUp.clone();
		upinterpolator = new CosInterpolator();
		uptime = 0.0f;	
	}
	
	public void moveFov(float fov) {
		newfov = fov;
		oldfov = mFov;
		fovinterpolator = new CosInterpolator();
		fovtime = 0.0f;	
	}
}
