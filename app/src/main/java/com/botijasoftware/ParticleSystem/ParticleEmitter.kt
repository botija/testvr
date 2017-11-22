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

class ParticleEmitter @JvmOverloads constructor(private val packageName: String, private val baseName: String, private val mLayout: Layout?, var particlePool: ParticlePool? = null, private val mPTM: Int = 1) {

    private var shape: ParticleEmitterShape? = null
    var emitterRate: EmitterRate? = null
        private set
    private var initializer: ParticleInitializer? = null
    private val elapsedTime = 0.0f
    private var emitterPosition = Vector3.ZERO.clone()
    private var emitterSpeed = Vector3.ZERO.clone()
    var texture: Atlas? = null
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


    private fun getEmitterShape(resources: ResourceManager): ParticleEmitterShape {
        //emitter shape
        val tmp = baseName + "_shape"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            return ParticleEmitterShape()
        }
        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val shapetype = particleinfo.getInt(0, 0).toInt() //shape type
        val shapedist = particleinfo.getInt(1, 0).toInt() //shape distribution -- ignored by now

        var shape: ParticleEmitterShape? = null

        if (shapetype == EMITTER_SHAPE_TYPE_POINT) {
            var x = particleinfo.getFloat(2, 0.0f)
            var y = particleinfo.getFloat(3, 0.0f)
            var z = particleinfo.getFloat(4, 0.0f)

            if (mUnitType == EMITTER_UNIT_PERCENT && mLayout != null) {
                x = mLayout.getHorizontal(x)
                y = mLayout.getVertical(y)
                z = mLayout.getHorizontal(z)
            } else if (mUnitType == EMITTER_UNIT_METER) {
                x *= mPTM.toFloat()
                y *= mPTM.toFloat()
                z *= mPTM.toFloat()
            }

            shape = EmitterShapePoint(Vector3(x, y, z))
        } else if (shapetype == EMITTER_SHAPE_TYPE_SPHERE) {
            var x = particleinfo.getFloat(2, 0.0f)
            var y = particleinfo.getFloat(3, 0.0f)
            var z = particleinfo.getFloat(4, 0.0f)
            var radius = particleinfo.getFloat(5, 0.0f)

            if (mUnitType == EMITTER_UNIT_PERCENT && mLayout != null) {
                x = mLayout.getHorizontal(x)
                y = mLayout.getVertical(y)
                z = mLayout.getHorizontal(z)
                radius = mLayout.getHorizontal(radius)
            } else if (mUnitType == EMITTER_UNIT_METER) {
                x *= mPTM.toFloat()
                y *= mPTM.toFloat()
                z *= mPTM.toFloat()
                radius *= mPTM.toFloat()
            }

            shape = EmitterShapeSphere(Vector3(x, y, z), radius)
        } else if (shapetype == EMITTER_SHAPE_TYPE_CUBE) {
            var x = particleinfo.getFloat(2, 0.0f)
            var y = particleinfo.getFloat(3, 0.0f)
            var z = particleinfo.getFloat(4, 0.0f)
            var w = particleinfo.getFloat(5, 0.0f)
            var h = particleinfo.getFloat(6, 0.0f)
            var d = particleinfo.getFloat(7, 0.0f)

            if (mUnitType == EMITTER_UNIT_PERCENT && mLayout != null) {
                x = mLayout.getHorizontal(x)
                y = mLayout.getVertical(y)
                z = mLayout.getHorizontal(z)
                w = mLayout.getHorizontal(w)
                h = mLayout.getVertical(h)
                d = mLayout.getHorizontal(d)
            } else if (mUnitType == EMITTER_UNIT_METER) {
                x *= mPTM.toFloat()
                y *= mPTM.toFloat()
                z *= mPTM.toFloat()
                w *= mPTM.toFloat()
                h *= mPTM.toFloat()
                d *= mPTM.toFloat()
            }

            shape = EmitterShapeCube(Vector3(x, y, z), Vector3(w, h, d))
        } else if (shapetype == EMITTER_SHAPE_TYPE_LINE) {
            var x0 = particleinfo.getFloat(2, 0.0f)
            var y0 = particleinfo.getFloat(3, 0.0f)
            var z0 = particleinfo.getFloat(4, 0.0f)
            var x1 = particleinfo.getFloat(5, 0.0f)
            var y1 = particleinfo.getFloat(6, 0.0f)
            var z1 = particleinfo.getFloat(7, 0.0f)

            if (mUnitType == EMITTER_UNIT_PERCENT && mLayout != null) {
                x0 = mLayout.getHorizontal(x0)
                y0 = mLayout.getVertical(y0)
                z0 = mLayout.getHorizontal(z0)
                x1 = mLayout.getHorizontal(x1)
                y1 = mLayout.getVertical(y1)
                z1 = mLayout.getHorizontal(z1)
            } else if (mUnitType == EMITTER_UNIT_METER) {
                x0 *= mPTM.toFloat()
                y0 *= mPTM.toFloat()
                z0 *= mPTM.toFloat()
                x1 *= mPTM.toFloat()
                y1 *= mPTM.toFloat()
                z1 *= mPTM.toFloat()
            }

            shape = EmitterShapeLine(Vector3(x0, y0, z0), Vector3(x1, y1, z1))
        } else if (shapetype == EMITTER_SHAPE_TYPE_CIRCLE) {
            var x = particleinfo.getFloat(2, 0.0f)
            var y = particleinfo.getFloat(3, 0.0f)
            var radius = particleinfo.getFloat(4, 0.0f)

            if (mUnitType == EMITTER_UNIT_PERCENT && mLayout != null) {
                x = mLayout.getHorizontal(x)
                y = mLayout.getVertical(y)
                radius = mLayout.getHorizontal(radius)
            } else if (mUnitType == EMITTER_UNIT_METER) {
                x *= mPTM.toFloat()
                y *= mPTM.toFloat()
                radius *= mPTM.toFloat()
            }

            shape = EmitterShapeCircle(Vector2(x, y), radius)
        } else if (shapetype == EMITTER_SHAPE_TYPE_RECTANGLE) {
            var x = particleinfo.getFloat(2, 0.0f)
            var y = particleinfo.getFloat(3, 0.0f)
            var w = particleinfo.getFloat(4, 0.0f)
            var h = particleinfo.getFloat(5, 0.0f)

            if (mUnitType == EMITTER_UNIT_PERCENT && mLayout != null) {
                x = mLayout.getHorizontal(x)
                y = mLayout.getVertical(y)
                w = mLayout.getHorizontal(w)
                h = mLayout.getVertical(h)
            } else if (mUnitType == EMITTER_UNIT_METER) {
                x *= mPTM.toFloat()
                y *= mPTM.toFloat()
                w *= mPTM.toFloat()
                h *= mPTM.toFloat()
            }

            shape = EmitterShapeRectangle(Vector2(x, y), Vector2(w, h))
        } else {
            shape = ParticleEmitterShape()
        }

        particleinfo.recycle()

        return shape
    }

    private fun getEmitterRate(resources: ResourceManager): EmitterRate {

        val tmp = baseName + "_rate"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            return EmitterRate()
        }
        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val ratetype = particleinfo.getInt(0, 0).toInt() //rate type

        var emitterrate: EmitterRate? = null

        if (ratetype == EMITTER_RATE_CONSTANT) {
            val rate = particleinfo.getInt(1, 0)
            emitterrate = EmitterRateConstant(rate)
        } else if (ratetype == EMITTER_RATE_ONDEMAND) {
            val min = particleinfo.getInt(1, 0)
            val max = particleinfo.getInt(2, 0)
            emitterrate = EmitterRateOnDemand(min, max)
        } else if (ratetype == EMITTER_RATE_RANDOM) {
            val min = particleinfo.getInt(1, 0)
            val max = particleinfo.getInt(2, 0)
            emitterrate = EmitterRateRandom(min, max)
        } else {
            emitterrate = EmitterRate()
        }

        particleinfo.recycle()

        return emitterrate
    }

    private fun getTrailEmitter(gl: GL10, resources: ResourceManager) {

        val tmp = baseName + "_trail"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            return
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val trailemitter = particleinfo.getString(0)


        particleinfo.recycle()

        trailEmitter = ParticleEmitter(packageName, trailemitter, mLayout, mPTM)
        trailEmitter!!.LoadContent(gl, resources)
        mUseTrail = true
    }

    private fun getOnDeadEmitter(gl: GL10, resources: ResourceManager) {

        val tmp = baseName + "_ondead"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            return
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val ondeademitter = particleinfo.getString(0)
        val minemission = particleinfo.getFloat(1, 0.0f)
        val maxemission = particleinfo.getFloat(2, 0.0f)

        particleinfo.recycle()

        onDeadEmitter = ParticleEmitter(packageName, ondeademitter, mLayout, mPTM)
        onDeadEmitter!!.LoadContent(gl, resources)
        mUseOnDead = true
    }

    private fun getChildEmitter(gl: GL10, resources: ResourceManager) {

        val tmp = baseName + "_child"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            return
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val childemitter = particleinfo.getString(0)


        particleinfo.recycle()

        childEmitter = ParticleEmitter(packageName, childemitter, mLayout, mPTM)
        childEmitter!!.LoadContent(gl, resources)
        mUseChild = true
    }


    private fun getImageInitializer(gl: GL10, resources: ResourceManager): ImageInitializer? {

        val tmp = baseName + "_image"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            return null
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val type = particleinfo.getInt(0, 0).toInt()
        val imagename = particleinfo.getString(1)

        val drawableid = resources.context.resources.getIdentifier(imagename, "drawable", packageName)
        if (drawableid == 0)
            return null

        val texture = resources.loadTexture( gl.toInt(), drawableid)
        var atlas: Atlas? = null

        var nextvalue = 0
        if (type == IMAGE_TYPE_TEXTURE) {
            atlas = AtlasWrapper(texture)
            nextvalue = 2
        } else if (type == IMAGE_TYPE_SIMPLE_ATLAS) {

            val atlasw = particleinfo.getInt(2, 0).toInt()
            val atlash = particleinfo.getInt(3, 0).toInt()
            val atlastotal = particleinfo.getInt(4, 0).toInt()
            atlas = SimpleTextureAtlas(texture, atlasw, atlash, atlastotal)
            nextvalue = 5
        } else if (type == IMAGE_TYPE_TEXTURE_ATLAS) {
            val atlasdef = particleinfo.getString(2)
            val atlasdefid = resources.context.resources.getIdentifier(atlasdef, "array", packageName)
            atlas = TextureAtlas(texture, atlasdefid)
            nextvalue = 3
        }

        var imageinit: ImageInitializer? = null

        mUseAnimation = true

        val animtype = particleinfo.getInt(nextvalue++, 0).toInt()
        if (animtype == IMAGE_SELECTION_RANDOM) {
            imageinit = ImageInitializerRandom(atlas, atlas!!.textureCount)
        } else if (animtype == IMAGE_SELECTION_ANIMATION) {
            val animtime = particleinfo.getFloat(nextvalue++, 0.0f)
            val animloop = particleinfo.getBoolean(nextvalue++, false)
            imageinit = ImageInitializerAnimation(atlas, animtime, animloop)
        } else {
            imageinit = ImageInitializer(atlas)
            mUseAnimation = false
        }

        particleinfo.recycle()

        return imageinit
    }

    private fun getSizeInitializer(resources: ResourceManager): SizeInitializer {

        val tmp = baseName + "_size"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            return SizeInitializer()
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val sizetype = particleinfo.getInt(0, 0).toInt() //type

        var sizeinit: SizeInitializer? = null

        if (sizetype == VALUE_CONSTANT) {
            var sizex = particleinfo.getFloat(1, 0.0f)
            var sizey = particleinfo.getFloat(2, 0.0f)

            if (mUnitType == EMITTER_UNIT_PERCENT && mLayout != null) {
                sizex = mLayout.getHorizontal(sizex)
                sizey = mLayout.getVertical(sizey)
            } else if (mUnitType == EMITTER_UNIT_METER) {
                sizex *= mPTM.toFloat()
                sizey *= mPTM.toFloat()
            }

            sizeinit = SizeInitializerConstant(Vector2(sizex, sizey))
        } else if (sizetype == VALUE_RANDOM) {
            var minsizex = particleinfo.getFloat(1, 0.0f)
            var minsizey = particleinfo.getFloat(2, 0.0f)
            var maxsizex = particleinfo.getFloat(3, 0.0f)
            var maxsizey = particleinfo.getFloat(4, 0.0f)

            if (mUnitType == EMITTER_UNIT_PERCENT && mLayout != null) {
                minsizex = mLayout.getHorizontal(minsizex)
                minsizey = mLayout.getVertical(minsizey)
                maxsizex = mLayout.getHorizontal(maxsizex)
                maxsizey = mLayout.getVertical(maxsizey)
            } else if (mUnitType == EMITTER_UNIT_METER) {
                minsizex *= mPTM.toFloat()
                minsizey *= mPTM.toFloat()
                maxsizex *= mPTM.toFloat()
                maxsizey *= mPTM.toFloat()
            }

            sizeinit = SizeInitializerRandom(Vector2(minsizex, minsizey), Vector2(maxsizex, maxsizey))
        } else if (sizetype == VALUE_LIST) {
            val count = particleinfo.getInt(1, 0)
            val array = arrayOfNulls<Vector2>(count)
            var index = 2
            for (i in 0 until count) {
                var sizex = particleinfo.getFloat(index++, 0.0f)
                var sizey = particleinfo.getFloat(index++, 0.0f)

                if (mUnitType == EMITTER_UNIT_PERCENT && mLayout != null) {
                    sizex = mLayout.getHorizontal(sizex)
                    sizey = mLayout.getVertical(sizey)
                } else if (mUnitType == EMITTER_UNIT_METER) {
                    sizex *= mPTM.toFloat()
                    sizey *= mPTM.toFloat()
                }

                array[i] = Vector2(sizex, sizey)
            }
            sizeinit = SizeInitializerList(array)
        } else {
            sizeinit = SizeInitializer()
        }
        particleinfo.recycle()

        return sizeinit
    }


    private fun getSizeIncreaseInitializer(resources: ResourceManager): SizeIncreaseInitializer {
        val tmp = baseName + "_sizeincrease"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            return SizeIncreaseInitializer()
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val sizetype = particleinfo.getInt(0, 0).toInt() //type

        var sizeinit: SizeIncreaseInitializer? = null

        if (sizetype == VALUE_CONSTANT) {
            var sizex = particleinfo.getFloat(1, 0.0f)
            var sizey = particleinfo.getFloat(2, 0.0f)

            if (mUnitType == EMITTER_UNIT_PERCENT && mLayout != null) {
                sizex = mLayout.getHorizontal(sizex)
                sizey = mLayout.getVertical(sizey)
            } else if (mUnitType == EMITTER_UNIT_METER) {
                sizex *= mPTM.toFloat()
                sizey *= mPTM.toFloat()
            }

            sizeinit = SizeIncreaseInitializerConstant(Vector2(sizex, sizey))
        } else if (sizetype == VALUE_RANDOM) {
            var minsizex = particleinfo.getFloat(1, 0.0f)
            var minsizey = particleinfo.getFloat(2, 0.0f)
            var maxsizex = particleinfo.getFloat(3, 0.0f)
            var maxsizey = particleinfo.getFloat(4, 0.0f)

            if (mUnitType == EMITTER_UNIT_PERCENT && mLayout != null) {
                minsizex = mLayout.getHorizontal(minsizex)
                minsizey = mLayout.getVertical(minsizey)
                maxsizex = mLayout.getHorizontal(maxsizex)
                maxsizey = mLayout.getVertical(maxsizey)
            } else if (mUnitType == EMITTER_UNIT_METER) {
                minsizex *= mPTM.toFloat()
                minsizey *= mPTM.toFloat()
                maxsizex *= mPTM.toFloat()
                maxsizey *= mPTM.toFloat()
            }

            sizeinit = SizeIncreaseInitializerRandom(Vector2(minsizex, minsizey), Vector2(maxsizex, maxsizey))
        } else if (sizetype == VALUE_LIST) {
            val count = particleinfo.getInt(1, 0)
            val array = arrayOfNulls<Vector2>(count)
            var index = 2
            for (i in 0 until count) {
                var sizex = particleinfo.getFloat(index++, 0.0f)
                var sizey = particleinfo.getFloat(index++, 0.0f)

                if (mUnitType == EMITTER_UNIT_PERCENT && mLayout != null) {
                    sizex = mLayout.getHorizontal(sizex)
                    sizey = mLayout.getVertical(sizey)
                } else if (mUnitType == EMITTER_UNIT_METER) {
                    sizex *= mPTM.toFloat()
                    sizey *= mPTM.toFloat()
                }

                array[i] = Vector2(sizex, sizey)
            }
            sizeinit = SizeIncreaseInitializerList(array)
        } else {
            sizeinit = SizeIncreaseInitializer()
        }
        particleinfo.recycle()

        return sizeinit
    }

    private fun getSpeedInitializer(resources: ResourceManager): SpeedInitializer {

        val tmp = baseName + "_speed"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            return SpeedInitializer()
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val speedtype = particleinfo.getInt(0, 0).toInt() //type

        var speedinit: SpeedInitializer? = null

        if (speedtype == VALUE_CONSTANT) {
            var speedx = particleinfo.getFloat(1, 0.0f)
            var speedy = particleinfo.getFloat(2, 0.0f)
            var speedz = particleinfo.getFloat(3, 0.0f)

            if (mUnitType == EMITTER_UNIT_PERCENT && mLayout != null) {
                speedx = mLayout.getHorizontal(speedx)
                speedy = mLayout.getVertical(speedy)
                speedz = mLayout.getHorizontal(speedz)
            } else if (mUnitType == EMITTER_UNIT_METER) {
                speedx *= mPTM.toFloat()
                speedy *= mPTM.toFloat()
                speedz *= mPTM.toFloat()
            }

            speedinit = SpeedInitializerConstant(Vector3(speedx, speedy, speedz))
        } else if (speedtype == VALUE_RANDOM) {
            var minspeedx = particleinfo.getFloat(1, 0.0f)
            var minspeedy = particleinfo.getFloat(2, 0.0f)
            var minspeedz = particleinfo.getFloat(3, 0.0f)
            var maxspeedx = particleinfo.getFloat(4, 0.0f)
            var maxspeedy = particleinfo.getFloat(5, 0.0f)
            var maxspeedz = particleinfo.getFloat(6, 0.0f)

            if (mUnitType == EMITTER_UNIT_PERCENT && mLayout != null) {
                minspeedx = mLayout.getHorizontal(minspeedx)
                minspeedy = mLayout.getVertical(minspeedy)
                minspeedz = mLayout.getHorizontal(minspeedz)
                maxspeedx = mLayout.getHorizontal(maxspeedx)
                maxspeedy = mLayout.getVertical(maxspeedy)
                maxspeedz = mLayout.getHorizontal(maxspeedz)
            } else if (mUnitType == EMITTER_UNIT_METER) {
                minspeedx *= mPTM.toFloat()
                minspeedy *= mPTM.toFloat()
                minspeedz *= mPTM.toFloat()
                maxspeedx *= mPTM.toFloat()
                maxspeedy *= mPTM.toFloat()
                maxspeedz *= mPTM.toFloat()
            }

            speedinit = SpeedInitializerRandom(Vector3(minspeedx, minspeedy, minspeedz), Vector3(maxspeedx, maxspeedy, maxspeedz))
        } else if (speedtype == VALUE_LIST) {
            val count = particleinfo.getInt(1, 0)
            val array = arrayOfNulls<Vector3>(count)
            var index = 2
            for (i in 0 until count) {
                var speedx = particleinfo.getFloat(index++, 0.0f)
                var speedy = particleinfo.getFloat(index++, 0.0f)
                var speedz = particleinfo.getFloat(index++, 0.0f)

                if (mUnitType == EMITTER_UNIT_PERCENT && mLayout != null) {
                    speedx = mLayout.getHorizontal(speedx)
                    speedy = mLayout.getVertical(speedy)
                    speedz = mLayout.getHorizontal(speedz)
                } else if (mUnitType == EMITTER_UNIT_METER) {
                    speedx *= mPTM.toFloat()
                    speedy *= mPTM.toFloat()
                    speedz *= mPTM.toFloat()
                }

                array[i] = Vector3(speedx, speedy, speedz)
            }
            speedinit = SpeedInitializerList(array)
        } else {
            speedinit = SpeedInitializer()
        }
        particleinfo.recycle()

        return speedinit
    }


    private fun getForceInitializer(resources: ResourceManager): ForceInitializer {
        val tmp = baseName + "_force"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            return ForceInitializer()
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val forcetype = particleinfo.getInt(0, 0).toInt() //type

        var forceinit: ForceInitializer? = null

        if (forcetype == VALUE_CONSTANT) {
            var forcex = particleinfo.getFloat(1, 0.0f)
            var forcey = particleinfo.getFloat(2, 0.0f)
            var forcez = particleinfo.getFloat(3, 0.0f)

            if (mUnitType == EMITTER_UNIT_PERCENT && mLayout != null) {
                forcex = mLayout.getHorizontal(forcex)
                forcey = mLayout.getVertical(forcey)
                forcez = mLayout.getHorizontal(forcez)
            } else if (mUnitType == EMITTER_UNIT_METER) {
                forcex *= mPTM.toFloat()
                forcey *= mPTM.toFloat()
                forcez *= mPTM.toFloat()
            }
            forceinit = ForceInitializerConstant(Vector3(forcex, forcey, forcez))
        } else if (forcetype == VALUE_RANDOM) {
            var minforcex = particleinfo.getFloat(1, 0.0f)
            var minforcey = particleinfo.getFloat(2, 0.0f)
            var minforcez = particleinfo.getFloat(3, 0.0f)
            var maxforcex = particleinfo.getFloat(4, 0.0f)
            var maxforcey = particleinfo.getFloat(5, 0.0f)
            var maxforcez = particleinfo.getFloat(6, 0.0f)

            if (mUnitType == EMITTER_UNIT_PERCENT && mLayout != null) {
                minforcex = mLayout.getHorizontal(minforcex)
                minforcey = mLayout.getVertical(minforcey)
                minforcez = mLayout.getHorizontal(minforcez)
                maxforcex = mLayout.getHorizontal(maxforcex)
                maxforcey = mLayout.getVertical(maxforcey)
                maxforcez = mLayout.getHorizontal(maxforcez)
            } else if (mUnitType == EMITTER_UNIT_METER) {
                minforcex *= mPTM.toFloat()
                minforcey *= mPTM.toFloat()
                minforcez *= mPTM.toFloat()
                maxforcex *= mPTM.toFloat()
                maxforcey *= mPTM.toFloat()
                maxforcez *= mPTM.toFloat()
            }

            forceinit = ForceInitializerRandom(Vector3(minforcex, minforcey, minforcez), Vector3(maxforcex, maxforcey, maxforcez))
        } else if (forcetype == VALUE_LIST) {
            val count = particleinfo.getInt(1, 0)
            val array = arrayOfNulls<Vector3>(count)
            var index = 2
            for (i in 0 until count) {
                var forcex = particleinfo.getFloat(index++, 0.0f)
                var forcey = particleinfo.getFloat(index++, 0.0f)
                var forcez = particleinfo.getFloat(index++, 0.0f)

                if (mUnitType == EMITTER_UNIT_PERCENT && mLayout != null) {
                    forcex = mLayout.getHorizontal(forcex)
                    forcey = mLayout.getVertical(forcey)
                    forcez = mLayout.getHorizontal(forcez)
                } else if (mUnitType == EMITTER_UNIT_METER) {
                    forcex *= mPTM.toFloat()
                    forcey *= mPTM.toFloat()
                    forcez *= mPTM.toFloat()
                }

                array[i] = Vector3(forcex, forcey, forcez)
            }
            forceinit = ForceInitializerList(array)
        } else {
            forceinit = ForceInitializer()
        }
        particleinfo.recycle()

        return forceinit
    }


    private fun getColorInitializer(resources: ResourceManager): ColorInitializer {

        val tmp = baseName + "_color"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            return ColorInitializer()
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val colortype = particleinfo.getInt(0, 0).toInt() //type

        var colorinit: ColorInitializer? = null
        mUseColor = true

        if (colortype == VALUE_CONSTANT) {
            val r = particleinfo.getFloat(1, 0.0f).toFloat()
            val g = particleinfo.getFloat(2, 0.0f).toFloat()
            val b = particleinfo.getFloat(3, 0.0f).toFloat()
            val a = particleinfo.getFloat(4, 0.0f).toFloat()
            colorinit = ColorInitializerConstant(ColorRGBA(r, g, b, a))
        } else if (colortype == VALUE_RANDOM) {
            val minr = particleinfo.getFloat(1, 0.0f).toFloat()
            val ming = particleinfo.getFloat(2, 0.0f).toFloat()
            val minb = particleinfo.getFloat(3, 0.0f).toFloat()
            val mina = particleinfo.getFloat(4, 0.0f).toFloat()
            val maxr = particleinfo.getFloat(5, 0.0f).toFloat()
            val maxg = particleinfo.getFloat(6, 0.0f).toFloat()
            val maxb = particleinfo.getFloat(7, 0.0f).toFloat()
            val maxa = particleinfo.getFloat(8, 0.0f).toFloat()

            colorinit = ColorInitializerRandom(ColorRGBA(minr, ming, minb, mina), ColorRGBA(maxr, maxg, maxb, maxa))
        } else if (colortype == VALUE_LIST) {
            val count = particleinfo.getInt(1, 0)
            val array = arrayOfNulls<ColorRGBA>(count)
            var index = 2
            for (i in 0 until count) {
                val r = particleinfo.getFloat(index++, 0.0f)
                val g = particleinfo.getFloat(index++, 0.0f)
                val b = particleinfo.getFloat(index++, 0.0f)
                val a = particleinfo.getFloat(index++, 0.0f)
                array[i] = ColorRGBA(r, g, b, a)
            }
            colorinit = ColorInitializerList(array)
        } else {
            colorinit = ColorInitializer()
            mUseColor = false
        }

        particleinfo.recycle()

        return colorinit
    }


    private fun getFadeRateInitializer(resources: ResourceManager): FadeRateInitializer {

        val tmp = baseName + "_fade"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            return FadeRateInitializer()
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val fadetype = particleinfo.getInt(0, 0).toInt() //type

        var fadeinit: FadeRateInitializer? = null

        if (fadetype == VALUE_CONSTANT) {
            val r = particleinfo.getFloat(1, 0.0f).toFloat()
            val g = particleinfo.getFloat(2, 0.0f).toFloat()
            val b = particleinfo.getFloat(3, 0.0f).toFloat()
            val a = particleinfo.getFloat(4, 0.0f).toFloat()
            fadeinit = FadeRateInitializerConstant(ColorRGBA(r, g, b, a))
            mUseColor = true
        } else if (fadetype == VALUE_RANDOM) {
            val minr = particleinfo.getFloat(1, 0.0f).toFloat()
            val ming = particleinfo.getFloat(2, 0.0f).toFloat()
            val minb = particleinfo.getFloat(3, 0.0f).toFloat()
            val mina = particleinfo.getFloat(4, 0.0f).toFloat()
            val maxr = particleinfo.getFloat(5, 0.0f).toFloat()
            val maxg = particleinfo.getFloat(6, 0.0f).toFloat()
            val maxb = particleinfo.getFloat(7, 0.0f).toFloat()
            val maxa = particleinfo.getFloat(8, 0.0f).toFloat()

            fadeinit = FadeRateInitializerRandom(ColorRGBA(minr, ming, minb, mina), ColorRGBA(maxr, maxg, maxb, maxa))
            mUseColor = true
        } else if (fadetype == VALUE_LIST) {
            val count = particleinfo.getInt(1, 0)
            val array = arrayOfNulls<ColorRGBA>(count)
            var index = 2
            for (i in 0 until count) {
                val r = particleinfo.getFloat(index++, 0.0f)
                val g = particleinfo.getFloat(index++, 0.0f)
                val b = particleinfo.getFloat(index++, 0.0f)
                val a = particleinfo.getFloat(index++, 0.0f)
                array[i] = ColorRGBA(r, g, b, a)
            }
            fadeinit = FadeRateInitializerList(array)
            mUseColor = true
        } else {
            fadeinit = FadeRateInitializer()
        }

        particleinfo.recycle()

        return fadeinit
    }


    private fun getRotationInitializer(resources: ResourceManager): RotationInitializer {
        val tmp = baseName + "_rotation"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            return RotationInitializer()
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val rotationtype = particleinfo.getInt(0, 0).toInt() //type

        var rotationinit: RotationInitializer? = null
        mUseRotation = true

        if (rotationtype == VALUE_CONSTANT) {
            val rotationx = particleinfo.getFloat(1, 0.0f)
            val rotationy = particleinfo.getFloat(2, 0.0f)
            val rotationz = particleinfo.getFloat(3, 0.0f)
            rotationinit = RotationInitializerConstant(Vector3(rotationx, rotationy, rotationz))
        } else if (rotationtype == VALUE_RANDOM) {
            val minrotationx = particleinfo.getFloat(1, 0.0f)
            val minrotationy = particleinfo.getFloat(2, 0.0f)
            val minrotationz = particleinfo.getFloat(3, 0.0f)
            val maxrotationx = particleinfo.getFloat(4, 0.0f)
            val maxrotationy = particleinfo.getFloat(5, 0.0f)
            val maxrotationz = particleinfo.getFloat(6, 0.0f)

            rotationinit = RotationInitializerRandom(Vector3(minrotationx, minrotationy, minrotationz), Vector3(maxrotationx, maxrotationy, maxrotationz))
        } else if (rotationtype == VALUE_LIST) {
            val count = particleinfo.getInt(1, 0)
            val array = arrayOfNulls<Vector3>(count)
            var index = 2
            for (i in 0 until count) {
                val rotationx = particleinfo.getFloat(index++, 0.0f)
                val rotationy = particleinfo.getFloat(index++, 0.0f)
                val rotationz = particleinfo.getFloat(index++, 0.0f)
                array[i] = Vector3(rotationx, rotationy, rotationz)
            }
            rotationinit = RotationInitializerList(array)
        } else {
            rotationinit = RotationInitializer()
            mUseRotation = false
        }
        particleinfo.recycle()

        return rotationinit
    }


    private fun getRotationIncreaseInitializer(resources: ResourceManager): RotationIncreaseInitializer {
        val tmp = baseName + "_rotationincrease"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            return RotationIncreaseInitializer()
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val rotationtype = particleinfo.getInt(0, 0).toInt() //type

        var rotationinit: RotationIncreaseInitializer? = null

        if (rotationtype == VALUE_CONSTANT) {
            val rotationx = particleinfo.getFloat(1, 0.0f)
            val rotationy = particleinfo.getFloat(2, 0.0f)
            val rotationz = particleinfo.getFloat(3, 0.0f)
            rotationinit = RotationIncreaseInitializerConstant(Vector3(rotationx, rotationy, rotationz))
            mUseRotation = true
        } else if (rotationtype == VALUE_RANDOM) {
            val minrotationx = particleinfo.getFloat(1, 0.0f)
            val minrotationy = particleinfo.getFloat(2, 0.0f)
            val minrotationz = particleinfo.getFloat(3, 0.0f)
            val maxrotationx = particleinfo.getFloat(4, 0.0f)
            val maxrotationy = particleinfo.getFloat(5, 0.0f)
            val maxrotationz = particleinfo.getFloat(6, 0.0f)

            rotationinit = RotationIncreaseInitializerRandom(Vector3(minrotationx, minrotationy, minrotationz), Vector3(maxrotationx, maxrotationy, maxrotationz))
            mUseRotation = true
        } else if (rotationtype == VALUE_LIST) {
            val count = particleinfo.getInt(1, 0)
            val array = arrayOfNulls<Vector3>(count)
            var index = 2
            for (i in 0 until count) {
                val rotationx = particleinfo.getFloat(index++, 0.0f)
                val rotationy = particleinfo.getFloat(index++, 0.0f)
                val rotationz = particleinfo.getFloat(index++, 0.0f)
                array[i] = Vector3(rotationx, rotationy, rotationz)
            }
            rotationinit = RotationIncreaseInitializerList(array)
            mUseRotation = true
        } else {
            rotationinit = RotationIncreaseInitializer()
        }
        particleinfo.recycle()

        return rotationinit
    }


    private fun getTTLInitializer(resources: ResourceManager): TTLInitializer {

        val tmp = baseName + "_ttl"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            return TTLInitializer()
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val ttltype = particleinfo.getInt(0, 0).toInt() //type

        var ttlinit: TTLInitializer? = null

        if (ttltype == VALUE_CONSTANT) {
            val ttl = particleinfo.getFloat(1, 0.0f).toFloat()
            ttlinit = TTLInitializerConstant(ttl)
        } else if (ttltype == VALUE_RANDOM) {
            val minttl = particleinfo.getFloat(1, 0.0f).toFloat()
            val maxttl = particleinfo.getFloat(2, 0.0f).toFloat()

            ttlinit = TTLInitializerRandom(minttl, maxttl)
        } else if (ttltype == VALUE_LIST) {
            val count = particleinfo.getInt(1, 0)
            val array = FloatArray(count)
            var index = 2
            for (i in 0 until count) {
                array[i] = particleinfo.getFloat(index++, 0.0f)
            }
            ttlinit = TTLInitializerList(array)
        } else {
            ttlinit = TTLInitializer()
        }

        particleinfo.recycle()

        return ttlinit
    }


    fun LoadContent(gl: GL10, resources: ResourceManager) {


        val id = resources.context.resources.getIdentifier(baseName, "array", packageName)
        size = MAX_PARTICLES

        if (id != 0) {

            val particleinfo = resources.context.resources.obtainTypedArray(id)
            size = particleinfo.getInt(0, 0).toInt() //max particles per emitter
            mUnitType = particleinfo.getInt(1, 0).toInt() //pixels, meters or percentage

            var sfactor = SRC_BLEND_DEFAULT
            var dfactor = DST_BLEND_DEFAULT

            if (particleinfo.length() > 3) {

                sfactor = particleinfo.getInt(2, SRC_BLEND_DEFAULT).toInt()
                dfactor = particleinfo.getInt(3, DST_BLEND_DEFAULT).toInt()
            }

            when (sfactor) {
                SRC_BLEND_ZERO -> mSrcBlendMode = GLES10.GL_ZERO
                SRC_BLEND_ONE -> mSrcBlendMode = GLES10.GL_ONE
                SRC_BLEND_DST_COLOR -> mSrcBlendMode = GLES10.GL_DST_COLOR
                SRC_BLEND_ONE_MINUS_DST_COLOR -> mSrcBlendMode = GLES10.GL_ONE_MINUS_DST_COLOR
                SRC_BLEND_SRC_ALPHA -> mSrcBlendMode = GLES10.GL_SRC_ALPHA
                SRC_BLEND_ONE_MINUS_SRC_ALPHA -> mSrcBlendMode = GLES10.GL_ONE_MINUS_SRC_ALPHA
                SRC_BLEND_DST_ALPHA -> mSrcBlendMode = GLES10.GL_DST_ALPHA
                SRC_BLEND_ONE_MINUS_DST_ALPHA -> mSrcBlendMode = GLES10.GL_ONE_MINUS_DST_ALPHA
                SRC_BLEND_SRC_ALPHA_SATURATE -> mSrcBlendMode = GLES10.GL_SRC_ALPHA_SATURATE
                else -> mSrcBlendMode = GLES10.GL_SRC_ALPHA
            }

            when (dfactor) {
                DST_BLEND_ZERO -> mDstBlendMode = GLES10.GL_ZERO
                DST_BLEND_ONE -> mDstBlendMode = GLES10.GL_ONE
                DST_BLEND_SRC_COLOR -> mDstBlendMode = GLES10.GL_SRC_COLOR
                DST_BLEND_ONE_MINUS_SRC_COLOR -> mDstBlendMode = GLES10.GL_ONE_MINUS_SRC_COLOR
                DST_BLEND_SRC_ALPHA -> mDstBlendMode = GLES10.GL_SRC_ALPHA
                DST_BLEND_ONE_MINUS_SRC_ALPHA -> mDstBlendMode = GLES10.GL_ONE_MINUS_SRC_ALPHA
                DST_BLEND_DST_ALPHA -> mDstBlendMode = GLES10.GL_DST_ALPHA
                DST_BLEND_ONE_MINUS_DST_ALPHA -> mDstBlendMode = GLES10.GL_ONE_MINUS_DST_ALPHA
                else -> mDstBlendMode = GLES10.GL_ONE_MINUS_SRC_ALPHA
            }

        }

        if (particlePool == null) {
            particlePool = ParticlePool(size)
        }

        val image = getImageInitializer(gl, resources) ?: return

        shape = getEmitterShape(resources)
        emitterRate = getEmitterRate(resources)

        val speed = getSpeedInitializer(resources)
        val size = getSizeInitializer(resources)
        val sizeIncrease = getSizeIncreaseInitializer(resources)
        val force = getForceInitializer(resources)
        val color = getColorInitializer(resources)
        val fadeRate = getFadeRateInitializer(resources)
        val rotation = getRotationInitializer(resources)
        val rotationIncrease = getRotationIncreaseInitializer(resources)
        val ttl = getTTLInitializer(resources)
        getTrailEmitter(gl, resources)
        getOnDeadEmitter(gl, resources)
        getChildEmitter(gl, resources)


        initializer = ParticleInitializer()
        //initializer.setParentParticle(mParentParticle);

        initializer!!.setImageInitializer(image)
        initializer!!.setSpeedInitializer(speed)
        initializer!!.setSizeInitializer(size)
        initializer!!.setSizeIncreaseInitializer(sizeIncrease)
        initializer!!.setForceInitializer(force)
        initializer!!.setColorInitializer(color)
        initializer!!.setFadeRateInitializer(fadeRate)
        initializer!!.setRotationInitializer(rotation)
        initializer!!.setRotationIncreaseInitializer(rotationIncrease)
        initializer!!.setTTLInitializer(ttl)
        initializer!!.setTrailInitializer(trailEmitter)
        initializer!!.setOnDeadInitializer(onDeadEmitter)

        texture = image.atlas

        mRenderer = ParticleRenderer(this)

        mInitalized = true

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
