package com.botijasoftware.utils.renderer;

import java.util.ArrayList;

import android.opengl.GLES20;

import com.botijasoftware.utils.IndexBuffer;
import com.botijasoftware.utils.IndexBufferByte;
import com.botijasoftware.utils.IndexBufferInt;
import com.botijasoftware.utils.IndexBufferShort;

public class IndexBufferManager {
	
	private ArrayList<IndexBuffer> onUse = new ArrayList<IndexBuffer>();
	private ArrayList<IndexBuffer> spare = new ArrayList<IndexBuffer>();
	
	public IndexBuffer requestIB(int type, int format, int size) {
		IndexBuffer ib = searchSpareIB(type, format, size);
		if (ib != null) {
			return ib;
		}
		else {
			switch (format) {
			case GLES20.GL_BYTE:
				ib = new IndexBufferByte(type, size);
				break;
			case GLES20.GL_UNSIGNED_SHORT:
				ib = new IndexBufferShort(type, size);
				break;
			case GLES20.GL_SHORT:
				ib = new IndexBufferInt(type, size);
				break;
			default:
				ib = null;
				break;
			}
			if (ib != null)
				onUse.add(ib);
			return ib;
		}
	}
	
	public IndexBuffer searchSpareIB(int type, int format, int size) {
		
		for (int i=0; i< spare.size(); i++) {
			IndexBuffer ib = spare.get(i);
			if (ib.getMaxElements() == size && ib.getType() == type && ib.getFormat() == format) {
				spare.remove(i);
				onUse.add(ib);
				return ib;
			}
		}
		return null;
	}
	
	public void freeIB(IndexBuffer ib) {
		if (onUse.contains(ib)) {
			ib.position(0);
			onUse.remove(ib);
			spare.add(ib);
		}
	}
	
	
}