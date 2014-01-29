package com.desparddesign.orienteering.codechecking.sportident;

@Deprecated
public class SI5Time
{
	public boolean noTime;
	public int hours;
	public int minutes;
	public int seconds;
	
	public String toString()
	{
		if(noTime)
			return "none";
		
		return hours + ":" + minutes + ":" + seconds;
	}
	
	public SI5Time()
	{
		noTime = true;
		hours = 0;
		minutes = 0;
		seconds = 0;
	}
	
	public SI5Time(int h, int m, int s)
	{
		noTime = false;
		hours = h;
		seconds = s;
		minutes = m;
	}
}