package com.botijasoftware.ParticleSystem

import com.botijasoftware.utils.Vector2
import com.botijasoftware.utils.Vector3

open class ParticleEmitterShape {

    open fun get(): Vector3 {
        return Vector3.ZERO
    }

    open fun setPosition(position: Vector3) {}
}

internal class EmitterShapePoint(private var p: Vector3?) : ParticleEmitterShape() {

    override fun get(): Vector3 {
        return p!!.clone()
    }

    override fun setPosition(position: Vector3) {
        p = position
    }
}

internal class EmitterShapeSphere(private var c: Vector3?, private val r: Float) : ParticleEmitterShape() {

    override fun get(): Vector3 {
        val x = c!!.X + (Math.random() * 2.0 * r.toDouble() - r).toFloat()
        val y = c!!.Y + (Math.random() * 2.0 * r.toDouble() - r).toFloat()
        val z = c!!.Z + (Math.random() * 2.0 * r.toDouble() - r).toFloat()
        return Vector3(x, y, z)
    }

    override fun setPosition(position: Vector3) {
        c = position
    }
}

internal class EmitterShapeCube(private var p: Vector3?, private val s: Vector3) : ParticleEmitterShape() {

    override fun get(): Vector3 {
        val x = p!!.X + (Math.random() * s.X).toFloat()
        val y = p!!.Y + (Math.random() * s.Y).toFloat()
        val z = p!!.Z + (Math.random() * s.Z).toFloat()
        return Vector3(x, y, z)
    }

    override fun setPosition(position: Vector3) {
        p = position
    }
}

internal class EmitterShapeLine(private var p0: Vector3?, private val p1: Vector3) : ParticleEmitterShape() {

    override fun get(): Vector3 {

        val x = p0!!.X + (Math.random() * (p1.X - p0!!.X)).toFloat()
        val y = p0!!.Y + (Math.random() * (p1.Y - p0!!.Y)).toFloat()
        val z = p0!!.Z + (Math.random() * (p1.Z - p0!!.Z)).toFloat()
        return Vector3(x, y, z)
    }

    override fun setPosition(position: Vector3) {
        p0 = position
        p1.sub(position)
    }
}

internal class EmitterShapeCircle(private var c: Vector2?, private val r: Float) : ParticleEmitterShape() {

    override fun get(): Vector3 {
        val x = c!!.X + (Math.random() * 2.0 * r.toDouble() - r).toFloat()
        val y = c!!.Y + (Math.random() * 2.0 * r.toDouble() - r).toFloat()
        val z = 0.0f
        return Vector3(x, y, z)
    }

    fun setPosition(position: Vector2) {
        c = position
    }
}

internal class EmitterShapeRectangle(private var p: Vector2?, private val s: Vector2) : ParticleEmitterShape() {

    override fun get(): Vector3 {
        val x = p!!.X + (Math.random() * s.X).toFloat()
        val y = p!!.Y + (Math.random() * s.Y).toFloat()
        val z = 0.0f
        return Vector3(x, y, z)
    }

    fun setPosition(position: Vector2) {
        p = position
    }
}
