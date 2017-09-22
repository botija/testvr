package com.botijasoftware.utils.materials

import java.util.ArrayList
import com.botijasoftware.utils.ColorRGB
import com.botijasoftware.utils.ColorRGBA
import com.botijasoftware.utils.ResourceManager

import android.opengl.GLES10

class Material {

    var mPass: ArrayList<MaterialPass>
    var mDiffuseColor: ColorRGBA
    var mAmbientColor: ColorRGB
    var mEmissionColor: ColorRGB
    var mShininess: Float = 0.toFloat()
    var mSpecularColor: ColorRGB
    var isLoaded = false
        private set
    private val color = FloatArray(4)
    var mLocalArgs = FloatArray(MAX_LOCAL_PARAMETERS)


    var sort = SORT_SOLID
    var mask = 0
    var alphatest = 1.0f
    var polygonoffset = 0.0f

    fun addMask(maskbits: Int) {
        mask = mask or maskbits
    }

    fun checkMask(maskbits: Int): Boolean {
        return mask and maskbits != 0
    }

    init {
        mPass = ArrayList<MaterialPass>()
        mDiffuseColor = ColorRGBA(1f, 1f, 1f, 1f)
        mAmbientColor = ColorRGB(1f, 1f, 1f)
        mShininess = 128f
        mSpecularColor = ColorRGB(1f, 1f, 1f)
        mEmissionColor = ColorRGB(1f, 1f, 1f)
    }

    fun LoadContent(resources: ResourceManager) {
        for (i in mPass.indices)
            mPass[i].LoadContent(resources)

        isLoaded = true
    }

    fun setDiffuseColor(r: Float, g: Float, b: Float) {
        mDiffuseColor.R = r
        mDiffuseColor.G = g
        mDiffuseColor.B = b
    }

    fun setAmbientColor(r: Float, g: Float, b: Float) {
        mAmbientColor.R = r
        mAmbientColor.G = g
        mAmbientColor.B = b
    }

    fun setShininess(shininess: Float) {
        mShininess = shininess
    }

    fun setSpecularColor(r: Float, g: Float, b: Float) {
        mSpecularColor.R = r
        mSpecularColor.G = g
        mSpecularColor.B = b
    }

    fun setEmissionColor(r: Float, g: Float, b: Float) {
        mEmissionColor.R = r
        mEmissionColor.G = g
        mEmissionColor.B = b
    }


    fun set() {

        if (mask and MAT_FLAT != 0) GLES10.glShadeModel(GLES10.GL_FLAT)
        if (mask and MAT_TWOSIDED != 0)
            GLES10.glDisable(GLES10.GL_CULL_FACE)
        else if (mask and MAT_BACKSIDED != 0) GLES10.glCullFace(GLES10.GL_CCW)
        if (mask and MAT_NODEPTHTEST != 0) GLES10.glDisable(GLES10.GL_DEPTH_TEST)
        if (mask and MAT_MASKDEPTH != 0) GLES10.glDepthMask(true)
        if (mask and MAT_NOBLEND != 0) GLES10.glDisable(GLES10.GL_BLEND)
        if (mask and MAT_NOLIGHTS != 0) GLES10.glDisable(GLES10.GL_LIGHTING)
        if (mask and MAT_ALPHATEST != 0) {
            GLES10.glEnable(GLES10.GL_ALPHA_TEST)
            GLES10.glAlphaFunc(GLES10.GL_GREATER, alphatest)
        }

        if (mask and MAT_POLYGONOFFSET != 0) {
            GLES10.glEnable(GLES10.GL_POLYGON_OFFSET_FILL)
            GLES10.glPolygonOffset(0.0f, polygonoffset)
        }

        GLES10.glDepthFunc(GLES10.GL_LEQUAL)

        GLES10.glColor4f(mDiffuseColor.R, mDiffuseColor.G, mDiffuseColor.B, mDiffuseColor.A)
        /*color[0] = mDiffuseColor.R;
		color[1] = mDiffuseColor.G;
		color[2] = mDiffuseColor.B;
		color[3] = 1.0f;
		GLES10.glMaterialfv(GLES10.GL_FRONT_AND_BACK, GLES10.GL_DIFFUSE, color,0);
		color[0] = mAmbientColor.R;
		color[1] = mAmbientColor.G;
		color[2] = mAmbientColor.B;
		GLES10.glMaterialfv(GLES10.GL_FRONT_AND_BACK, GLES10.GL_AMBIENT, color,0);
		color[0] = mSpecularColor.R;
		color[1] = mSpecularColor.G;
		color[2] = mSpecularColor.B;
		GLES10.glMaterialfv(GLES10.GL_FRONT_AND_BACK, GLES10.GL_SPECULAR, color,0);
		color[0] = mEmissionColor.R;
		color[1] = mEmissionColor.G;
		color[2] = mEmissionColor.B;
		GLES10.glMaterialfv(GLES10.GL_FRONT_AND_BACK, GLES10.GL_EMISSION, color,0);
		GLES10.glMaterialf(GLES10.GL_FRONT_AND_BACK, GLES10.GL_SHININESS, mShininess);
		GLES10.glEnable(GLES10.GL_COLOR_MATERIAL);*/

    }

    fun unSet() {

        if (mask and MAT_FLAT != 0) GLES10.glShadeModel(GLES10.GL_SMOOTH)
        if (mask and MAT_TWOSIDED != 0)
            GLES10.glEnable(GLES10.GL_CULL_FACE)
        else if (mask and MAT_BACKSIDED != 0) GLES10.glCullFace(GLES10.GL_CW)
        if (mask and MAT_NODEPTHTEST != 0) GLES10.glEnable(GLES10.GL_DEPTH_TEST)
        if (mask and MAT_MASKDEPTH != 0) GLES10.glDepthMask(false)
        if (mask and MAT_NOBLEND != 0) GLES10.glEnable(GLES10.GL_BLEND)
        if (mask and MAT_NOLIGHTS != 0) GLES10.glEnable(GLES10.GL_LIGHTING)
        if (mask and MAT_ALPHATEST != 0) GLES10.glDisable(GLES10.GL_ALPHA_TEST)
        if (mask and MAT_POLYGONOFFSET != 0) GLES10.glDisable(GLES10.GL_POLYGON_OFFSET_FILL)

        GLES10.glColor4f(1f, 1f, 1f, 1f)
        //GLES10.glMaterialf(GLES10.GL_FRONT_AND_BACK, GLES10.GL_SHININESS, 1);
        //GLES10.glDisable(GLES10.GL_COLOR_MATERIAL);


    }

    fun addPass(pass: MaterialPass) {
        mPass.add(pass)
    }

    fun passCount(): Int {
        return mPass.size
    }

    fun getTime(): Float {
        return Material.time
    }

    fun getGlobalArg(index: Int): Float {
        if (index > 0 && index < MAX_GLOBAL_PARAMETERS) {
            return Material.mGlobalArgs[index]
        }
        return 0.0f
    }

    fun getLocalArg(index: Int): Float {
        if (index > 0 && index < MAX_LOCAL_PARAMETERS) {
            return mLocalArgs[index]
        }
        return 0.0f
    }

    fun setGlobalArg(index: Int, `val`: Float) {
        if (index > 0 && index < MAX_GLOBAL_PARAMETERS) {
            Material.mGlobalArgs[index] = `val`
        }
    }

    fun setLocalArg(index: Int, `val`: Float) {
        if (index > 0 && index < MAX_LOCAL_PARAMETERS) {
            mLocalArgs[index] = `val`
        }
    }

    companion object {

        val MAX_GLOBAL_PARAMETERS = 16
        val MAX_LOCAL_PARAMETERS = 4
        var mGlobalArgs = FloatArray(MAX_GLOBAL_PARAMETERS)
        var time: Float = 0.toFloat()


        val MAT_VISIBLE = 0x01
        val MAT_NOSHADOWS = 0x02
        val MAT_NODEPTHTEST = 0x04
        val MAT_TRANSLUCENT = 0x08
        val MAT_ALPHATEST = 0x10
        val MAT_MASKDEPTH = 0x20
        val MAT_NOBLEND = 0x40
        val MAT_NOLIGHTS = 0x80
        val MAT_FLAT = 0x100
        val MAT_BACKSIDED = 0x200
        val MAT_TWOSIDED = 0x400
        val MAT_POLYGONOFFSET = 0x800


        val SORT_BACKGROUND = 0
        val SORT_SOLID = 1
        val SORT_TRANSPARENT = 2
        val SORT_SCREEN = 3
        val SORT_POSTPROCESS = 4
    }
}
