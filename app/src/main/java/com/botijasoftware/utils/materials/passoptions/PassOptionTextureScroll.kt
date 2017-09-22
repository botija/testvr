package com.botijasoftware.utils.materials.passoptions

import android.opengl.GLES10

import com.botijasoftware.utils.materials.Expression
import com.botijasoftware.utils.materials.Material

class PassOptionTextureScroll(internal var scrollx: Expression, internal var scrolly: Expression) : PassOption() {

    override fun set(material: Material) {
        GLES10.glMatrixMode(GLES10.GL_TEXTURE)
        GLES10.glPushMatrix()
        GLES10.glTranslatef(scrollx.evaluate(material), scrolly.evaluate(material), 0.0f)
    }

    override fun unSet() {
        GLES10.glMatrixMode(GLES10.GL_TEXTURE)
        GLES10.glPopMatrix()
        GLES10.glMatrixMode(GLES10.GL_MODELVIEW)
    }

}