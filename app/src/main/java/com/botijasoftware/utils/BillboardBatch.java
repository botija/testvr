package com.botijasoftware.utils;


import android.opengl.GLES20;

import com.botijasoftware.utils.renderer.Renderer;

import javax.microedition.khronos.opengles.GL10;

public class BillboardBatch {

    protected VertexBuffer mVertexBuffer;
    protected IndexBuffer mIndexBuffer;
    protected Texture mTexture = null;
    protected boolean mVisible = true;
    private final static int MAXBILLBOARDS= 128;
    private final static int PVERTEX = 0;
    private final static int PTEXTCOORD = 1;
    private final static int PCOLOR = 2;
    private int count = 0;
    private int maxbillboards = 0;

    public BillboardBatch() {
        this(MAXBILLBOARDS);
    }

    public BillboardBatch(int nbillboards) {
        maxbillboards = nbillboards;
        VertexBufferDeclaration vbd = new VertexBufferDeclaration();
        vbd.add(new VertexBufferDefinition(VertexBufferDefinition.FLOAT, VertexBufferDefinition.DEF_VERTEX,3,VertexBufferDefinition.ACCESS_DYNAMIC));
        vbd.add(new VertexBufferDefinition(VertexBufferDefinition.FLOAT, VertexBufferDefinition.DEF_TEXTURE_COORD,2,VertexBufferDefinition.ACCESS_DYNAMIC));
        vbd.add(new VertexBufferDefinition(VertexBufferDefinition.BYTE, VertexBufferDefinition.DEF_COLOR,4,VertexBufferDefinition.ACCESS_DYNAMIC));

        mVertexBuffer = Renderer.vbManager.requestVB(vbd, 4 * nbillboards);
        mIndexBuffer = Renderer.ibManager.requestIB(GLES20.GL_TRIANGLES, GLES20.GL_UNSIGNED_SHORT, 6 * nbillboards);


        for (int v = 0, i = 0; v < nbillboards*4;v+=4,i+=6) {
            mIndexBuffer.put(i,v);
            mIndexBuffer.put(i+1,v+1);
            mIndexBuffer.put(i+2,v+2);
            mIndexBuffer.put(i+3,v+1);
            mIndexBuffer.put(i+4,v+2);
            mIndexBuffer.put(i+5,v+3);
        }
    }


    private void drawVB(GL10 gl) {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture.getID() );

        Renderer.pushModelViewMatrix();
        Renderer.modelview.loadIdentity();

        //Renderer.loadModelViewMatrix();
        mIndexBuffer.setSize(count*6); // two triangles per sprite
        mVertexBuffer.Draw(gl, mIndexBuffer);

        Renderer.popModelViewMatrix();
        //Renderer.modelview.loadMatrix();

    }


    private void putBillboardOnVB(Billboard billboard) {
        /* public Vector3 position;
        protected Vector2 size;
        public Vector3 scale = new Vector3(1,1,1);
        protected float rotation;
        protected Vector3 rotation_axis; */


    }

    public void Draw(GL10 gl) {


    }

    public void end(GL10 gl) {

        if (count > 0) {
            drawVB(gl);
            count = 0;
            mTexture = null;
        }
        //disable sprite shader
        //clean vb
    }

    public void begin(GL10 gl) {
        count = 0;
        mTexture = null;
        //enable sprite shader
        //prepare vb
    }

}