package com.botijasoftware.utils.renderer

import java.util.ArrayList
import java.util.HashMap

import com.botijasoftware.utils.Camera
import com.botijasoftware.utils.materials.Material


class RenderQueue {
    internal var queue = HashMap<Material, ArrayList<RenderItem>>()

    fun sort(camera: Camera) {

    }

    fun add(item: RenderItem) {

        if (queue.containsKey(item.material)) {
            queue[item.material]!!.add(item)
        } else {
            val list = ArrayList<RenderItem>()
            list.add(item)
            queue.put(item.material, list)
        }

    }

    fun clear() {
        val set = queue.values

        for (list in set) {
            list.clear()
        }

        queue.clear()
    }

    fun size(): Int {
        return queue.size
    }

    val materials: ArrayList<Material>
        get() {
            val set = queue.keys
            val i = set.iterator()

            val list = ArrayList<Material>()

            while (i.hasNext()) {
                val m = i.next()
                list.add(m)
            }
            return list
        }

    fun getRenderItem(material: Material): ArrayList<RenderItem>? {
        if (queue.containsKey(material))
            return queue[material]
        else
            return ArrayList()
    }
}