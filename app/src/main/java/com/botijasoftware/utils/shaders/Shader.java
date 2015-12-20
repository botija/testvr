package com.botijasoftware.utils.shaders;

import com.botijasoftware.utils.ResourceManager;
import com.botijasoftware.utils.ShaderProgram;

public class Shader {
	protected ShaderProgram program;
	protected String vsSource;
	protected String fsSource;
	protected int programid;

	public void LoadContent(ResourceManager resources) {
		
	}
	
	public void enable() {
		
	}

    public void reload(ResourceManager resources) {
        LoadContent(resources);
    }

	
}
