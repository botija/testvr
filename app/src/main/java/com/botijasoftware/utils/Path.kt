package com.botijasoftware.utils

import java.util.ArrayList

import com.botijasoftware.utils.curves.Catmull3

class Path(private val startpoint: Vector3) {

    private var path: Catmull3? = null
    private val points = ArrayList<Vector3>()
    private var pointindex = 0
    private var pathtime = 0.0f
    private var pathspeed = 1.0f
    val simulatedPath = ArrayList<Vector3>()
    val currentPosition: Vector3
    val currentDirection = Vector3(0f, 0f, 0f)
    var isOnPath = false
        private set
    var units_per_second = 20.0f
    var path_closed = false
    private val scratch0 = Vector3(0f, 0f, 0f) //scratch vectors for temp operations
    private val scratch1 = Vector3(0f, 0f, 0f)
    private val scratch2 = Vector3(0f, 0f, 0f)
    private var simpath = true


    init {

        points.add(startpoint)
        currentPosition = startpoint.clone()
        pointindex = 3
        path = null
    }

    fun enableSimulatePath() {
        simpath = true
    }

    fun disableSimulatePath() {
        simpath = false
    }


    private fun setPathSpeed() {
        val v0 = points[pointindex - 2]
        val v1 = points[pointindex - 1]
        pathspeed = units_per_second / v0.distance(v1)
    }

    fun Update(time: Float) {

        pathtime += time * pathspeed

        if (path == null) {
            if (pointindex < points.size - 1) {
                setPathSpeed()
                path = Catmull3(points[pointindex - 3], points[pointindex - 2], points[pointindex - 1], points[pointindex])
            }
            pathtime = 0.0f
            isOnPath = true
        }

        while (pathtime >= 1.0f && path != null && isOnPath) {
            if (pointindex < points.size - 1) {
                pointindex++
                setPathSpeed()

                path!!.set(points[pointindex - 3], points[pointindex - 2], points[pointindex - 1], points[pointindex])
                pathtime -= 1.0f
                if (pathtime <= 1.0f)
                    simulatePath()
            } else
                isOnPath = false
        }

        if (isOnPath && path != null) {
            path!!.interpolate(pathtime, currentPosition)
            path!!.interpolateTangent(pathtime, currentDirection)
        }

    }

    val nextPoint: Vector3
        get() {
            if (pointindex <= points.size - 1)
                return points[pointindex]
            else
                return points[points.size - 1]
        }

    fun addPath(v: Vector3) {

        if (points.isEmpty())
            points.add(v)
        else {
            val p0 = points[points.size - 1]
            val angle = checkAngle(v)
            if (p0.distancesq(v) >= 625 || angle > 20.0f) {
                if (angle > 15)
                    points.add(v)
                else {
                    points.add(straight(v))
                }
                simulatePath()
            }

        }
    }

    private fun straight(v: Vector3): Vector3 {
        val size = points.size
        if (size > 1) {

            scratch0.setValue(points[size - 2])
            scratch1.setValue(points[size - 1])
            val d = scratch0.distance(v)
            val d2 = scratch1.distance(v)
            return scratch1.mul(d2 - d).clone()
        }
        return v
    }

    private fun checkAngle(v: Vector3): Float {
        val size = points.size
        if (size > 1) {
            return getAngle(points[size - 2], points[size - 1], v)
        } else
            return 180.0f
    }

    private fun getAngle(v0: Vector3, v1: Vector3, v2: Vector3): Float {
        scratch0.setValue(v2)
        scratch1.setValue(v2)
        scratch0.sub(v0)
        scratch1.sub(v1)
        return scratch0.dot(scratch1)
    }

    private fun simplifyPath() {

        var i = 2
        while (i < points.size) {
            scratch0.setValue(points[i - 2])
            scratch1.setValue(points[i - 1])
            scratch2.setValue(points[i])
            val angle = getAngle(scratch0, scratch1, scratch2)
            if (angle < 10) {
                points.removeAt(i - 1)
                i--
            }
            i++
        }
    }

    fun clearPath() {
        points.clear()
        points.add(startpoint)
        path = null
        pointindex = 3
    }

    fun closePath() {
        val size = points.size

        if (size > 3) {
            val v = points[size - 1] //duplicate last point
            points.add(v.clone())
        } else {
            points.add(startpoint)
        }
        //simplifyPath();
        simulatePath()
        path_closed = true
    }

    fun simulatePath() {
        if (!simpath)
            return

        simulatedPath.clear()


        if (points.size < 3)
            return

        var t: Float
        var ps: Float
        val v = Vector3(0f, 0f, 0f)
        val c = Catmull3(v, v, v, v)

        for (i in pointindex + 1..points.size - 1) {
            c.set(points[i - 3], points[i - 2], points[i - 1], points[i])

            val v0 = points[i - 2]
            val v1 = points[i - 1]

            val d = v0.distance(v1)

            val steps = (d / 5.0f).toInt() + 1
            ps = 1.0f / steps

            for (j in 0..steps - 1) {
                t = j * ps
                c.interpolate(t, v)
                v.Z = 0f
                simulatedPath.add(v.clone())
            }

        }

    }

    fun resetPath() {
        pointindex = 3
        pathtime = 0.0f
        pathspeed = 1.0f
        path = null
        path_closed = true
    }

}