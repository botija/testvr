package com.botijasoftware.utils;

public class Quaternion
{
	public Quaternion () {
		X = Y = Z = W = 0.0f;
	}

	public Quaternion (float _x, float _y, float _z ) {
  		X = _x;
  		Y = _y;
  		Z = _z;
  	
        computeW ( );
	}

   public Quaternion (float _x, float _y, float _z, float _w ) {
   		X = _x;
   		Y = _y;
   		Z = _z;
   		W = _w;
   }


	public Quaternion (Vector3 v ) {
	   X = v.X;
	   Y = v.Y;
	   Z = v.Z;
	   
       computeW();
   }


	public Quaternion (Vector3 v, float radians ) {
        
	   float ang = radians * 0.5f;
        W = (float)Math.cos(ang);

        X = v.X * ang;
        Y = v.Y * ang;
        Z = v.Z * ang;
   }

	public Quaternion(Quaternion q) {
	   X = q.X;
	   Y = q.Y;
	   Z = q.Z;
	   W = q.W;
   }
   
	public void setValue(Quaternion q) {
		   X = q.X;
		   Y = q.Y;
		   Z = q.Z;
		   W = q.W;	
	} 

   
   public Quaternion clone() {
	   return new Quaternion(X, Y, Z , W);
   }


	public Quaternion normalize ( ) {
        float len = length();
        if (len > 0.0f)
        {
                len = 1.0f / len;
                X *= len;
                Y *= len;
                Z *= len;
                W *= len;
        }
        return this;
	}


	public float length ( ) {
        return (float)Math.sqrt(X * X + Y * Y + Z * Z + W * W);
	}


	public float length2 ( ) {
        return X * X + Y * Y + Z * Z + W * W;
	}

	private void computeW ( ) {
        float w = 1.0f - (X * X - Y * Y - Z * Z);
        if (w < 0.0f)
                W = w;
        else
                W = -(float)Math.sqrt(w);

	}

	public void identity() {
		W = 1.0f;
        X = Y = Z = 0.0f;
	}

	public Quaternion slerp (Quaternion q, float factor ) {

        if (factor <= 0.0f)
                return this;
        else if (factor >= 1.0f)
                return q;
        else {

                float cosOmega = dot( q );

                Quaternion qtmp =  q.clone();

                if (cosOmega <= 0.0f) {
                        qtmp.set(qtmp.negate()); //-qtmp
                        cosOmega = -cosOmega;
                }

                float k0, k1;

                if (cosOmega < 0.9999f) { //too close - use linerar interpolation
                        k0 = 1.0f - factor;
                        k1 = factor;
                }
                else {

                        /* compute the sin of the angle using the
                        trig identity sin^2(omega) + cos^2(omega) = 1 */
                        float sinOmega = (float)Math.sqrt (1.0f - (cosOmega * cosOmega));

                        /* compute the angle from its sin and cosine */
                        float omega = (float)Math.atan2( sinOmega, cosOmega );

                        float oneOverSinOmega = 1.0f / sinOmega;

                        /* Compute interpolation parameters */
                        k0 = (float)Math.sin ((1.0f - factor) * omega) * oneOverSinOmega;
                        k1 = (float)Math.sin (factor * omega) * oneOverSinOmega;

                }

                return this.mul(k0).add(qtmp.mul(k1));
        }
}

public Vector3 rotate (Vector3 v)  {

        Quaternion inv = inverse();
        inv.mul(v);
        inv.mul(inverse());
        return new Vector3(inv.X, inv.Y, inv.Z);
}

	public Quaternion rotate(Vector3 v, float radians)  {

        float ang = radians * 0.5f;
        float sin_ang = (float)Math.sin(ang);
        //return new Quaternion( v.Y * sin_ang, v.X * sin_ang, v.Z * sin_ang, (float)Math.cos(ang));
        X = v.Y * sin_ang;
        Y = v.X * sin_ang; 
        Z = v.Z * sin_ang; 
        W = (float)Math.cos(ang);
        return this;
}

	public Quaternion rotateX(float radians) {
	
	float ang = radians * 0.5f;
	X = (float)Math.sin( ang );
	Y = 0;
	Z = 0;
	W = (float)Math.cos( ang );
	return this;
}

public Quaternion rotateY(float radians) {
	
	float ang = radians * 0.5f;
	X = 0;
	Y = (float)Math.sin( ang );
	Z = 0;
	W = (float)Math.cos( ang );
	return this;
}


public Quaternion rotateZ(float radians) {
	
	float ang = radians * 0.5f;
	X = 0;
	Y = 0;
	Z = (float)Math.sin( ang );
	W = (float)Math.cos( ang );
	return this;
}

	public Quaternion invert ( ) {
        X = -X;
        Y = -Y;
        Z = -Z;
        return this;
	}


	public Quaternion inverse ( ) {
        return new Quaternion(-X, -Y, -Z, W);
	}


	public Quaternion conjugate ( ) {
        return new Quaternion(-X, -Y, -Z, W);
	}


	public Quaternion negate ( ) {
        return new Quaternion(-X, -Y, -Z, -W);
	}


	public float dot (Quaternion q ) {
        return X * q.X + Y * q.Y + Z * q.Z + W * q.W;
	}


	public Quaternion add(Quaternion q ) {
        //return new Quaternion(X + q.X, Y + q.Y, Z + q.Z, W + q.W);
		X += q.X;
		Y += q.Y;
		Z += q.Z;
		W += q.W;
		return this;
	}

	public Quaternion add (float n )  {
        //return new Quaternion(X + n, Y + n, Z + n, W + n);
		X += n;
		Y += n;
		Z += n;
		W += n;
		return this;
	}


	public Quaternion sub ( Quaternion q )  {
        //return new Quaternion(X - q.X, Y - q.Y, Z - q.Z, W - q.W);
		X -= q.X;
		Y -= q.Y;
		Z -= q.Z;
		W -= q.W;
		return this;
	}

	public Quaternion sub (float n) {
        //return new Quaternion(X - n, Y - n, Z - n, W - n);
		X -= n;
		Y -= n;
		Z -= n;
		W -= n;
		return this;
	}


	public Quaternion mul (Quaternion q ) {
        /*return new Quaternion( (X * q.W) + (W * q.X) + (Y * q.Z) - (Z * q.Y),
                            (Y * q.W) + (W * q.Y) + (Z * q.X) - (X * q.Z),
                            (Z * q.W) + (W * q.Z) + (X * q.Y) - (Y * q.X),
                            (W * q.W) - (X * q.X) - (Y * q.Y) - (Z * q.Z));*/
		float nX = (X * q.W) + (W * q.X) + (Y * q.Z) - (Z * q.Y);
		float nY = (Y * q.W) + (W * q.Y) + (Z * q.X) - (X * q.Z);
		float nZ = (Z * q.W) + (W * q.Z) + (X * q.Y) - (Y * q.X);
		float nW = (W * q.W) - (X * q.X) - (Y * q.Y) - (Z * q.Z);
		X = nX;
		Y = nY;
		Z = nZ;
		W = nW;
		return this;
	}


	public Quaternion mul (float n )  {
        //return new Quaternion(X * n, Y * n, Z * n, W * n);
		X *= n;
		Y *= n;
		Z *= n;
		W *= n;
		return this;
	}


	public Quaternion mul ( Vector3 v )  {
		/*return new Quaternion(   (W * v.X) + (Y * v.Z) - (Z * v.Y),
                        (W * v.Y) + (Z * v.X) - (X * v.Z),
                        (W * v.Z) + (X * v.Y) - (Y * v.X),
                        - (X * v.X) - (Y * v.Y) - (Z * v.Z));*/
		float nX = (W * v.X) + (Y * v.Z) - (Z * v.Y);
		float nY = (W * v.Y) + (Z * v.X) - (X * v.Z);
		float nZ = (W * v.Z) + (X * v.Y) - (Y * v.X);
		float nW = - (X * v.X) - (Y * v.Y) - (Z * v.Z);
		X = nX;
		Y = nY;
		Z = nZ;
		W = nW;
		return this;
	}


	public Quaternion div ( Quaternion q )  {
        //return new Quaternion(X / q.X, Y / q.Y, Z / q.Z, W / q.W);
		X /= q.X;
		Y /= q.Y;
		Z /= q.Z;
		W /= q.W;
		return this;
	}

	public Quaternion div (float n )  {
        float inv = 1.0f / n;
        //return new Quaternion(X * inv, Y * inv, Z * inv, W * inv);
        X *= inv;
        Y *= inv;
        Z *= inv;
        W *= inv;
        return this;
	}

	public Quaternion set(float _x, float _y, float _z, float _w ) {
		X = _x;
		Y = _y;
		Z = _z;
		W = _w;
		return this;
	}
	
	public Quaternion set(float _x, float _y, float _z ) {
		X = _x;
		Y = _y;
		Z = _z;
		
		computeW();
		
		return this;
	}

	public Quaternion set(Quaternion q ) {
		X = q.X;
        Y = q.Y;
        Z = q.Z;
        W = q.W;
        return this;
	}

	public void getMatrix(float[] matrix) {
		
		if (W > 1) normalize();

		float x2 = X * X;
		float y2 = Y * Y;
		float z2 = Z * Z;
		float xy = X * Y;
		float xz = X * Z;
		float yz = Y * Z;
		float wx = W * X;
		float wy = W * Y;
		float wz = W * Z;
	 
		// This calculation would be a lot more complicated for non-unit length quaternions
		// Note: The constructor of Matrix4 expects the Matrix in column-major format like expected by
		//   OpenGL
		// Wikipedia
		matrix [0] = 1.0f - 2.0f * (y2 + z2);
		matrix [1] = 2.0f * (xy - wz);
		matrix [2] = 2.0f * (xz + wy);
		matrix [3] = 0.0f;
		matrix [4] = 2.0f * (xy + wz);
		matrix [5] = 1.0f - 2.0f * (x2 + z2);
		matrix [6] = 2.0f * (yz - wx);
		matrix [7] = 0.0f;
		matrix [8] = 2.0f * (xz - wy);
		matrix [9] = 2.0f * (yz + wx);
		matrix [10] = 1.0f - 2.0f * (x2 + y2);
		matrix [11] = 0.0f;
		matrix [12] = 0.0f;
		matrix [13] = 0.0f;
		matrix [14] = 0.0f;
		matrix [15] = 1.0f;
	}
	
	public float getRotation(Vector3 axisv) {
		if (W > 1) normalize();
		float angle = 2.0f * (float)Math.acos(W);
		float s = (float)Math.sqrt(1-W*W);
		if (s < 0.001) {
			axisv.X = X;
			axisv.Z = Y;
			axisv.Z = Z;
		}
		else {
			axisv.X = X/s;
			axisv.Z = Y/s;
			axisv.Z = Z/s;
		}
		
		return  angle;
	}
	
	
	public Quaternion setLookRotation(Vector3 lookat, Vector3 up) {
			
			Vector3 forward = lookat.clone();
			Vector3 updir = up.clone();
			updir = Vector3.orthoNormalize(forward, updir);
			Vector3 right = Vector3.cross(updir,forward);
					
			float w = (float)Math.sqrt(1.0f + right.X + updir.Y + forward.Z) * 0.5f;
			float w4_recip = 1.0f / (4.0f * w);
			  
			X= (updir.Z - forward.Y) * w4_recip;
			Y= (forward.X - right.Z) * w4_recip;
			Z= (right.Y - updir.X) * w4_recip;
			W = w;
			
			return this;
		}

	
	public static Quaternion lookRotation(Vector3 lookat, Vector3 up) {
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
		
		Vector3 forward = lookat.clone();
		Vector3 updir = up.clone();
		updir = Vector3.orthoNormalize(forward, updir);
		Vector3 right = Vector3.cross(updir,forward);

		float w = (float)Math.sqrt(1.0f + right.X + updir.Y + forward.Z) * 0.5f;
		float w4_recip = 1.0f / (4.0f * w);
		
		//Quaternion ret = new Quaternion((forward.Y - updir.Z) * w4_recip, (right.Z - forward.X) * w4_recip,(updir.X - right.Y) * w4_recip,  w);  
		return new Quaternion((updir.Z - forward.Y) * w4_recip, (forward.X - right.Z) * w4_recip,(right.Y - updir.X) * w4_recip,  w);
	}


	public boolean equals ( Quaternion q )  {
        return ((X == q.X) && (Y == q.Y) && (Z == q.Z) && (W == q.W));
	}


	public static Quaternion slerp(Quaternion q1, Quaternion q2, float factor) {

	        if (factor <= 0.0f)
	                return q1;
	        else if (factor >= 1.0f)
	                return q2;
	        else {

	                float cosOmega = q1.dot( q2 );

	                Quaternion qtmp1 = q1.clone();
	                Quaternion qtmp2 =  q2.clone();

	                if (cosOmega <= 0.0f) {
	                        qtmp2 = qtmp2.negate();
	                        cosOmega = -cosOmega;
	                }

	                float k0, k1;

	                if (cosOmega < 0.9999f) { //too close - use linerar interpolation
	                        k0 = 1.0f - factor;
	                        k1 = factor;
	                }
	                else {

	                        /* compute the sin of the angle using the
	                        trig identity sin^2(omega) + cos^2(omega) = 1 */
	                        float sinOmega = (float)Math.sqrt (1.0f - (cosOmega * cosOmega));

	                        /* compute the angle from its sin and cosine */
	                        float omega = (float)Math.atan2( sinOmega, cosOmega );

	                        float oneOverSinOmega = 1.0f / sinOmega;

	                        /* Compute interpolation parameters */
	                        k0 = (float)Math.sin ((1.0f - factor) * omega) * oneOverSinOmega;
	                        k1 = (float)Math.sin (factor * omega) * oneOverSinOmega;

	                }
	                
	                qtmp2.mul(k1);
	                qtmp1.mul(k0);
	                
	                return qtmp1.add(qtmp2);
	        }
	}
	

	public float X;
	public float Y;
	public float Z;
	public float W;
	public final static Quaternion ZERO = new Quaternion(0,0,0,0);
	public final static Quaternion IDENTITY = new Quaternion(0,0,0,1);

}
