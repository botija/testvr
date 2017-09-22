package com.botijasoftware.utils

import javax.microedition.khronos.opengles.GL10

import android.content.res.TypedArray

class TextureAtlas(texture: Texture, protected var atlasDef: Int) : Atlas(texture) {

    init {
        textureCount = 0
    }

    /*loads atlas definition from xml file*/
    fun LoadContent(gl: GL10, resources: ResourceManager) {
        val atlasinfo = resources.context.resources.obtainTypedArray(atlasDef)
        textureCount = atlasinfo.getInt(0, 0)

        mCoords = Array<TextureCoords>(textureCount, { i -> TextureCoords(atlasinfo.getFloat(i*4, 0.0f), atlasinfo.getFloat(i*4 + 1, 0.0f), atlasinfo.getFloat(i*4 + 2, 0.0f), atlasinfo.getFloat(i*4 + 3, 0.0f)) } )

        /*var i = 1
        var j = 0
        while (i <= textureCount * 4) {
            mCoords[j] = TextureCoords(atlasinfo.getFloat(i, 0.0f), atlasinfo.getFloat(i + 1, 0.0f), atlasinfo.getFloat(i + 2, 0.0f), atlasinfo.getFloat(i + 3, 0.0f))
            i += 4
            j++
        }*/
        atlasinfo.recycle()
    }

}

