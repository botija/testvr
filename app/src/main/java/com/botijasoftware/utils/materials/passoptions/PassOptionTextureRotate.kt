package com.botijasoftware.utils.materials.passoptions

import android.opengl.GLES10

import com.botijasoftware.utils.materials.Expression
import com.botijasoftware.utils.materials.Material

class PassOptionTextureRotate(internal var rotation: Expression) : PassOption() {

    override fun set(material: Material) {
        GLES10.glMatrixMode(GLES10.GL_TEXTURE)
        GLES10.glPushMatrix()
        GLES10.glTranslatef(0.5f, 0.5f, 0.0f)
        GLES10.glRotatef(rotation.evaluate(material), 0f, 0f, 1.0f)
        GLES10.glTranslatef(-0.5f, -0.5f, 0.0f)
        GLES10.glMatrixMode(GLES10.GL_MODELVIEW)
    }

    override fun unSet() {
        GLES10.glMatrixMode(GLES10.GL_TEXTURE)
        GLES10.glPopMatrix()
        GLES10.glMatrixMode(GLES10.GL_MODELVIEW)
    }

}