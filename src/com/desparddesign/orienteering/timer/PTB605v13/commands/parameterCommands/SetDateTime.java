package com.desparddesign.orienteering.timer.PTB605v13.commands.parameterCommands;

import org.joda.time.LocalDateTime;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;
import com.desparddesign.orienteering.timer.PTB605v13.commands.PTB605Command;

public class SetDateTime extends PTB605Command 
{
	LocalDateTime dateTime;
	
	
	public SetDateTime(LocalDateTime dt)
	{		
		//dateTime = dt;
		//make sure we send 0 as seconds.  The PTB always waits for a sync pulse after setting the dateTime, and 
		//sets the time to 0 seconds at the sync pulse.
		dateTime = new LocalDateTime(dt.getYear(),dt.getMonthOfYear(),dt.getDayOfMonth(),dt.getHourOfDay(),dt.getMinuteOfHour());
	}
	
	public SetDateTime(PTB605CommandPacket command)
	{
		byte[] data = command.data();
		String s = new String(data);
		//US format
		if(data[0] == 'P' && data[1] == 'd')
		{
			//PdMMDDYYhhmm
			Integer month = Integer.parseInt(s.substring(2,4));
			Integer day = Integer.parseInt(s.substring(4,6));
			Integer year = Integer.parseInt(s.substring(6,8));
			Integer hour = Integer.parseInt(s.substring(8,10));
			Integer min = Integer.parseInt(s.substring(10,12));
			
			year+=2000;
			
			dateTime = new LocalDateTime(year,month,day,hour,min);
		}
		//Euro format
		else
		{
			
		}
	}
	
	public PTB605CommandPacket rawPacket()
	{
		PTB605CommandPacket packet = new PTB605CommandPacket();
		packet.addByte((byte)'P');
		packet.addByte((byte)'d');
		
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
	
	public LocalDateTime getDateTime()
	{
		return dateTime;
	}
}
