package com.botijasoftware.utils

import android.opengl.GLES20

abstract class RenderTarget(var width: Int, var height: Int) {

    abstract fun init()
    abstract fun setTarget()
    abstract fun clear()
    abstract fun initRender()
    abstract fun endRender()
    open lateinit var texture: Texture

    //it's included always in gles 2.0
    /*public static boolean checkFBO() {
		return checkExtension("GL_OES_framebuffer_object");
	}*/

    private fun checkExtension(extension: String): Boolean {
        val extensions = " " + GLES20.glGetString(GLES20.GL_EXTENSIONS) + " "
        return extensions.contains(" $extension ")
    }


}