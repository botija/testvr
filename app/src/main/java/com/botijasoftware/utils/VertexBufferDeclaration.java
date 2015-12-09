package com.botijasoftware.utils;

import java.util.ArrayList;

public class VertexBufferDeclaration {
	protected ArrayList<VertexBufferDefinition> declaration = new ArrayList<VertexBufferDefinition>();

	public int add(VertexBufferDefinition vbd) {
		declaration.add(vbd);
		return vbd.mSize * vbd.getDataSize();
	}

	public int getCount() {
		return declaration.size();
	}

	public VertexBufferDefinition getDefinition(int index) {
		if (index >= 0 && index < declaration.size()) {
			return declaration.get(index);
		}
		return null;
	}
	
	public VertexBufferDefinition searchByUse(int use) {
		for (int i = 0; i < declaration.size(); i++) {
			VertexBufferDefinition vbd = declaration.get(i);
			if (vbd.mUsage == use)
				return vbd;
		}
		return null;
	}

}