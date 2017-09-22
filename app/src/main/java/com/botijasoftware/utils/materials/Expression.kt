package com.botijasoftware.utils.materials

interface Expression {
    fun evaluate(material: Material): Float
}