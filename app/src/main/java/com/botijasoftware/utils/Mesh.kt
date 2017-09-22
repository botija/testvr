package com.botijasoftware.utils

import android.opengl.GLES20
import com.botijasoftware.utils.materials.Material

class Mesh(var mName: String, var mTexture: Texture, var mVertexBuffer: VertexBuffer, var mIndexBuffer: IndexBuffer) {

    fun LoadContent(resources: ResourceManager) {}

    fun Draw() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture.id)
        mVertexBuffer.Draw(mIndexBuffer)
    }

    var mMaterial: Material? = null
}
