package com.botijasoftware.utils;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import android.opengl.GLES20;
import com.botijasoftware.utils.animation.Skeleton;
import com.botijasoftware.utils.materials.Material;
import com.botijasoftware.utils.materials.MaterialPass;
import com.botijasoftware.utils.materials.MaterialRenderer;

public class Model {
	
	public Model(int modelid) {
		
		this(modelid, (byte)255);
	}

	public Model(int modelid, byte alpha) {
		mModelID = modelid;
		mName = "";
		mMesh = new ArrayList<Mesh>();
		mAlpha = alpha;
	}


	public Model(String name) {
		this(name, (byte)255);
	}
	
	
	public Model(String name, byte alpha) {
		mName = name;
		mModelID = 0;
		mMesh = new ArrayList<Mesh>();
		mAlpha = alpha;
	}
	
	public void LoadContent(ResourceManager resources) {
		
		if (mModelID == 0 && !mName.equals("")) {
			String packageName = resources.getContext().getApplicationContext().getPackageName();
		
			mModelID = resources.getContext().getResources().getIdentifier(mName, "raw", packageName);
		}
		
		if (mModelID != 0)
			readRAW(resources, mModelID);
	}	
	
	
	private boolean readRAW(ResourceManager resources, int fileid) {
		
		try {
			
			InputStream is = resources.getContext().getResources().openRawResource(fileid);

		    DataInputStream osr = new DataInputStream ( is );
			//ObjectInputStream osr = new ObjectInputStream(is);
			
			byte magic0 = osr.readByte();
			byte magic1 = osr.readByte();
			byte magic2 = osr.readByte();
			byte magic3 = osr.readByte();
			if (magic0 != '3' || magic1 != 'D' || magic2 != 'B' || magic3 != 'T')
				return false;
					
			byte version_major = osr.readByte();
			byte version_minor = osr.readByte();
			if (version_major != 0 && version_minor != 1)
				return false;
			
			int nmesh = osr.readShort();
			
			
			for (int m = 0; m < nmesh; m++) {
			
				int namelen = osr.readUnsignedByte();
				String name = "";
				for (int i=0;i<namelen;i++)
					name += (char)osr.readUnsignedByte();
			
				int texturelen = osr.readUnsignedByte();
				String texturename = "";
				for (int i=0;i<texturelen;i++)
					texturename += (char)osr.readUnsignedByte();
			
				//skip padding bytes
				byte padding = osr.readByte();
				osr.skip(padding);
			

				int nvertex = osr.readInt();
				int nnormal = osr.readInt();
				int nuv = osr.readInt();
				int ncolor = osr.readInt();
				int nindex = osr.readInt();
			
				String packageName = resources.getContext().getApplicationContext().getPackageName();
				int drawableid = resources.getContext().getResources().getIdentifier(texturename.toLowerCase(), "drawable", packageName);
				if (drawableid == 0)
					return false;
			
				Texture texture = resources.loadTexture(drawableid, TextureOptions.linear_repeat);
			
			
				if (nvertex > 0 && nvertex < 65536 && nindex > 0 && nindex < 65536) {
					VertexBufferDeclaration vbd = new VertexBufferDeclaration();
					vbd.add(new VertexBufferDefinition(VertexBufferDefinition.FLOAT, VertexBufferDefinition.DEF_VERTEX,3,VertexBufferDefinition.ACCESS_DYNAMIC));
					vbd.add(new VertexBufferDefinition(VertexBufferDefinition.FLOAT, VertexBufferDefinition.DEF_TEXTURE_COORD,2,VertexBufferDefinition.ACCESS_DYNAMIC));
					if (ncolor>0)
						vbd.add(new VertexBufferDefinition(VertexBufferDefinition.BYTE, VertexBufferDefinition.DEF_COLOR,4,VertexBufferDefinition.ACCESS_DYNAMIC));
					if (nnormal>0)
						vbd.add(new VertexBufferDefinition(VertexBufferDefinition.FLOAT, VertexBufferDefinition.DEF_NORMAL,3,VertexBufferDefinition.ACCESS_DYNAMIC));

					
					vb = new VertexBuffer(nvertex, vbd); // use color & normals
					ib = new IndexBufferShort(GLES20.GL_TRIANGLES, nindex);
					
					Mesh newmesh = new Mesh(name, texture, vb, ib);

					Material mat = resources.loadMaterial( texturename);
					if (mat == null) { //create a simple material if nothing is found
						mat = new Material();
						mat.addPass(new MaterialPass(mat, drawableid));
						mat.LoadContent(resources);
						resources.registerMaterial(texturename, mat);
					}
					newmesh.mMaterial = mat;
					mMesh.add( newmesh );
					//tmpArray = new float[nvertex*4];
				}
				else
					return false;
				float tmp[] = new float[nvertex*3];
				
				for (int i = 0; i < nvertex*3; i+=3) {
					tmp[i] = osr.readFloat();
					tmp[i+1] = osr.readFloat();
					tmp[i+2] = osr.readFloat();
					
				}
				vb.getBuffer(0).put(tmp);				
			
				if (nnormal>0) {
					for (int i = 0; i < nvertex*3; i+=3) {
						tmp[i] = osr.readFloat();
						tmp[i+1] = osr.readFloat();
						tmp[i+2] = osr.readFloat();
					}
				
					if (ncolor>0)
						vb.getBuffer(3).put(tmp);
					else
						vb.getBuffer(2).put(tmp);
				}
			
				tmp = new float[nuv*2];
				for (int i= 0; i < nuv*2; i+=2) {
					tmp[i] = osr.readFloat();
					tmp[i+1] = osr.readFloat();
				}
				vb.getBuffer(1).put(tmp);
				//vb.loadTextCoordData(tmpArray, nuv*2);
			
				
				if (ncolor>0) {
					byte ctmp[] = new byte[ncolor*4];
					for (int i = 0; i < ncolor*4; i+=4) {
						ctmp[i] = (byte) ((byte)osr.readByte() & 0xff);
						ctmp[i+1] = (byte) ((byte)osr.readByte() & 0xff);
						ctmp[i+2] = (byte) ((byte)osr.readByte() & 0xff);
						ctmp[i+3] = (byte) (mAlpha & 0xff);
            	
					}
					vb.getBuffer(2).put(ctmp);
				}
				//vb.loadColorData(tmpArray, ncolor*4);
			
				short itmp[] = new short[nindex];
				for (short i = 0; i < nindex; i++) {
            		itmp[i] = osr.readShort();
				}
				ib.put(itmp);
			
			}
			
			osr.close();
			is.close();
			
		}
		catch (Exception e) {
			//System.out.println("XML Pasing Excpetion = " + e);
			return false;
		}
		
		for (int i = 0; i< mMesh.size(); i++)
			mMesh.get(i).mVertexBuffer.makeVBO();

	return true;
	}

	
	public void Draw() {
		MaterialRenderer.renderModel(this);
		
		/*int size = mMesh.size();
		for (int i = 0; i < size; i++) {
			Mesh m = mMesh.get(i);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m.mTexture.getID());
			m.mVertexBuffer.Draw(gl);
		}*/
	}

	public void Draw(Material material) {
		MaterialRenderer.renderModel(this, material);
	}
	
	public Mesh getMesh(String name) {
		int size = mMesh.size();
		for (int i = 0; i < size; i++) {
			Mesh m = mMesh.get(i);
			if (m.mName.equals(name)) {
				return m;
			}
		}
		return null;
	}
	
	public void unload() {
		for (int i = 0; i < mMesh.size(); i++) {
			mMesh.get(i).mVertexBuffer.free();
		}
	}

	private VertexBuffer vb;
	private IndexBuffer ib;
	private byte mAlpha;
	public int mModelID;
	public String mName;
	public ArrayList<Mesh> mMesh;
	public ArrayList<Skeleton> mSkeleton;
	//private float[] tmpArray;
}
