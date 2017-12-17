package com.botijasoftware.ParticleSystem

import java.util.ArrayList

import javax.microedition.khronos.opengles.GL10

import android.content.res.TypedArray
import android.opengl.GLES10

import com.botijasoftware.utils.Atlas
import com.botijasoftware.utils.AtlasWrapper
import com.botijasoftware.utils.ColorRGBA
import com.botijasoftware.utils.Layout
import com.botijasoftware.utils.ResourceManager
import com.botijasoftware.utils.SimpleTextureAtlas
import com.botijasoftware.utils.Texture
import com.botijasoftware.utils.TextureAtlas
import com.botijasoftware.utils.Vector2
import com.botijasoftware.utils.Vector3
import android.support.annotation.StyleableRes



class ParticleEmitter @JvmOverloads constructor(private val packageName: String, private val baseName: String, private val mLayout: Layout?, var particlePool: ParticlePool? = null, private val mPTM: Int = 1) {

    lateinit private var shape: ParticleEmitterShape
    lateinit var emitterRate: EmitterRate
        private set
    lateinit private var initializer: ParticleInitializer
    private val elapsedTime = 0.0f
    private var emitterPosition = Vector3.ZERO.clone()
    private var emitterSpeed = Vector3.ZERO.clone()
    lateinit var texture: Atlas
        private set
    var size: Int = 0
        private set
    private var emissionQtyModifier = 1.0f
    private val particleSpeedModifier = 1.0f
    var onDeadEmitter: ParticleEmitter? = null
        private set
    var trailEmitter: ParticleEmitter? = null
        private set
    var childEmitter: ParticleEmitter? = null
        private set
    var mInitalized: Boolean = false
    var mUseAnimation: Boolean = false
    var mUseColor: Boolean = false
    var mUseRotation: Boolean = false
    var mUseTrail: Boolean = false
    var mUseOnDead: Boolean = false
    var mUseChild: Boolean = false
    var mBillboard: Boolean = false
    var isEnabled: Boolean = false

    private var mUnitType: Int = 0

    private val mSharedParticlePool: Boolean = false

    var mSrcBlendMode: Int = 0
    var mDstBlendMode: Int = 0

    private var mRenderer: ParticleRenderer? = null

    constructor(packName: String, name: String, layout: Layout, ptm: Int) : this(packName, name, layout, null, ptm) {}

    init {
        mInitalized = false
        size = 0
        mUseAnimation = false
        mUseColor = false
        mUseRotation = false
        mUseTrail = false
        mUseOnDead = false
        mUseChild = false
        mBillboard = false
        isEnabled = true
        mUnitType = EMITTER_UNIT_PIXEL
    }


    

    @JvmOverloads
    fun Update(time: Float, emmitparticles: Boolean = true) {
        if (!mInitalized)
            return

        if (emmitparticles && isEnabled)
            emmit(time)

        val activeparticles = particlePool!!.activeParticles
        val size = activeparticles.size
        for (i in 0 until size) {
            val p = activeparticles[i]
            p.Update(time)
            if (!p.alive) {
                particlePool!!.recycleParticle(p)
            }
        }

        mRenderer!!.Update(time)
        particlePool!!.Update(time)

        if (trailEmitter != null)
            trailEmitter!!.Update(time, false)
        if (onDeadEmitter != null)
            onDeadEmitter!!.Update(time, false)
        if (childEmitter != null)
            childEmitter!!.Update(time, false)

    }


    fun Draw(gl: GL10) {
        if (mInitalized) {
            mRenderer!!.Draw(gl)
        }
    }

    fun enable() {
        isEnabled = true
        if (trailEmitter != null)
            trailEmitter!!.enable()
        if (onDeadEmitter != null)
            onDeadEmitter!!.enable()
        if (childEmitter != null)
            childEmitter!!.enable()
    }

    fun disable() {
        isEnabled = false
        if (trailEmitter != null)
            trailEmitter!!.disable()
        if (onDeadEmitter != null)
            onDeadEmitter!!.disable()
        if (childEmitter != null)
            childEmitter!!.disable()
    }

    fun emmit(time: Float) {
        val count = (emitterRate!!.getCount(time) * emissionQtyModifier).toInt()
        forceEmmit(count)
        if (mUseChild)
            childEmitter!!.emmit(time)
    }

    fun emmit() {
        val count = (emitterRate!!.count * emissionQtyModifier).toInt()
        forceEmmit(count)
        if (mUseChild)
            childEmitter!!.emmit()
    }

    fun emmit(p: Particle, time: Float) {
        val count = (p.trailRate!!.getCount(time) * emissionQtyModifier).toInt()
        forceEmmit(p, count)
    }

    fun emmit(p: Particle) {
        val count = (p.trailRate!!.count * emissionQtyModifier).toInt()
        forceEmmit(p, count)
    }

    fun emmitOnDead(p: Particle) {
        val count = (emitterRate!!.count * emissionQtyModifier).toInt()
        forceEmmit(p, count)
    }

    fun forceEmmit(count: Int) {

        if (!mInitalized)
            return

        var particle: Particle?
        for (i in 0 until count) {

            val pos = shape!!.get()
            particle = particlePool!!.freeParticle

            if (particle != null) {
                pos.add(emitterPosition)  // add emitter position
                initializer!!.init(particle, pos)
                particle.speed.add(emitterSpeed) // addd emitter speed
                particle.speed.mul(particleSpeedModifier)
            }
        }
    }

    fun forceEmmit(p: Particle, count: Int) {

        if (!mInitalized)
            return


        var particle: Particle?
        for (i in 0 until count) {

            particle = particlePool!!.freeParticle

            if (particle != null) {
                initializer!!.init(particle, p)
            }
        }
    }

    fun killParticles() {

        particlePool!!.recycle()
    }

    fun setPosition(position: Vector3) {
        if (shape != null) {
            emitterPosition = position
            //shape.setPosition(emitterPosition);
        }
        if (mUseChild)
            childEmitter!!.setPosition(position)
    }

    fun setPosition(x: Float, y: Float, z: Float) {
        if (shape != null) {
            emitterPosition.setValue(x, y, z)
        }
        if (mUseChild)
            childEmitter!!.setPosition(x, y, z)
    }

    fun setSpeed(speed: Vector3) {
        if (shape != null) {
            emitterSpeed = speed
        }
        if (mUseChild)
            childEmitter!!.setSpeed(speed)
    }

    fun setSpeed(x: Float, y: Float, z: Float) {
        if (shape != null) {
            emitterSpeed.setValue(x, y, z)
        }
        if (mUseChild)
            childEmitter!!.setSpeed(x, y, z)
    }

    fun setTexture(atlas: SimpleTextureAtlas) {
        texture = atlas
    }

    fun modifyRate(modifier: Float) {
        emissionQtyModifier = modifier
    }

    companion object {

        private val MAX_PARTICLES = 256

        protected val EMITTER_SHAPE_TYPE_POINT = 0
        protected val EMITTER_SHAPE_TYPE_SPHERE = 1
        protected val EMITTER_SHAPE_TYPE_CUBE = 2
        protected val EMITTER_SHAPE_TYPE_LINE = 3
        protected val EMITTER_SHAPE_TYPE_CIRCLE = 4
        protected val EMITTER_SHAPE_TYPE_RECTANGLE = 5

        protected val EMITTER_SHAPE_DISTRIBUTION_RANDOM = 0
        protected val EMITTER_SHAPE_DISTRIBUTION_LINEAR = 1

        protected val EMITTER_RATE_CONSTANT = 0
        protected val EMITTER_RATE_ONDEMAND = 1
        protected val EMITTER_RATE_BURST = 2
        protected val EMITTER_RATE_RANDOM = 3
        protected val EMITTER_RATE_RANDOMBURST = 4

        protected val IMAGE_TYPE_TEXTURE = 0
        protected val IMAGE_TYPE_SIMPLE_ATLAS = 1
        protected val IMAGE_TYPE_TEXTURE_ATLAS = 2


        protected val IMAGE_SELECTION_RANDOM = 0
        protected val IMAGE_SELECTION_ANIMATION = 1
        protected val IMAGE_SELECTION_NONE = 2

        protected val VALUE_CONSTANT = 0
        protected val VALUE_RANDOM = 1
        protected val VALUE_LIST = 2

        protected val EMITTER_UNIT_PIXEL = 0
        protected val EMITTER_UNIT_PERCENT = 1
        protected val EMITTER_UNIT_METER = 2

        protected val SRC_BLEND_ZERO = 0
        protected val SRC_BLEND_ONE = 1
        protected val SRC_BLEND_DST_COLOR = 2
        protected val SRC_BLEND_ONE_MINUS_DST_COLOR = 3
        protected val SRC_BLEND_SRC_ALPHA = 4
        protected val SRC_BLEND_ONE_MINUS_SRC_ALPHA = 5
        protected val SRC_BLEND_DST_ALPHA = 6
        protected val SRC_BLEND_ONE_MINUS_DST_ALPHA = 7
        protected val SRC_BLEND_SRC_ALPHA_SATURATE = 8
        protected val SRC_BLEND_DEFAULT = SRC_BLEND_SRC_ALPHA


        protected val DST_BLEND_ZERO = 0
        protected val DST_BLEND_ONE = 1
        protected val DST_BLEND_SRC_COLOR = 2
        protected val DST_BLEND_ONE_MINUS_SRC_COLOR = 3
        protected val DST_BLEND_SRC_ALPHA = 4
        protected val DST_BLEND_ONE_MINUS_SRC_ALPHA = 5
        protected val DST_BLEND_DST_ALPHA = 6
        protected val DST_BLEND_ONE_MINUS_DST_ALPHA = 7
        protected val DST_BLEND_DEFAULT = DST_BLEND_ONE_MINUS_SRC_ALPHA
    }


}
