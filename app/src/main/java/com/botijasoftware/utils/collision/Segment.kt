package com.botijasoftware.utils.collision

import com.botijasoftware.utils.Vector3

class Segment(var p0: Vector3, var p1: Vector3) {
    private val tmp0 = Vector3(0f)
    private val tmp1 = Vector3(0f)
    private val projection = Vector3(0f)

    operator fun set(p0: Vector3, p1: Vector3) {
        this.p0 = p0
        this.p1 = p1
    }

    fun distance(p: Vector3): Float {

        val l2 = p0.distancesq(p1)
        if (l2 == 0.0f) {
            return p0.distance(p)
        }

        tmp0.setValue(p)
        tmp0.sub(p0)
        tmp1.setValue(p1)
        tmp1.sub(p0)
        val t = tmp0.dot(tmp1) / l2

        if (t <= 0.0)
            return p0.distance(p)       // Beyond the 'p0' end of the segment
        else if (t > 1.0) return p1.distance(p)  // Beyond the 'p1' end of the segment

        projection.setValue(p0)
        tmp1.mul(t)
        projection.add(tmp1)

        return projection.distance(p)
    }

    fun distancesq(p: Vector3): Float {

        val l2 = p0.distancesq(p1)
        if (l2 == 0.0f) {
            return p0.distancesq(p)
        }

        tmp0.setValue(p)
        tmp0.sub(p0)
        tmp1.setValue(p1)
        tmp1.sub(p0)
        val t = tmp0.dot(tmp1) / l2

        if (t <= 0.0)
            return p0.distancesq(p)       // Beyond the 'p0' end of the segment
        else if (t > 1.0) return p1.distancesq(p)  // Beyond the 'p1' end of the segment

        projection.setValue(p0)
        tmp1.mul(t)
        projection.add(tmp1)

        return projection.distancesq(p)
    }

}