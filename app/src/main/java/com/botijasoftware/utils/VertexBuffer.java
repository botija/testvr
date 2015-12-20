package com.botijasoftware.utils;

import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLES20;

import com.botijasoftware.utils.renderer.Renderer;

public class VertexBuffer {
	
	protected int nelements;
	protected VertexBufferDeclaration declaration;
	protected ArrayList<ArrayBuffer> buffer = new ArrayList<ArrayBuffer>();
	protected ArrayList<Integer> vbobuffer = new ArrayList<Integer>();
	//protected int vboindex;
	protected boolean useVBO = false;
	
	public VertexBuffer(int nelements, VertexBufferDeclaration declaration) {
		this.nelements = nelements;
		this.declaration = declaration;
		for (int i = 0; i< declaration.getCount(); i++) {
			VertexBufferDefinition vbd = declaration.getDefinition(i);
			
			switch (vbd.mFormat) {
			case VertexBufferDefinition.FLOAT:
				buffer.add(new ArrayBufferFloat(vbd, nelements));
				break;
			case VertexBufferDefinition.INT:
				buffer.add(new ArrayBufferInt(vbd, nelements));
				break;
			case VertexBufferDefinition.SHORT:
				buffer.add(new ArrayBufferShort(vbd, nelements));
				break;
			case VertexBufferDefinition.BYTE:
				buffer.add(new ArrayBufferByte(vbd, nelements));
				break;
				
			default:
				break;
			}
			
		}
	}
	
    public ArrayBuffer getBuffer(int index) {
    	if (index >=0 && index < buffer.size()) {
    		return buffer.get(index);
    	}
    	return null;
    	
    }
		
	
	/*public void Draw(GL10 gl) {
		Draw(gl, 0);
	}*/
	
	public void Draw(IndexBuffer ib) {
		
		enableState();
		fastDraw(ib);
		disableState();
		
	}
	
	/*public void fastDraw(GL10 gl) {
		fastDraw(gl, 0);
	}*/
	
	public void fastDraw(IndexBuffer ib) {
		
		if (useVBO) {
		 
			for (int i = 0; i < declaration.getCount(); i++) {
				VertexBufferDefinition vbd = declaration.getDefinition(i);
				GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbobuffer.get(i));
				GLES20.glVertexAttribPointer(Renderer.attributes[vbd.mUsage], vbd.mSize, vbd.getFormatGL(), true, 0, 0);
			}
			
			//GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferIndex);
			//GLES20.glDrawElements(mFormat, nvertex, GLES20.GL_UNSIGNED_SHORT, 0);
			GLES20.glDrawElements(ib.type, ib.getSize(), ib.format, ib.getBuffer());
			
			//GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
			
		}
		else {

		//GLES10.glGetError();
		for (int i=0;i<declaration.getCount();i++) {
			VertexBufferDefinition vbd = declaration.getDefinition(i);
			GLES20.glVertexAttribPointer(Renderer.attributes[vbd.mUsage], vbd.mSize, vbd.getFormatGL(), true, 0, buffer.get(i).getBuffer());
			/*int error = GLES20.glGetError();
			if (error != GLES20.GL_NO_ERROR) {
				Log.d("Space", "error:" + error);
				Log.d("Space", "usage; " + vbd.mUsage +" size: " + vbd.mSize + " format: " + vbd.getFormatGL());
			}*/
		}	
		
			GLES20.glDrawElements(ib.type, ib.getSize(), ib.format, ib.getBuffer());
			//int error = GLES20.glGetError();
			//if (error != GLES20.GL_NO_ERROR)
			//	Log.d("Space", "glDrawElements() error:" + error);
		}
			
	}

	public void enableState() {
		
		for (int i=0;i<declaration.getCount();i++) {
            VertexBufferDefinition vbd = declaration.getDefinition(i);
            GLES20.glEnableVertexAttribArray(Renderer.attributes[vbd.mUsage]);
		}
	}
	
	public void disableState() {

		for (int i=0;i<declaration.getCount();i++) {
            VertexBufferDefinition vbd = declaration.getDefinition(i);
            GLES20.glDisableVertexAttribArray(Renderer.attributes[vbd.mUsage]);
        }
	}
	
	public void makeVBO() {
				
		int count = declaration.getCount();
        int[] idbuffer = new int[count];
        GLES20.glGenBuffers(count, idbuffer, 0);
        for (int i=0;i < count;i++) {
        	VertexBufferDefinition vbd = declaration.getDefinition(i);
        	int bufferIndex = idbuffer[i];
        	GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferIndex);
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vbd.getDataSize() * vbd.mSize * nelements, buffer.get(i).getBuffer(), vbd.getAccessGL());
            vbobuffer.add(bufferIndex);
        }
        
        //vboindex = idbuffer[count];
        //GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vboindex);
        //GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, vbd.getDataSize() * vbd.mSize * nelements, inde, vbd.getAccessGL());

        //GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        useVBO = true;
	}
	
	
	public void free() {
		
		//TODO: free FloatBuffers if possible
		
		if (useVBO) {
			int count = declaration.getCount();
			int[] idbuffer = new int[count];
			for (int i=0;i<count;i++) {
				idbuffer[i] = vbobuffer.get(i);
			}

			GLES20.glDeleteBuffers(count, idbuffer, 0);
		}
		
		useVBO = false;
	}


    public void reload() {
        if (useVBO) {
            makeVBO();
        }
    }
	
	
	public VertexBufferDeclaration getDeclaration() {
		return declaration;
	}
	
	public int getElementCount() {
		return nelements;
	}
}