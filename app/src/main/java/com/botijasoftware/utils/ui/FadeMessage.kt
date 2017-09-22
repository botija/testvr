package com.botijasoftware.utils.ui

import com.botijasoftware.utils.Rectanglef
import com.botijasoftware.utils.Texture


class FadeMessage(texture: Texture, rect: Rectanglef, time: Float) : Message(texture, rect, time) {

    override fun Update(time: Float) {
        super.Update(time)

        when (state) {
            Message.STATE_IN -> alpha = elapsedTime / inTime
            Message.STATE_SHOW -> alpha = 1.0f
            Message.STATE_OUT -> alpha = 1.0f - elapsedTime / outTime
        }
    }

}
