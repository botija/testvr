package com.botijasoftware.ParticleSystem

import java.util.ArrayList

import javax.microedition.khronos.opengles.GL10

import com.botijasoftware.utils.ResourceManager


class ParticlePool(protected var mMaxParticles: Int) {

    val freeParticle: Particle?
        get() {
            val size = mParticles.size
            if (size > 0) {
                val p = mParticles.removeAt(size - 1)
                activeParticles.add(p)
                return p
            } else
                return null
        }

    var mParticles: ArrayList<Particle>
    var activeParticles: ArrayList<Particle>
    var mDeadParticles: ArrayList<Particle>

    init {

        mParticles = ArrayList(mMaxParticles)
        activeParticles = ArrayList(mMaxParticles)
        mDeadParticles = ArrayList()

        for (i in 0 until mMaxParticles) {
            mParticles.add(Particle())
        }
    }

    fun LoadContent(gl: GL10, resources: ResourceManager) {}

    fun Update(time: Float) {
        //move all dead particles to mParticle
        val size = mDeadParticles.size
        for (i in 0 until size) {
            activeParticles.remove(mDeadParticles[i])
        }
        mParticles.addAll(mDeadParticles)
        mDeadParticles.clear()
    }

    fun recycleParticle(p: Particle) {
        mDeadParticles.add(p)
    }

    fun recycle() {
        mDeadParticles.clear()
        mParticles.addAll(activeParticles)
        activeParticles.clear()
    }
}
