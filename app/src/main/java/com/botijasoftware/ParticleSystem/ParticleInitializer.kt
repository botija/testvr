package com.botijasoftware.ParticleSystem

import com.botijasoftware.utils.Atlas
import com.botijasoftware.utils.ColorRGBA
import com.botijasoftware.utils.Vector2
import com.botijasoftware.utils.Vector3

class ParticleInitializer {

    private var image: ImageInitializer? = null
    private var speed: SpeedInitializer? = null
    private var size: SizeInitializer? = null
    private var sizeIncrease: SizeIncreaseInitializer? = null
    private var force: ForceInitializer? = null
    private var color: ColorInitializer? = null
    private var fadeRate: FadeRateInitializer? = null
    private var rotation: RotationInitializer? = null
    private var rotationIncrease: RotationIncreaseInitializer? = null
    private var ttl: TTLInitializer? = null
    private var mTrailEmitter: ParticleEmitter? = null
    private var mOnDeadEmitter: ParticleEmitter? = null


    fun init(p: Particle, parent: Particle) {

        p.position.setValue(parent.position)
        image!!.init(p)
        size!!.init(p.size, parent)
        sizeIncrease!!.init(p.sizeIncrease, parent)
        speed!!.init(p.speed, parent)
        force!!.init(p.force, parent)
        color!!.init(p.color, parent)
        fadeRate!!.init(p.fadeRate, parent)
        rotation!!.init(p.rotation, parent)
        rotationIncrease!!.init(p.rotationIncrease, parent)
        p.trailEmitter = mTrailEmitter
        if (mTrailEmitter != null && p.trailRate == null)
            p.trailRate = mTrailEmitter!!.emitterRate!!.clone()

        p.onDeadEmitter = mOnDeadEmitter
        p.ttl = ttl!!.init(p.ttl, parent)
        p.alive = true
        p.elapsedTime = 0.0f
    }

    fun init(p: Particle, position: Vector3) {
        p.position.setValue(position)
        image!!.init(p)
        size!!.init(p.size)
        sizeIncrease!!.init(p.sizeIncrease)
        speed!!.init(p.speed)
        force!!.init(p.force)
        color!!.init(p.color)
        fadeRate!!.init(p.fadeRate)
        rotation!!.init(p.rotation)
        rotationIncrease!!.init(p.rotationIncrease)
        p.trailEmitter = mTrailEmitter
        if (mTrailEmitter != null && p.trailRate == null)
            p.trailRate = mTrailEmitter!!.emitterRate!!.clone()

        p.onDeadEmitter = mOnDeadEmitter
        p.ttl = ttl!!.init(p.ttl)
        p.alive = true
        p.elapsedTime = 0.0f
    }


    internal fun setImageInitializer(imageInit: ImageInitializer) {
        image = imageInit
    }

    fun setTrailInitializer(trailemitter: ParticleEmitter) {
        mTrailEmitter = trailemitter
    }

    fun setOnDeadInitializer(ondeademitter: ParticleEmitter) {
        mOnDeadEmitter = ondeademitter
    }

    internal fun setSpeedInitializer(speedInit: SpeedInitializer) {
        speed = speedInit
    }

    internal fun setSizeInitializer(sizeInit: SizeInitializer) {
        size = sizeInit
    }

    internal fun setSizeIncreaseInitializer(sizeIncInit: SizeIncreaseInitializer) {
        sizeIncrease = sizeIncInit
    }

    internal fun setForceInitializer(forceInit: ForceInitializer) {
        force = forceInit
    }

    internal fun setColorInitializer(colorInit: ColorInitializer) {
        color = colorInit
    }

    internal fun setFadeRateInitializer(fadeRateInit: FadeRateInitializer) {
        fadeRate = fadeRateInit
    }

    internal fun setRotationInitializer(rotationInit: RotationInitializer) {
        rotation = rotationInit
    }

    internal fun setRotationIncreaseInitializer(rotationIncreaseInit: RotationIncreaseInitializer) {
        rotationIncrease = rotationIncreaseInit
    }

    internal fun setTTLInitializer(ttlInit: TTLInitializer) {
        ttl = ttlInit
    }
}


internal open class ImageInitializer(atlas: Atlas) {

    var atlas: Atlas
        protected set

    init {
        this.atlas = atlas
    }

    open fun init(p: Particle) {
        p.animation.reset(0, 0, 0.0f, false)
    }
}


internal class ImageInitializerRandom(atlas: Atlas, private val maxImages: Int) : ImageInitializer(atlas) {

    override fun init(p: Particle) {
        p.animation.reset(atlas.textureCount, (Math.random() * maxImages).toInt(), java.lang.Float.MAX_VALUE, false)
    }
}

internal class ImageInitializerAnimation(atlas: Atlas, private val frameTime: Float, loop: Boolean) : ImageInitializer(atlas) {
    private val looping: Boolean = false

    override fun init(p: Particle) {
        p.animation.reset(atlas.textureCount, 0, frameTime, looping)
    }
}


//size
internal open class SizeInitializer {

    open fun init(size: Vector2) {
        size.setValue(Vector2.ZERO)
    }

    open fun init(size: Vector2, p: Particle) {
        size.setValue(p.size)
    }

}

internal class SizeInitializerConstant(var defaultSize: Vector2) : SizeInitializer() {

    override fun init(size: Vector2) {
        size.setValue(defaultSize)
    }

    override fun init(size: Vector2, p: Particle) {
        init(size)
    }
}

internal class SizeInitializerRandom(private val minSize: Vector2, private val maxSize: Vector2) : SizeInitializer() {

    override fun init(size: Vector2) {
        val x = (Math.random() * (maxSize.X - minSize.X) + minSize.X).toFloat()
        val y = (Math.random() * (maxSize.Y - minSize.Y) + minSize.Y).toFloat()
        size.setValue(x, y)
    }

    override fun init(size: Vector2, p: Particle) {
        init(size)
    }
}


internal class SizeInitializerList(sizes: Array<Vector2>) : SizeInitializer() {

    private val arraySize: Array<Vector2>
    private val count: Int

    init {
        arraySize = sizes.clone()
        count = sizes.size
    }

    override fun init(size: Vector2) {
        val i = (Math.random() * count).toInt()
        size.setValue(arraySize[i])
    }

    override fun init(size: Vector2, p: Particle) {
        init(size)
    }
}

//size increase
internal open class SizeIncreaseInitializer {

    open fun init(size: Vector2) {
        size.setValue(Vector2.ZERO)
    }

    open fun init(size: Vector2, p: Particle) {
        size.setValue(p.size)
    }

}

internal class SizeIncreaseInitializerConstant(var defaultSize: Vector2) : SizeIncreaseInitializer() {

    override fun init(size: Vector2) {
        size.setValue(defaultSize)
    }

    override fun init(size: Vector2, p: Particle) {
        init(size)
    }
}

internal class SizeIncreaseInitializerRandom(private val minSize: Vector2, private val maxSize: Vector2) : SizeIncreaseInitializer() {

    override fun init(size: Vector2) {
        val x = (Math.random() * (maxSize.X - minSize.X) + minSize.X).toFloat()
        val y = (Math.random() * (maxSize.Y - minSize.Y) + minSize.Y).toFloat()
        size.setValue(x, y)
    }

    override fun init(size: Vector2, p: Particle) {
        init(size)
    }
}

internal class SizeIncreaseInitializerList(sizes: Array<Vector2>) : SizeIncreaseInitializer() {

    private val arraySize: Array<Vector2>
    private val count: Int

    init {
        arraySize = sizes.clone()
        count = sizes.size
    }

    override fun init(size: Vector2) {
        val i = (Math.random() * count).toInt()
        size.setValue(arraySize[i])
    }

    override fun init(size: Vector2, p: Particle) {
        init(size)
    }
}

//speed
internal open class SpeedInitializer {

    open fun init(speed: Vector3) {
        speed.setValue(Vector3.ZERO)
    }

    open fun init(speed: Vector3, p: Particle) {
        speed.setValue(p.speed)
    }

}

internal class SpeedInitializerConstant(var defaultSpeed: Vector3) : SpeedInitializer() {

    override fun init(speed: Vector3) {
        speed.setValue(defaultSpeed)
    }

    override fun init(speed: Vector3, p: Particle) {
        init(speed)
    }
}

internal class SpeedInitializerRandom(private val minSpeed: Vector3, private val maxSpeed: Vector3) : SpeedInitializer() {

    override fun init(speed: Vector3) {
        val x = (Math.random() * (maxSpeed.X - minSpeed.X) + minSpeed.X).toFloat()
        val y = (Math.random() * (maxSpeed.Y - minSpeed.Y) + minSpeed.Y).toFloat()
        val z = (Math.random() * (maxSpeed.Z - minSpeed.Z) + minSpeed.Z).toFloat()
        speed.setValue(x, y, z)
    }

    override fun init(speed: Vector3, p: Particle) {
        init(speed)
    }
}

internal class SpeedInitializerList(speeds: Array<Vector3>) : SpeedInitializer() {

    private val arraySpeed: Array<Vector3>
    private val count: Int

    init {
        arraySpeed = speeds.clone()
        count = speeds.size
    }

    override fun init(speed: Vector3) {
        val i = (Math.random() * count).toInt()
        speed.setValue(arraySpeed[i])
    }

    override fun init(speed: Vector3, p: Particle) {
        init(speed)
    }
}

//force
internal open class ForceInitializer {

    open fun init(force: Vector3) {
        force.setValue(Vector3.ZERO)
    }

    open fun init(force: Vector3, p: Particle) {
        force.setValue(p.force)
    }
}

internal class ForceInitializerConstant(var defaultForce: Vector3) : ForceInitializer() {

    override fun init(force: Vector3) {
        force.setValue(defaultForce)
    }

    override fun init(force: Vector3, p: Particle) {
        init(force)
    }
}

internal class ForceInitializerRandom(private val minForce: Vector3, private val maxForce: Vector3) : ForceInitializer() {

    override fun init(force: Vector3) {
        val x = (Math.random() * (maxForce.X - minForce.X) + minForce.X).toFloat()
        val y = (Math.random() * (maxForce.Y - minForce.Y) + minForce.Y).toFloat()
        val z = (Math.random() * (maxForce.Z - minForce.Z) + minForce.Z).toFloat()
        force.setValue(x, y, z)
    }

    override fun init(force: Vector3, p: Particle) {
        init(force)
    }
}

internal class ForceInitializerList(forces: Array<Vector3>) : ForceInitializer() {

    private val arrayForces: Array<Vector3>
    private val count: Int

    init {
        arrayForces = forces.clone()
        count = forces.size
    }

    override fun init(force: Vector3) {
        val i = (Math.random() * count).toInt()
        force.setValue(arrayForces[i])
    }

    override fun init(force: Vector3, p: Particle) {
        init(force)
    }
}


//rotation
internal open class RotationInitializer {

    open fun init(rotation: Vector3) {
        rotation.setValue(Vector3.ZERO)
    }

    open fun init(rotation: Vector3, p: Particle) {
        rotation.setValue(p.rotation)
    }
}

internal class RotationInitializerConstant(var defaultRotation: Vector3) : RotationInitializer() {

    override fun init(rotation: Vector3) {
        rotation.setValue(defaultRotation)
    }

    override fun init(rotation: Vector3, p: Particle) {
        init(rotation)
    }
}

internal class RotationInitializerRandom(private val minRotation: Vector3, private val maxRotation: Vector3) : RotationInitializer() {

    override fun init(force: Vector3) {
        val x = (Math.random() * (maxRotation.X - minRotation.X) + minRotation.X).toFloat()
        val y = (Math.random() * (maxRotation.Y - minRotation.Y) + minRotation.Y).toFloat()
        val z = (Math.random() * (maxRotation.Z - minRotation.Z) + minRotation.Z).toFloat()
        force.setValue(x, y, z)
    }

    override fun init(rotation: Vector3, p: Particle) {
        init(rotation)
    }
}

internal class RotationInitializerList(rotations: Array<Vector3>) : RotationInitializer() {

    private val arrayRotation: Array<Vector3>
    private val count: Int

    init {
        arrayRotation = rotations.clone()
        count = rotations.size
    }

    override fun init(rotation: Vector3) {
        val i = (Math.random() * count).toInt()
        rotation.setValue(arrayRotation[i])
    }

    override fun init(rotation: Vector3, p: Particle) {
        init(rotation)
    }
}


//rotation increase
internal open class RotationIncreaseInitializer {

    open fun init(rotation: Vector3) {
        rotation.setValue(Vector3.ZERO)
    }

    open fun init(rotation: Vector3, p: Particle) {
        rotation.setValue(p.rotation)
    }
}

internal class RotationIncreaseInitializerConstant(var defaultRotation: Vector3) : RotationIncreaseInitializer() {

    override fun init(rotation: Vector3) {
        rotation.setValue(defaultRotation)
    }
}

internal class RotationIncreaseInitializerRandom(private val minRotation: Vector3, private val maxRotation: Vector3) : RotationIncreaseInitializer() {

    override fun init(force: Vector3) {
        val x = (Math.random() * (maxRotation.X - minRotation.X) + minRotation.X).toFloat()
        val y = (Math.random() * (maxRotation.Y - minRotation.Y) + minRotation.Y).toFloat()
        val z = (Math.random() * (maxRotation.Z - minRotation.Z) + minRotation.Z).toFloat()
        force.setValue(x, y, z)
    }

    override fun init(rotation: Vector3, p: Particle) {
        init(rotation)
    }
}

internal class RotationIncreaseInitializerList(rotations: Array<Vector3>) : RotationIncreaseInitializer() {

    private val arrayRotation: Array<Vector3>
    private val count: Int

    init {
        arrayRotation = rotations.clone()
        count = rotations.size
    }

    override fun init(rotation: Vector3) {
        val i = (Math.random() * count).toInt()
        rotation.setValue(arrayRotation[i])
    }

    override fun init(rotation: Vector3, p: Particle) {
        init(rotation)
    }
}


//ttl
internal open class TTLInitializer {

    open fun init(ttl: Float): Float {
        var ttl = ttl
        ttl = 0.0f
        return ttl
    }

    open fun init(ttl: Float, p: Particle): Float {
        var ttl = ttl
        ttl = p.ttl
        return p.ttl
    }
}

internal class TTLInitializerConstant(var defaultTTL: Float) : TTLInitializer() {

    override fun init(ttl: Float): Float {
        var ttl = ttl
        ttl = defaultTTL
        return ttl
    }

    override fun init(ttl: Float, p: Particle): Float {
        return init(ttl)
    }
}

internal class TTLInitializerRandom(private val minTTL: Float, private val maxTTL: Float) : TTLInitializer() {

    override fun init(ttl: Float): Float {
        var ttl = ttl
        ttl = (Math.random() * (maxTTL - minTTL) + minTTL).toFloat()
        return ttl
    }

    override fun init(ttl: Float, p: Particle): Float {
        return init(ttl)
    }
}

internal class TTLInitializerList(ttls: FloatArray) : TTLInitializer() {

    private val arrayTTL: FloatArray
    private val count: Int

    init {
        arrayTTL = ttls.clone()
        count = ttls.size
    }

    override fun init(ttl: Float): Float {
        var ttl = ttl
        val i = (Math.random() * count).toInt()
        ttl = arrayTTL[i]
        return arrayTTL[i]
    }

    override fun init(ttl: Float, p: Particle): Float {
        return init(ttl)
    }
}

//color
internal open class ColorInitializer {

    open fun init(color: ColorRGBA) {
        color.setValue(1f)
    }

    open fun init(color: ColorRGBA, p: Particle) {
        color.setValue(p.color.R, p.color.G, p.color.B, p.color.A)
    }
}

internal class ColorInitializerConstant(var defaultColor: ColorRGBA) : ColorInitializer() {

    override fun init(color: ColorRGBA) {
        color.setValue(defaultColor.R, defaultColor.G, defaultColor.B, defaultColor.A)
    }

    override fun init(color: ColorRGBA, p: Particle) {
        init(color)
    }
}

internal class ColorInitializerRandom(private val minColor: ColorRGBA, private val maxColor: ColorRGBA) : ColorInitializer() {

    override fun init(color: ColorRGBA) {
        val r = (Math.random() * (maxColor.R - minColor.R) + minColor.R).toFloat()
        val g = (Math.random() * (maxColor.G - minColor.G) + minColor.G).toFloat()
        val b = (Math.random() * (maxColor.B - minColor.B) + minColor.G).toFloat()
        val a = (Math.random() * (maxColor.A - minColor.A) + minColor.A).toFloat()
        color.setValue(r, g, b, a)

    }

    override fun init(color: ColorRGBA, p: Particle) {
        init(color)
    }
}

internal class ColorInitializerList(colors: Array<ColorRGBA>) : ColorInitializer() {

    private val arrayColors: Array<ColorRGBA>
    private val count: Int

    init {
        arrayColors = colors.clone()
        count = colors.size
    }

    override fun init(color: ColorRGBA) {
        val i = (Math.random() * count).toInt()
        color.setValue(arrayColors[i].R, arrayColors[i].G, arrayColors[i].B, arrayColors[i].A)
    }

    override fun init(color: ColorRGBA, p: Particle) {
        init(color)
    }
}

//fade rate
internal open class FadeRateInitializer {

    open fun init(color: ColorRGBA) {
        var color = color
        color = ColorRGBA(0f) //black without alpha
    }

    open fun init(color: ColorRGBA, p: Particle) {
        color.setValue(p.color.R, p.color.G, p.color.B, p.color.A)
    }
}

internal class FadeRateInitializerConstant(var defaultColor: ColorRGBA) : FadeRateInitializer() {

    override fun init(color: ColorRGBA) {
        color.setValue(defaultColor.R, defaultColor.G, defaultColor.B, defaultColor.A)
    }

    override fun init(color: ColorRGBA, p: Particle) {
        init(color)
    }
}

internal class FadeRateInitializerRandom(private val minColor: ColorRGBA, private val maxColor: ColorRGBA) : FadeRateInitializer() {

    override fun init(color: ColorRGBA) {
        val r = (Math.random() * (maxColor.R - minColor.R) + minColor.R).toFloat()
        val g = (Math.random() * (maxColor.G - minColor.G) + minColor.G).toFloat()
        val b = (Math.random() * (maxColor.B - minColor.B) + minColor.G).toFloat()
        val a = (Math.random() * (maxColor.A - minColor.A) + minColor.A).toFloat()
        color.setValue(r, g, b, a)

    }

    override fun init(color: ColorRGBA, p: Particle) {
        init(color)
    }
}

internal class FadeRateInitializerList(colors: Array<ColorRGBA>) : FadeRateInitializer() {

    private val arrayColors: Array<ColorRGBA>
    private val count: Int

    init {
        arrayColors = colors.clone()
        count = colors.size
    }

    override fun init(color: ColorRGBA) {
        val i = (Math.random() * count).toInt()
        color.setValue(arrayColors[i].R, arrayColors[i].G, arrayColors[i].B, arrayColors[i].A)
    }

    override fun init(color: ColorRGBA, p: Particle) {
        init(color)
    }
}


