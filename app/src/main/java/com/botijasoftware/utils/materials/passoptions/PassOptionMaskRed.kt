package com.botijasoftware.utils.materials.passoptions

import android.opengl.GLES10

import com.botijasoftware.utils.materials.Material

class PassOptionMaskRed : PassOption() {

    override fun set(material: Material) {
        GLES10.glColorMask(false, true, true, true)
    }

    override fun unSet() {
        GLES10.glColorMask(true, true, true, true)
    }

}