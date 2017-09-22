package com.botijasoftware.utils

import android.media.AudioManager
import android.media.SoundPool

class SoundSystem(private val mAudioManager: AudioManager, private val mSoundPool: SoundPool) {

    var isMuted: Boolean = false
        private set


    init {
        isMuted = false
    }

    fun Play(sound: Sound) {

        if (isMuted)
            return

        var loop = 0
        if (sound.looping)
            loop = -1

        val streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val streamID = mSoundPool.play(sound.id, streamVolume.toFloat(), streamVolume.toFloat(), 255, loop, 1f)
        sound.streamID = streamID
    }

    fun volumeDown() {
        mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 0)
    }

    fun volumeUp() {
        mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 0)
    }

    fun Stop(sound: Sound) {
        mSoundPool.stop(sound.streamID)
    }

    fun Pause(sound: Sound) {
        mSoundPool.pause(sound.streamID)
    }

    fun Resume(sound: Sound) {

        if (!isMuted)
            mSoundPool.resume(sound.streamID)
    }

    fun Mute(mute: Boolean) {
        isMuted = mute
    }
}
