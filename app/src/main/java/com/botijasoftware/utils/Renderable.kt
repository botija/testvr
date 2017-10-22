package com.botijasoftware.utils

interface Renderable {


    fun Update(time: Float)
    fun LoadContent(resources: ResourceManager)
    fun Draw()
    fun freeContent(resources: ResourceManager)

}