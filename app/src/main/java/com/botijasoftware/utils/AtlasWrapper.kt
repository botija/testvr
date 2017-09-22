package com.botijasoftware.utils

import java.util.ArrayList


//simple class that allows the use of a texture as a 1x1 atlas
class AtlasWrapper(texture: Texture) : Atlas(texture) {
    init {
        textureCount = 1
        mCoords = Array<TextureCoords>(1, {texture.textureCoords})
        //mCoords[0] = texture.textureCoords
    }
}