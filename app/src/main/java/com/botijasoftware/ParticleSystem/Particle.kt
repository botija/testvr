package com.botijasoftware.ParticleSystem

import com.botijasoftware.utils.ColorRGBA
import com.botijasoftware.utils.Vector2
import com.botijasoftware.utils.Vector3

class Particle {
    var position: Vector3
    var size: Vector2
    var sizeIncrease: Vector2
    var speed: Vector3
    var force: Vector3
    var rotation: Vector3
    var rotationIncrease: Vector3
    var color: ColorRGBA
    var fadeRate: ColorRGBA
    var ttl: Float = 0.toFloat()
    var elapsedTime: Float = 0.toFloat()
    var alive: Boolean = false
    var animation: ParticleAnimation
    var trailEmitter: ParticleEmitter? = null
    var trailRate: EmitterRate? = null
    var onDeadEmitter: ParticleEmitter? = null

    init {
        position = Vector3(0f, 0f, 0f)
        size = Vector2(0f, 0f)
        sizeIncrease = Vector2(0f, 0f)
        speed = Vector3(0f, 0f, 0f)
        force = Vector3(0f, 0f, 0f)
        color = ColorRGBA(1f, 1f, 1f, 1f)
        fadeRate = ColorRGBA(0f, 0f, 0f, 0f)
        rotation = Vector3(0f, 0f, 0f)
        rotationIncrease = Vector3(0f, 0f, 0f)
        ttl = 0.0f//Float.MAX_VALUE;
        elapsedTime = 0.0f
        alive = false
        animation = ParticleAnimation()
        trailEmitter = null
        trailRate = null
        onDeadEmitter = null
    }


    fun Update(time: Float) {

        if (trailEmitter != null)
            trailEmitter!!.emmit(this, time)

        elapsedTime += time
        speed.X += force.X * time
        speed.Y += force.Y * time
        speed.Z += force.Z * time
        position.X += speed.X * time
        position.Y += speed.Y * time
        position.Z += speed.Z * time
        size.X += sizeIncrease.X * time
        size.Y += sizeIncrease.Y * time
        color.R -= fadeRate.R * time
        color.G -= fadeRate.G * time
        color.B -= fadeRate.B * time
        color.A -= fadeRate.A * time
        rotation.X += rotationIncrease.X * time
        rotation.Y += rotationIncrease.Y * time
        rotation.Z += rotationIncrease.Z * time
        animation.Update(time)
        alive = elapsedTime < ttl
        if (!alive && onDeadEmitter != null) {
            onDeadEmitter!!.emmitOnDead(this) //amount should be read from xml
        }

    }

}