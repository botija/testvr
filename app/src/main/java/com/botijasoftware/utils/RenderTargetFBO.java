package com.botijasoftware.utils;

import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLES20;
import android.util.Log;

public class RenderTargetFBO extends RenderTarget {

	private Texture target = null;
	private int frame_buffer[] = new int[1];
	private int color_texture[] = new int[1];
	private int depth_texture[] = new int[1];
	
	public RenderTargetFBO(int width, int height) {
		super(width, height);
	}

	public void init(GL10 gl) {


		// Create a framebuffer
		GLES20.glGenFramebuffers(1, frame_buffer, 0);

		// Generate a texture to hold the colour buffer
		GLES20.glGenTextures(1, color_texture, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, color_texture[0]);
		// Width and height do not have to be a power of two
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width,
				height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);

		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
				GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);

		// Probably just paranoia
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frame_buffer[0]);
		
		// Create a texture to hold the depth buffer
		/*GLES20.glGenTextures(1, depth_texture, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, depth_texture[0]);

		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0,
				GLES20.GL_DEPTH_COMPONENT16, width, height, 0,
				GLES20.GL_DEPTH_COMPONENT16, GLES20.GL_UNSIGNED_SHORT,
				null);

		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
				GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
				GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);*/
		
		
		//new method
        GLES20.glGenRenderbuffers(1, depth_texture, 0);
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, depth_texture[0]);
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER,
                GLES20.GL_DEPTH_COMPONENT16, width, height);
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER,
                GLES20.GL_DEPTH_COMPONENT16,
                GLES20.GL_RENDERBUFFER, depth_texture[0]);


        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,
                GLES20.GL_COLOR_ATTACHMENT0, GL10.GL_TEXTURE_2D,
                color_texture[0], 0);
		


		// Associate the textures with the FBO.

		/*GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,
				GLES20.GL_COLOR_ATTACHMENT0,
				GLES20.GL_TEXTURE_2D, color_texture[0], 0);

		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,
				GLES20.GL_DEPTH_ATTACHMENT,
				GLES20.GL_TEXTURE_2D, depth_texture[0], 0);*/

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

		// Check FBO status.
		int status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER);

		if (status == GLES20.GL_FRAMEBUFFER_COMPLETE) {
			target = new Texture(color_texture[0], width, height);
		} else
			Log.d("Space", "FBO error " + status);
		
		//gl11ep.glBindFramebufferOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES, 0);

	}

	@Override
	public void setTarget(GL10 gl) {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,
				frame_buffer[0]);

	}

	@Override
	public void clear(GL10 gl) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initRender(GL10 gl) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endRender(GL10 gl) {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

	}

	public Texture getTexture() {
		return target;
	}
	

}