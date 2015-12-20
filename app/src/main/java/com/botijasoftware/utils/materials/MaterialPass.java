package com.botijasoftware.utils.materials;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES10;
import android.opengl.GLES20;

import com.botijasoftware.utils.ColorRGB;
import com.botijasoftware.utils.ResourceManager;
import com.botijasoftware.utils.Texture;
import com.botijasoftware.utils.TextureOptions;
import com.botijasoftware.utils.materials.passoptions.PassOption;

public class MaterialPass {
	
	protected PassCondition condition = null; 
	protected ArrayList<PassOption> options = new ArrayList<PassOption>();
	private boolean lastCheck;
	private TextureOptions textureoptions = TextureOptions.default_options;
	private int textureid;
	protected Material material;
	
	public Texture mTexture;
	public int mBlendMode;
	public float mAlpha;
	public ColorRGB mColor;
	
	public final static int BLEND_NOBLEND = 0;
	public final static int BLEND_NONE = 1;
	public final static int BLEND_ADD = 2;
	public final static int BLEND_BLEND = 3;
	public final static int BLEND_FILTER = 4;
	public final static int BLEND_MODULATE = 5;
	public final static int BLEND_CUSTOM = 6;
	
	public int srcblend, dstblend;
	
	public void LoadContent(ResourceManager resources) {
		mTexture = resources.loadTexture(textureid, textureoptions);
	}

	public MaterialPass(Material material, int textureid) {
		this.material = material;
		this.textureid = textureid;
		this.mAlpha = 1.0f;
		this.mColor = new ColorRGB(1,1,1);
		setBlendMode(BLEND_NOBLEND);
	}
	
	public void setTextureOptions(TextureOptions textureoptions) {
		this.textureoptions = textureoptions;
	}
	
	public void setBlendMode(int mode) {
		mBlendMode = mode;
		switch (mode) {
		case BLEND_NONE:srcblend = GLES20.GL_ZERO; dstblend = GLES20.GL_ONE; break;
		case BLEND_ADD:srcblend = GLES20.GL_ONE; dstblend = GLES20.GL_ONE; break;
		case BLEND_BLEND:srcblend = GLES20.GL_SRC_ALPHA; dstblend = GLES20.GL_ONE_MINUS_SRC_ALPHA; break;
		case BLEND_MODULATE:srcblend = GLES20.GL_DST_COLOR; dstblend = GLES20.GL_ZERO; break;
		case BLEND_FILTER:srcblend = GLES20.GL_DST_COLOR; dstblend = GLES20.GL_ZERO; break;
		default:srcblend = GLES20.GL_ONE; dstblend = GLES20.GL_ONE; break;
		}
	}
	
	public void setBlendMode(int src, int dst) {
		mBlendMode = BLEND_CUSTOM;
		srcblend = src; 
		dstblend = dst;
	}
	
	public MaterialPass addOption(PassOption op) {
		options.add(op);
		return this;
	}
	
	
	public MaterialPass setCondtion(PassCondition condition) {
		this.condition = condition;
		return this;
	}
	
	public boolean set() {
		
		lastCheck = true;
		if (condition != null)
			lastCheck = condition.check(material);
		
		if (!lastCheck)
			return false;
		
		int size = options.size();
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture.getID());
		if (mBlendMode == BLEND_NOBLEND) {
			GLES20.glDisable(GLES20.GL_BLEND);
		}
		else {
			GLES20.glEnable(GLES20.GL_BLEND);
			GLES20.glBlendFunc(dstblend, srcblend);
		}
		for (int i = 0; i< size; i++) {
			options.get(i).set(material);
		}
		
		//GLES20.glColor4f(mColor.R, mColor.G, mColor.B, mAlpha);
		
		return true;
	}
	
	public void unSet() {
		if (!lastCheck)
			return;
		GLES20.glDisable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		int size = options.size()-1;
		for (int i = size; i >=0; i--)
		{
			options.get(i).unSet();
		}
		//GLES20.glColor4f(1, 1, 1, 1);
	}
	

}
