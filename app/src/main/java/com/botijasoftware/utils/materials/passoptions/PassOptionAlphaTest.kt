package com.botijasoftware.utils.materials.passoptions

import android.opengl.GLES10

import com.botijasoftware.utils.materials.Expression
import com.botijasoftware.utils.materials.Material

class PassOptionAlphaTest(internal var alphatest: Expression) : PassOption() {

    override fun set(material: Material) {
        GLES10.glAlphaFunc(GLES10.GL_GREATER, alphatest.evaluate(material))
        GLES10.glEnable(GLES10.GL_ALPHA_TEST)
    }

    override fun unSet() {
        GLES10.glDisable(GLES10.GL_ALPHA_TEST)
    }

}