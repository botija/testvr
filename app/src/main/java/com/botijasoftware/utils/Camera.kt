package com.botijasoftware.utils

import android.opengl.GLU
import com.botijasoftware.utils.collision.Ray
import com.botijasoftware.utils.interpolator.*
import com.botijasoftware.utils.renderer.Renderer

class Camera(private var mViewport: Viewport, var eye: Vector3, private var mLookAt: Vector3, private var mUp: Vector3) {
    var mFov = 75.0f
    var mNearPlane = 1.0f
    var mFarPlane = 2000.0f
    private val viewport = IntArray(4)
    private val znear = FloatArray(4)
    private val zfar = FloatArray(4)
    private val mvMatrix = GLMatrix()
    private val pMatrix = GLMatrix()
    private val invMatrix = GLMatrix()
    private lateinit var neweye: Vector3
    private lateinit var oldeye: Vector3
    private var eyetime: Float = 0.0f
    private lateinit  var eyeinterpolator: Interpolator
    private lateinit var newtarget: Vector3
    private lateinit var oldtarget: Vector3
    private var targettime: Float = 0.0f
    private lateinit var targetinterpolator: Interpolator
    private lateinit var newup: Vector3
    private lateinit var oldup: Vector3
    private var uptime: Float = 0.0f
    private lateinit var upinterpolator: Interpolator
    private var newfov: Float = 0.0f
    private var oldfov: Float = 0.0f
    private var fovtime: Float = 0.0f
    private lateinit var fovinterpolator: Interpolator

    operator fun set(eye: Vector3, lookat: Vector3, up: Vector3) {
        this.eye = eye
        this.mLookAt = lookat
        this.mUp = up
    }

    fun setViewport(viewport: Viewport) {
        this.mViewport = viewport
    }

    fun setLookAt(lookat: Vector3) {
        this.mLookAt = lookat
    }

    fun setUpVector(up: Vector3) {
        this.mUp = up
    }

    fun setOrtho(width: Int, height: Int) {
        // GLES10.glOrthof(left, right, bottom, top, zNear, zFar)
        // GLU.gluOrtho2D(gl, 0, width, 0, height);
        // [ 0 4 8 12 ]
        // [ 1 5 9 13 ]
        // [ 2 6 10 14 ]
        // [ 3 7 11 15 ]
        //MaterialRenderer.projection.loadIdentity();
        val matrix = Renderer.projection.matrix
        val left = 0.0f
        val right = width.toFloat()
        val bottom = 0.0f
        val top = height.toFloat()
        val znear = -1.0f
        val zfar = 1.0f
        matrix[0] = 2.0f / (right - left)
        matrix[5] = 2.0f / (top - bottom)
        matrix[10] = -2.0f / (zfar - znear)
        matrix[12] = -(right + left) / (right - left)
        matrix[13] = -(top + bottom) / (top - bottom)
        matrix[14] = -(zfar + znear) / (zfar - znear)
        matrix[15] = 1.0f

        //frustum(0, width, 0, height, -1, 1);


        pMatrix.restore(Renderer.projection)
        Renderer.loadProjectionMatrix()
    }


    fun set() {
        // GLES10.glMatrixMode(GLES10.GL_MODELVIEW);
        // GLU.gluLookAt(gl, mEye.X, mEye.Y, mEye.Z, mLookAt.X, mLookAt.Y,
        // mLookAt.Z, mUp.X, mUp.Y, mUp.Z);
        Renderer.modelview.setLookAt(eye.X, eye.Y, eye.Z, mLookAt.X,
                mLookAt.Y, mLookAt.Z, mUp.X, mUp.Y, mUp.Z)
        // MaterialRenderer.loadModelViewMatrix();
        getMatrixFromStack()
    }

    private fun getMatrixFromStack() {
        // GLES11.glGetFloatv(GLES11.GL_MODELVIEW_MATRIX, mvMatrix, 0);
        // GLES11.glGetFloatv(GLES11.GL_PROJECTION_MATRIX, pMatrix, 0);
        // GLES11.glGetIntegerv(GLES11.GL_VIEWPORT, viewport, 0);
        Renderer.modelview.save(mvMatrix)
        Renderer.projection.save(pMatrix)
        mViewport.getAsArray(viewport)
    }

    fun unproject(x: Float, y: Float, z: Float): Vector3 {
        var nx: Float
        var ny: Float
        var nz: Float

        GLU.gluUnProject(x, y, 0f, mvMatrix.matrix, 0, pMatrix.matrix, 0,
                viewport, 0, znear, 0)
        GLU.gluUnProject(x, y, 1f, mvMatrix.matrix, 0, pMatrix.matrix, 0,
                viewport, 0, zfar, 0)

        znear[0] /= znear[3]
        znear[1] /= znear[3]
        znear[2] /= znear[3]

        zfar[0] /= zfar[3]
        zfar[1] /= zfar[3]
        zfar[2] /= zfar[3]

        if (znear[2] == zfar[2])
        // this means we have no solutions
            return Vector3(0f, 0f, 0f)

        val t = (znear[2] - z) / (znear[2] - zfar[2])

        nx = znear[0] + (zfar[0] - znear[0]) * t
        ny = znear[1] + (zfar[1] - znear[1]) * t
        nz = znear[2] + (zfar[2] - znear[2]) * t

        return Vector3(nx, ny, nz)
    }

    fun getUnprojectRay(x: Float, y: Float): Ray {

        GLU.gluUnProject(x, y, 0f, mvMatrix.matrix, 0, pMatrix.matrix, 0,
                viewport, 0, znear, 0)
        GLU.gluUnProject(x, y, 1f, mvMatrix.matrix, 0, pMatrix.matrix, 0,
                viewport, 0, zfar, 0)

        invMatrix.invert(mvMatrix)
        // Matrix.invertM(mvMatrix, 0, invMatrix, 0);

        znear[0] /= znear[3]
        znear[1] /= znear[3]
        znear[2] /= znear[3]

        zfar[0] /= zfar[3]
        zfar[1] /= zfar[3]
        zfar[2] /= zfar[3]

        val ro = Vector3(znear[0], zfar[1], zfar[2])
        val rd = Vector3(zfar[0] - znear[0], zfar[1] - znear[1], zfar[2] - znear[2])
        rd.normalize()


        /* ro = new Vector3(invMatrix.matrix[12],invMatrix.matrix[13],invMatrix.matrix[14]);
		 rd = new Vector3(zfar[0] - znear[0], zfar[1] - znear[1],zfar[2] - znear[2]);
		 rd.normalize();


		 rd.X = rd.X * invMatrix.matrix[0] + rd.Y * invMatrix.matrix[1] + rd.Z * invMatrix.matrix[2];
		 rd.Y = rd.X * invMatrix.matrix[4] + rd.Y * invMatrix.matrix[5] + rd.Z * invMatrix.matrix[6];
		 rd.Z = rd.X * invMatrix.matrix[8] + rd.Y * invMatrix.matrix[9] + rd.Z * invMatrix.matrix[10];
		 rd.normalize(); */


        //val r = Ray(ro, rd)
        // r.RayFromPoints(new Vector3(znear[0], znear[1], znear[2]), new
        // Vector3(zfar[0], zfar[1], zfar[2]));
        return Ray(ro, rd)
    }

    // from http://www.opengl.org/wiki/GluPerspective_code
    // matrix will receive the calculated perspective matrix.
    // You would have to upload to your shader
    // or use glLoadMatrixf if you aren't using shaders.
    fun setPerspective(width: Int, height: Int) {

        // GLU.gluPerspective(gl, mFov, (float)width/(float)width, mNearPlane,
        // mFarPlane);
        val ymax: Float = mNearPlane * Math.tan(mFov * Math.PI / 360.0).toFloat()
        val xmax: Float = ymax * width.toFloat() / height.toFloat()
        // ymin = -ymax;
        // xmin = -ymax * aspectRatio;
        //xmax = ymax * width.toFloat() / height.toFloat()

        frustum(-xmax, xmax, -ymax, ymax, mNearPlane, mFarPlane)

        pMatrix.restore(Renderer.projection)
        Renderer.loadProjectionMatrix()
    }

    private fun frustum(left: Float, right: Float, bottom: Float, top: Float,
                        znear: Float, zfar: Float) {
        val temp: Float = 2.0f * znear
        val temp2: Float = right - left
        val temp3: Float = top - bottom
        val temp4: Float = zfar - znear
        val matrix = Renderer.projection.matrix
        matrix[0] = temp / temp2
        matrix[1] = 0.0f
        matrix[2] = 0.0f
        matrix[3] = 0.0f
        matrix[4] = 0.0f
        matrix[5] = temp / temp3
        matrix[6] = 0.0f
        matrix[7] = 0.0f
        matrix[8] = (right + left) / temp2
        matrix[9] = (top + bottom) / temp3
        matrix[10] = (-zfar - znear) / temp4
        matrix[11] = -1.0f
        matrix[12] = 0.0f
        matrix[13] = 0.0f
        matrix[14] = -temp * zfar / temp4
        matrix[15] = 0.0f
    }

    fun Update(time: Float) {
        if (eyeinterpolator != null) {
            eyetime += time
            if (eyetime <= 1.0f) {
                eye.X = eyeinterpolator.interpolate(oldeye.X, neweye.X, eyetime)
                eye.Y = eyeinterpolator.interpolate(oldeye.Y, neweye.Y, eyetime)
                eye.Z = eyeinterpolator.interpolate(oldeye.Z, neweye.Z, eyetime)
            } else {
                //eyeinterpolator = null
                eye.setValue(neweye)
            }
        }

        if (targetinterpolator != null) {
            targettime += time
            if (targettime <= 1.0f) {
                mLookAt.X = targetinterpolator.interpolate(oldtarget.X, newtarget.X, targettime)
                mLookAt.Y = targetinterpolator.interpolate(oldtarget.Y, newtarget.Y, targettime)
                mLookAt.Z = targetinterpolator.interpolate(oldtarget.Z, newtarget.Z, targettime)
            } else {
                //targetinterpolator = null
                mLookAt.setValue(newtarget)
            }
        }

        if (upinterpolator != null) {
            uptime += time
            if (uptime <= 1.0f) {
                mUp.X = upinterpolator.interpolate(oldup.X, newup.X, uptime)
                mUp.Y = upinterpolator.interpolate(oldup.Y, newup.Y, uptime)
                mUp.Z = upinterpolator.interpolate(oldup.Z, newup.Z, uptime)
            } else {
                //upinterpolator = null
                mUp.setValue(newup)
            }
        }

        if (fovinterpolator != null) {
            fovtime += time
            if (fovtime <= 1.0f) {
                mFov = fovinterpolator.interpolate(oldfov, newfov, fovtime)
            } else {
                //fovinterpolator = null
                mFov = newfov
            }
        }
    }

    fun moveEye(eye: Vector3) {
        neweye = eye
        oldeye = this.eye.clone()
        eyeinterpolator = CosInterpolator()
        eyetime = 0.0f
    }

    fun moveTarget(target: Vector3) {
        newtarget = target
        oldtarget = mLookAt.clone()
        targetinterpolator = CosInterpolator()
        targettime = 0.0f
    }

    fun moveUp(up: Vector3) {
        newup = up
        oldup = mUp.clone()
        upinterpolator = CosInterpolator()
        uptime = 0.0f
    }

    fun moveFov(fov: Float) {
        newfov = fov
        oldfov = mFov
        fovinterpolator = CosInterpolator()
        fovtime = 0.0f
    }
}
