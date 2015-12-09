package com.botijasoftware.utils;

public class Sound {

	public Sound(int id, boolean loop)
	{
		mID = id;
		mLoop = loop;
	}
	
	public int getID(){
		return mID;
	}


	public boolean getLooping() {
		return mLoop;
	}
	
	public void setStreamID(int streamID) {
		mStreamID = streamID;
	}
	
	public int getStreamID() {
		return mStreamID;
	}
	
	private int mID;
	private int mStreamID;
	private boolean mLoop;
}
