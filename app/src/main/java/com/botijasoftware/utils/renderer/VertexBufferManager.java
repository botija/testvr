package com.botijasoftware.utils.renderer;

import java.util.ArrayList;
import com.botijasoftware.utils.VertexBuffer;
import com.botijasoftware.utils.VertexBufferDeclaration;
import com.botijasoftware.utils.VertexBufferDefinition;

public class VertexBufferManager {
	
	private ArrayList<VertexBuffer> onUse = new ArrayList<VertexBuffer>();
	private ArrayList<VertexBuffer> spare = new ArrayList<VertexBuffer>();
	
	public VertexBuffer requestVB(VertexBufferDeclaration vbd, int size) {
		VertexBuffer vb = searchSpareVB(vbd, size);
		if (vb != null) {
			return vb;
		}
		else {
			vb = new VertexBuffer(size, vbd);
			onUse.add(vb);
			return vb;
		}
	}
	
	public VertexBuffer searchSpareVB(VertexBufferDeclaration vbd, int size) {
		boolean found = false;
		for (int i=0; i< spare.size(); i++) {
			VertexBuffer vb = spare.get(i);
			if (vb.getElementCount() == size) { //same size
				VertexBufferDeclaration dec = vb.getDeclaration();
				if (dec.getCount() == vbd.getCount()) { //same number of elements
					found = true;
					for (int j=0; j<vbd.getCount() && found; j++) { //check each element
						VertexBufferDefinition def1 = dec.getDefinition(j);
						VertexBufferDefinition def2 = vbd.getDefinition(j);
						if (def1.mAccess != def2.mAccess || def1.mFormat != def2.mFormat || def1.mSize != def2.mSize || def1.mUsage != def2.mUsage) {						
							found = false;
						}
					}
					if (found) {
						spare.remove(vb);
						onUse.add(vb);
						return vb;
					}
				}
			}
		}
		return null;
	}
	
	public void freeVB(VertexBuffer vb) {
		if (onUse.contains(vb)) {
			onUse.remove(vb);
			spare.add(vb);
		}
	}
	
	
}