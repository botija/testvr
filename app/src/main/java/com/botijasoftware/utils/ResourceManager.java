package com.botijasoftware.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.botijasoftware.utils.materials.Material;
import com.botijasoftware.utils.materials.MaterialManager;
import com.botijasoftware.utils.shaders.Shader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Build;

public class ResourceManager {

	
	public ResourceManager(Context context)
	{
		mContext = context;
		glTextureID = new int[1];
		textures = new HashMap< Integer, Texture>();
		sounds = new HashMap< Integer, Sound> ();
		models = new HashMap< Integer, Model> ();
        shaders = new HashMap<String, Shader>();
		//mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);

        // API 21
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

			AudioAttributes audioAttributesBuilder = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
			SoundPool.Builder builder = new SoundPool.Builder();
			builder.setMaxStreams(4);
			builder.setAudioAttributes(audioAttributesBuilder);
			mSoundPool = builder.build();
		}
		else {
			mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		}


		mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
		mBitmapOptions = new BitmapFactory.Options();
		mBitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//Bitmap.Config.RGB_4444
		mMaterialManager = new MaterialManager();
	}
	
	private Texture getTexture(int textureName) {
		if (textures.containsKey(textureName))
			return textures.get(textureName);
		else
			return null;
	}
	
	private void addTexture(int textureName, Texture texture) {
		textures.put(textureName, texture);
	}
	
	public Texture loadTexture(int textureName) {
		return loadTexture(textureName, TextureOptions.default_options);
	}
	
	public Texture loadTexture(int textureName, TextureOptions options) {
		Texture t = getTexture(textureName);
		if (t != null) {
            t.addReference();
            return t;
        }
		else {
			t = loadTextureFromFile(textureName, options);
            t.addReference();
			if (t != null)
				addTexture(textureName, t);
			
			return t;
		}
	}

	public void preloadTexture(int textureName, TextureOptions options) {
		Texture t = loadTexture(textureName, options);
        t.removeReference();
	}
	
	public void preloadTexture(int textureName) {
		preloadTexture(textureName, TextureOptions.default_options);
	}

	public Texture loadTextureFromCache(int textureName) {
		Texture t = getTexture(textureName);
		if (t != null) {
			t.addReference();
		}
		return t;
	}


	public Texture loadTextureFromFile(int textureName) {
		return loadTextureFromFile(textureName, TextureOptions.default_options);
	}
	
	
	public Texture loadTextureFromFile(int textureName, TextureOptions options)
	{

		GLES20.glGenTextures(1, glTextureID, 0);
	    int id = glTextureID[0];  
    
	    //Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), textureName, mBitmapOptions);
		
		//Loading code from replica island (for testing on real hardware)
		InputStream is = mContext.getResources().openRawResource(textureName);
        Bitmap bmp;
        try {
            bmp = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
              	e.printStackTrace();
                    // Ignore.
            }
        }
		
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id);

	    int width = bmp.getWidth();
	    int height = bmp.getHeight();
	    // Set all of our texture parameters:
	    options.apply();
	    //GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, options.minFilter);//_MIPMAP_NEAREST);
	    //GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, options.maxFilter);//_MIPMAP_NEAREST);
	    //GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, options.wraps); //GL_REPEAT
	    //GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, options.wrapt); //GL_REPEAT
	    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);
		//GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA,width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, bmp); 

	    if (options.mipmap) {
	    	
		    Bitmap bmp2 = Bitmap.createScaledBitmap(bmp, width>>1, height>>1, true);
		    bmp.recycle();
		    bmp = bmp2;
		    
	    // Generate, and load up all of the mipmaps:
			for(int level=1, h = bmp.getHeight(), w = bmp.getWidth(); true; level++) {
				// Push the bitmap onto the GPU:
			    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, level, bmp, 0);
	        
	        // We need to stop when the texture is 1x1:
			    if(h==1 && w==1) break;
	        
	        // Resize, and let's go again:
			    w >>= 1; h >>= 1;
			    if(w<1)  w = 1;
			    if(h<1) h = 1;
	        
			    bmp2 = Bitmap.createScaledBitmap(bmp, w, h, true);
			    bmp.recycle();
			    bmp = bmp2;
			}
		}

	    bmp.recycle();
		
		return new Texture(id, width, height);
	}

	public Texture loadTextureFromBitmap(Bitmap bmp, TextureOptions options) {
		
		GLES20.glGenTextures(1, glTextureID, 0);
	    int id = glTextureID[0];  
	    
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id);

	    int width = bmp.getWidth();
	    int height = bmp.getHeight();

	    options.apply();
	    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);
	    
	    if (options.mipmap) {
	    	
		    Bitmap bmp2 = Bitmap.createScaledBitmap(bmp, width>>1, height>>1, true);
		    //bmp.recycle();
		    bmp = bmp2;
		    
	    // Generate, and load up all of the mipmaps:
			for(int level=1, h = bmp.getHeight(), w = bmp.getWidth(); true; level++) {
				// Push the bitmap onto the GPU:
			    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, level, bmp, 0);
	        
	        // We need to stop when the texture is 1x1:
			    if(h==1 && w==1) break;
	        
	        // Resize, and let's go again:
			    w >>= 1; h >>= 1;
			    if(w<1)  w = 1;
			    if(h<1) h = 1;
	        
			    bmp2 = Bitmap.createScaledBitmap(bmp, w, h, true);
			    bmp.recycle();
			    bmp = bmp2;
			}
			bmp.recycle();
		}

	     
	    
		
		return new Texture(id, width, height);
	}
	
	
	public CubeMapTexture  loadCubeMapTexure(String basename ) {
		
		GLES20.glGenTextures(1, glTextureID, 0);
	    int id = glTextureID[0];  
	    
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, id);
	    
	    Bitmap bmp = getBitmap(basename+"_px");

	    int width = bmp.getWidth();
	    int height = bmp.getHeight();		
		
	    GLES20.glTexParameterf(GLES20.GL_TEXTURE_CUBE_MAP,
	    		GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
	    GLES20.glTexParameterf(GLES20.GL_TEXTURE_CUBE_MAP,
	    		GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        
        //GLES20.glTexGeni(GLES20.GL_TEXTURE_GEN_STR, GLES20.GL_TEXTURE_GEN_MODE, GLES20.GL_REFLECTION_MAP);
        //GLES20.glEnable(GLES20.GL_TEXTURE_GEN_STR);
        
        loadCubeMapFace(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X, bmp);
        bmp.recycle();
        bmp = getBitmap(basename+"_py");
        loadCubeMapFace(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, bmp);
        bmp.recycle();
        bmp = getBitmap(basename+"_pz");
        loadCubeMapFace(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, bmp);
        bmp.recycle();
        bmp = getBitmap(basename+"_nx");
        loadCubeMapFace(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, bmp);
        bmp.recycle();
        bmp = getBitmap(basename+"_ny");
        loadCubeMapFace(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, bmp);
        bmp.recycle();
        bmp = getBitmap(basename+"_nz");
        loadCubeMapFace(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, bmp);
        bmp.recycle();
        
	    return new CubeMapTexture(id, width, height);
	}
	
	private void loadCubeMapFace(int face, Bitmap bmp) {
		
	}
	
	private Bitmap getBitmap(String filename) {
		
		int id = mContext.getResources().getIdentifier(filename, "drawable", mContext.getPackageName());
		if (id == 0)
			return null;
		InputStream is =  mContext.getResources().openRawResource(id);
        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
              	e.printStackTrace();
            }
        }
		return bmp;
	}
	
	public void unloadTexture(int textureName) {
        Texture t = getTexture(textureName);
        if (t != null) {
            t.removeReference();
            if (!t.isReferenced()) {
                forceUnloadTexture(textureName);

            }
        }

	}
	
	/*public void forceUnloadTexture(Texture texture) {
			glTextureID[0] = texture.getID();
			GLES20.glDeleteTextures(1, glTextureID,0);
	}*/
	
	
	
	public void forceUnloadTexture(int textureName) {
	
		if (textures.containsKey(textureName)) {
			glTextureID[0] = textures.get(textureName).getID(); 
			//IntBuffer textureid = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
			//textureid.put(0, textures.get(textureName).getID());
			GLES20.glDeleteTextures(1, glTextureID, 0);  //release texture memory
			textures.remove(textureName);
		}
	}
	
	//TODO: optimize with glTextureID and remove IntBuffer
	public void unloadAllTextures() {
		Object[] tex = textures.values().toArray();
		int size = tex.length;
		IntBuffer textureid = ByteBuffer.allocateDirect( 4 * size ).order(ByteOrder.nativeOrder()).asIntBuffer();
		
		for (int i = 0; i < size; i++) {
			Texture t = (Texture)tex[i];
			textureid.put(i, t.getID());
		}
		GLES20.glDeleteTextures(size, textureid);
		textures.clear();
	}

	public void reloadAllTextures() {
		
		Set<Entry<Integer, Texture>> set = textures.entrySet();

		for (Entry<Integer, Texture> me : set) {
			Texture oldtexture = me.getValue();
			int id = me.getKey();
			Texture newtexture = loadTextureFromFile(id);
			oldtexture.mID = newtexture.getID();

		}

	}
	
	
	private Sound loadSoundFromFile(int soundName, boolean loop) {
		return new Sound( mSoundPool.load( mContext, soundName, 1), loop);
	}
	
	public Sound loadSound(int soundName, boolean loop) {
		
		if (sounds.containsKey(soundName)) {
			return sounds.get(soundName);
		}
		else {
			Sound sound = loadSoundFromFile(soundName, loop);
			sounds.put(soundName, sound);
			return sound;
		}
	}
	
	public void preloadSound(int soundName, boolean loop) {
		
		loadSound(soundName, loop);
	}
	
	public void unloadSound(int soundName) {
		forceUnloadSound(soundName);
	}

	public void forceUnloadSound(int soundName) {
	
		if (sounds.containsKey(soundName)) {
			mSoundPool.unload(sounds.get(soundName).getID());
			sounds.remove(soundName);
		}
	}

	public void unloadAllSounds() {
		mSoundPool.release(); //free all sound resources
		sounds.clear(); //remove all Sounds from sounds hashmap
	}

	public void unloadAllModels() {
		Collection<Model> set = models.values();

		for (Model m : set) {
			m.unload();
		}
	    
	    models.clear();
	}
	
	public void unloadAllContent() {
		unloadAllTextures();
		unloadAllSounds();
		unloadAllModels();
	}


	public Model loadModel(int modelid) {
		if (models.containsKey(modelid)) {
			return models.get(modelid);
		}
		else {
			Model m = new Model(modelid);
			m.LoadContent(this);
			models.put(modelid, m);
			return m;
		}
	}
	
	public void preloadModel(int modelid) {
		loadModel(modelid);
	}
	
	public Context getContext() {
		return mContext;
	}
	
	public SoundPool getSoundPool() {
		return mSoundPool;
	}
	
	public AudioManager getAudioManager() {
		return mAudioManager;
	}
	
	public MaterialManager getMaterialManager() {
		return mMaterialManager;
	}
	
	public Material loadMaterial(String name) {
		Material m = mMaterialManager.getMaterial(name);
		if (m!= null && !m.isLoaded())
			m.LoadContent(this);
		
		return m;
	}
	
	public void registerMaterial(String name, Material material) {
		mMaterialManager.registerMaterial(name, material);
	}


    public void registerShader(String name, Shader sp) {
        if (!shaders.containsKey(name)) {
            shaders.put(name, sp);
        }
    }

    public void unregisterShader(String name) {
        if (shaders.containsKey(name)) {
            shaders.remove(name);
        }
    }

    public Shader getShader(String name) {
        if (shaders.containsKey(name)) {
            return shaders.get(name);
        }
        return null;
    }

    public void reloadAllShaders() {
        Set<Entry<String, Shader>> set = shaders.entrySet();

		for (Entry<String, Shader> me : set) {
			Shader sp = me.getValue();
			sp.reload(this);
		}
    }


    private boolean isPOT(int value) {
        return (value & (value - 1)) == 0;
    }

    private int getNextPOT(int value) {
        int x = value & 0xFF;

        x = x - 1;
        x = x | (x >> 1);
        x = x | (x >> 2);
        x = x | (x >> 4);
        x = x | (x >> 8);
        x = x | (x >>16);
        return x + 1;
    }
	
	private Context mContext;
	private HashMap< Integer, Texture> textures;
	private HashMap< Integer, Model> models;
    private HashMap< String, Shader> shaders;
	private AudioManager mAudioManager;
	private SoundPool mSoundPool;
	private HashMap< Integer, Sound> sounds;
	private BitmapFactory.Options mBitmapOptions;
	private int[] glTextureID;
	private MaterialManager mMaterialManager;
}
