package com.desparddesign.orienteering.timer.PTB605.commands;

import org.joda.time.LocalDateTime;

import com.desparddesign.orienteering.timer.PTB605.PTB605CommandPacket;

public class SetDateTime extends PTB605Command 
{
	LocalDateTime dateTime;
	
	public SetDateTime(LocalDateTime dt)
	{		
		dateTime = dt;
	}
	
	public PTB605CommandPacket rawPacket()
	{
		PTB605CommandPacket packet = new PTB605CommandPacket();
		packet.addByte((byte)'B');
		packet.addByte((byte)'D');
		packet.addByte((byte)'M');
		packet.addByte((byte)'Y');
		Integer DD = dateTime.dayOfMonth().get();
		if(DD < 10)
		{
			packet.addByte((byte)'0');
			packet.addByte((byte)(DD.toString().charAt(0)));
		}
		else
		{
			packet.addByte((byte)(DD.toString().charAt(0)));
			packet.addByte((byte)(DD.toString().charAt(1)));
		}
		packet.addByte((byte)'.');
		Integer MM = dateTime.monthOfYear().get();
		if(MM < 10)
		{
			packet.addByte((byte)'0');
			packet.addByte((byte)(MM.toString().charAt(0)));
		}
		else
		{
			packet.addByte((byte)(MM.toString().charAt(0)));
			packet.addByte((byte)(MM.toString().charAt(1)));
		}
		packet.addByte((byte)'.');
		Integer YY = dateTime.yearOfCentury().get();
		if(YY < 10)
		{
			packet.addByte((byte)'0');
			packet.addByte((byte)(YY.toString().charAt(0)));
		}
		else
		{
			packet.addByte((byte)(YY.toString().charAt(0)));
			packet.addByte((byte)(YY.toString().charAt(1)));
		}
		packet.addByte((byte)' ');
		Integer hh = dateTime.hourOfDay().get();
		if(hh < 10)
		{
			packet.addByte((byte)'0');
			packet.addByte((byte)(hh.toString().charAt(0)));
		}
		else
		{
			packet.addByte((byte)(hh.toString().charAt(0)));
			packet.addByte((byte)(hh.toString().charAt(1)));
		}
		packet.addByte((byte)'h');
		Integer mm = dateTime.minuteOfHour().get();
		if(mm < 10)
		{
			packet.addByte((byte)'0');
			packet.addByte((byte)(mm.toString().charAt(0)));
		}
		else
		{
			packet.addByte((byte)(mm.toString().charAt(0)));
			packet.addByte((byte)(mm.toString().charAt(1)));
		}
		
		
		return packet;
	}
}
