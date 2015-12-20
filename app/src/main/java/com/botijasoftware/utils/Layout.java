package com.botijasoftware.utils;


public class Layout {
	
	private int w,h;
	private float wratio, hratio;
	
	public Layout(int width, int height) {
		setSize(width, height);
	}
	
	public void setSize(int width, int height) {
		w = width;
		h = height;
		wratio = w / 100.0f;
		hratio = h / 100.0f;
	}
	
	public int getHorizontal(int percent) {
		return (int) (percent * wratio);
	}
	
	public int getHorizontal(int percent, int minimum) {
		int weq = (int) (percent * wratio);
		if (weq < minimum)
			weq = minimum;
		return weq;
	}
	
	public int getHorizontal(int percent, int minimum, int maximum) {
		int weq = (int) (percent * wratio);
		if (weq < minimum)
			weq = minimum;
		if (weq > maximum)
			weq = maximum;
		return weq;
	}
	
	public float getHorizontal(float percent) {
		return percent * wratio;
	}
	
	public float getHorizontal(float percent, float minimum) {
		float weq = percent * wratio;
		if (weq < minimum)
			weq = minimum;
		return weq;
	}
	
	public float getHorizontal(float percent, float minimum, float maximum) {
		float weq = percent * wratio;
		if (weq < minimum)
			weq = minimum;
		if (weq > maximum)
			weq = maximum;
		return weq;
	}
	
	
	public int getVertical(int percent) {
		return (int) (percent * hratio);
	}
	
	public int getVertical(int percent, int minimum) {
		int heq = (int) (percent * hratio);
		if (heq < minimum)
			heq = minimum;
		return heq;
	}
	
	public int getVertical(int percent, int minimum, int maximum) {
		int heq = (int) (percent * hratio);
		if (heq < minimum)
			heq = minimum;
		if (heq > maximum)
			heq = maximum;
		return heq;
	}
	
	public float getVertical(float percent) {
		return percent * hratio;
	}
	
	public float getVertical(float percent, float minimum) {
		float heq = percent * hratio;
		if (heq < minimum)
			heq = minimum;
		return heq;
	}
	
	public float getVertical(float percent, float minimum, float maximum) {
		float heq = percent * hratio;
		if (heq < minimum)
			heq = minimum;
		if (heq > maximum)
			heq = maximum;
		return heq;
	}
}
