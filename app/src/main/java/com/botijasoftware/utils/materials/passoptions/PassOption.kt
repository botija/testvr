package com.botijasoftware.utils.materials.passoptions

import com.botijasoftware.utils.materials.Material

abstract class PassOption {
    abstract fun set(material: Material)

    abstract fun unSet()
}