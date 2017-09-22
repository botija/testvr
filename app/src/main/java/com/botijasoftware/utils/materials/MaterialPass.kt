package com.botijasoftware.utils.materials

import java.util.ArrayList

import android.opengl.GLES20

import com.botijasoftware.utils.ColorRGB
import com.botijasoftware.utils.ResourceManager
import com.botijasoftware.utils.Texture
import com.botijasoftware.utils.TextureOptions
import com.botijasoftware.utils.materials.passoptions.PassOption

class MaterialPass(protected var material: Material, private val textureid: Int) {

    protected lateinit var condition: PassCondition
    protected var options = ArrayList<PassOption>()
    private var lastCheck: Boolean = false
    private var textureoptions = TextureOptions.default_options

    lateinit var mTexture: Texture
    var mBlendMode: Int = 0
    var mAlpha: Float = 0.toFloat()
    var mColor: ColorRGB

    var srcblend: Int = 0
    var dstblend: Int = 0

    fun LoadContent(resources: ResourceManager) {
        mTexture = resources.loadTexture(textureid, textureoptions)
    }

    init {
        this.mAlpha = 1.0f
        this.mColor = ColorRGB(1f, 1f, 1f)
        setBlendMode(BLEND_NOBLEND)
    }

    fun setTextureOptions(textureoptions: TextureOptions) {
        this.textureoptions = textureoptions
    }

    fun setBlendMode(mode: Int) {
        mBlendMode = mode
        when (mode) {
            BLEND_NONE -> {
                srcblend = GLES20.GL_ZERO
                dstblend = GLES20.GL_ONE
            }
            BLEND_ADD -> {
                srcblend = GLES20.GL_ONE
                dstblend = GLES20.GL_ONE
            }
            BLEND_BLEND -> {
                srcblend = GLES20.GL_SRC_ALPHA
                dstblend = GLES20.GL_ONE_MINUS_SRC_ALPHA
            }
            BLEND_MODULATE -> {
                srcblend = GLES20.GL_DST_COLOR
                dstblend = GLES20.GL_ZERO
            }
            BLEND_FILTER -> {
                srcblend = GLES20.GL_DST_COLOR
                dstblend = GLES20.GL_ZERO
            }
            else -> {
                srcblend = GLES20.GL_ONE
                dstblend = GLES20.GL_ONE
            }
        }
    }

    fun setBlendMode(src: Int, dst: Int) {
        mBlendMode = BLEND_CUSTOM
        srcblend = src
        dstblend = dst
    }

    fun addOption(op: PassOption): MaterialPass {
        options.add(op)
        return this
    }


    fun setCondtion(condition: PassCondition): MaterialPass {
        this.condition = condition
        return this
    }

    fun set(): Boolean {

        lastCheck = true
        if (condition != null)
            lastCheck = condition.check(material)

        if (!lastCheck)
            return false

        val size = options.size
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture.id)
        if (mBlendMode == BLEND_NOBLEND) {
            GLES20.glDisable(GLES20.GL_BLEND)
        } else {
            GLES20.glEnable(GLES20.GL_BLEND)
            GLES20.glBlendFunc(dstblend, srcblend)
        }
        for (i in 0..size - 1) {
            options[i].set(material)
        }

        //GLES20.glColor4f(mColor.R, mColor.G, mColor.B, mAlpha);

        return true
    }

    fun unSet() {
        if (!lastCheck)
            return
        GLES20.glDisable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
        val size = options.size - 1
        for (i in size downTo 0) {
            options[i].unSet()
        }
        //GLES20.glColor4f(1, 1, 1, 1);
    }

    companion object {

        val BLEND_NOBLEND = 0
        val BLEND_NONE = 1
        val BLEND_ADD = 2
        val BLEND_BLEND = 3
        val BLEND_FILTER = 4
        val BLEND_MODULATE = 5
        val BLEND_CUSTOM = 6
    }


}
