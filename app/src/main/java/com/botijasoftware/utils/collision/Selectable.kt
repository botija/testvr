package com.botijasoftware.utils.collision

interface Selectable {
    fun select(r: Ray): Boolean
}