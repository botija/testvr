package com.botijasoftware.utils;

import android.media.AudioManager;
import android.media.SoundPool;

public class SoundSystem {

	public SoundSystem(AudioManager audioManager, SoundPool soundPool) {
		mAudioManager = audioManager;
		mSoundPool = soundPool;
		mMuted = false;
	}
	
	public void Play(Sound sound) {
		
		if (mMuted)
			return;
		
		int loop = 0;
		if (sound.getLooping())
			loop = -1;
			
		int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); 
	    int streamID = mSoundPool.play(sound.getID(), streamVolume, streamVolume, 255, loop, 1f);
		sound.setStreamID(streamID);
	}
	
	public void volumeDown() {
		mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 0);
	}
	
	public void volumeUp() {
		mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 0);
	}
	
	public void Stop(Sound sound) {
		mSoundPool.stop(sound.getStreamID());
	}
	
	public void Pause(Sound sound) {
		mSoundPool.pause(sound.getStreamID());
	}
	
	public void Resume(Sound sound) {
		
		if (!mMuted)
			mSoundPool.resume(sound.getStreamID());
	}
	
	public boolean isMuted() {
		return mMuted;
	}
	
	public void Mute(boolean mute) {
		mMuted = mute;
	}
	
	private AudioManager mAudioManager;
	private SoundPool mSoundPool;
	private boolean mMuted;
}
