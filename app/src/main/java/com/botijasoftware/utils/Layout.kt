package com.botijasoftware.utils


class Layout(width: Int, height: Int) {

    private var w: Int = 0
    private var h: Int = 0
    private var wratio: Float = 0.toFloat()
    private var hratio: Float = 0.toFloat()

    init {
        setSize(width, height)
    }

    fun setSize(width: Int, height: Int) {
        w = width
        h = height
        wratio = w / 100.0f
        hratio = h / 100.0f
    }

    fun getHorizontal(percent: Int): Int {
        return (percent * wratio).toInt()
    }

    fun getHorizontal(percent: Int, minimum: Int): Int {
        var weq = (percent * wratio).toInt()
        if (weq < minimum)
            weq = minimum
        return weq
    }

    fun getHorizontal(percent: Int, minimum: Int, maximum: Int): Int {
        var weq = (percent * wratio).toInt()
        if (weq < minimum)
            weq = minimum
        if (weq > maximum)
            weq = maximum
        return weq
    }

    fun getHorizontal(percent: Float): Float {
        return percent * wratio
    }

    fun getHorizontal(percent: Float, minimum: Float): Float {
        var weq = percent * wratio
        if (weq < minimum)
            weq = minimum
        return weq
    }

    fun getHorizontal(percent: Float, minimum: Float, maximum: Float): Float {
        var weq = percent * wratio
        if (weq < minimum)
            weq = minimum
        if (weq > maximum)
            weq = maximum
        return weq
    }


    fun getVertical(percent: Int): Int {
        return (percent * hratio).toInt()
    }

    fun getVertical(percent: Int, minimum: Int): Int {
        var heq = (percent * hratio).toInt()
        if (heq < minimum)
            heq = minimum
        return heq
    }

    fun getVertical(percent: Int, minimum: Int, maximum: Int): Int {
        var heq = (percent * hratio).toInt()
        if (heq < minimum)
            heq = minimum
        if (heq > maximum)
            heq = maximum
        return heq
    }

    fun getVertical(percent: Float): Float {
        return percent * hratio
    }

    fun getVertical(percent: Float, minimum: Float): Float {
        var heq = percent * hratio
        if (heq < minimum)
            heq = minimum
        return heq
    }

    fun getVertical(percent: Float, minimum: Float, maximum: Float): Float {
        var heq = percent * hratio
        if (heq < minimum)
            heq = minimum
        if (heq > maximum)
            heq = maximum
        return heq
    }
}
