package com.botijasoftware.utils.renderer

import java.util.ArrayList

import android.opengl.GLES20
import android.opengl.Matrix

import com.botijasoftware.utils.GLMatrix
import com.botijasoftware.utils.GLMatrixStack
import com.botijasoftware.utils.IndexBuffer
import com.botijasoftware.utils.VertexBuffer
import com.botijasoftware.utils.Camera
import com.botijasoftware.utils.materials.Material
import com.botijasoftware.utils.materials.MaterialRenderer

class Renderer {
    private val camera: Camera? = null

    companion object {

        var vbManager = VertexBufferManager()
        var ibManager = IndexBufferManager()

        var projection = GLMatrix()
        var modelview = GLMatrix()
        var mvpmatrix = GLMatrix()
        var scratch0 = GLMatrix()
        var scratch1 = GLMatrix()
        var scratch2 = GLMatrix()
        var scratch3 = GLMatrix()
        private val mvstack = GLMatrixStack()
        //private static GLMatrixStack pstack = new GLMatrixStack();
        var normalmatrix = FloatArray(12)


        val ATTRIBUTE_VERTEX = 0
        val ATTRIBUTE_TEXCOORDS = 1
        val ATTRIBUTE_COLOR = 2
        val ATTRIBUTE_NORMAL = 3
        val ATTRIBUTE_CUSTOM0 = 4
        val ATTRIBUTE_CUSTOM1 = 5
        val ATTRIBUTE_CUSTOM2 = 6
        val ATTRIBUTE_CUSTOM3 = 7
        val ATTRIBUTE_CUSTOM4 = 8
        val ATTRIBUTE_CUSTOM5 = 9
        val ATTRIBUTE_CUSTOM6 = 10
        val ATTRIBUTE_CUSTOM7 = 11
        val MAX_ATTRIBUTES = 12

        var attributes = IntArray(MAX_ATTRIBUTES)

        /*public static int uvertex = -1;
	public static int ucolor = -1;
	public static int unormal = -1;
	public static int utextcoord = -1;*/

        val MAX_TEXURES = 8

        val TEXTURE0 = 0
        val TEXTURE1 = 1
        val TEXTURE2 = 2
        val TEXTURE3 = 3
        val TEXTURE4 = 4
        val TEXTURE5 = 5
        val TEXTURE6 = 6
        val TEXTURE7 = 7

        var texturehandles = IntArray(MAX_TEXURES)


        /*public static int texture0 = -1;
	public static int texture1 = -1;
	public static int texture2 = -1;
	public static int texture3 = -1;
	public static int texture4 = -1;
	public static int texture5 = -1;
	public static int texture6 = -1;
	public static int texture7 = -1;*/

        private val background = RenderQueue()
        private val solid = RenderQueue()
        private val transparent = RenderQueue()
        private val screen = RenderQueue()
        private val postprocess = RenderQueue()

        fun setTextureHandle(unit: Int, texturehandle: Int) {
            texturehandles[unit] = texturehandle
        }

        fun BindTexture(unit: Int, texture: Int) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + unit)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture)
            GLES20.glUniform1i(texturehandles[unit], unit)
        }

        fun BindAttribute(index: Int, value: Int) {
            attributes[index] = value
        }

        fun toRender(vb: VertexBuffer, ib: IndexBuffer, mvmatrix: GLMatrix, material: Material) {

            val ri = RenderItem(vb, ib, mvmatrix, material)
            solid.add(ri)

            /*switch (material.sort) {
		case Material.SORT_SOLID:
			solid.add( ri );
			break;
		case Material.SORT_TRANSPARENT:
			transparent.add( ri );
			break;
		case Material.SORT_SCREEN:
			solid.add( ri );
			break;
		case Material.SORT_POSTPROCESS:
			postprocess.add( ri );
			break;
		default:
			break;
		}*/
        }

        fun render() {
            val size = solid.size()

            pushModelViewMatrix()
            var i = 0
            while (i < size) {
                val materials = solid.materials
                for (mi in materials.indices) {
                    val items = solid.getRenderItem(materials[mi])

                    val ii = 0
                    while (ii < items!!.size) {
                        val ri = items[ii]
                        mvpmatrix.save(ri.mvmatrix)
                        MaterialRenderer.renderVertexBuffer(ri.material, ri.vb, ri.ib)
                        i++
                    }
                }
                i++

            }
            popModelViewMatrix()


        }

        fun pushModelViewMatrix() {
            mvstack.pushMatrix(modelview)
        }

        fun popModelViewMatrix() {
            mvstack.popMatrix(modelview)
        }

        //insert matrix into opengl stack.
        fun loadModelViewMatrix() {
            //GLES20.glMatrixMode(GLES20.GL_MODELVIEW);
            //modelview.loadMatrix();
        }

        /*static public void pushProjectionMatrix() {
		pstack.pushMatrix(projection);
	}

	static public void popProjectionMatrix() {
		pstack.popMatrix(projection);
	}*/

        //insert matrix into opengl stack.
        fun loadProjectionMatrix() {
            //GLES20.glMatrixMode(GLES20.GL_PROJECTION);
            //projection.loadMatrix();
            //GLES20.glMatrixMode(GLES20.GL_MODELVIEW);
        }

        fun calcModelViewProjectionMatrix() {
            mvpmatrix.multMatrix(modelview, projection)
        }

        fun calNormalMatrix() {
            Matrix.invertM(modelview.matrix, 0, scratch0.matrix, 0)
            Matrix.transposeM(scratch0.matrix, 0, scratch1.matrix, 0)
            val m = scratch1.matrix
            normalmatrix[0] = m[0]
            normalmatrix[1] = m[1]
            normalmatrix[2] = m[2]

            normalmatrix[3] = m[4]
            normalmatrix[4] = m[5]
            normalmatrix[5] = m[6]

            normalmatrix[6] = m[8]
            normalmatrix[7] = m[9]
            normalmatrix[28] = m[10]
        }
    }

}