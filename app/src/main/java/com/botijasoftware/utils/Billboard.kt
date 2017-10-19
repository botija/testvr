package com.botijasoftware.utils

import com.botijasoftware.utils.renderer.Renderer

import android.opengl.GLES20

open class Billboard @JvmOverloads constructor(var mTexture: Texture, var position: Vector3, protected var size: Vector2, protected var flip: Boolean = false) : Renderable {
    var scale = Vector3(1f, 1f, 1f)
    var mVertexBuffer: VertexBuffer
    var mIndexBuffer: IndexBuffer
    var camdistance = 0.0f
    protected var rotation: Float = 0.0f
    protected var rotation_axis = Vector3(0f, 0f, 1f)
    var alpha = 1.0f
        set(alpha) {
            field = alpha
            if (this.alpha > 1.0f)
                field = 1.0f
            else if (this.alpha < 0.0f)
                field = 0.0f
        }

    init {
        val vbd = VertexBufferDeclaration()
        vbd.add(VertexBufferDefinition(VertexBufferDefinition.FLOAT, VertexBufferDefinition.DEF_VERTEX, 3, VertexBufferDefinition.ACCESS_DYNAMIC))
        vbd.add(VertexBufferDefinition(VertexBufferDefinition.FLOAT, VertexBufferDefinition.DEF_TEXTURE_COORD, 2, VertexBufferDefinition.ACCESS_DYNAMIC))

        mVertexBuffer = Renderer.vbManager.requestVB(vbd, 4)
        mIndexBuffer = Renderer.ibManager.requestIB(GLES20.GL_TRIANGLE_FAN, GLES20.GL_UNSIGNED_SHORT, 4)!!
        //mVertexBuffer = new VertexBuffer(4,vbd);
        //mIndexBuffer = new IndexBufferShort(GLES10.GL_TRIANGLE_FAN, 4);

        mIndexBuffer.put(indexdata)

        setTexture(mTexture)
        //setSize(this.size)
        //this.size.setValue(size)
    }

    override fun Update(time: Float) {
        // TODO Auto-generated method stub

    }

    override fun LoadContent(resources: ResourceManager) {
        // TODO Auto-generated method stub

    }

    override fun freeContent(resources: ResourceManager) {
        Renderer.vbManager.freeVB(mVertexBuffer)
        Renderer.ibManager.freeIB(mIndexBuffer)
    }

    override fun scale(x: Float, y: Float) {

    }

    override fun move(x: Float, y: Float) {

    }


    override fun Draw() {

        //GLES10.glPushMatrix();
        Renderer.pushModelViewMatrix()

        Transform()

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture.id)

        Renderer.loadModelViewMatrix()
        //GLES20.glColor4f(1.0f, 1.0f, 1.0f, alpha);
        mVertexBuffer.Draw(mIndexBuffer)
        //GLES20.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Renderer.popModelViewMatrix()
        Renderer.loadModelViewMatrix()
        //GLES10.glPopMatrix();

    }

    open fun Transform() {

        Renderer.modelview.translate(position.X, position.Y, position.Z)
        Renderer.modelview.rotate(rotation, rotation_axis.X, rotation_axis.Y, rotation_axis.Z)
        Renderer.modelview.scale(scale.X, scale.Y, scale.Z)
        //GLES10.glTranslatef(position.X, position.Y, position.Z);
        //GLES10.glRotatef(rotation, rotation_axis.X, rotation_axis.Y, rotation_axis.Z);
        //GLES10.glScalef(scale.X, scale.Y, scale.Z);

    }


    fun setTexture(texture: Texture) {
        mTexture = texture

        val coords = mTexture.textureCoords

        if (flip) {
            coords.flipVertical()
        }

        val stdata = floatArrayOf(coords.s0, coords.t0, coords.s1, coords.t0, coords.s0, coords.t1, coords.s1, coords.t1)
        mVertexBuffer.getBuffer(1)!!.put(stdata)
    }

    fun setTexture(texture: Texture, flip: Boolean) {

        this.flip = flip
        setTexture(texture)
    }

    fun setSize(x: Float, y: Float) {
        this.size.X = x
        this.size.Y = y
        val hw = x / 2.0f
        val hh = y / 2.0f
        val vertexdata = floatArrayOf(-hw, -hh, 0.0f, hw, -hh, 0.0f, -hw, hh, 0.0f, hw, hh, 0.0f)
        mVertexBuffer.getBuffer(0)!!.put(vertexdata)
    }

    /*fun setSize(size: Vector2) {

        setSize(size.X, size.Y)
    }

    fun setRotation(rotation: Float) {
        this.rotation = rotation
    }*/

    fun calcDistance(camera: Vector3): Float {
        camdistance = camera.distance(position)
        return camdistance
    }

    fun calcDistancesq(camera: Vector3): Float {
        camdistance = camera.distancesq(position)
        return camdistance
    }

    /*fun setRotationAxis(axis: Vector3) {
        rotation_axis = axis
    }

    fun setRotationAxis(axis_x: Float, axis_y: Float, axis_z: Float) {
        rotation_axis.setValue(axis_x, axis_y, axis_z)
    }*/

    companion object {
        protected var matrix = FloatArray(16)
        private val indexdata = shortArrayOf(0, 1, 3, 2)
    }

}