package com.botijasoftware.utils.shaders

import javax.microedition.khronos.opengles.GL10

import android.opengl.GLES20

import com.botijasoftware.utils.ResourceManager
import com.botijasoftware.utils.ShaderProgram
import com.botijasoftware.utils.renderer.Renderer

class ShaderTest : Shader() {

    private var uvertex = -1
    private var ucolor = -1
    private var unormal = -1
    private var utextcoord = -1
    private var texture0 = -1
    private val texture1 = -1
    private val eye = -1
    private val lightpos = -1
    private var mMVMatrixUniformLocation = -1
    private var mPMatrixUniformLocation = -1
    private var mNormalMatrixUniformLocation = -1

    fun LoadContent(gl: GL10, resources: ResourceManager) {

        val vsSource = "attribute vec3 aVertexPosition;\n" +
                "attribute vec4 aVertexColor;\n" +
                "attribute vec3 aVertexNormal;\n" +
                "attribute vec2 aVertexTextureCoord;\n" +
                "uniform mat4 uMVMatrix;\n" +
                "uniform mat4 uPMatrix;\n" +
                "uniform mat3 uNormalMatrix;\n" +
                "uniform mat3 vLightPosition;\n" +
                "varying vec3 normal;\n" +
                "varying vec3 lightdir;\n" +
                "varying vec2 st;\n" +
                "varying vec4 color;\n" +
                "void main() {\n" +
                "	normal = uNormalMatrix * aVertexNormal;\n" +
                "   vec4 vPosition4 = uMVMatrix * aVertexPosition;\n" +
                "   vec3 vPosition3 = VPosition4.xyz / VPosition.w;\n" +
                "   lightdir = normalize(vLightPosition - vPosition3);\n" +
                "	st = aVertexTextureCoord;\n" +
                "	color = aVertexColor;\n" +
                "	gl_Position = uPMatrix * uMVMatrix * vec4(aVertexPosition, 1.0);\n" +
                "}\n"

        val fsSource = "precision highp float;\n" +
                "varying vec3 normal;\n" +
                "varying vec2 st;\n" +
                "varying vec4 color;\n" +
                "uniform sampler2D texture0;\n" +
                "void main() {\n" +
                "	vec4 c = texture2D(texture0, st);\n" +
                "	gl_FragColor = c + color;\n" +
                "}\n"

        program = ShaderProgram(vsSource, fsSource)


        program.LoadContent(resources)

        programid = program.programID
        uvertex = GLES20.glGetAttribLocation(programid, "aVertexPosition")
        ucolor = GLES20.glGetAttribLocation(programid, "aVertexColor")
        unormal = GLES20.glGetAttribLocation(programid, "aVertexNormal")
        utextcoord = GLES20.glGetAttribLocation(programid, "aVertexTextureCoord")
        mMVMatrixUniformLocation = GLES20.glGetUniformLocation(programid, "uMVMatrix")
        mPMatrixUniformLocation = GLES20.glGetUniformLocation(programid, "uPMatrix")
        mNormalMatrixUniformLocation = GLES20.glGetUniformLocation(programid, "uNormalMatrix")

        texture0 = GLES20.glGetUniformLocation(programid, "texture0")
        texture0 = GLES20.glGetUniformLocation(programid, "texture1")
    }

    override fun enable() {

        Renderer.BindAttribute(Renderer.ATTRIBUTE_VERTEX, uvertex)
        Renderer.BindAttribute(Renderer.ATTRIBUTE_COLOR, ucolor)
        Renderer.BindAttribute(Renderer.ATTRIBUTE_NORMAL, unormal)
        Renderer.BindAttribute(Renderer.ATTRIBUTE_TEXCOORDS, utextcoord)

        Renderer.setTextureHandle(Renderer.TEXTURE0, texture0)
        Renderer.setTextureHandle(Renderer.TEXTURE1, texture1)

        program.Use()

        GLES20.glUniformMatrix4fv(mMVMatrixUniformLocation, 1, false, Renderer.modelview.matrix, 0)
        GLES20.glUniformMatrix4fv(mPMatrixUniformLocation, 1, false, Renderer.projection.matrix, 0)

    }
}