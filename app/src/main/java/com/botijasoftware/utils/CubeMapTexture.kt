package com.botijasoftware.utils

class CubeMapTexture : Texture {

    constructor(id: Int, w: Int, h: Int) : super(id, w, h) {}

    constructor(id: Int, w: Int, h: Int, tc: TextureCoords) : super(id, w, h, tc) {}

}
