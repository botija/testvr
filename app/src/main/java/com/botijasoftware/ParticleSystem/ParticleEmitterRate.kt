package com.botijasoftware.ParticleSystem

open class EmitterRate {

    open val count: Int
        get() = 0

    protected var remaining = 0.0f

    open fun getCount(time: Float): Int {
        return 0
    }

    open fun clone(): EmitterRate {
        return EmitterRate()
    }
}

internal class EmitterRateConstant(private val emissionRate: Int) : EmitterRate() {

    init {
        remaining = 0.0f
    }

    override fun getCount(time: Float): Int {
        val count = emissionRate * time
        if (remaining > 1.0f)
            remaining -= 1.0f
        remaining += count - count.toInt()
        return (count + remaining).toInt()
    }

    override fun clone(): EmitterRateConstant {
        return EmitterRateConstant(emissionRate)
    }
}

internal class EmitterRateRandom(private val emissionRateMin: Int, private val emissionRateMax: Int) : EmitterRate() {

    init {
        remaining = 0.0f
    }

    override fun getCount(time: Float): Int {
        val count = ((Math.random() * (emissionRateMax - emissionRateMin) + emissionRateMin) * time).toFloat()
        if (remaining > 1.0f)
            remaining -= 1.0f
        remaining += count - count.toInt()
        return (count + remaining).toInt()
    }

    override fun clone(): EmitterRateRandom {
        return EmitterRateRandom(emissionRateMin, emissionRateMax)
    }
}

internal class EmitterRateOnDemand(private val emissionRateMin: Int, private val emissionRateMax: Int) : EmitterRate() {

    override val count: Int
        get() = (Math.random() * (emissionRateMax - emissionRateMin) + emissionRateMin).toInt()

    init {
        remaining = 0.0f
    }

    override fun clone(): EmitterRateOnDemand {
        return EmitterRateOnDemand(emissionRateMin, emissionRateMax)
    }
}

