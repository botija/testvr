package com.botijasoftware.utils

class Quaternion {
    constructor() {
        W = 0.0f
        Z = W
        Y = Z
        X = Y
    }

    constructor(_x: Float, _y: Float, _z: Float) {
        X = _x
        Y = _y
        Z = _z

        computeW()
    }

    constructor(_x: Float, _y: Float, _z: Float, _w: Float) {
        X = _x
        Y = _y
        Z = _z
        W = _w
    }


    constructor(v: Vector3) {
        X = v.X
        Y = v.Y
        Z = v.Z

        computeW()
    }


    constructor(v: Vector3, radians: Float) {

        val ang = radians * 0.5f
        W = Math.cos(ang.toDouble()).toFloat()

        X = v.X * ang
        Y = v.Y * ang
        Z = v.Z * ang
    }

    constructor(q: Quaternion) {
        X = q.X
        Y = q.Y
        Z = q.Z
        W = q.W
    }

    fun setValue(q: Quaternion) {
        X = q.X
        Y = q.Y
        Z = q.Z
        W = q.W
    }


    fun clone(): Quaternion {
        return Quaternion(X, Y, Z, W)
    }


    fun normalize(): Quaternion {
        var len = length()
        if (len > 0.0f) {
            len = 1.0f / len
            X *= len
            Y *= len
            Z *= len
            W *= len
        }
        return this
    }


    fun length(): Float {
        return Math.sqrt((X * X + Y * Y + Z * Z + W * W).toDouble()).toFloat()
    }


    fun length2(): Float {
        return X * X + Y * Y + Z * Z + W * W
    }

    private fun computeW() {
        val w = 1.0f - (X * X - Y * Y - Z * Z)
        if (w < 0.0f)
            W = w
        else
            W = -Math.sqrt(w.toDouble()).toFloat()

    }

    fun identity() {
        W = 1.0f
        Z = 0.0f
        Y = Z
        X = Y
    }

    fun slerp(q: Quaternion, factor: Float): Quaternion {

        when {
            factor <= 0.0f -> return this
            factor >= 1.0f -> return q
            else -> {

                var cosOmega = dot(q)

                val qtmp = q.clone()

                if (cosOmega <= 0.0f) {
                    qtmp.set(qtmp.negate()) //-qtmp
                    cosOmega = -cosOmega
                }

                val k0: Float
                val k1: Float

                if (cosOmega < 0.9999f) { //too close - use linerar interpolation
                    k0 = 1.0f - factor
                    k1 = factor
                } else {

                    /* compute the sin of the angle using the
                        trig identity sin^2(omega) + cos^2(omega) = 1 */
                    val sinOmega = Math.sqrt((1.0f - cosOmega * cosOmega).toDouble()).toFloat()

                    /* compute the angle from its sin and cosine */
                    val omega = Math.atan2(sinOmega.toDouble(), cosOmega.toDouble()).toFloat()

                    val oneOverSinOmega = 1.0f / sinOmega

                    /* Compute interpolation parameters */
                    k0 = Math.sin(((1.0f - factor) * omega).toDouble()).toFloat() * oneOverSinOmega
                    k1 = Math.sin((factor * omega).toDouble()).toFloat() * oneOverSinOmega

                }

                return this.mul(k0).add(qtmp.mul(k1))
            }
        }
    }

    fun rotate(v: Vector3): Vector3 {

        val inv = inverse()
        inv.mul(v)
        inv.mul(inverse())
        return Vector3(inv.X, inv.Y, inv.Z)
    }

    fun rotate(v: Vector3, radians: Float): Quaternion {

        val ang = radians * 0.5f
        val sin_ang = Math.sin(ang.toDouble()).toFloat()
        //return new Quaternion( v.Y * sin_ang, v.X * sin_ang, v.Z * sin_ang, (float)Math.cos(ang));
        X = v.Y * sin_ang
        Y = v.X * sin_ang
        Z = v.Z * sin_ang
        W = Math.cos(ang.toDouble()).toFloat()
        return this
    }

    fun rotateX(radians: Float): Quaternion {

        val ang = radians * 0.5f
        X = Math.sin(ang.toDouble()).toFloat()
        Y = 0f
        Z = 0f
        W = Math.cos(ang.toDouble()).toFloat()
        return this
    }

    fun rotateY(radians: Float): Quaternion {

        val ang = radians * 0.5f
        X = 0f
        Y = Math.sin(ang.toDouble()).toFloat()
        Z = 0f
        W = Math.cos(ang.toDouble()).toFloat()
        return this
    }


    fun rotateZ(radians: Float): Quaternion {

        val ang = radians * 0.5f
        X = 0f
        Y = 0f
        Z = Math.sin(ang.toDouble()).toFloat()
        W = Math.cos(ang.toDouble()).toFloat()
        return this
    }

    fun invert(): Quaternion {
        X = -X
        Y = -Y
        Z = -Z
        return this
    }


    fun inverse(): Quaternion {
        return Quaternion(-X, -Y, -Z, W)
    }


    fun conjugate(): Quaternion {
        return Quaternion(-X, -Y, -Z, W)
    }


    fun negate(): Quaternion {
        return Quaternion(-X, -Y, -Z, -W)
    }


    fun dot(q: Quaternion): Float {
        return X * q.X + Y * q.Y + Z * q.Z + W * q.W
    }


    fun add(q: Quaternion): Quaternion {
        //return new Quaternion(X + q.X, Y + q.Y, Z + q.Z, W + q.W);
        X += q.X
        Y += q.Y
        Z += q.Z
        W += q.W
        return this
    }

    fun add(n: Float): Quaternion {
        //return new Quaternion(X + n, Y + n, Z + n, W + n);
        X += n
        Y += n
        Z += n
        W += n
        return this
    }


    fun sub(q: Quaternion): Quaternion {
        //return new Quaternion(X - q.X, Y - q.Y, Z - q.Z, W - q.W);
        X -= q.X
        Y -= q.Y
        Z -= q.Z
        W -= q.W
        return this
    }

    fun sub(n: Float): Quaternion {
        //return new Quaternion(X - n, Y - n, Z - n, W - n);
        X -= n
        Y -= n
        Z -= n
        W -= n
        return this
    }


    fun mul(q: Quaternion): Quaternion {
        /*return new Quaternion( (X * q.W) + (W * q.X) + (Y * q.Z) - (Z * q.Y),
                            (Y * q.W) + (W * q.Y) + (Z * q.X) - (X * q.Z),
                            (Z * q.W) + (W * q.Z) + (X * q.Y) - (Y * q.X),
                            (W * q.W) - (X * q.X) - (Y * q.Y) - (Z * q.Z));*/
        val nX = X * q.W + W * q.X + Y * q.Z - Z * q.Y
        val nY = Y * q.W + W * q.Y + Z * q.X - X * q.Z
        val nZ = Z * q.W + W * q.Z + X * q.Y - Y * q.X
        val nW = W * q.W - X * q.X - Y * q.Y - Z * q.Z
        X = nX
        Y = nY
        Z = nZ
        W = nW
        return this
    }


    fun mul(n: Float): Quaternion {
        //return new Quaternion(X * n, Y * n, Z * n, W * n);
        X *= n
        Y *= n
        Z *= n
        W *= n
        return this
    }


    fun mul(v: Vector3): Quaternion {
        /*return new Quaternion(   (W * v.X) + (Y * v.Z) - (Z * v.Y),
                        (W * v.Y) + (Z * v.X) - (X * v.Z),
                        (W * v.Z) + (X * v.Y) - (Y * v.X),
                        - (X * v.X) - (Y * v.Y) - (Z * v.Z));*/
        val nX = W * v.X + Y * v.Z - Z * v.Y
        val nY = W * v.Y + Z * v.X - X * v.Z
        val nZ = W * v.Z + X * v.Y - Y * v.X
        val nW = -(X * v.X) - Y * v.Y - Z * v.Z
        X = nX
        Y = nY
        Z = nZ
        W = nW
        return this
    }


    operator fun div(q: Quaternion): Quaternion {
        //return new Quaternion(X / q.X, Y / q.Y, Z / q.Z, W / q.W);
        X /= q.X
        Y /= q.Y
        Z /= q.Z
        W /= q.W
        return this
    }

    operator fun div(n: Float): Quaternion {
        val inv = 1.0f / n
        //return new Quaternion(X * inv, Y * inv, Z * inv, W * inv);
        X *= inv
        Y *= inv
        Z *= inv
        W *= inv
        return this
    }

    operator fun set(_x: Float, _y: Float, _z: Float, _w: Float): Quaternion {
        X = _x
        Y = _y
        Z = _z
        W = _w
        return this
    }

    operator fun set(_x: Float, _y: Float, _z: Float): Quaternion {
        X = _x
        Y = _y
        Z = _z

        computeW()

        return this
    }

    fun set(q: Quaternion): Quaternion {
        X = q.X
        Y = q.Y
        Z = q.Z
        W = q.W
        return this
    }

    fun getMatrix(matrix: FloatArray) {

        if (W > 1) normalize()

        val x2 = X * X
        val y2 = Y * Y
        val z2 = Z * Z
        val xy = X * Y
        val xz = X * Z
        val yz = Y * Z
        val wx = W * X
        val wy = W * Y
        val wz = W * Z

        // This calculation would be a lot more complicated for non-unit length quaternions
        // Note: The constructor of Matrix4 expects the Matrix in column-major format like expected by
        //   OpenGL
        // Wikipedia
        matrix[0] = 1.0f - 2.0f * (y2 + z2)
        matrix[1] = 2.0f * (xy - wz)
        matrix[2] = 2.0f * (xz + wy)
        matrix[3] = 0.0f
        matrix[4] = 2.0f * (xy + wz)
        matrix[5] = 1.0f - 2.0f * (x2 + z2)
        matrix[6] = 2.0f * (yz - wx)
        matrix[7] = 0.0f
        matrix[8] = 2.0f * (xz - wy)
        matrix[9] = 2.0f * (yz + wx)
        matrix[10] = 1.0f - 2.0f * (x2 + y2)
        matrix[11] = 0.0f
        matrix[12] = 0.0f
        matrix[13] = 0.0f
        matrix[14] = 0.0f
        matrix[15] = 1.0f
    }

    fun getRotation(axisv: Vector3): Float {
        if (W > 1) normalize()
        val angle = 2.0f * Math.acos(W.toDouble()).toFloat()
        val s = Math.sqrt((1 - W * W).toDouble()).toFloat()
        if (s < 0.001) {
            axisv.X = X
            axisv.Z = Y
            axisv.Z = Z
        } else {
            axisv.X = X / s
            axisv.Z = Y / s
            axisv.Z = Z / s
        }

        return angle
    }


    fun setLookRotation(lookat: Vector3, up: Vector3): Quaternion {

        val forward = lookat.clone()
        var updir = up.clone()
        updir = Vector3.orthoNormalize(forward, updir)
        val right = Vector3.cross(updir, forward)

        val w = Math.sqrt((1.0f + right.X + updir.Y + forward.Z).toDouble()).toFloat() * 0.5f
        val w4_recip = 1.0f / (4.0f * w)

        X = (updir.Z - forward.Y) * w4_recip
        Y = (forward.X - right.Z) * w4_recip
        Z = (right.Y - updir.X) * w4_recip
        W = w

        return this
    }


    fun equals(q: Quaternion): Boolean {
        return X == q.X && Y == q.Y && Z == q.Z && W == q.W
    }


    var X: Float = 0.toFloat()
    var Y: Float = 0.toFloat()
    var Z: Float = 0.toFloat()
    var W: Float = 0.toFloat()

    companion object {


        fun lookRotation(lookat: Vector3, up: Vector3): Quaternion {
            /*Quaternion::LookRotation(Vector& lookAt, Vector& upDirection) {
	Vector forward = lookAt; Vector up = upDirection;
	Vector::OrthoNormalize(&forward, &up);
	Vector right = Vector::Cross(up, forward);

	Quaternion ret;
	ret.w = sqrtf(1.0f + right.x + up.y + forward.z) * 0.5f;
	float w4_recip = 1.0f / (4.0f * ret.w);
	ret.x = (up.z - forward.y) * w4_recip;
	ret.y = (forward.x - right.z) * w4_recip;
	ret.z = (right.y - up.x) * w4_recip;
	return ret;
	*/

            val forward = lookat.clone()
            var updir = up.clone()
            updir = Vector3.orthoNormalize(forward, updir)
            val right = Vector3.cross(updir, forward)

            val w = Math.sqrt((1.0f + right.X + updir.Y + forward.Z).toDouble()).toFloat() * 0.5f
            val w4_recip = 1.0f / (4.0f * w)

            //Quaternion ret = new Quaternion((forward.Y - updir.Z) * w4_recip, (right.Z - forward.X) * w4_recip,(updir.X - right.Y) * w4_recip,  w);
            return Quaternion((updir.Z - forward.Y) * w4_recip, (forward.X - right.Z) * w4_recip, (right.Y - updir.X) * w4_recip, w)
        }


        fun slerp(q1: Quaternion, q2: Quaternion, factor: Float): Quaternion {

            if (factor <= 0.0f)
                return q1
            else if (factor >= 1.0f)
                return q2
            else {

                var cosOmega = q1.dot(q2)

                val qtmp1 = q1.clone()
                var qtmp2 = q2.clone()

                if (cosOmega <= 0.0f) {
                    qtmp2 = qtmp2.negate()
                    cosOmega = -cosOmega
                }

                val k0: Float
                val k1: Float

                if (cosOmega < 0.9999f) { //too close - use linerar interpolation
                    k0 = 1.0f - factor
                    k1 = factor
                } else {

                    /* compute the sin of the angle using the
	                        trig identity sin^2(omega) + cos^2(omega) = 1 */
                    val sinOmega = Math.sqrt((1.0f - cosOmega * cosOmega).toDouble()).toFloat()

                    /* compute the angle from its sin and cosine */
                    val omega = Math.atan2(sinOmega.toDouble(), cosOmega.toDouble()).toFloat()

                    val oneOverSinOmega = 1.0f / sinOmega

                    /* Compute interpolation parameters */
                    k0 = Math.sin(((1.0f - factor) * omega).toDouble()).toFloat() * oneOverSinOmega
                    k1 = Math.sin((factor * omega).toDouble()).toFloat() * oneOverSinOmega

                }

                qtmp2.mul(k1)
                qtmp1.mul(k0)

                return qtmp1.add(qtmp2)
            }
        }

        val ZERO = Quaternion(0f, 0f, 0f, 0f)
        val IDENTITY = Quaternion(0f, 0f, 0f, 1f)
    }

}
