package com.botijasoftware.utils.materials.passoptions

import android.opengl.GLES10

import com.botijasoftware.utils.materials.Material

class PassOptionMaskDepth : PassOption() {

    override fun set(material: Material) {
        GLES10.glDepthMask(true)
    }

    override fun unSet() {
        GLES10.glDepthMask(false)
    }

}