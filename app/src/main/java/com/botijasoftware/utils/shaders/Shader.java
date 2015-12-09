package com.botijasoftware.utils.shaders;


import javax.microedition.khronos.opengles.GL10;

import com.botijasoftware.utils.ResourceManager;
import com.botijasoftware.utils.ShaderProgram;

public class Shader {
	protected ShaderProgram program;
	protected String vsSource;
	protected String fsSource;
	protected int programid;

	public void LoadContent(GL10 gl, ResourceManager resources) {
		
	}
	
	public void enable() {
		
	}

    public void reload(GL10 gl, ResourceManager resources) {
        LoadContent(gl, resources);
    }

	
}
