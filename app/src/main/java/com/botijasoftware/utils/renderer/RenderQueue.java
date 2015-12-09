package com.botijasoftware.utils.renderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.botijasoftware.utils.Camera;
import com.botijasoftware.utils.materials.Material;


public class RenderQueue {
	HashMap< Material, ArrayList<RenderItem>> queue = new HashMap< Material, ArrayList<RenderItem>> ();
	
	public void sort(Camera camera) {
	
	}
	
	public void add(RenderItem item) {
		
		if (queue.containsKey(item.material)) {
			queue.get(item.material).add(item);
		}
		else {
			ArrayList<RenderItem> list = new ArrayList<RenderItem>();
			list.add(item);
			queue.put(item.material, list);
		}
		
	}
	
	public void clear() {
		Collection<ArrayList<RenderItem>> set = queue.values();

		for (ArrayList<RenderItem> list : set) {
			list.clear();
		}
	    
	    queue.clear();
	}
	
	public int size() {
		return queue.size();
	}
	
	public ArrayList<Material> getMaterials() {
		Collection<Material> set = queue.keySet();
	    Iterator<Material> i = set.iterator();
	    
	    ArrayList<Material> list = new ArrayList<Material>();
	    		
	    while(i.hasNext()){
	    	Material m = i.next();
	    	list.add(m);
	    }
	    return list;
	}
	
	public ArrayList<RenderItem> getRenderItem(Material material) {
		if (queue.containsKey(material))
			return queue.get(material);
		else
			return new ArrayList<RenderItem>();
	}
}