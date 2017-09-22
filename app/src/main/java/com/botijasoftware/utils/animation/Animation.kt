package com.botijasoftware.utils.animation

import java.util.ArrayList

class Animation(var name: String, var lenght: Float) {

    var tracks = ArrayList<Track>()

    fun addTrack(track: Track) {
        tracks.add(track)
    }

}
