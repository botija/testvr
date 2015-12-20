package com.botijasoftware.utils.materials;

import java.util.ArrayList;
import com.botijasoftware.utils.ColorRGB;
import com.botijasoftware.utils.ColorRGBA;
import com.botijasoftware.utils.ResourceManager;

import android.opengl.GLES10;

public class Material {

	public ArrayList<MaterialPass> mPass;
	public ColorRGBA mDiffuseColor;
	public ColorRGB mAmbientColor;
	public ColorRGB mEmissionColor;
	public float mShininess;
	public ColorRGB mSpecularColor;
	private boolean loaded = false; 
	private float color[] = new float[4];
	
	public static final int MAX_GLOBAL_PARAMETERS = 16;
	public static final int MAX_LOCAL_PARAMETERS = 4;
	public static float[] mGlobalArgs = new float[MAX_GLOBAL_PARAMETERS];
	public float[] mLocalArgs = new float[MAX_LOCAL_PARAMETERS];
	public static float time;
	
	
	public final static int MAT_VISIBLE = 0x01;
	public final static int MAT_NOSHADOWS = 0x02;
	public final static int MAT_NODEPTHTEST = 0x04;
	public final static int MAT_TRANSLUCENT = 0x08;
	public final static int MAT_ALPHATEST = 0x10;
	public final static int MAT_MASKDEPTH = 0x20;
	public final static int MAT_NOBLEND = 0x40;
	public final static int MAT_NOLIGHTS = 0x80;
	public final static int MAT_FLAT = 0x100;
	public final static int MAT_BACKSIDED = 0x200;
	public final static int MAT_TWOSIDED = 0x400;
	public final static int MAT_POLYGONOFFSET = 0x800;
	
	
	public final static int SORT_BACKGROUND = 0;
	public final static int SORT_SOLID = 1;
	public final static int SORT_TRANSPARENT = 2;
	public final static int SORT_SCREEN = 3;
	public final static int SORT_POSTPROCESS = 4;

	
	public int sort = SORT_SOLID;
	public int mask = 0;
	public float alphatest = 1.0f;
	public float polygonoffset = 0.0f;
	
	public void setMask(int maskbits) {
		mask = maskbits;
	}
	
	public void addMask(int maskbits) {
		mask |= maskbits;
	}
	
	public int getMask() {
		return mask;
	}
	
	public boolean checkMask(int maskbits) {
		return ((mask & maskbits) != 0);
	}
	
	
	public Material() {
		mPass = new ArrayList<MaterialPass>();
		mDiffuseColor = new ColorRGBA(1,1,1,1);
		mAmbientColor = new ColorRGB(1,1,1);
		mShininess = 128f;
		mSpecularColor = new ColorRGB(1,1,1);
		mEmissionColor = new ColorRGB(1,1,1);
	}
	
	public void LoadContent(ResourceManager resources) {
		for (int i = 0; i< mPass.size();i++)
			mPass.get(i).LoadContent(resources);
		
		loaded = true;
	}
	
	public void setDiffuseColor(float r, float g, float b) {
		mDiffuseColor.R = r;
		mDiffuseColor.G = g;
		mDiffuseColor.B = b;
	}
	
	public void setAmbientColor(float r, float g, float b) {
		mAmbientColor.R = r;
		mAmbientColor.G = g;
		mAmbientColor.B = b;
	}
	
	public void setShininess(float shininess) {
		mShininess = shininess;
	}

	public void setSpecularColor(float r, float g, float b) {
		mSpecularColor.R = r;
		mSpecularColor.G = g;
		mSpecularColor.B = b;
	}
	
	public void setEmissionColor(float r, float g, float b) {
		mEmissionColor.R = r;
		mEmissionColor.G = g;
		mEmissionColor.B = b;
	}
	
	
	public void set() {
		
		if ((mask & MAT_FLAT) != 0 ) GLES10.glShadeModel(GLES10.GL_FLAT);
		if ((mask & MAT_TWOSIDED) != 0 ) GLES10.glDisable(GLES10.GL_CULL_FACE);
		else if ((mask & MAT_BACKSIDED) != 0 ) GLES10.glCullFace(GLES10.GL_CCW);
		if ((mask & MAT_NODEPTHTEST) != 0 ) GLES10.glDisable(GLES10.GL_DEPTH_TEST);
		if ((mask & MAT_MASKDEPTH) != 0 ) GLES10.glDepthMask(true);
		if ((mask & MAT_NOBLEND) != 0 ) GLES10.glDisable(GLES10.GL_BLEND);
		if ((mask & MAT_NOLIGHTS) != 0 ) GLES10.glDisable(GLES10.GL_LIGHTING);
		if ((mask & MAT_ALPHATEST) != 0 ) {
			GLES10.glEnable(GLES10.GL_ALPHA_TEST);
			GLES10.glAlphaFunc(GLES10.GL_GREATER, alphatest);
		}
		
		if ((mask & MAT_POLYGONOFFSET) != 0 ) {
			GLES10.glEnable(GLES10.GL_POLYGON_OFFSET_FILL);
			GLES10.glPolygonOffset(0.0f, polygonoffset);
		}
		
		GLES10.glDepthFunc(GLES10.GL_LEQUAL);
		
		GLES10.glColor4f(mDiffuseColor.R, mDiffuseColor.G, mDiffuseColor.B, mDiffuseColor.A);
		/*color[0] = mDiffuseColor.R;
		color[1] = mDiffuseColor.G;
		color[2] = mDiffuseColor.B;
		color[3] = 1.0f;
		GLES10.glMaterialfv(GLES10.GL_FRONT_AND_BACK, GLES10.GL_DIFFUSE, color,0);
		color[0] = mAmbientColor.R;
		color[1] = mAmbientColor.G;
		color[2] = mAmbientColor.B;		
		GLES10.glMaterialfv(GLES10.GL_FRONT_AND_BACK, GLES10.GL_AMBIENT, color,0);
		color[0] = mSpecularColor.R;
		color[1] = mSpecularColor.G;
		color[2] = mSpecularColor.B;		
		GLES10.glMaterialfv(GLES10.GL_FRONT_AND_BACK, GLES10.GL_SPECULAR, color,0);
		color[0] = mEmissionColor.R;
		color[1] = mEmissionColor.G;
		color[2] = mEmissionColor.B;		
		GLES10.glMaterialfv(GLES10.GL_FRONT_AND_BACK, GLES10.GL_EMISSION, color,0);
		GLES10.glMaterialf(GLES10.GL_FRONT_AND_BACK, GLES10.GL_SHININESS, mShininess);
		GLES10.glEnable(GLES10.GL_COLOR_MATERIAL);*/
		
	}
	
	public void unSet() {
		
		if ((mask & MAT_FLAT) != 0 ) GLES10.glShadeModel(GLES10.GL_SMOOTH);
		if ((mask & MAT_TWOSIDED) != 0 )GLES10.glEnable(GLES10.GL_CULL_FACE);
		else if ((mask & MAT_BACKSIDED) != 0 ) GLES10.glCullFace(GLES10.GL_CW);
		if ((mask & MAT_NODEPTHTEST) != 0 ) GLES10.glEnable(GLES10.GL_DEPTH_TEST);
		if ((mask & MAT_MASKDEPTH) != 0 ) GLES10.glDepthMask(false);
		if ((mask & MAT_NOBLEND) != 0 ) GLES10.glEnable(GLES10.GL_BLEND);
		if ((mask & MAT_NOLIGHTS) != 0 ) GLES10.glEnable(GLES10.GL_LIGHTING);
		if ((mask & MAT_ALPHATEST) != 0 ) GLES10.glDisable(GLES10.GL_ALPHA_TEST);
		if ((mask & MAT_POLYGONOFFSET) != 0 ) GLES10.glDisable(GLES10.GL_POLYGON_OFFSET_FILL);
		
		GLES10.glColor4f(1, 1, 1, 1);
		//GLES10.glMaterialf(GLES10.GL_FRONT_AND_BACK, GLES10.GL_SHININESS, 1);
		//GLES10.glDisable(GLES10.GL_COLOR_MATERIAL);
		

	}

	public void addPass(MaterialPass pass) {
		mPass.add(pass);
	}
	
	public int passCount() {
		return mPass.size();
	}
	
	public float getTime() {
		return Material.time;
	}
	
	public float getGlobalArg(int index) {
		if (index >0 && index < MAX_GLOBAL_PARAMETERS) {
			return Material.mGlobalArgs[index];
		}
		return 0.0f;
	}
	
	public float getLocalArg(int index) {
		if (index >0 && index < MAX_LOCAL_PARAMETERS) {
			return mLocalArgs[index];
		}
		return 0.0f;
	}
	
	public void setGlobalArg(int index, float val) {
		if (index >0 && index < MAX_GLOBAL_PARAMETERS) {
			Material.mGlobalArgs[index] = val;
		}
	}
	
	public void setLocalArg(int index, float val) {
		if (index >0 && index < MAX_LOCAL_PARAMETERS) {
			mLocalArgs[index] = val;
		}
	}

	public boolean isLoaded() {
		return loaded;
	}
}
