package com.desparddesign.orienteering.timer.PTB605v13.commands.responses;

import org.joda.time.LocalDateTime;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;
import com.desparddesign.orienteering.timer.PTB605v13.commands.PTB605Command;

public class GetDateTimeResponse extends PTB605Command
{
	LocalDateTime dateTime;
	
	public GetDateTimeResponse(LocalDateTime dt)
	{
		dateTime = dt;
	}
	
	public GetDateTimeResponse(PTB605CommandPacket packet)
	{
		String data = new String(packet.data());
		String dt = data.substring(2);//v13 used Pd and PD
		String DD,MM,YY,hh,mm,ss;
		if(packet.data()[1] =='D')
		{
			//DDMMYYhhmmss
			DD = dt.substring(0,2);
			MM = dt.substring(2,4);
		}
		else
		{
			//MMDDYYhhmmss
			MM = dt.substring(0,2);
			DD = dt.substring(2,4);
		}
		YY = dt.substring(4,6);
		hh = dt.substring(6,8);
		mm = dt.substring(8,10);
		ss = dt.substring(10,12);
		try
		{
			dateTime = new LocalDateTime(2000+new Integer(YY),new Integer(MM),new Integer(DD),new Integer(hh),new Integer(mm),new Integer(ss));
		}
		catch(Exception ex)
		{
			dateTime = new LocalDateTime();
		}
	}
	
	public PTB605CommandPacket rawPacket()
	{
		PTB605CommandPacket packet = new PTB605CommandPacket();
		packet.encapsulate = false;
		
		packet.addByte((byte)'P');
		packet.addByte((byte)'d');
		Integer month = dateTime.getMonthOfYear();
		if(month < 10)
		{
			packet.addByte((byte)'0');
			packet.addByte((byte)(month.toString().charAt(0)));
		}
		else
		{
			packet.addByte((byte)(month.toString().charAt(0)));
			packet.addByte((byte)(month.toString().charAt(1)));
		}
		Integer day = dateTime.getDayOfMonth();
		if(day < 10)
		{
			packet.addByte((byte)'0');
			packet.addByte((byte)(day.toString().charAt(0)));
		}
		else
		{
			packet.addByte((byte)(day.toString().charAt(0)));
			packet.addByte((byte)(day.toString().charAt(1)));
		}
		Integer year = dateTime.getYearOfCentury();
		if(year < 10)
		{
			packet.addByte((byte)'0');
			packet.addByte((byte)(year.toString().charAt(0)));
		}
		else
		{
			packet.addByte((byte)(year.toString().charAt(0)));
			packet.addByte((byte)(year.toString().charAt(1)));
		}
		Integer hour = dateTime.getHourOfDay();
		if(hour < 10)
		{
			packet.addByte((byte)'0');
			packet.addByte((byte)(hour.toString().charAt(0)));
		}
		else
		{
			packet.addByte((byte)(hour.toString().charAt(0)));
			packet.addByte((byte)(hour.toString().charAt(1)));
		}
		Integer min = dateTime.getMinuteOfHour();
		if(min < 10)
		{
			packet.addByte((byte)'0');
			packet.addByte((byte)(min.toString().charAt(0)));
		}
		else
		{
			packet.addByte((byte)(min.toString().charAt(0)));
			packet.addByte((byte)(min.toString().charAt(1)));
		}
		Integer sec = dateTime.getSecondOfMinute();
		if(sec < 10)
		{
			packet.addByte((byte)'0');
			packet.addByte((byte)(sec.toString().charAt(0)));
		}
		else
		{
			packet.addByte((byte)(sec.toString().charAt(0)));
			packet.addByte((byte)(sec.toString().charAt(1)));
		}
		return packet;
	}
	
	public String toString()
	{
		return "DateTime: " + dateTime;
	}
}
