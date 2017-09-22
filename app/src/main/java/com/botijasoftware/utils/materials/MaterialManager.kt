package com.botijasoftware.utils.materials

import java.util.HashMap

class MaterialManager {
    private val materials = HashMap<String, Material>()

    init {
        Material.time = 0.0f
    }

    fun registerMaterial(name: String, material: Material) {
        if (!materials.containsKey(name))
            materials.put(name, material)
    }

    fun getMaterial(name: String): Material? {
        if (materials.containsKey(name))
            return materials[name]
        else
            return null
    }

    fun Update(time: Float) {
        Material.time += time
    }

}