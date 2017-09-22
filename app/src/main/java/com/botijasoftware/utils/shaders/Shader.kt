package com.botijasoftware.utils.shaders

import com.botijasoftware.utils.ResourceManager
import com.botijasoftware.utils.ShaderProgram

open class Shader {
    protected lateinit var program: ShaderProgram
    protected lateinit var vsSource: String
    protected lateinit var fsSource: String
    protected var programid: Int = 0

    fun LoadContent(resources: ResourceManager) {

    }

    open fun enable() {

    }

    fun reload(resources: ResourceManager) {
        LoadContent(resources)
    }


}
