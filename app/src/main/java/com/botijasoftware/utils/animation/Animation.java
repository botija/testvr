package com.botijasoftware.utils.animation;

import java.util.ArrayList;

public class Animation {

	public String name;
	public float lenght;

	public ArrayList<Track> tracks = new ArrayList<Track>();


	public Animation(String name, float lenght) {
		this.name = name;
		this.lenght = lenght;
	}

	public void addTrack(Track track) {
		tracks.add(track);
	}

}
