package com.botijasoftware.utils.materials.passoptions

import android.opengl.GLES10

import com.botijasoftware.utils.materials.Expression
import com.botijasoftware.utils.materials.Material

class PassOptionTextureScale(internal var scalex: Expression, internal var scaley: Expression) : PassOption() {

    override fun set(material: Material) {
        GLES10.glMatrixMode(GLES10.GL_TEXTURE)
        GLES10.glPushMatrix()
        GLES10.glScalef(scalex.evaluate(material), scaley.evaluate(material), 0.0f)
        GLES10.glMatrixMode(GLES10.GL_MODELVIEW)
    }

    override fun unSet() {
        GLES10.glMatrixMode(GLES10.GL_TEXTURE)
        GLES10.glPopMatrix()
        GLES10.glMatrixMode(GLES10.GL_MODELVIEW)
    }

}