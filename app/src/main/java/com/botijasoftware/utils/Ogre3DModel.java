package com.botijasoftware.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import com.botijasoftware.utils.Mesh;
import javax.microedition.khronos.opengles.GL10;
import com.botijasoftware.utils.ResourceManager;
import com.botijasoftware.utils.animation.Skeleton;
import com.botijasoftware.utils.materials.Material;

public class Ogre3DModel {

	private VertexBuffer vb;
	private float mAlpha;
	public int mModelID;
	public String mName;
	protected boolean sharedvertices;
	public ArrayList<ArrayBuffer> mDataBuffers = new ArrayList<ArrayBuffer>();
	public ArrayBuffer mSharedDataBuffer;
	public ArrayList<IndexBuffer> mIndexBuffers = new ArrayList<IndexBuffer>();
	public ArrayList<Skeleton> mSkeleton;
	public ArrayList<Material> mMaterial;
	protected VertexBufferDeclaration mVertexDeclaration = new VertexBufferDeclaration();

	private final static int M_HEADER = 0x1000;
	private final static int M_MESH = 0x3000;
	private final static int M_SUBMESH = 0x4000;
	private final static int M_SUBMESH_OPERATION = 0x4010;
	private final static int M_SUBMESH_BONE_ASSIGNMENT = 0x4100;
	private final static int M_SUBMESH_TEXTURE_ALIAS = 0x4200;
	private final static int M_GEOMETRY = 0x5000;
	private final static int M_GEOMETRY_VERTEX_DECLARATION = 0x5100;
	private final static int M_GEOMETRY_VERTEX_ELEMENT = 0x5110;
	private final static int M_GEOMETRY_VERTEX_BUFFER = 0x5200;
	private final static int M_GEOMETRY_VERTEX_BUFFER_DATA = 0x5210;
	private final static int M_MESH_SKELETON_LINK = 0x6000;
	private final static int M_MESH_BONE_ASSIGNMENT = 0x7000;
	private final static int M_MESH_LOD = 0x8000;
	private final static int M_MESH_LOD_USAGE = 0x8100;
	private final static int M_MESH_LOD_MANUAL = 0x8110;
	private final static int M_MESH_LOD_GENERATED = 0x8120;
	private final static int M_MESH_BOUNDS = 0x9000;
	private final static int M_SUBMESH_NAME_TABLE = 0xA000;
	private final static int M_SUBMESH_NAME_TABLE_ELEMENT = 0xA100;
	private final static int M_EDGE_LISTS = 0xB000;
	private final static int M_EDGE_LIST_LOD = 0xB100;
	private final static int M_EDGE_GROUP = 0xB110;
	private final static int M_POSES = 0xC000;
	private final static int M_POSE = 0xC100;
	private final static int M_POSE_VERTEX = 0xC111;
	private final static int M_ANIMATIONS = 0xD000;
	private final static int M_ANIMATION = 0xD100;
	private final static int M_ANIMATION_TRACK = 0xD110;
	private final static int M_ANIMATION_MORPH_KEYFRAME = 0xD111;
	private final static int M_ANIMATION_POSE_KEYFRAME = 0xD112;
	private final static int M_ANIMATION_POSE_REF = 0xD113;
	private final static int M_TABLE_EXTREMES = 0xE000,
			M_GEOMETRY_NORMALS = 0x5100;
	private final static int M_GEOMETRY_COLOURS = 0x5200;
	private final static int M_GEOMETRY_TEXCOORDS = 0x5300;

	private final static int OT_POINT_LIST = 1;
	private final static int OT_LINE_LIST = 2;
	private final static int OT_LINE_STRIP = 3;
	private final static int OT_TRIANGLE_LIST = 4;
	private final static int OT_TRIANGLE_STRIP = 5;
	private final static int OT_TRIANGLE_FAN = 6;
	
	
	private final static int VES_POSITION = 1;// Position, 3 reals per vertex
	private final static int VES_BLEND_WEIGHTS = 2;// Blending weights
	private final static int VES_BLEND_INDICES = 3;// Blending indices
    private final static int VES_NORMAL = 4;// Normal, 3 reals per vertex
    private final static int VES_DIFFUSE = 5;// Diffuse colours
    private final static int VES_SPECULAR = 6;// Specular colours
    private final static int VES_TEXTURE_COORDINATES = 7;// Texture coordinates
    private final static int VES_BINORMAL = 8;// Binormal (Y axis if normal is Z)
    private final static int VES_TANGENT = 9;// Tangent (X axis if normal is Z)


    private final static int VET_FLOAT1 = 0;
    private final static int VET_FLOAT2 = 1;
    private final static int VET_FLOAT3 = 2;
    private final static int VET_FLOAT4 = 3;
    private final static int VET_COLOUR = 4;
    private final static int VET_SHORT1 = 5;
    private final static int VET_SHORT2 = 6;
    private final static int VET_SHORT3 = 7;
    private final static int VET_SHORT4 = 8;
    private final static int VET_UBYTE4 = 9;
    private final static int VET_COLOUR_ARGB = 10;
    private final static int VET_COLOUR_ABGR = 11;


	public Ogre3DModel(int modelid) {

		this(modelid, 1.0f);
	}

	public Ogre3DModel(int modelid, float alpha) {
		mModelID = modelid;
		mName = "";
		mAlpha = alpha;
	}

	public Ogre3DModel(String name) {
		this(name, 1.0f);
	}

	public Ogre3DModel(String name, float alpha) {
		mName = name;
		mModelID = 0;
		mAlpha = alpha;
	}

	public void LoadContent(GL10 gl, ResourceManager resources) {

		if (mModelID == 0 && !Objects.equals(mName, "")) {
			String packageName = resources.getContext().getApplicationContext()
					.getPackageName();

			mModelID = resources.getContext().getResources()
					.getIdentifier(mName, "raw", packageName);
		}

		if (mModelID != 0)
			readRAW(gl, resources, mModelID);
	}

	private String readString(DataInputStream dis) throws IOException {
		String str = "";
		byte c = dis.readByte();
		while (c != 0x0a) {
			str += c;
			c = dis.readByte();
		}
		return str;
	}

	private boolean readHeader(DataInputStream dis) throws IOException {
		int chunkid = dis.readUnsignedShort();
		String id = "";
		if (chunkid == M_HEADER) {
			id = readString(dis);
			return readMesh(dis);
		}
		return false;
	}

	private boolean readMesh(DataInputStream dis) throws IOException {
		int chunkid = dis.readUnsignedShort();
		int size = 0;
		if (chunkid != M_MESH)
			return false;

		int meshchunksize = dis.readInt();
		boolean skeletallyAnimated = dis.readBoolean();

		size += Short.SIZE + Integer.SIZE + 1;
		byte c;

		while (size < meshchunksize) {
			int subchunkid = dis.readUnsignedShort();
			int submeshchunksize = dis.readInt();
			size += submeshchunksize;
			switch (subchunkid) {
			case M_GEOMETRY:
				readGeometry(dis);
				break;
			case M_SUBMESH:
				readSubmesh(dis);
				break;
			case M_MESH_SKELETON_LINK:
				String skeletonNane = readString(dis);
				break;
			case M_MESH_BONE_ASSIGNMENT:
				int vertexindex = dis.readInt();
				int boneindex = dis.readUnsignedShort();
				float weight = dis.readFloat();
				break;
			case M_MESH_LOD:
				dis.skipBytes(submeshchunksize - Short.SIZE - Integer.SIZE); // skip chunk
				break;
			case M_MESH_BOUNDS:
				dis.skipBytes(submeshchunksize - Short.SIZE - Integer.SIZE); // skip chunk
				break;
			default:
				dis.skipBytes(submeshchunksize - Short.SIZE - Integer.SIZE); // skip chunk
				break;
			}
		}

		return false;
	}

	private void readSubmesh(DataInputStream dis) throws IOException {
		String materialName = readString(dis);

		boolean useSharedVertices = dis.readBoolean();
		int indexCount = dis.readInt();
		boolean indexes32bit = dis.readBoolean();
		if (indexes32bit) {
			for (int i = 0; i < indexCount; i++) {
				int index = dis.readInt();
			}
		} else {
			for (int i = 0; i < indexCount; i++) {
				int index = dis.readUnsignedShort();
			}
		}
		if (!useSharedVertices) {
			readGeometry(dis);
		}

		dis.mark(Integer.MAX_VALUE);
		int chunkid = dis.readUnsignedShort();

		while (chunkid == M_GEOMETRY || chunkid == M_SUBMESH_OPERATION
				|| chunkid == M_SUBMESH_BONE_ASSIGNMENT
				|| chunkid == M_SUBMESH_TEXTURE_ALIAS) {
			int chunksize = dis.readInt();

			switch (chunkid) {
			case M_GEOMETRY:
				readGeometry(dis);
				break;
			case M_SUBMESH_OPERATION:
				int operationType = dis.readUnsignedShort();
				break;
			case M_SUBMESH_BONE_ASSIGNMENT:
				int vertexindex = dis.readInt();
				int boneindex = dis.readUnsignedShort();
				float weight = dis.readFloat();
				break;
			case M_SUBMESH_TEXTURE_ALIAS:
				String aliasName = readString(dis);
				String textureName = readString(dis);
				break;
			default:
				break;
			}

			dis.mark(Integer.MAX_VALUE);
			chunkid = dis.readUnsignedShort();
		}
		dis.reset();

	}

	private boolean readGeometry(DataInputStream dis) throws IOException {
		int vertexCount = dis.readInt();
		int chunkid = dis.readUnsignedShort();

		int subchunkid;

		switch (chunkid) {
		case M_GEOMETRY_VERTEX_DECLARATION:
			dis.mark(Integer.MAX_VALUE);
			subchunkid = dis.readUnsignedShort();
			while (subchunkid == M_GEOMETRY_VERTEX_ELEMENT) {
				int subchunksize = dis.readInt();
				int source = dis.readUnsignedShort();
				int type = dis.readUnsignedShort();
				int semantic = dis.readUnsignedShort();
				int offset = dis.readUnsignedShort();
				int index = dis.readUnsignedShort();
			    
			    
			    int vbformat = 0;
				int vbusage = 0;
				int vbsize = 0;
			    
				switch (type) {
				case VET_FLOAT1:
					vbformat = VertexBufferDefinition.FLOAT;
					vbsize = 1;
					break;
				case VET_FLOAT2:
					vbformat = VertexBufferDefinition.FLOAT;
					vbsize = 2;
					break;
				case VET_FLOAT3:
					vbformat = VertexBufferDefinition.FLOAT;
					vbsize = 3;
					break;
				case VET_FLOAT4:
					vbformat = VertexBufferDefinition.FLOAT;
					vbsize = 4;
					break;
				case VET_COLOUR:
					vbformat = VertexBufferDefinition.BYTE;
					vbsize = 4;
					break;
				case VET_SHORT1:
					vbformat = VertexBufferDefinition.SHORT;
					vbsize = 1;
					break;
				case VET_SHORT2:
					vbformat = VertexBufferDefinition.SHORT;
					vbsize = 2;
					break;
				case VET_SHORT3:
					vbformat = VertexBufferDefinition.SHORT;
					vbsize = 3;
					break;
				case VET_SHORT4:
					vbformat = VertexBufferDefinition.SHORT;
					vbsize = 4;
					break;
				case VET_UBYTE4:
					vbformat = VertexBufferDefinition.BYTE;
					vbsize = 4;
					break;
				case VET_COLOUR_ABGR:
					vbformat = VertexBufferDefinition.BYTE;
					vbsize = 4;
					break;
				case VET_COLOUR_ARGB:
					vbformat = VertexBufferDefinition.BYTE;
					vbsize = 4;
					break;
				default:
					break;
				}
				
			
				switch (semantic) {
				case VES_POSITION:
					vbusage = VertexBufferDefinition.DEF_VERTEX;
					break;
				case VES_NORMAL:
					vbusage = VertexBufferDefinition.DEF_NORMAL;
					break;
				case VES_DIFFUSE:
					vbusage = VertexBufferDefinition.DEF_COLOR;
					break;
				case VES_TEXTURE_COORDINATES:
					vbusage = VertexBufferDefinition.DEF_TEXTURE_COORD;
					break;
				case VES_TANGENT:
					vbusage = VertexBufferDefinition.DEF_TANGENT;
					break;
					
				default:
					break;
				}
				
				
				VertexBufferDefinition vd = new VertexBufferDefinition(vbformat, vbusage, vbsize, VertexBufferDefinition.ACCESS_DYNAMIC);
				mVertexDeclaration.add(vd);

				dis.mark(Integer.MAX_VALUE);
				subchunkid = dis.readUnsignedShort();
			}
			dis.reset();
			break;
		case M_GEOMETRY_VERTEX_BUFFER:
			int chunksize = dis.readInt();
			int bindIndex = dis.readUnsignedShort();
			int vertexSize = dis.readUnsignedShort();

			subchunkid = dis.readUnsignedShort();
			if (subchunkid == M_GEOMETRY_VERTEX_BUFFER_DATA) {
				int subchunksize = dis.readInt();
				//TODO: read data
			}
			break;
		default:
			break;
		}

		return true;
	}

	private short readChunk(DataInputStream dis) throws IOException {
		short chunkid = dis.readShort();
		return chunkid;
	}

	private boolean readRAW(GL10 gl, ResourceManager resources, int fileid) {

		try {

			InputStream is = resources.getContext().getResources()
					.openRawResource(fileid);

			DataInputStream osr = new DataInputStream(is);
			// ObjectInputStream osr = new ObjectInputStream(is);

			
			osr.close();
			is.close();

		} catch (Exception e) {
			// System.out.println("XML Pasing Excpetion = " + e);
			return false;
		}

		/*for (int i = 0; i < mMesh.size(); i++)
			mMesh.get(i).mVertexBuffer.makeVBO(gl);*/

		return true;
	}

	public void Draw(GL10 gl) {

		/*int size = mMesh.size();
		for (int i = 0; i < size; i++) {
			Mesh m = mMesh.get(i);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m.mTexture.getID());
			m.mVertexBuffer.Draw(gl, m.mIndexBuffer);
		}*/
	}

	public Mesh getMesh(String name) {
		/*int size = mMesh.size();
		for (int i = 0; i < size; i++) {
			Mesh m = mMesh.get(i);
			if (m.mName == name) {
				return m;
			}
		}*/
		return null;
	}

	public void unload(GL10 gl) {
		/*for (int i = 0; i < mMesh.size(); i++) {
			mMesh.get(i).mVertexBuffer.free(gl);
		}*/
	}

}
