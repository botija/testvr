package com.botijasoftware.utils

import android.opengl.GLES20
import com.botijasoftware.utils.renderer.Renderer

class SpriteBatch @JvmOverloads constructor(nsprites: Int = MAXSPRITES) : Renderable {

    protected var mVertexBuffer: VertexBuffer
    protected var mIndexBuffer: IndexBuffer
    protected lateinit var mTexture: Texture
    protected var mVisible = true
    private var count = 0
    private var maxsprites = 0
    var maxspritecount = 0
    var drawcalls = 0
    var usedsprites = 0
    lateinit var tmpSprite: Sprite

    init {

        maxsprites = nsprites
        val vbd = VertexBufferDeclaration()
        vbd.add(VertexBufferDefinition(VertexBufferDefinition.FLOAT, VertexBufferDefinition.DEF_VERTEX, 3, VertexBufferDefinition.ACCESS_DYNAMIC))
        vbd.add(VertexBufferDefinition(VertexBufferDefinition.FLOAT, VertexBufferDefinition.DEF_TEXTURE_COORD, 2, VertexBufferDefinition.ACCESS_DYNAMIC))
        vbd.add(VertexBufferDefinition(VertexBufferDefinition.BYTE, VertexBufferDefinition.DEF_COLOR, 4, VertexBufferDefinition.ACCESS_DYNAMIC))

        mVertexBuffer = Renderer.vbManager.requestVB(vbd, 4 * nsprites)
        mIndexBuffer = Renderer.ibManager.requestIB(GLES20.GL_TRIANGLES, GLES20.GL_UNSIGNED_SHORT, 6 * nsprites)!!


        var v = 0
        var i = 0
        while (v < nsprites * 4) {
            mIndexBuffer.put(i, v)
            mIndexBuffer.put(i + 1, v + 1)
            mIndexBuffer.put(i + 2, v + 2)
            mIndexBuffer.put(i + 3, v + 1)
            mIndexBuffer.put(i + 4, v + 2)
            mIndexBuffer.put(i + 5, v + 3)
            v += 4
            i += 6
        }

    }

    fun DrawSprite(sprite: Sprite) {
        val t = sprite.mTexture

        if (mTexture == null) {
            mTexture = t
        }

        //same texture --> to vb
        if (mTexture.id == t.id) {
            putSpriteOnVB(sprite)
            if (count >= maxsprites) {
                drawVB()
                count = 0
            }
        } else { //diferent texture --> draw vb, and push new sprite to empty vb
            drawVB()
            count = 0
            mTexture = t
            putSpriteOnVB(sprite)
        }
    }

    fun DrawSprite(sprite: Sprite, color: ColorRGBAb) {
        val t = sprite.mTexture

        if (mTexture == null) {
            mTexture = t
        }

        //same texture --> to vb
        if (mTexture.id == t.id) {
            putSpriteOnVB(sprite, color)
            if (count >= maxsprites) {
                drawVB()
                count = 0
            }
        } else { //diferent texture --> draw vb, and push new sprite to empty vb
            drawVB()
            count = 0
            mTexture = t
            putSpriteOnVB(sprite, color)
        }
    }

    private fun putSpriteOnVB(sprite: Sprite, color: ColorRGBAb) {
        putSpriteOnVB(sprite, sprite.mScale, sprite.mRotation, color, sprite.mFlip)
    }

    private fun putSpriteOnVB(sprite: Sprite, scale: Vector2 = sprite.mScale, rotation: Float = sprite.mRotation, color: ColorRGBAb = sprite.mColor, flip: Boolean = sprite.mFlip) {

        usedsprites++

        val rect = sprite.mRectangle

        val ohw = rect.width * sprite.mAnchor.X
        val ohh = rect.height * sprite.mAnchor.Y
        val x = rect.X + ohw
        val y = rect.Y + ohh

        val hw = ohw * scale.X
        val hh = ohh * scale.Y

        // |cos -sin| * [x y]  --> xr = x * cos - y * sin
        // |sin  cos|              yr = x * sin + y * cos


        val sin = Math.sin(rotation.toDouble()).toFloat()
        val cos = Math.cos(rotation.toDouble()).toFloat()

        val x0 = -hw
        val x1 = +hw
        val y0 = -hh
        val y1 = +hh

        val data = floatArrayOf(x + x0 * cos - y0 * sin, y + x0 * sin + y0 * cos, 0.0f, x + x1 * cos - y0 * sin, y + x1 * sin + y0 * cos, 0.0f, x + x0 * cos - y1 * sin, y + x0 * sin + y1 * cos, 0.0f, x + x1 * cos - y1 * sin, y + x1 * sin + y1 * cos, 0.0f)

        //float data [] = {x0, y0, 0.0f , x1, y0, 0.0f, x0, y1, 0.0f, x1, y1, 0.0f};


        var vb: ArrayBuffer = mVertexBuffer.getBuffer(PVERTEX)!!
        vb.position(count * 12)
        vb.put(data)
        vb.position(0)

        val coords = sprite.mTexture.textureCoords

        if (flip) {
            coords.flipVertical()
        }

        val st = floatArrayOf(coords.s0, coords.t0, coords.s1, coords.t0, coords.s0, coords.t1, coords.s1, coords.t1)
        vb = mVertexBuffer.getBuffer(PTEXTCOORD)!!
        vb.position(count * 8)
        vb.put(st)
        vb.position(0)

        val r = color.R
        val g = color.G
        val b = color.B
        val a = color.A

        val cdata = byteArrayOf(r, g, b, a, r, g, b, a, r, g, b, a, r, g, b, a)

        //mVertexBuffer.getBuffer(2).put(cdata);
        vb = mVertexBuffer.getBuffer(PCOLOR)!!
        vb.position(count * 16)
        vb.put(cdata)
        vb.position(0)

        count++
    }

    private fun drawVB() {
        //GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture.getID() );
        //Renderer.BindTexture(Renderer.TEXTURE0, mTexture.getID());
        Renderer.BindTexture(Renderer.TEXTURE0, mTexture.id)

        Renderer.pushModelViewMatrix()
        Renderer.modelview.loadIdentity()

        Renderer.loadModelViewMatrix()
        mIndexBuffer.size = count * 6 // two triangles per sprite
        mVertexBuffer.Draw(mIndexBuffer)

        Renderer.popModelViewMatrix()
        //Renderer.modelview.loadMatrix();

        drawcalls++
        if (count > maxspritecount)
            maxspritecount = count

    }

    override fun Update(time: Float) {

    }

    override fun LoadContent(resources: ResourceManager) {

    }

    override fun Draw() {

        if (count > 0) {
            drawVB()

        }

    }

    fun end() {

        if (count > 0) {
            drawVB()
            count = 0
            //mTexture = null
        }
        //disable sprite shader
        //clean vb
    }

    fun begin() {
        count = 0
        //mTexture = null

        maxspritecount = 0
        drawcalls = 0
        usedsprites = 0
        //enable sprite shader
        //prepare vb
    }


    fun flush() {
        if (count > 0) {
            drawVB()
            count = 0
            //mTexture = null
        }
    }

    override fun freeContent(resources: ResourceManager) {
        Renderer.vbManager.freeVB(mVertexBuffer)
        Renderer.ibManager.freeIB(mIndexBuffer)
    }

    override fun move(x: Float, y: Float) {}

    override fun scale(x: Float, y: Float) {}

    @JvmOverloads fun DrawText(font: Font, text: String, x: Float, y: Float, color: ColorRGBAb? = null) {
        val fontsize = font.fontSize.toFloat()
        val fontsizesep = fontsize * 3 / 7

        if (tmpSprite != null) {
            tmpSprite.rectangle = Rectanglef(0f, 0f, fontsize, fontsize)
            tmpSprite.texture = font.texture
        } else {

            tmpSprite = Sprite(Rectanglef(0f, 0f, fontsize, fontsize), font.texture)
        }

        val textLen = text.length

        if (color != null)
            tmpSprite.setColor(color)
        else
            tmpSprite.setColor(ColorRGBAb.WHITE)

        var ascii: Int

        for (i in 0..textLen - 1) {

            ascii = text.codePointAt(i)
            tmpSprite.texture = font.getTexture(ascii)
            tmpSprite.rectangle.setPosition(x + i * fontsizesep, y)
            DrawSprite(tmpSprite)

        }
    }


    fun getTextSize(font: Font, text: String): Vector2 {
        val fontsize = font.fontSize.toFloat()
        val fontsizesep = fontsize * 3 / 7
        return Vector2(fontsizesep * text.length, fontsize)
    }

    companion object {
        private val MAXSPRITES = 64
        private val PVERTEX = 0
        private val PTEXTCOORD = 1
        private val PCOLOR = 2
    }

}