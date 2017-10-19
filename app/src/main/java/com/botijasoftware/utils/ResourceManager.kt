package com.botijasoftware.utils

import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.IntBuffer
import java.util.HashMap
import kotlin.collections.Map.Entry

import com.botijasoftware.utils.materials.Material
import com.botijasoftware.utils.materials.MaterialManager
import com.botijasoftware.utils.shaders.Shader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.opengl.GLES20
import android.opengl.GLUtils
import android.os.Build

class ResourceManager(val context: Context) {

    private val textures: HashMap<Int, Texture>
    private val models: HashMap<Int, Model>
    private val shaders: HashMap<String, Shader>
    val audioManager: AudioManager
    var soundPool: SoundPool
    private val sounds: HashMap<Int, Sound>
    private val mBitmapOptions: BitmapFactory.Options
    private val glTextureID: IntArray
    val materialManager: MaterialManager


    init {
        glTextureID = IntArray(1)
        textures = HashMap<Int, Texture>()
        sounds = HashMap<Int, Sound>()
        models = HashMap<Int, Model>()
        shaders = HashMap<String, Shader>()
        //mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);

        // API 21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            val audioAttributesBuilder = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
            val builder = SoundPool.Builder()
            builder.setMaxStreams(4)
            builder.setAudioAttributes(audioAttributesBuilder)
            soundPool = builder.build()
        } else {
            soundPool = SoundPool(4, AudioManager.STREAM_MUSIC, 0)
        }


        audioManager = this.context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mBitmapOptions = BitmapFactory.Options()
        mBitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888//Bitmap.Config.RGB_4444
        materialManager = MaterialManager()
    }

    private fun getTexture(textureName: Int): Texture? {
        if (textures.containsKey(textureName))
            return textures[textureName]
        else
            return null
    }

    private fun addTexture(textureName: Int, texture: Texture) {
        textures.put(textureName, texture)
    }

    @JvmOverloads fun loadTexture(textureName: Int, options: TextureOptions = TextureOptions.default_options): Texture {
        var t = getTexture(textureName)
        if (t != null) {
            t.addReference()
            return t
        } else {
            t = loadTextureFromFile(textureName, options)
            t.addReference()
            //if (t != null)
            addTexture(textureName, t)

            return t
        }
    }

    @JvmOverloads fun preloadTexture(textureName: Int, options: TextureOptions = TextureOptions.default_options) {
        val t = loadTexture(textureName, options)
        t.removeReference()
    }

    fun loadTextureFromCache(textureName: Int): Texture {
        val t = getTexture(textureName)
        t!!.addReference()
        return t
    }


    @JvmOverloads fun loadTextureFromFile(textureName: Int, options: TextureOptions = TextureOptions.default_options): Texture {

        GLES20.glGenTextures(1, glTextureID, 0)
        val id = glTextureID[0]

        //Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), textureName, mBitmapOptions);

        //Loading code from replica island (for testing on real hardware)
        val `is` = context.resources.openRawResource(textureName)
        var bmp: Bitmap
        try {
            bmp = BitmapFactory.decodeStream(`is`)
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
                // Ignore.
            }

        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id)

        val width = bmp.width
        val height = bmp.height
        // Set all of our texture parameters:
        options.apply()
        //GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, options.minFilter);//_MIPMAP_NEAREST);
        //GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, options.maxFilter);//_MIPMAP_NEAREST);
        //GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, options.wraps); //GL_REPEAT
        //GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, options.wrapt); //GL_REPEAT
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0)
        //GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA,width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, bmp);

        if (options.mipmap) {

            var bmp2 = Bitmap.createScaledBitmap(bmp, width shr 1, height shr 1, true)
            bmp.recycle()
            bmp = bmp2

            // Generate, and load up all of the mipmaps:
            var level = 1
            var h = bmp.height
            var w = bmp.width
            while (true) {
                // Push the bitmap onto the GPU:
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, level, bmp, 0)

                // We need to stop when the texture is 1x1:
                if (h == 1 && w == 1) break

                // Resize, and let's go again:
                w = w shr 1
                h = h shr 1
                if (w < 1) w = 1
                if (h < 1) h = 1

                bmp2 = Bitmap.createScaledBitmap(bmp, w, h, true)
                bmp.recycle()
                bmp = bmp2
                level++
            }
        }

        bmp.recycle()

        return Texture(id, width, height)
    }

    fun loadTextureFromBitmap(bmp: Bitmap, options: TextureOptions): Texture {
        var nbmp = bmp

        GLES20.glGenTextures(1, glTextureID, 0)
        val id = glTextureID[0]

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id)

        val width = nbmp.width
        val height = nbmp.height

        options.apply()
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, nbmp, 0)

        if (options.mipmap) {

            var bmp2 = Bitmap.createScaledBitmap(nbmp, width shr 1, height shr 1, true)
            //bmp.recycle();
            nbmp = bmp2

            // Generate, and load up all of the mipmaps:
            var level = 1
            var h = nbmp.height
            var w = nbmp.width
            while (true) {
                // Push the bitmap onto the GPU:
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, level, nbmp, 0)

                // We need to stop when the texture is 1x1:
                if (h == 1 && w == 1) break

                // Resize, and let's go again:
                w = w shr 1
                h = h shr 1
                if (w < 1) w = 1
                if (h < 1) h = 1

                bmp2 = Bitmap.createScaledBitmap(nbmp, w, h, true)
                nbmp.recycle()
                nbmp = bmp2
                level++
            }
            bmp.recycle()
        }




        return Texture(id, width, height)
    }


    fun loadCubeMapTexure(basename: String): CubeMapTexture? {

        GLES20.glGenTextures(1, glTextureID, 0)
        val id = glTextureID[0]

        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, id)

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_CUBE_MAP,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR.toFloat())
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_CUBE_MAP,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR.toFloat())

        //GLES20.glTexGeni(GLES20.GL_TEXTURE_GEN_STR, GLES20.GL_TEXTURE_GEN_MODE, GLES20.GL_REFLECTION_MAP);
        //GLES20.glEnable(GLES20.GL_TEXTURE_GEN_STR);

        var width = 0
        var height = 0

        var bmp = getBitmap(basename + "_px")
        if (bmp != null) {

            width = bmp.width
            height = bmp.height

            loadCubeMapFace(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X, bmp)
            bmp.recycle()
        } else
            return null

        bmp = getBitmap(basename + "_py")
        if (bmp != null) {
            loadCubeMapFace(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, bmp)
            bmp.recycle()
        } else
            return null

        bmp = getBitmap(basename + "_pz")
        if (bmp != null) {
            loadCubeMapFace(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, bmp)
            bmp.recycle()
        } else
            return null

        bmp = getBitmap(basename + "_nx")
        if (bmp != null) {
            loadCubeMapFace(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, bmp)
            bmp.recycle()
        } else
            return null

        bmp = getBitmap(basename + "_ny")
        if (bmp != null) {
            loadCubeMapFace(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, bmp)
            bmp.recycle()
        } else
            return null

        bmp = getBitmap(basename + "_nz")
        if (bmp != null) {
            loadCubeMapFace(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, bmp)
            bmp.recycle()
        } else
            return null

        return CubeMapTexture(id, width, height)

    }

    private fun loadCubeMapFace(face: Int, bmp: Bitmap) {

    }

    private fun getBitmap(filename: String): Bitmap? {

        val id = context.resources.getIdentifier(filename, "drawable", context.packageName)
        if (id == 0)
            return null
        val inputstream = context.resources.openRawResource(id)
        var bmp: Bitmap?
        try {
            bmp = BitmapFactory.decodeStream(inputstream)
        } finally {
            try {
                inputstream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return bmp
    }

    fun unloadTexture(textureName: Int) {
        val t = getTexture(textureName)
        if (t != null) {
            t.removeReference()
            if (!t.isReferenced) {
                forceUnloadTexture(textureName)

            }
        }

    }

    /*public void forceUnloadTexture(Texture texture) {
			glTextureID[0] = texture.getID();
			GLES20.glDeleteTextures(1, glTextureID,0);
	}*/


    fun forceUnloadTexture(textureName: Int) {

        if (textures.containsKey(textureName)) {
            glTextureID[0] = textures[textureName]!!.id
            //IntBuffer textureid = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
            //textureid.put(0, textures.get(textureName).getID());
            GLES20.glDeleteTextures(1, glTextureID, 0)  //release texture memory
            textures.remove(textureName)
        }
    }

    //TODO: optimize with glTextureID and remove IntBuffer
    fun unloadAllTextures() {
        val tex = textures.values.toTypedArray()
        val size = tex.size
        val textureid = ByteBuffer.allocateDirect(4 * size).order(ByteOrder.nativeOrder()).asIntBuffer()

        for (i in 0..size - 1) {
            val t = tex[i]
            textureid.put(i, t.id)
        }
        GLES20.glDeleteTextures(size, textureid)
        textures.clear()
    }

    fun reloadAllTextures() {

        val set = textures.entries

        for ((id, oldtexture) in set) {
            val newtexture = loadTextureFromFile(id)
            oldtexture.id = newtexture.id

        }

    }


    private fun loadSoundFromFile(soundName: Int, loop: Boolean): Sound {
        return Sound(soundPool.load(context, soundName, 1), loop)
    }

    fun loadSound(soundName: Int, loop: Boolean): Sound {

        if (sounds.containsKey(soundName)) {
            return sounds[soundName]!!
        } else {
            val sound = loadSoundFromFile(soundName, loop)
            sounds.put(soundName, sound)
            return sound
        }
    }

    fun preloadSound(soundName: Int, loop: Boolean) {

        loadSound(soundName, loop)
    }

    fun unloadSound(soundName: Int) {
        forceUnloadSound(soundName)
    }

    fun forceUnloadSound(soundName: Int) {

        if (sounds.containsKey(soundName)) {
            soundPool.unload(sounds[soundName]!!.id)
            sounds.remove(soundName)
        }
    }

    fun unloadAllSounds() {
        soundPool.release() //free all sound resources
        sounds.clear() //remove all Sounds from sounds hashmap
    }

    fun unloadAllModels() {
        val set = models.values

        for (m in set) {
            m.unload()
        }

        models.clear()
    }

    fun unloadAllContent() {
        unloadAllTextures()
        unloadAllSounds()
        unloadAllModels()
    }


    fun loadModel(modelid: Int): Model {
        if (models.containsKey(modelid)) {
            return models[modelid]!!
        } else {
            val m = Model(modelid)
            m.LoadContent(this)
            models.put(modelid, m)
            return m
        }
    }

    fun preloadModel(modelid: Int) {
        loadModel(modelid)
    }

    fun loadMaterial(name: String): Material? {
        val m = materialManager.getMaterial(name)
        if (m != null && !m.isLoaded)
            m.LoadContent(this)

        return m
    }

    fun registerMaterial(name: String, material: Material) {
        materialManager.registerMaterial(name, material)
    }


    fun registerShader(name: String, sp: Shader) {
        if (!shaders.containsKey(name)) {
            shaders.put(name, sp)
        }
    }

    fun unregisterShader(name: String) {
        if (shaders.containsKey(name)) {
            shaders.remove(name)
        }
    }

    fun getShader(name: String): Shader? {
        if (shaders.containsKey(name)) {
            return shaders[name]
        }
        return null
    }

    fun reloadAllShaders() {
        val set = shaders.entries

        for ((_, sp) in set) {
            sp.reload(this)
        }
    }


    private fun isPOT(value: Int): Boolean {
        return value and value - 1 == 0
    }

    private fun getNextPOT(value: Int): Int {
        var x = value and 0xFF

        x -= 1
        x = x or (x shr 1)
        x = x or (x shr 2)
        x = x or (x shr 4)
        x = x or (x shr 8)
        x = x or (x shr 16)
        return x + 1
    }

}
