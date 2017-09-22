package com.botijasoftware.utils

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

import android.opengl.GLES20

class ShaderProgram {

    var programID: Int = 0
    var mFragmentShader: Int = 0
    var mVertexShader: Int = 0
    var mLinked = false
    private var mReadFile: Boolean = false
    private var mVsID: Int = 0
    private var mFsID: Int = 0
    private lateinit var mVsStr: String
    private lateinit var mFsStr: String

    constructor(vs: Int, fs: Int) {
        programID = 0
        mFragmentShader = 0
        mVertexShader = 0
        mReadFile = true
        mVsID = vs
        mFsID = fs
    }

    constructor(vs: String, fs: String) {
        programID = 0
        mFragmentShader = 0
        mVertexShader = 0
        mReadFile = false
        mVsStr = vs
        mFsStr = fs
    }

    fun LoadContent(resources: ResourceManager) {
        programID = GLES20.glCreateProgram()
        if (mReadFile) {
            mVertexShader = loadShader(resources, mVsID, GLES20.GL_VERTEX_SHADER)
            mFragmentShader = loadShader(resources, mFsID, GLES20.GL_FRAGMENT_SHADER)
        } else {
            mVertexShader = loadShader(mVsStr, GLES20.GL_VERTEX_SHADER)
            mFragmentShader = loadShader(mFsStr, GLES20.GL_FRAGMENT_SHADER)
        }

        Link()

    }

    private fun loadShader(resources: ResourceManager, code: Int, type: Int): Int {

        try {
            val `is` = resources.context.resources.openRawResource(code)
            //DataInputStream dsr = new DataInputStream ( is );
            val dsr = BufferedReader(InputStreamReader(`is`))

            var readcode = ""
            var line: String? = dsr.readLine()

            while (line != null) {
                readcode += line + "\n"
                line = dsr.readLine()
            }

            return loadShader(readcode, type)
        } catch (e: Exception) {
            //System.out.println("XML Pasing Excpetion = " + e);
            return 0
        }

    }

    private fun loadShader(code: String, type: Int): Int {
        val shaderid = GLES20.glCreateShader(type)
        if (shaderid == 0)
            return 0

        GLES20.glShaderSource(shaderid, code)
        GLES20.glCompileShader(shaderid)


        //IntBuffer intbuf = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
        val status = intArrayOf(0)
        GLES20.glGetProgramiv(programID, GLES20.GL_COMPILE_STATUS, status, 0)
        //int linked = intbuf.get(0);
        if (status[0] == 0) {
            GLES20.glGetProgramiv(programID, GLES20.GL_INFO_LOG_LENGTH, status, 0)
            val infoLogLength = status[0]
            if (infoLogLength > 1) {
                //Log.d("GL2", GLES20.glGetProgramInfoLog(mProgramID));
                //return 0;
            }
            return shaderid //0

            //throw new RuntimeException( "Creating program didn't work" );
        }
        return shaderid
    }

    private fun Link(): Boolean {
        if (programID != 0 && mVertexShader != 0 && mFragmentShader != 0 && !mLinked) {
            GLES20.glAttachShader(programID, mVertexShader)
            GLES20.glAttachShader(programID, mFragmentShader)

            GLES20.glLinkProgram(programID)

            val status = intArrayOf(0)
            GLES20.glGetProgramiv(programID, GLES20.GL_LINK_STATUS, status, 0)

            if (status[0] == 0) {
                GLES20.glGetProgramiv(programID, GLES20.GL_INFO_LOG_LENGTH, status, 0)
                val infoLogLength = status[0]
                if (infoLogLength > 1) {
                    //Log.d("GL2", GLES20.glGetProgramInfoLog(mProgramID));

                }
                return false

                //throw new RuntimeException( "Creating program didn't work" );
            }
            mLinked = true
            return true
        }
        return false
    }

    fun BindParam(index: Int, name: String) {

        GLES20.glBindAttribLocation(programID, index, name)

    }

    fun Use() {
        GLES20.glUseProgram(programID)
    }

    fun Enable(): Boolean {
        if (mLinked) {
            GLES20.glUseProgram(programID)
            return true
        }
        return false
    }

}
