package com.botijasoftware.utils

open class Sprite(var mRectangle: Rectanglef, texture: Texture, var scalex: Float, var scaley: Float, rotation: Float, color: ColorRGBAb, var mFlip: Boolean) {
    var mTexture: Texture
    var mScale = Vector2(1.0f, 1.0f)
    var mAnchor = Vector2(0.5f, 0.5f)
    var mRotation = 0.0f
    lateinit var mColor: ColorRGBAb
    var isVisible = true
        protected set


    init {

        mRotation = rotation
        mTexture = texture
        //rectangle = mRectangle
        setScale(scalex, scaley)
        setColor(color)

    }

    @JvmOverloads constructor(rectangle: Rectanglef, texture: Texture, scale: Float, rotation: Float = 0.0f, color: ColorRGBAb = ColorRGBAb(255.toByte(), 255.toByte(), 255.toByte(), 255.toByte()), flip: Boolean = false) : this(rectangle, texture, scale, scale, rotation, color, flip) {}

    constructor(rectangle: Rectanglef, texture: Texture) : this(rectangle, texture, 1.0f, 0.0f, ColorRGBAb(255.toByte(), 255.toByte(), 255.toByte(), 255.toByte()), false) {}

    constructor(rectangle: Rectanglef, texture: Texture, flip: Boolean) : this(rectangle, texture, 1.0f, 0.0f, ColorRGBAb(255.toByte(), 255.toByte(), 255.toByte(), 255.toByte()), flip) {}

    constructor(rectangle: Rectanglef, texture: Texture, color: ColorRGBAb) : this(rectangle, texture, 1.0f, 0.0f, color, false) {}

    constructor(rectangle: Rectanglef, texture: Texture, scale: Float, rotation: Float, flip: Boolean) : this(rectangle, texture, scale, rotation, ColorRGBAb(255.toByte(), 255.toByte(), 255.toByte(), 255.toByte()), flip) {}

    fun setTexture(texture: Texture, flip: Boolean) {

        mFlip = flip
        this.texture = texture
    }

    fun setPosition(x: Float, y: Float) {
        mRectangle.X = x
        mRectangle.Y = y
    }

    fun setPosition(v: Vector2) {
        mRectangle.X = v.X
        mRectangle.Y = v.Y
    }

    fun setCenter(v: Vector2) {
        mRectangle.center = v
    }

    fun setCenter(x: Float, y: Float) {
        mRectangle.setCenter(x, y)
    }

    open fun setSize(nw: Float, nh: Float) {
        mRectangle.width = nw
        mRectangle.height = nh

        rectangle = mRectangle
    }

    open fun setSize(v: Vector2) {
        mRectangle.width = v.X
        mRectangle.height = v.Y

        rectangle = mRectangle
    }

    fun setScale(scalex: Float, scaley: Float) {
        mScale.X = scalex
        mScale.Y = scaley
        rectangle = mRectangle
    }

    fun setScale(scale: Vector2) {
        mScale.X = scale.X
        mScale.Y = scale.Y
        rectangle = mRectangle
    }

    fun setRotation(rotation: Float) {
        mRotation = rotation
    }

    fun setColor(color: ColorRGBAb) {

        mColor = color

    }

    fun setAlpha(alpha: Float) {
        mColor.A = (255 * alpha).toByte()
        setColor(mColor)
    }

    fun setAlpha(alpha: Byte) {
        mColor.A = alpha
        setColor(mColor)
    }


    open var rectangle: Rectanglef
        get() = mRectangle
        set(rectangle) {

            mRectangle.X = rectangle.X
            mRectangle.Y = rectangle.Y
            mRectangle.width = rectangle.width * mScale.X
            mRectangle.height = rectangle.height * mScale.Y

        }

    fun show() {
        isVisible = true
    }

    fun hide() {
        isVisible = false
    }

    val floatAlpha: Float
        get() = mColor.A / 255.0f

    val byteAlpha: Byte
        get() = mColor.A

    open var texture: Texture
        get() = mTexture
        set(texture) {
            mTexture = texture

            val coords = mTexture.textureCoords

            if (mFlip) {
                coords.flipVertical()
            }

        }


    fun move(x: Float, y: Float) {
        mRectangle.X += x
        mRectangle.Y += y
    }


    fun scale(x: Float, y: Float) {
        mScale.X *= x
        mScale.Y *= y
    }

    fun setAnchor(x: Float, y: Float) {
        mAnchor.X = x
        mAnchor.Y = y
    }

    var anchor: Vector2
        get() = mAnchor
        set(anchor) {
            mAnchor.X = anchor.X
            mAnchor.Y = anchor.Y
        }

}
