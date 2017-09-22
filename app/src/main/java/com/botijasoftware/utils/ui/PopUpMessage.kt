package com.botijasoftware.utils.ui

import com.botijasoftware.utils.Rectanglef
import com.botijasoftware.utils.Texture


class PopUpMessage(texture: Texture, rect: Rectanglef, time: Float) : Message(texture, rect, time) {

    private val sizeincx: Float
    private val sizeincy: Float
    private val growRectangle: Rectanglef
    private var quadChanged = true

    init {
        sizeincx = rect.width / inTime
        sizeincy = rect.height / inTime
        growRectangle = Rectanglef(0f, 0f, 0f, 0f)
        growRectangle.center = mRectanglef.center
    }

    override fun Update(time: Float) {

        super.Update(time)

        if (state == Message.STATE_IN) {
            growRectangle.setSize((elapsedTime * sizeincx).toInt().toFloat(), (elapsedTime * sizeincy).toInt().toFloat())
            growRectangle.center = mRectanglef.center
            sprite.rectangle = growRectangle
            quadChanged = true
        } else if (state == Message.STATE_SHOW) {
            if (quadChanged) {
                sprite.rectangle = mRectanglef
                quadChanged = false
            }
        } else if (state == Message.STATE_OUT) {
            growRectangle.setSize(((outTime - elapsedTime) * sizeincx).toInt().toFloat(), ((outTime - elapsedTime) * sizeincy).toInt().toFloat())
            growRectangle.center = mRectanglef.center
            sprite.rectangle = growRectangle
            quadChanged = true
        }
    }

}