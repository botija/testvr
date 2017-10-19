package com.botijasoftware.utils

import com.botijasoftware.utils.renderer.Renderer

class BillboardFast : Billboard {

    constructor(texture: Texture, position: Vector3, size: Vector2) : super(texture, position, size, false)

    constructor(texture: Texture, position: Vector3, size: Vector2, flip: Boolean) : super(texture, position, size, flip)

    override fun Transform() {

        //GLES10.glTranslatef(position.X, position.Y, position.Z);
        Renderer.modelview.translate(position.X, position.Y, position.Z)
    }

}