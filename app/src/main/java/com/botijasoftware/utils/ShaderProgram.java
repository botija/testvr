package com.botijasoftware.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.opengl.GLES20;

public class ShaderProgram {

	public int mProgramID;
	public int mFragmentShader;
	public int mVertexShader;
	public boolean mLinked = false;
	private boolean mReadFile;
	private int mVsID;
	private int mFsID;
	private String mVsStr;
	private String mFsStr;
	
	public ShaderProgram(int vs, int fs) {
		mProgramID = 0;
		mFragmentShader = 0;
		mVertexShader = 0;
		mReadFile = true;
		mVsID = vs;
		mFsID = fs;
	}
	
	public ShaderProgram(String vs, String fs) {
		mProgramID = 0;
		mFragmentShader = 0;
		mVertexShader = 0;
		mReadFile = false;
		mVsStr = vs;
		mFsStr = fs;
	}
	
	public void LoadContent( ResourceManager resources) {
		mProgramID = GLES20.glCreateProgram();
		if (mReadFile) {
			mVertexShader = loadShader(resources, mVsID, GLES20.GL_VERTEX_SHADER);
			mFragmentShader = loadShader(resources, mFsID, GLES20.GL_FRAGMENT_SHADER);
		} 
		else {
			mVertexShader = loadShader(mVsStr, GLES20.GL_VERTEX_SHADER);
			mFragmentShader = loadShader( mFsStr, GLES20.GL_FRAGMENT_SHADER);
		}
		
		Link();

	}

	private int loadShader(ResourceManager resources, int code, int type) {

		try {
			InputStream is = resources.getContext().getResources().openRawResource(code);
            //DataInputStream dsr = new DataInputStream ( is );
            BufferedReader dsr = new BufferedReader(new InputStreamReader(is));
		
			String readcode = "";
			String line = dsr.readLine();
		
			while (line != null) {
				readcode += line+"\n";
				line = dsr.readLine();
			}
			
			return loadShader(readcode, type);
		}
		catch (Exception e) {
			//System.out.println("XML Pasing Excpetion = " + e);
			return 0;
		}
	}
	
	private int loadShader( String code, int type) {
		int shaderid = GLES20.glCreateShader(type);
		if (shaderid == 0)
			return 0;
		
		GLES20.glShaderSource(shaderid, code);
		GLES20.glCompileShader(shaderid);
		

		//IntBuffer intbuf = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
		int[] status = {0};
		GLES20.glGetProgramiv( mProgramID, GLES20.GL_COMPILE_STATUS, status, 0 );
		//int linked = intbuf.get(0);
		if( status[0] == 0 )
		{
			GLES20.glGetProgramiv( mProgramID, GLES20.GL_INFO_LOG_LENGTH, status, 0 );
			int infoLogLength = status[0];
			if( infoLogLength > 1 )
			{
				//Log.d("GL2", GLES20.glGetProgramInfoLog(mProgramID));
				//return 0;
			}
			return shaderid; //0

			//throw new RuntimeException( "Creating program didn't work" );
		}
		return shaderid;
	}
	
	private boolean Link() {
		if (mProgramID != 0 && mVertexShader != 0 && mFragmentShader != 0 && !mLinked) {
			GLES20.glAttachShader(mProgramID, mVertexShader);
			GLES20.glAttachShader(mProgramID, mFragmentShader);
			
			GLES20.glLinkProgram(mProgramID);
			
			int[] status = {0};
			GLES20.glGetProgramiv( mProgramID, GLES20.GL_LINK_STATUS, status, 0 );
			
			if( status[0] == 0 )
			{
				GLES20.glGetProgramiv( mProgramID, GLES20.GL_INFO_LOG_LENGTH, status, 0 );
				int infoLogLength = status[0];
				if( infoLogLength > 1 )
				{
					//Log.d("GL2", GLES20.glGetProgramInfoLog(mProgramID));
					
				}
				return false;
 
				//throw new RuntimeException( "Creating program didn't work" );
			}
			mLinked = true;
			return true;
		}
		return false;
	}
	
	public void BindParam(int index, String name) {

		GLES20.glBindAttribLocation(mProgramID, index, name);

	}
	
	public void Use() {
		GLES20.glUseProgram(mProgramID);
	}
	
	public boolean Enable() {
		if (mLinked) {
			GLES20.glUseProgram(mProgramID);
			return true;
		}
		return false;
	}

    public int getProgramID() {
        return mProgramID;
    }

}
