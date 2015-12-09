package com.botijasoftware.utils.shaders;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;

import com.botijasoftware.utils.ResourceManager;
import com.botijasoftware.utils.ShaderProgram;
import com.botijasoftware.utils.renderer.Renderer;

public class ShaderSprite extends Shader {
	
	private int uvertex = -1;
	private int ucolor = -1;
	private int utextcoord = -1;
	private int texture0 = -1;
    private int mMVMatrixUniformLocation = -1;
    private int mPMatrixUniformLocation = -1;
	
	public void LoadContent(GL10 gl, ResourceManager resources) {

		String vsSource = "attribute vec3 aVertexPosition;\n" +
				"attribute vec4 aVertexColor;\n" +
				"attribute vec2 aVertexTextureCoord;\n" +
				"uniform mat4 uMVMatrix;\n" +
				"uniform mat4 uPMatrix;\n" +
				"varying vec2 st;\n" +
				"varying vec4 color;\n" +
				"void main() {\n" +  
				"	st = aVertexTextureCoord;\n" +
				"	color = aVertexColor;\n" +
				"	gl_Position = uPMatrix * uMVMatrix * vec4(aVertexPosition, 1.0);\n" +
				"}\n";
		
		String fsSource = "precision highp float;\n" +
				"varying vec2 st;\n" +
				"varying vec4 color;\n" +
				"uniform sampler2D texture0;\n" +
				"void main() {\n" +
				"	vec4 c = texture2D(texture0, st);\n" +
				"	gl_FragColor = c * color;\n" +
				"}\n";
		
		program = new ShaderProgram(  vsSource , fsSource );
				
		
		program.LoadContent(resources);
		
		programid = program.getProgramID();
        uvertex = GLES20.glGetAttribLocation(programid, "aVertexPosition");
        ucolor = GLES20.glGetAttribLocation(programid, "aVertexColor");
        utextcoord = GLES20.glGetAttribLocation(programid, "aVertexTextureCoord");
        mMVMatrixUniformLocation = GLES20.glGetUniformLocation(programid, "uMVMatrix");
        mPMatrixUniformLocation = GLES20.glGetUniformLocation(programid, "uPMatrix");
        
        texture0 = GLES20.glGetUniformLocation(programid, "texture0");
	}
	
	public void enable() {

        Renderer.BindAttribute(Renderer.ATTRIBUTE_VERTEX, uvertex);
        Renderer.BindAttribute(Renderer.ATTRIBUTE_COLOR, ucolor);
        Renderer.BindAttribute(Renderer.ATTRIBUTE_TEXCOORDS, utextcoord);

        Renderer.setTextureHandle(Renderer.TEXTURE0, texture0);

        program.Use();
			
		GLES20.glUniformMatrix4fv(mMVMatrixUniformLocation, 1, false, Renderer.modelview.matrix, 0);
        GLES20.glUniformMatrix4fv(mPMatrixUniformLocation, 1, false, Renderer.projection.matrix, 0);
		
	}
}