package com.botijasoftware.utils.materials;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import com.botijasoftware.utils.IndexBuffer;
import com.botijasoftware.utils.Mesh;
import com.botijasoftware.utils.Model;
import com.botijasoftware.utils.VertexBuffer;
import com.botijasoftware.utils.renderer.RenderItem;

public class MaterialRenderer {
	
	static public void renderVertexBuffer(Material material, VertexBuffer vb, IndexBuffer ib) {
		if (material == null || !material.isLoaded())
			return;
		
		material.set();
		for (int i = 0; i < material.passCount(); i++) {
			MaterialPass pass = material.mPass.get(i);
			if (pass != null && pass.set()) {
				vb.Draw(ib);
				pass.unSet();
			}
		}
		material.unSet();
	}

	static public void renderModel(Model model) {

		int size = model.mMesh.size();
		for (int i = 0; i < size; i++) {
			Mesh m = model.mMesh.get(i);
			renderVertexBuffer(m.mMaterial, m.mVertexBuffer, m.mIndexBuffer);
			//Renderer.toRender(m.mVertexBuffer, m.mIndexBuffer, Renderer.modelview, m.mMaterial);
		}
	}
	
	static public void renderModel(Model model, Material material) {

		int size = model.mMesh.size();
		for (int i = 0; i < size; i++) {
			Mesh m = model.mMesh.get(i);
			renderVertexBuffer(material, m.mVertexBuffer, m.mIndexBuffer);
			//Renderer.toRender(m.mVertexBuffer, m.mIndexBuffer, Renderer.modelview, m.mMaterial);
		}
	}
	
	
	static public void renderList(Material material, ArrayList<RenderItem> list) {
		if (material == null || !material.isLoaded())
			return;
		
		material.set();
		int size = list.size();
		for (int i = 0; i < material.passCount(); i++) {
			MaterialPass pass = material.mPass.get(i);
			if (pass != null && pass.set()) {
				for (int j=0;j<size;j++) {
					RenderItem ri = list.get(j);
					ri.vb.Draw(ri.ib);
				}
				pass.unSet();
			}
		}
		material.unSet();
	}


}