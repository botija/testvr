package com.botijasoftware.utils.materials

interface PassCondition {

    fun check(material: Material): Boolean
}