package com.botijasoftware.utils

import java.io.DataInputStream
import java.io.InputStream
import java.util.ArrayList
import android.opengl.GLES20
import com.botijasoftware.utils.animation.Skeleton
import com.botijasoftware.utils.materials.Material
import com.botijasoftware.utils.materials.MaterialPass
import com.botijasoftware.utils.materials.MaterialRenderer
import kotlin.experimental.and

class Model {

    private lateinit var vb: VertexBuffer
    private lateinit  var ib: IndexBuffer
    private var mAlpha: Byte = 0
    var mModelID: Int = 0
    var mName: String
    var mMesh: ArrayList<Mesh>
    lateinit var mSkeleton: ArrayList<Skeleton>

    @JvmOverloads constructor(modelid: Int, alpha: Byte = 255.toByte()) {
        mModelID = modelid
        mName = ""
        mMesh = ArrayList<Mesh>()
        mAlpha = alpha
    }


    @JvmOverloads constructor(name: String, alpha: Byte = 255.toByte()) {
        mName = name
        mModelID = 0
        mMesh = ArrayList<Mesh>()
        mAlpha = alpha
    }

    fun LoadContent(resources: ResourceManager) {

        if (mModelID == 0 && mName != "") {
            val packageName = resources.context.applicationContext.packageName

            mModelID = resources.context.resources.getIdentifier(mName, "raw", packageName)
        }

        if (mModelID != 0)
            readRAW(resources, mModelID)
    }


    private fun readRAW(resources: ResourceManager, fileid: Int): Boolean {

        try {

            val inputstream = resources.context.resources.openRawResource(fileid)

            val osr = DataInputStream(inputstream)
            //ObjectInputStream osr = new ObjectInputStream(is);

            val magic0 = osr.readByte()
            val magic1 = osr.readByte()
            val magic2 = osr.readByte()
            val magic3 = osr.readByte()
            if (magic0 != '3'.toByte() || magic1 != 'D'.toByte() || magic2 != 'B'.toByte() || magic3 != 'T'.toByte())
                return false

            val version_major = osr.readByte()
            val version_minor = osr.readByte()
            if (version_major.toInt() != 0 && version_minor.toInt() != 1)
                return false

            val nmesh = osr.readShort().toInt()


            for (m in 0..nmesh - 1) {

                val namelen = osr.readUnsignedByte()
                var name = ""
                for (i in 0..namelen - 1)
                    name += osr.readUnsignedByte().toChar()

                val texturelen = osr.readUnsignedByte()
                var texturename = ""
                for (i in 0..texturelen - 1)
                    texturename += osr.readUnsignedByte().toChar()

                //skip padding bytes
                val padding = osr.readByte()
                osr.skip(padding.toLong())


                val nvertex = osr.readInt()
                val nnormal = osr.readInt()
                val nuv = osr.readInt()
                val ncolor = osr.readInt()
                val nindex = osr.readInt()

                val packageName = resources.context.applicationContext.packageName
                val drawableid = resources.context.resources.getIdentifier(texturename.toLowerCase(), "drawable", packageName)
                if (drawableid == 0)
                    return false

                val texture = resources.loadTexture(drawableid, TextureOptions.trilinear_repeat)


                if (nvertex > 0 && nvertex < 65536 && nindex > 0 && nindex < 65536) {
                    val vbd = VertexBufferDeclaration()
                    vbd.add(VertexBufferDefinition(VertexBufferDefinition.FLOAT, VertexBufferDefinition.DEF_VERTEX, 3, VertexBufferDefinition.ACCESS_DYNAMIC))
                    vbd.add(VertexBufferDefinition(VertexBufferDefinition.FLOAT, VertexBufferDefinition.DEF_TEXTURE_COORD, 2, VertexBufferDefinition.ACCESS_DYNAMIC))
                    if (ncolor > 0)
                        vbd.add(VertexBufferDefinition(VertexBufferDefinition.BYTE, VertexBufferDefinition.DEF_COLOR, 4, VertexBufferDefinition.ACCESS_DYNAMIC))
                    if (nnormal > 0)
                        vbd.add(VertexBufferDefinition(VertexBufferDefinition.FLOAT, VertexBufferDefinition.DEF_NORMAL, 3, VertexBufferDefinition.ACCESS_DYNAMIC))


                    vb = VertexBuffer(nvertex, vbd) // use color & normals
                    ib = IndexBufferShort(GLES20.GL_TRIANGLES, nindex)

                    val newmesh = Mesh(name, texture, vb, ib)

                    var mat: Material? = resources.loadMaterial(texturename)
                    if (mat == null) { //create a simple material if nothing is found
                        mat = Material()
                        mat.addPass(MaterialPass(mat, drawableid))
                        mat.LoadContent(resources)
                        resources.registerMaterial(texturename, mat)
                    }
                    newmesh.mMaterial = mat
                    mMesh.add(newmesh)
                    //tmpArray = new float[nvertex*4];
                } else
                    return false
                var tmp = FloatArray(nvertex * 3)

                run {
                    var i = 0
                    while (i < nvertex * 3) {
                        tmp[i] = osr.readFloat()
                        tmp[i + 1] = osr.readFloat()
                        tmp[i + 2] = osr.readFloat()
                        i += 3

                    }
                }
                vb.getBuffer(0)!!.put(tmp)

                if (nnormal > 0) {
                    var i = 0
                    while (i < nvertex * 3) {
                        tmp[i] = osr.readFloat()
                        tmp[i + 1] = osr.readFloat()
                        tmp[i + 2] = osr.readFloat()
                        i += 3
                    }

                    if (ncolor > 0)
                        vb.getBuffer(3)!!.put(tmp)
                    else
                        vb.getBuffer(2)!!.put(tmp)
                }

                tmp = FloatArray(nuv * 2)
                run {
                    var i = 0
                    while (i < nuv * 2) {
                        tmp[i] = osr.readFloat()
                        tmp[i + 1] = osr.readFloat()
                        i += 2
                    }
                }

                vb.getBuffer(1)!!.put(tmp)
                //vb.loadTextCoordData(tmpArray, nuv*2);


                if (ncolor > 0) {
                    val ctmp = ByteArray(ncolor * 4)
                    var i = 0
                    while (i < ncolor * 4) {
                        ctmp[i] = (osr.readByte() and 0xff.toByte())
                        ctmp[i + 1] = (osr.readByte() and 0xff.toByte())
                        ctmp[i + 2] = (osr.readByte() and 0xff.toByte())
                        ctmp[i + 3] = (mAlpha and 0xff.toByte())
                        i += 4

                    }
                    vb.getBuffer(2)!!.put(ctmp)
                }
                //vb.loadColorData(tmpArray, ncolor*4);

                val itmp = ShortArray(nindex)
                for (i in 0..nindex - 1) {
                    itmp[i] = osr.readShort()
                }
                ib.put(itmp)

            }

            osr.close()
            inputstream.close()

        } catch (e: Exception) {
            //System.out.println("XML Pasing Excpetion = " + e);
            return false
        }

        for (i in mMesh.indices)
            mMesh[i].mVertexBuffer.makeVBO()

        return true
    }


    fun Draw() {
        MaterialRenderer.renderModel(this)

        /*int size = mMesh.size();
		for (int i = 0; i < size; i++) {
			Mesh m = mMesh.get(i);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m.mTexture.getID());
			m.mVertexBuffer.Draw(gl);
		}*/
    }

    fun Draw(material: Material) {
        MaterialRenderer.renderModel(this, material)
    }

    fun getMesh(name: String): Mesh? {
        val size = mMesh.size
        for (i in 0..size - 1) {
            val m = mMesh[i]
            if (m.mName == name) {
                return m
            }
        }
        return null
    }

    fun unload() {
        for (i in mMesh.indices) {
            mMesh[i].mVertexBuffer.free()
        }
    }


}
