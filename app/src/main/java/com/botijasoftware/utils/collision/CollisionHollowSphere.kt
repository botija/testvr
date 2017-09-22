package com.botijasoftware.utils.collision

import com.botijasoftware.utils.Vector3

class CollisionHollowSphere(center: Vector3, outradius: Float, inradius: Float, private val holeangle: Float) : CollisionVolume() {
    private val outsphere: Sphere
    private val insphere: Sphere
    private val halfholeangle: Float
    private var angle: Float = 0.toFloat()


    init {
        outsphere = Sphere(center, outradius)
        insphere = Sphere(center, inradius)
        this.halfholeangle = this.holeangle / 2
    }

    private fun inHole(v: Vector3): Boolean {
        val a = Math.atan2((-(v.Y - outsphere.Y)).toDouble(), (v.X - outsphere.X).toDouble()).toFloat()
        return a < angle + halfholeangle && a > angle - halfholeangle
    }

    override fun collides(c: CollisionVolume): Boolean {
        return c.collides(outsphere) && !c.collides(insphere) && !inHole(c.position)
    }

    override fun collides(s: Sphere): Boolean {
        return outsphere.collides(s) && !insphere.collides(s) && !inHole(s.center)
    }

    override fun collides(v: Vector3): Boolean {
        return outsphere.collides(v) && !insphere.collides(v) && !inHole(v)
    }

    override var position: Vector3
        get() = insphere.center
        set(v) {
            outsphere.center = v
            insphere.center = v
        }

    var outRadius: Float
        get() = outsphere.radius
        set(r) { outsphere.radius = r }

    var inRadius: Float
        get() = insphere.radius
        set(r) { insphere.radius = r }

    fun rotate(angle: Float) {
        this.angle = angle
    }

}