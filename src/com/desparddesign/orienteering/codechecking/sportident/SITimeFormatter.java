package com.desparddesign.orienteering.codechecking.sportident;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;


public class SITimeFormatter
{

	public SITimeFormatter()
	{
	}

	/**
	 * @deprecated Method formatTime is deprecated
	 */

	public static SI5Time formatTime(int time)
	{
		if(time > 43200)
		{
			return new SI5Time();
		} else
		{
			int hours = time / 3600;
			int minutes = (time - hours * 3600) / 60;
			int seconds = time - hours * 3600 - minutes * 60;
			return new SI5Time(hours, minutes, seconds);
		}
	}

	public static LocalTime format12HourTime(int TH, int TL)
	{
		int time = (TH & 0xff) << 8 | TL & 0xff;
		if(time > 43200)
		{
			return null;
		} else
		{
			int hours = time / 3600;
			int minutes = (time - hours * 3600) / 60;
			int seconds = time - hours * 3600 - minutes * 60;
			return new LocalTime(hours, minutes, seconds);
		}
	}

	public static LocalTime format24HourTime(int TD, int TH, int TL)
	{
		int time = (TH & 0xff) << 8 | TL & 0xff;
		if(time > 43200)
			return null;
		int hours = time / 3600;
		int minutes = (time - hours * 3600) / 60;
		int seconds = time - hours * 3600 - minutes * 60;
		if((TD & 0xff & 1) == 1)
			hours += 12;
		return new LocalTime(hours, minutes, seconds);
	}

	public static LocalTime format24HourTime(int TD, int TH, int TL, int TSS)
	{
		int time = (TH & 0xff) << 8 | TL & 0xff;
		if(time > 43200)
			return null;
		int hours = time / 3600;
		int minutes = (time - hours * 3600) / 60;
		int seconds = time - hours * 3600 - minutes * 60;
		if((TD & 0xff & 1) == 1)
			hours += 12;
		return new LocalTime(hours, minutes, seconds, (int)(((double)TSS * 1000D) / 256D));
	}

	public static int unFormat24HourTime(LocalTime time)
	{
		int TD = 0;
		int hours = time.getHourOfDay();
		if(hours >= 12)
		{
			hours-=12;
			TD = 1;
		}
		int minutes = time.getMinuteOfHour();
		int seconds = time.getSecondOfMinute();
		int millis = time.getMillisOfSecond();
		
		int TSS = (int)((double)millis * (256.0/1000.0));
		
		int t = hours*3600 + minutes*60 + seconds;
		
		return ((TD & 0xff) << 24) | ((t & 0xffff) << 8) | (TSS & 0xff);
	}
	
	public static LocalDateTime format24HourDateTime(int YY, int MM, int DD, int TD, int TH, int TL, int TSS)
	{
		int time = (TH & 0xff) << 8 | TL & 0xff;
		if(time > 43200)
			return null;
		int hours = time / 3600;
		int minutes = (time - hours * 3600) / 60;
		int seconds = time - hours * 3600 - minutes * 60;
		if((TD & 0xff & 1) == 1)
			hours += 12;
		return new LocalDateTime(2000 + YY, MM, DD, hours, minutes, seconds, (int)(((double)TSS * 1000D) / 256D));
	}
}
