package com.botijasoftware.utils

import java.util.ArrayList

import android.opengl.GLES20

import com.botijasoftware.utils.renderer.Renderer

class VertexBuffer(nelements: Int, declaration: VertexBufferDeclaration) {

    var elementCount: Int = 0
        protected set
    var declaration: VertexBufferDeclaration
        protected set
    protected var buffer = ArrayList<ArrayBuffer>()
    protected var vbobuffer = ArrayList<Int>()
    //protected int vboindex;
    protected var useVBO = false

    init {
        this.elementCount = nelements
        this.declaration = declaration
        for (i in 0..declaration.count - 1) {
            val vbd = declaration.getDefinition(i)

            when (vbd!!.mFormat) {
                VertexBufferDefinition.FLOAT -> buffer.add(ArrayBufferFloat(vbd, nelements))
                VertexBufferDefinition.INT -> buffer.add(ArrayBufferInt(vbd, nelements))
                VertexBufferDefinition.SHORT -> buffer.add(ArrayBufferShort(vbd, nelements))
                VertexBufferDefinition.BYTE -> buffer.add(ArrayBufferByte(vbd, nelements))

                else -> {
                }
            }

        }
    }

    fun getBuffer(index: Int): ArrayBuffer? {
        if (index >= 0 && index < buffer.size) {
            return buffer[index]
        }
        return null

    }


    /*public void Draw(GL10 gl) {
		Draw(gl, 0);
	}*/

    fun Draw(ib: IndexBuffer) {

        enableState()
        fastDraw(ib)
        disableState()

    }

    /*public void fastDraw(GL10 gl) {
		fastDraw(gl, 0);
	}*/

    fun fastDraw(ib: IndexBuffer) {

        if (useVBO) {

            for (i in 0..declaration.count - 1) {
                val vbd = declaration.getDefinition(i)
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbobuffer[i])
                GLES20.glVertexAttribPointer(Renderer.attributes[vbd!!.usageGL], vbd.mSize, vbd.formatGL, true, 0, 0)
            }

            //GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferIndex);
            //GLES20.glDrawElements(mFormat, nvertex, GLES20.GL_UNSIGNED_SHORT, 0);
            GLES20.glDrawElements(ib.type, ib.size , ib.format, ib.buffer)

            //GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)

        } else {

            //GLES10.glGetError();
            for (i in 0..declaration.count - 1) {
                val vbd = declaration.getDefinition(i)
                GLES20.glVertexAttribPointer(Renderer.attributes[vbd!!.usageGL], vbd.mSize, vbd.formatGL, true, 0, buffer[i].buffer)
                /*int error = GLES20.glGetError();
			if (error != GLES20.GL_NO_ERROR) {
				Log.d("Space", "error:" + error);
				Log.d("Space", "usage; " + vbd.mUsage +" size: " + vbd.mSize + " format: " + vbd.getFormatGL());
			}*/
            }

            GLES20.glDrawElements(ib.type, ib.size, ib.format, ib.buffer)
            //int error = GLES20.glGetError();
            //if (error != GLES20.GL_NO_ERROR)
            //	Log.d("Space", "glDrawElements() error:" + error);
        }

    }

    fun enableState() {

        for (i in 0..declaration.count - 1) {
            val vbd = declaration.getDefinition(i)
            GLES20.glEnableVertexAttribArray(Renderer.attributes[vbd!!.usageGL])
        }
    }

    fun disableState() {

        for (i in 0..declaration.count - 1) {
            val vbd = declaration.getDefinition(i)
            GLES20.glDisableVertexAttribArray(Renderer.attributes[vbd!!.usageGL])
        }
    }

    fun makeVBO() {

        val count = declaration.count
        val idbuffer = IntArray(count)
        GLES20.glGenBuffers(count, idbuffer, 0)
        for (i in 0..count - 1) {
            val vbd = declaration.getDefinition(i)
            val bufferIndex = idbuffer[i]
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferIndex)
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vbd!!.dataSize * vbd.mSize * elementCount, buffer[i].buffer, vbd.accessGL)
            vbobuffer.add(bufferIndex)
        }

        //vboindex = idbuffer[count];
        //GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vboindex);
        //GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, vbd.getDataSize() * vbd.mSize * nelements, inde, vbd.getAccessGL());

        //GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)

        useVBO = true
    }


    fun free() {

        //TODO: free FloatBuffers if possible

        if (useVBO) {
            val count = declaration.count
            val idbuffer = IntArray(count)
            for (i in 0..count - 1) {
                idbuffer[i] = vbobuffer[i]
            }

            GLES20.glDeleteBuffers(count, idbuffer, 0)
        }

        useVBO = false
    }


    fun reload() {
        if (useVBO) {
            makeVBO()
        }
    }
}