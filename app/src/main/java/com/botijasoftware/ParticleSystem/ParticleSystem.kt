package com.botijasoftware.ParticleSystem

import javax.microedition.khronos.opengles.GL10

import android.content.res.TypedArray

import com.botijasoftware.utils.Atlas
import com.botijasoftware.utils.Layout
import com.botijasoftware.utils.ResourceManager
import com.botijasoftware.utils.SimpleTextureAtlas
import com.botijasoftware.utils.Texture
import com.botijasoftware.utils.TextureAtlas

import android.opengl.GLES10
import android.util.Log

class ParticleSystem @JvmOverloads constructor(private val packageName: String, private val baseName: String, layout: Layout, private val mPTM: Int = 1) {

    val isEnabled: Boolean
        external get


    private val mParticleEmitterPtr: Int = 0
    private val mLayout: Layout? = null
    private var mUnitType: Int = 0
    private var mEmitterType: Int = 0

    private var mSharedParticleSystem: ParticleSystem? = null

    init {

        mSharedParticleSystem = null
        mUnitType = EMITTER_UNIT_PIXEL
        mEmitterType = EMITTER_TYPE_NORMAL


        try {
            initCpp()
        } catch (e: UnsatisfiedLinkError) {
            Log.e("ParticleSystem", e.message)
        }

    }

    fun LoadContent(gl: GL10, resources: ResourceManager) {

        val id = resources.context.resources.getIdentifier(baseName, "array", packageName)
        var maxParticles = MAX_PARTICLES

        if (id != 0) {

            val particleinfo = resources.context.resources.obtainTypedArray(id)
            maxParticles = particleinfo.getInt(0, 0).toInt() //max particles per emitter
            mUnitType = particleinfo.getInt(1, 0).toInt() //pixels, meters or percentage
            mEmitterType = particleinfo.getInt(2, 0).toInt() //use billboards?

            if (mEmitterType == EMITTER_TYPE_BILLBOARD)
                setBillboardMode(true)

            var sfactor = SRC_BLEND_DEFAULT
            var dfactor = DST_BLEND_DEFAULT

            if (particleinfo.length() > 4) {

                sfactor = particleinfo.getInt(3, SRC_BLEND_DEFAULT).toInt()
                dfactor = particleinfo.getInt(4, DST_BLEND_DEFAULT).toInt()
            }

            particleinfo.recycle()

            val mSrcBlendMode: Int
            val mDstBlendMode: Int
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

            setBlendMode(mSrcBlendMode, mDstBlendMode)

        }


        getEmitterShape(resources)
        getEmitterRate(resources)
        getTrailEmitter(gl, resources)
        getOnDeadEmitter(gl, resources)
        getChildEmitter(gl, resources)

        getImageInitializer(gl, resources)

        getSizeInitializer(resources)
        getSizeIncreaseInitializer(resources)
        getSpeedInitializer(resources)
        getForceInitializer(resources)
        getColorInitializer(resources)
        getFadeRateInitializer(resources)
        getRotationInitializer(resources)
        getAngularSpeedInitializer(resources)
        getAngularAccelerationInitializer(resources)
        getTTLInitializer(resources)

        if (mSharedParticleSystem != null)
            shareParticlePool(mSharedParticleSystem)
        else
            createParticlePool(maxParticles)
    }


    fun setSharedParticlePool(shared: ParticleSystem) {
        mSharedParticleSystem = shared
    }

    private fun getEmitterShape(resources: ResourceManager) {
        //emitter shape
        val tmp = baseName + "_shape"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)

        if (id == 0) {
            setEmitterShapePoint(0.0f, 0.0f, 0.0f)
            return
        }
        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val shapetype = particleinfo.getInt(0, 0).toInt() //shape type
        val shapedist = particleinfo.getInt(1, 0).toInt() //shape distribution -- ignored by now


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

            setEmitterShapePoint(x, y, z)
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

            setEmitterShapeSphere(x, y, z, radius)
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

            setEmitterShapeCube(x, y, z, w, h, d)
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

            setEmitterShapeLine(x0, y0, z0, x1, y1, z1)
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

            setEmitterShapeCircle(x, y, radius)
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

            setEmitterShapeRectangle(x, y, w, h)
        } else {
            setEmitterShapePoint(0.0f, 0.0f, 0.0f)
        }

        particleinfo.recycle()

    }

    private fun getEmitterRate(resources: ResourceManager) {

        val tmp = baseName + "_rate"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            setEmitterRateConstant(0f)
            return
        }
        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val ratetype = particleinfo.getInt(0, 0).toInt() //rate type


        if (ratetype == EMITTER_RATE_CONSTANT) {
            val rate = particleinfo.getFloat(1, 0.0f)
            setEmitterRateConstant(rate)
        } else if (ratetype == EMITTER_RATE_ONDEMAND) {
            val min = particleinfo.getFloat(1, 0.0f)
            val max = particleinfo.getFloat(2, 0.0f)
            setEmitterRateOnDemand(min, max)
        } else if (ratetype == EMITTER_RATE_RANDOM) {
            val min = particleinfo.getFloat(1, 0.0f)
            val max = particleinfo.getFloat(2, 0.0f)
            setEmitterRateRandom(min, max)
        } else {
            setEmitterRateConstant(0.0f)
        }

        particleinfo.recycle()

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


        val emitter = ParticleSystem(packageName, trailemitter, mLayout, mPTM)
        emitter.LoadContent(gl, resources)
        //mUseTrail = true;
        setTrailEmitter(emitter)
    }

    private fun getOnDeadEmitter(gl: GL10, resources: ResourceManager) {

        val tmp = baseName + "_ondead"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            return
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val ondeademitter = particleinfo.getString(0)


        particleinfo.recycle()


        val emitter = ParticleSystem(packageName, ondeademitter, mLayout, mPTM)
        emitter.LoadContent(gl, resources)
        //mUseTrail = true;
        setOnDeadEmitter(emitter)
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


        val emitter = ParticleSystem(packageName, childemitter, mLayout, mPTM)
        emitter.LoadContent(gl, resources)
        //mUseTrail = true;
        setChildEmitter(emitter)
    }

    private fun getImageInitializer(gl: GL10, resources: ResourceManager) { //falta lectura de Atlas XML

        val tmp = baseName + "_image"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            return
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val type = particleinfo.getInt(0, 0).toInt()
        val imagename = particleinfo.getString(1)

        val drawableid = resources.context.resources.getIdentifier(imagename, "drawable", packageName)
        if (drawableid == 0)
            return

        val texture = resources.loadTexture(gl.toInt(), drawableid)
        var atlas: Atlas? = null

        var nextvalue = 0
        if (type == IMAGE_TYPE_TEXTURE) {
            setImageInitializerTexture(texture.getID(), texture.width, texture.height)
        } else if (type == IMAGE_TYPE_SIMPLE_ATLAS) {

            val atlasw = particleinfo.getInt(2, 0).toInt()
            val atlash = particleinfo.getInt(3, 0).toInt()
            val atlastotal = particleinfo.getInt(4, 0).toInt()
            atlas = SimpleTextureAtlas(texture, atlasw, atlash, atlastotal)
            nextvalue = 5

            val animtype = particleinfo.getInt(nextvalue++, 0).toInt()
            if (animtype == IMAGE_SELECTION_RANDOM) {
                setImageInitializerRandomAtlasSimple(texture.getID(), texture.width, texture.height, atlasw, atlash, atlastotal, atlastotal)
            } else if (animtype == IMAGE_SELECTION_ANIMATION) {
                val animtime = particleinfo.getFloat(nextvalue++, 0.0f)
                val animloop = particleinfo.getBoolean(nextvalue++, false)
                setImageInitializerAnimationAtlasSimple(texture.getID(), texture.width, texture.height, atlasw, atlash, atlastotal, animtime, animloop)
            } else {
                setImageInitializerTexture(texture.getID(), texture.width, texture.height)
                //mUseAnimation = false;
            }
        } else if (type == IMAGE_TYPE_TEXTURE_ATLAS) {
            val atlasdef = particleinfo.getString(2)
            val atlasdefid = resources.context.resources.getIdentifier(atlasdef, "array", packageName)
            atlas = TextureAtlas(texture, atlasdefid)
            nextvalue = 3

            val animtype = particleinfo.getInt(nextvalue++, 0).toInt()
            if (animtype == IMAGE_SELECTION_RANDOM) {
                //imageinit = new ImageInitializerRandom(atlas, atlas.getTextureCount());
            } else if (animtype == IMAGE_SELECTION_ANIMATION) {
                val animtime = particleinfo.getFloat(nextvalue++, 0.0f)
                val animloop = particleinfo.getBoolean(nextvalue++, false)
                //imageinit = new ImageInitializerAnimation(atlas, animtime, animloop);
            } else {
                //imageinit = new ImageInitializer(atlas);
                //mUseAnimation = false;
            }
        }

        particleinfo.recycle()
    }

    private fun getSizeInitializer(resources: ResourceManager) {

        val tmp = baseName + "_size"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            setSizeInitializerConstant(0.0f, 0.0f)
            return
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val sizetype = particleinfo.getInt(0, 0).toInt() //type

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

            setSizeInitializerConstant(sizex, sizey)
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

            setSizeInitializerRandom(minsizex, minsizey, maxsizex, maxsizey)
        } else if (sizetype == VALUE_LIST) {
            val count = particleinfo.getInt(1, 0)
            val array = FloatArray(count * 2)
            var index = 2
            var i = 0
            while (i < count * 2) {
                var sizex = particleinfo.getFloat(index++, 0.0f)
                var sizey = particleinfo.getFloat(index++, 0.0f)

                if (mUnitType == EMITTER_UNIT_PERCENT && mLayout != null) {
                    sizex = mLayout.getHorizontal(sizex)
                    sizey = mLayout.getVertical(sizey)
                } else if (mUnitType == EMITTER_UNIT_METER) {
                    sizex *= mPTM.toFloat()
                    sizey *= mPTM.toFloat()
                }

                array[i] = sizex
                array[i + 1] = sizey
                i += 2
            }
            setSizeInitializerList(count, array)
        } else {
            setSizeInitializerConstant(0.0f, 0.0f)
        }
        particleinfo.recycle()

    }

    private fun getSizeIncreaseInitializer(resources: ResourceManager) {
        val tmp = baseName + "_sizeincrease"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            setSizeIncreaseInitializerConstant(0.0f, 0.0f)
            return
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val sizetype = particleinfo.getInt(0, 0).toInt() //type

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

            setSizeIncreaseInitializerConstant(sizex, sizey)
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

            setSizeIncreaseInitializerRandom(minsizex, minsizey, maxsizex, maxsizey)
        } else if (sizetype == VALUE_LIST) {
            val count = particleinfo.getInt(1, 0)
            val array = FloatArray(count * 2)
            var index = 2
            var i = 0
            while (i < count * 2) {
                var sizex = particleinfo.getFloat(index++, 0.0f)
                var sizey = particleinfo.getFloat(index++, 0.0f)

                if (mUnitType == EMITTER_UNIT_PERCENT && mLayout != null) {
                    sizex = mLayout.getHorizontal(sizex)
                    sizey = mLayout.getVertical(sizey)
                } else if (mUnitType == EMITTER_UNIT_METER) {
                    sizex *= mPTM.toFloat()
                    sizey *= mPTM.toFloat()
                }

                array[i] = sizex
                array[i + 1] = sizey
                i += 2
            }
            setSizeIncreaseInitializerList(count, array)
        } else {
            setSizeIncreaseInitializerConstant(0.0f, 0.0f)
        }
        particleinfo.recycle()

    }

    private fun getSpeedInitializer(resources: ResourceManager) {

        val tmp = baseName + "_speed"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            setSpeedInitializerNull()
            return
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val speedtype = particleinfo.getInt(0, 0).toInt() //type

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

            setSpeedInitializerConstant(speedx, speedy, speedz)
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

            setSpeedInitializerRandom(minspeedx, minspeedy, minspeedz, maxspeedx, maxspeedy, maxspeedz)
        } else if (speedtype == VALUE_LIST) {
            val count = particleinfo.getInt(1, 0)
            val array = FloatArray(count * 3)
            var index = 2
            var i = 0
            while (i < count * 3) {
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

                array[i] = speedx
                array[i + 1] = speedy
                array[i + 2] = speedz
                i += 3
            }
            setSpeedInitializerList(count, array)
        } else {
            setSpeedInitializerNull()
        }
        particleinfo.recycle()

    }

    private fun getForceInitializer(resources: ResourceManager) {
        val tmp = baseName + "_force"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            setForceInitializerNull()
            return
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val forcetype = particleinfo.getInt(0, 0).toInt() //type

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
            setForceInitializerConstant(forcex, forcey, forcez)
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

            setForceInitializerRandom(minforcex, minforcey, minforcez, maxforcex, maxforcey, maxforcez)
        } else if (forcetype == VALUE_LIST) {
            val count = particleinfo.getInt(1, 0)
            val array = FloatArray(count * 3)
            var index = 2
            var i = 0
            while (i < count * 3) {
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

                array[i] = forcex
                array[i + 1] = forcey
                array[i + 2] = forcez
                i += 3
            }
            setForceInitializerList(count, array)
        } else {
            setForceInitializerNull()
        }
        particleinfo.recycle()
    }

    private fun getColorInitializer(resources: ResourceManager) {

        val tmp = baseName + "_color"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            setColorInitializerNull()
            return
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val colortype = particleinfo.getInt(0, 0).toInt() //type

        //mUseColor = true;

        if (colortype == VALUE_CONSTANT) {
            val r = particleinfo.getFloat(1, 0.0f).toFloat()
            val g = particleinfo.getFloat(2, 0.0f).toFloat()
            val b = particleinfo.getFloat(3, 0.0f).toFloat()
            val a = particleinfo.getFloat(4, 0.0f).toFloat()
            setColorInitializerConstant(r, g, b, a)
        } else if (colortype == VALUE_RANDOM) {
            val minr = particleinfo.getFloat(1, 0.0f).toFloat()
            val ming = particleinfo.getFloat(2, 0.0f).toFloat()
            val minb = particleinfo.getFloat(3, 0.0f).toFloat()
            val mina = particleinfo.getFloat(4, 0.0f).toFloat()
            val maxr = particleinfo.getFloat(5, 0.0f).toFloat()
            val maxg = particleinfo.getFloat(6, 0.0f).toFloat()
            val maxb = particleinfo.getFloat(7, 0.0f).toFloat()
            val maxa = particleinfo.getFloat(8, 0.0f).toFloat()

            setColorInitializerRandom(minr, ming, minb, mina, maxr, maxg, maxb, maxa)
        } else if (colortype == VALUE_LIST) {
            val count = particleinfo.getInt(1, 0)
            val array = FloatArray(count * 4)
            var index = 2
            for (i in 0 until count * 4) {
                array[i] = particleinfo.getFloat(index++, 0.0f)

            }
            setColorInitializerList(count, array)
        } else {
            setColorInitializerNull()
            //mUseColor = false;
        }

        particleinfo.recycle()
    }

    private fun getFadeRateInitializer(resources: ResourceManager) {

        val tmp = baseName + "_fade"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            setFadeRateInitializerNull()
            return
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val fadetype = particleinfo.getInt(0, 0).toInt() //type

        if (fadetype == VALUE_CONSTANT) {
            val r = particleinfo.getFloat(1, 0.0f).toFloat()
            val g = particleinfo.getFloat(2, 0.0f).toFloat()
            val b = particleinfo.getFloat(3, 0.0f).toFloat()
            val a = particleinfo.getFloat(4, 0.0f).toFloat()
            setFadeRateInitializerConstant(r, g, b, a)
            //mUseColor = true;
        } else if (fadetype == VALUE_RANDOM) {
            val minr = particleinfo.getFloat(1, 0.0f).toFloat()
            val ming = particleinfo.getFloat(2, 0.0f).toFloat()
            val minb = particleinfo.getFloat(3, 0.0f).toFloat()
            val mina = particleinfo.getFloat(4, 0.0f).toFloat()
            val maxr = particleinfo.getFloat(5, 0.0f).toFloat()
            val maxg = particleinfo.getFloat(6, 0.0f).toFloat()
            val maxb = particleinfo.getFloat(7, 0.0f).toFloat()
            val maxa = particleinfo.getFloat(8, 0.0f).toFloat()

            setFadeRateInitializerRandom(minr, ming, minb, mina, maxr, maxg, maxb, maxa)
            //mUseColor = true;
        } else if (fadetype == VALUE_LIST) {
            val count = particleinfo.getInt(1, 0)
            val array = FloatArray(count * 4)
            var index = 2
            for (i in 0 until count * 4) {
                array[i] = particleinfo.getFloat(index++, 0.0f)
            }
            setFadeRateInitializerList(count, array)
            //mUseColor = true;
        } else {
            setFadeRateInitializerNull()
        }

        particleinfo.recycle()
    }

    private fun getRotationInitializer(resources: ResourceManager) {
        val tmp = baseName + "_rotation"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            setRotationInitializerNull()
            return
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val rotationtype = particleinfo.getInt(0, 0).toInt() //type

        //mUseRotation = true;

        if (rotationtype == VALUE_CONSTANT) {
            val rotationx = particleinfo.getFloat(1, 0.0f)
            val rotationy = particleinfo.getFloat(2, 0.0f)
            val rotationz = particleinfo.getFloat(3, 0.0f)
            setRotationInitializerConstant(rotationx, rotationy, rotationz)
        } else if (rotationtype == VALUE_RANDOM) {
            val minrotationx = particleinfo.getFloat(1, 0.0f)
            val minrotationy = particleinfo.getFloat(2, 0.0f)
            val minrotationz = particleinfo.getFloat(3, 0.0f)
            val maxrotationx = particleinfo.getFloat(4, 0.0f)
            val maxrotationy = particleinfo.getFloat(5, 0.0f)
            val maxrotationz = particleinfo.getFloat(6, 0.0f)

            setRotationInitializerRandom(minrotationx, minrotationy, minrotationz, maxrotationx, maxrotationy, maxrotationz)
        } else if (rotationtype == VALUE_LIST) {
            val count = particleinfo.getInt(1, 0)
            val array = FloatArray(count * 3)
            var index = 2
            for (i in 0 until count * 3) {
                array[i] = particleinfo.getFloat(index++, 0.0f)
            }
            setRotationInitializerList(count, array)
        } else {
            setRotationInitializerNull()
            //mUseRotation = false;
        }
        particleinfo.recycle()

    }

    private fun getAngularSpeedInitializer(resources: ResourceManager) {
        val tmp = baseName + "_angularspeed"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            setAngularSpeedInitializerNull()
            return
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val rotationtype = particleinfo.getInt(0, 0).toInt() //type

        if (rotationtype == VALUE_CONSTANT) {
            val rotationx = particleinfo.getFloat(1, 0.0f)
            val rotationy = particleinfo.getFloat(2, 0.0f)
            val rotationz = particleinfo.getFloat(3, 0.0f)
            setAngularSpeedInitializerConstant(rotationx, rotationy, rotationz)
            //mUseRotation = true;
        } else if (rotationtype == VALUE_RANDOM) {
            val minrotationx = particleinfo.getFloat(1, 0.0f)
            val minrotationy = particleinfo.getFloat(2, 0.0f)
            val minrotationz = particleinfo.getFloat(3, 0.0f)
            val maxrotationx = particleinfo.getFloat(4, 0.0f)
            val maxrotationy = particleinfo.getFloat(5, 0.0f)
            val maxrotationz = particleinfo.getFloat(6, 0.0f)

            setAngularSpeedInitializerRandom(minrotationx, minrotationy, minrotationz, maxrotationx, maxrotationy, maxrotationz)
            //mUseRotation = true;
        } else if (rotationtype == VALUE_LIST) {
            val count = particleinfo.getInt(1, 0)
            val array = FloatArray(count * 3)
            var index = 2
            for (i in 0 until count * 3) {
                array[i] = particleinfo.getFloat(index++, 0.0f)
            }
            setAngularSpeedInitializerList(count, array)
            //mUseRotation = true;
        } else {
            setAngularSpeedInitializerNull()
        }
        particleinfo.recycle()

    }

    private fun getAngularAccelerationInitializer(resources: ResourceManager) {
        val tmp = baseName + "_angularacceleration"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            setAngularAccelerationInitializerNull()
            return
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val rotationtype = particleinfo.getInt(0, 0).toInt() //type

        if (rotationtype == VALUE_CONSTANT) {
            val rotationx = particleinfo.getFloat(1, 0.0f)
            val rotationy = particleinfo.getFloat(2, 0.0f)
            val rotationz = particleinfo.getFloat(3, 0.0f)
            setAngularAccelerationInitializerConstant(rotationx, rotationy, rotationz)
            //mUseRotation = true;
        } else if (rotationtype == VALUE_RANDOM) {
            val minrotationx = particleinfo.getFloat(1, 0.0f)
            val minrotationy = particleinfo.getFloat(2, 0.0f)
            val minrotationz = particleinfo.getFloat(3, 0.0f)
            val maxrotationx = particleinfo.getFloat(4, 0.0f)
            val maxrotationy = particleinfo.getFloat(5, 0.0f)
            val maxrotationz = particleinfo.getFloat(6, 0.0f)

            setAngularAccelerationInitializerRandom(minrotationx, minrotationy, minrotationz, maxrotationx, maxrotationy, maxrotationz)
            //mUseRotation = true;
        } else if (rotationtype == VALUE_LIST) {
            val count = particleinfo.getInt(1, 0)
            val array = FloatArray(count * 3)
            var index = 2
            for (i in 0 until count * 3) {
                array[i] = particleinfo.getFloat(index++, 0.0f)
            }
            setAngularAccelerationInitializerList(count, array)
            //mUseRotation = true;
        } else {
            setAngularAccelerationInitializerNull()
        }
        particleinfo.recycle()

    }

    private fun getTTLInitializer(resources: ResourceManager) {

        val tmp = baseName + "_ttl"
        val id = resources.context.resources.getIdentifier(tmp, "array", packageName)
        if (id == 0) {
            setTTLInitializerNull()
            return
        }

        val particleinfo = resources.context.resources.obtainTypedArray(id)

        val ttltype = particleinfo.getInt(0, 0).toInt() //type

        if (ttltype == VALUE_CONSTANT) {
            val ttl = particleinfo.getFloat(1, 0.0f).toFloat()
            setTTLInitializerConstant(ttl)
        } else if (ttltype == VALUE_RANDOM) {
            val minttl = particleinfo.getFloat(1, 0.0f).toFloat()
            val maxttl = particleinfo.getFloat(2, 0.0f).toFloat()

            setTTLInitializerRandom(minttl, maxttl)
        } else if (ttltype == VALUE_LIST) {
            val count = particleinfo.getInt(1, 0)
            val array = FloatArray(count)
            var index = 2
            for (i in 0 until count) {
                array[i] = particleinfo.getFloat(index++, 0.0f)
            }
            setTTLInitializerList(count, array)
        } else {
            setTTLInitializerNull()
        }

        particleinfo.recycle()
    }

    external fun initCpp()
    external fun detach()

    external fun createParticlePool(size: Int)
    external fun shareParticlePool(shared: ParticleSystem)

    external fun Update(time: Float, emmitparticles: Boolean)

    external fun Draw()

    external fun enable()
    external fun disable()

    external fun emmit(time: Float)
    external fun emmit()
    external fun forceEmmit(count: Int)

    external fun setPosition(x: Float, y: Float, z: Float)
    external fun setSpeed(x: Float, y: Float, z: Float)
    external fun setTexture(id: Int, width: Int, height: Int)
    external fun setAtlasSimple(id: Int, width: Int, height: Int, columns: Int, rows: Int, count: Int)
    external fun setAtlasXML(id: Int, width: Int, height: Int, count: Int, data: FloatArray)
    external fun modifyRate(modifier: Float)
    external fun modifySpeedRate(modifier: Float)
    external fun setBlendMode(src: Int, dst: Int)
    external fun setBillboardMode(billboard: Boolean)

    external fun setEmitterShapePoint(x: Float, y: Float, z: Float)
    external fun setEmitterShapeSphere(x: Float, y: Float, z: Float, r: Float)
    external fun setEmitterShapeCube(x: Float, y: Float, z: Float, w: Float, h: Float, d: Float)
    external fun setEmitterShapeLine(x0: Float, y0: Float, z0: Float, x1: Float, y1: Float, z1: Float)
    external fun setEmitterShapeCircle(x: Float, y: Float, r: Float)
    external fun setEmitterShapeRectangle(x: Float, y: Float, w: Float, h: Float)

    external fun setEmitterRateConstant(rate: Float)
    external fun setEmitterRateOnDemand(minrate: Float, maxrate: Float)
    external fun setEmitterRateRandom(minrate: Float, maxrate: Float)

    external fun setTrailEmitter(emitter: ParticleSystem)
    external fun setOnDeadEmitter(emitter: ParticleSystem)
    external fun setChildEmitter(emitter: ParticleSystem)

    external fun setImageInitializerTexture(id: Int, width: Int, height: Int)
    external fun setImageInitializerRandomAtlasSimple(id: Int, width: Int, height: Int, columns: Int, rows: Int, count: Int, max: Int)
    external fun setImageInitializerRandomAtlasXML(id: Int, width: Int, height: Int, count: Int, data: FloatArray, max: Int)
    external fun setImageInitializerAnimationAtlasSimple(id: Int, width: Int, height: Int, columns: Int, rows: Int, count: Int, frametime: Float, loop: Boolean)
    external fun setImageInitializerAnimationAtlasXML(id: Int, width: Int, height: Int, count: Int, data: FloatArray, frametime: Float, loop: Boolean)

    external fun setSizeInitializerNull()
    external fun setSizeInitializerConstant(x: Float, y: Float)
    external fun setSizeInitializerRandom(minx: Float, miny: Float, maxx: Float, maxy: Float)
    external fun setSizeInitializerList(count: Int, data: FloatArray)

    external fun setSizeIncreaseInitializerNull()
    external fun setSizeIncreaseInitializerConstant(x: Float, y: Float)
    external fun setSizeIncreaseInitializerRandom(minx: Float, miny: Float, maxx: Float, maxy: Float)
    external fun setSizeIncreaseInitializerList(count: Int, data: FloatArray)

    external fun setSpeedInitializerNull()
    external fun setSpeedInitializerConstant(x: Float, y: Float, z: Float)
    external fun setSpeedInitializerRandom(minx: Float, miny: Float, minz: Float, maxx: Float, maxy: Float, maxz: Float)
    external fun setSpeedInitializerList(count: Int, data: FloatArray)

    external fun setForceInitializerNull()
    external fun setForceInitializerConstant(x: Float, y: Float, z: Float)
    external fun setForceInitializerRandom(minx: Float, miny: Float, minz: Float, maxx: Float, maxy: Float, maxz: Float)
    external fun setForceInitializerList(count: Int, data: FloatArray)

    external fun setColorInitializerNull()
    external fun setColorInitializerConstant(r: Float, g: Float, b: Float, a: Float)
    external fun setColorInitializerRandom(minr: Float, ming: Float, minb: Float, mina: Float, maxr: Float, maxg: Float, maxb: Float, maxa: Float)
    external fun setColorInitializerList(count: Int, data: FloatArray)

    external fun setFadeRateInitializerNull()
    external fun setFadeRateInitializerConstant(r: Float, g: Float, b: Float, a: Float)
    external fun setFadeRateInitializerRandom(minr: Float, ming: Float, minb: Float, mina: Float, maxr: Float, maxg: Float, maxb: Float, maxa: Float)
    external fun setFadeRateInitializerList(count: Int, data: FloatArray)

    external fun setRotationInitializerNull()
    external fun setRotationInitializerConstant(x: Float, y: Float, z: Float)
    external fun setRotationInitializerRandom(minx: Float, miny: Float, minz: Float, maxx: Float, maxy: Float, maxz: Float)
    external fun setRotationInitializerList(count: Int, data: FloatArray)

    external fun setAngularSpeedInitializerNull()
    external fun setAngularSpeedInitializerConstant(x: Float, y: Float, z: Float)
    external fun setAngularSpeedInitializerRandom(minx: Float, miny: Float, minz: Float, maxx: Float, maxy: Float, maxz: Float)
    external fun setAngularSpeedInitializerList(count: Int, data: FloatArray)

    external fun setAngularAccelerationInitializerNull()
    external fun setAngularAccelerationInitializerConstant(x: Float, y: Float, z: Float)
    external fun setAngularAccelerationInitializerRandom(minx: Float, miny: Float, minz: Float, maxx: Float, maxy: Float, maxz: Float)
    external fun setAngularAccelerationInitializerList(count: Int, data: FloatArray)

    external fun setTTLInitializerNull()
    external fun setTTLInitializerConstant(t: Float)
    external fun setTTLInitializerRandom(mint: Float, maxt: Float)
    external fun setTTLInitializerList(count: Int, data: FloatArray)

    companion object {

        init {
            try {
                System.loadLibrary("botijautils")
            } catch (e: UnsatisfiedLinkError) {
                Log.e("ParticleSystem", e.message)
            }

        }


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

        protected val EMITTER_TYPE_NORMAL = 0
        protected val EMITTER_TYPE_BILLBOARD = 1

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
	
	
