package com.botijasoftware.utils.renderer;

import com.botijasoftware.utils.GLMatrix;
import com.botijasoftware.utils.IndexBuffer;
import com.botijasoftware.utils.VertexBuffer;
import com.botijasoftware.utils.materials.Material;

public class RenderItem {
	public VertexBuffer vb;
	public Material material;
	public IndexBuffer ib;
	public GLMatrix mvmatrix = new GLMatrix();
	
	public RenderItem(VertexBuffer vb, IndexBuffer ib, GLMatrix mvmatrix, Material material) {
		this.vb = vb;
		this.ib = ib;
		this.mvmatrix.restore(mvmatrix);
		this.material = material;
	}
}