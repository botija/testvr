package com.botijasoftware.utils

class TexturedQuad : Sprite {

    constructor(position: Vector2, texture: Texture) : super(Rectanglef(position.X, position.Y, texture.width.toFloat(), texture.height.toFloat()), texture)


    constructor(position: Vector2, texture: Texture, flip: Boolean) : super(Rectanglef(position.X, position.Y, texture.width.toFloat(), texture.height.toFloat()), texture, 1.0f, 0.0f, ColorRGBAb(255.toByte(), 255.toByte(), 255.toByte(), 255.toByte()), flip)

    constructor(position: Vector2, texture: Texture, color: ColorRGBAb) : super(Rectanglef(position.X, position.Y, texture.width.toFloat(), texture.height.toFloat()), texture, 1.0f, 0.0f, color, false)

    constructor(position: Vector2, texture: Texture, scale: Float) : super(Rectanglef(position.X, position.Y, texture.width.toFloat(), texture.height.toFloat()), texture, scale, 0.0f, ColorRGBAb(255.toByte(), 255.toByte(), 255.toByte(), 255.toByte()), false)

    constructor(position: Vector2, texture: Texture, scale: Float, rotation: Float) : super(Rectanglef(position.X, position.Y, texture.width.toFloat(), texture.height.toFloat()), texture, scale, rotation, ColorRGBAb(255.toByte(), 255.toByte(), 255.toByte(), 255.toByte()), false)

    constructor(position: Vector2, texture: Texture, scale: Float, rotation: Float, flip: Boolean) : super(Rectanglef(position.X, position.Y, texture.width.toFloat(), texture.height.toFloat()), texture, scale, rotation, ColorRGBAb(255.toByte(), 255.toByte(), 255.toByte(), 255.toByte()), flip)

    override var texture: Texture
        get() = super.texture
        set(texture) {
            mRectangle.setSize(texture.width.toFloat(), texture.height.toFloat())
            super.texture = texture
        }


    override var rectangle: Rectanglef
        get() = super.rectangle
        set(rectangle) {

            mRectangle.setPosition(rectangle.X, rectangle.Y)
            super.rectangle = mRectangle

        }

    override fun setSize(nw: Float, nh: Float) {}

    override fun setSize(v: Vector2) {

    }

}