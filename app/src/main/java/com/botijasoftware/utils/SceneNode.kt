package com.botijasoftware.utils


import java.util.ArrayList


class SceneNode(private val name: String, val model: Model?) : Renderable {

    val children = ArrayList<SceneNode>()
    private var parent: SceneNode? = null
    val transform = Transform()

    override fun Update(time: Float) {

    }

    override fun LoadContent(resources: ResourceManager) {

    }

    override fun Draw() {

    }

    override fun freeContent(resources: ResourceManager) {
        for (i in children.indices) {
            children[i].freeContent(resources)
        }
    }

    fun setScale(x: Float, y: Float, z: Float) {
        transform.scale.setValue(x, y, z)
    }

    fun scale(x: Float, y: Float, z: Float) {
        transform.scale.X *= x
        transform.scale.Y *= y
        transform.scale.Z *= z
    }

    fun setPosition(x: Float, y: Float, z: Float) {
        transform.translation.setValue(x, y, z)
    }

    fun move(x: Float, y: Float, z: Float) {

        transform.translation.X += x
        transform.translation.Y += y
        transform.translation.Z += z

    }

    fun setRotation(x: Float, y: Float, z: Float) {
        transform.rotation.identity()
        transform.rotation.rotate(Vector3(x, y, z))
    }

    fun setRotation(x: Float, y: Float, z: Float, w: Float) {
        transform.rotation.set(x, y, z, w)
    }

    fun rotateX(angle: Float) {
        transform.rotation.rotateX(Math.toRadians(angle.toDouble()).toFloat())
    }

    fun rotateY(angle: Float) {
        transform.rotation.rotateY(Math.toRadians(angle.toDouble()).toFloat())
    }

    fun rotateZ(angle: Float) {
        transform.rotation.rotateZ(Math.toRadians(angle.toDouble()).toFloat())
    }

    fun addNode(node: SceneNode) {
        node.parent = this
        children.add(node)
    }

    fun findNode(searchname: String): SceneNode? {
        if (name == searchname) {
            return this
        }
        for (i in children.indices) {
            val n = children[i]
            if (n.findNode(searchname) != null) {
                return n
            }
        }

        return null
    }
}