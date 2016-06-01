package com.botijasoftware.utils.renderer;

import java.util.ArrayList;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.botijasoftware.utils.GLMatrix;
import com.botijasoftware.utils.GLMatrixStack;
import com.botijasoftware.utils.IndexBuffer;
import com.botijasoftware.utils.VertexBuffer;
import com.botijasoftware.utils.Camera;
import com.botijasoftware.utils.materials.Material;
import com.botijasoftware.utils.materials.MaterialRenderer;

public class Renderer {
	
	public static VertexBufferManager vbManager = new VertexBufferManager();
	public static IndexBufferManager ibManager  = new IndexBufferManager();
	
	public static GLMatrix projection = new GLMatrix();
	public static GLMatrix modelview = new GLMatrix();
	public static GLMatrix mvpmatrix = new GLMatrix();
	public static GLMatrix scratch0 = new GLMatrix();
	public static GLMatrix scratch1 = new GLMatrix();
	public static GLMatrix scratch2 = new GLMatrix();
	public static GLMatrix scratch3 = new GLMatrix();
	private static GLMatrixStack mvstack = new GLMatrixStack();
	//private static GLMatrixStack pstack = new GLMatrixStack();
	public static float normalmatrix[] = new float[12];


    public final static int ATTRIBUTE_VERTEX = 0;
    public final static int ATTRIBUTE_TEXCOORDS = 1;
    public final static int ATTRIBUTE_COLOR = 2;
    public final static int ATTRIBUTE_NORMAL = 3;
    public final static int ATTRIBUTE_CUSTOM0 = 4;
    public final static int ATTRIBUTE_CUSTOM1 = 5;
    public final static int ATTRIBUTE_CUSTOM2 = 6;
    public final static int ATTRIBUTE_CUSTOM3 = 7;
    public final static int ATTRIBUTE_CUSTOM4 = 8;
    public final static int ATTRIBUTE_CUSTOM5 = 9;
    public final static int ATTRIBUTE_CUSTOM6 = 10;
    public final static int ATTRIBUTE_CUSTOM7 = 11;
    public final static int MAX_ATTRIBUTES = 12;

    public static int attributes[] = new int[MAX_ATTRIBUTES];

	/*public static int uvertex = -1;
	public static int ucolor = -1;
	public static int unormal = -1;
	public static int utextcoord = -1;*/

    public final static int MAX_TEXURES = 8;

    public final static int TEXTURE0 = 0;
    public final static int TEXTURE1 = 1;
    public final static int TEXTURE2 = 2;
    public final static int TEXTURE3 = 3;
    public final static int TEXTURE4 = 4;
    public final static int TEXTURE5 = 5;
    public final static int TEXTURE6 = 6;
    public final static int TEXTURE7 = 7;

    public static int[] texturehandles = new int [MAX_TEXURES];


	/*public static int texture0 = -1;
	public static int texture1 = -1;
	public static int texture2 = -1;
	public static int texture3 = -1;
	public static int texture4 = -1;
	public static int texture5 = -1;
	public static int texture6 = -1;
	public static int texture7 = -1;*/
	
	private static RenderQueue background = new RenderQueue();
	private static RenderQueue solid = new RenderQueue();
	private static RenderQueue transparent = new RenderQueue();
	private static RenderQueue screen = new RenderQueue();
	private static RenderQueue postprocess = new RenderQueue();
	private Camera camera;

    public static void setTextureHandle(int unit, int texturehandle) {
        texturehandles[unit] = texturehandle;
    }

    public static void BindTexture(int unit, int texture) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + unit);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        GLES20.glUniform1i(texturehandles[unit], unit);
    }

    public static void BindAttribute(int index, int value) {
        attributes[index] = value;
    }
	
	public static void toRender(VertexBuffer vb, IndexBuffer ib, GLMatrix mvmatrix, Material material) {
		
		RenderItem ri = new RenderItem(vb, ib, mvmatrix, material);
		solid.add(ri);
		
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
	
	public static void render() {
		int size = solid.size();
		
		pushModelViewMatrix();
		for (int i=0; i< size;i++) {
			ArrayList<Material> materials = solid.getMaterials();
			for (int mi=0; mi<materials.size(); mi++) {
				ArrayList<RenderItem> items = solid.getRenderItem(materials.get(mi));
				
				for (int ii=0; ii < items.size(); i++) {
					RenderItem ri = items.get(ii);
					mvpmatrix.save(ri.mvmatrix);
					MaterialRenderer.renderVertexBuffer(ri.material, ri.vb, ri.ib);
				}
			}
			
		}
		popModelViewMatrix();

		
	}
	
	static public void pushModelViewMatrix() {
		mvstack.pushMatrix(modelview);
	}
	
	static public void popModelViewMatrix() {
		mvstack.popMatrix(modelview);
	}
	
	//insert matrix into opengl stack.
	static public void loadModelViewMatrix() {
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
	static public void loadProjectionMatrix() {
		//GLES20.glMatrixMode(GLES20.GL_PROJECTION);
		//projection.loadMatrix();
		//GLES20.glMatrixMode(GLES20.GL_MODELVIEW);
	}
	
	static public void calcModelViewProjectionMatrix() {
		mvpmatrix.multMatrix(modelview, projection);
	}

    static public void calNormalMatrix() {
        Matrix.invertM(modelview.matrix, 0, scratch0.matrix, 0);
        Matrix.transposeM(scratch0.matrix,0, scratch1.matrix, 0);
        float[] m = scratch1.matrix;
        normalmatrix[0] = m[0];
        normalmatrix[1] = m[1];
        normalmatrix[2] = m[2];

        normalmatrix[3] = m[4];
        normalmatrix[4] = m[5];
        normalmatrix[5] = m[6];

        normalmatrix[6] = m[8];
        normalmatrix[7] = m[9];
        normalmatrix[28] = m[10];
    }

}