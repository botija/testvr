package com.botijasoftware.utils.materials;

import java.util.HashMap;

public class MaterialManager {
	private HashMap<String, Material> materials = new HashMap<String, Material>();
	
	public MaterialManager() {
		Material.time = 0.0f;
	}
	
	public void registerMaterial(String name, Material material) {
		if (!materials.containsKey(name))
			materials.put(name, material);
	} 
	
	public Material getMaterial(String name) {
		if (materials.containsKey(name))
			return materials.get(name);
		else
			return null;
	}
	
	public void Update(float time) {
		Material.time += time;
	}
	
}