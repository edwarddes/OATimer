package com.desparddesign.orienteering.codechecking.sportident.commands;

import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;

import org.joda.time.LocalDateTime;

public class SetTime extends SICommand
{

	public SetTime(LocalDateTime setTime)
	{
		time = setTime;
	}

	public SICommandPacket rawPacket()
	{
		SICommandPacket packet = new SICommandPacket();
		packet.setCommand(0xF6);
		packet.setLength(7);
		packet.addByte((byte)(time.getYear() - 2000 & 0xff));
		packet.addByte((byte)(time.getMonthOfYear() & 0xff));
		packet.addByte((byte)(time.getDayOfMonth() & 0xff));
		int hours = time.getHourOfDay();
		int TD = 0;
		if(hours > 12)
		{
			hours -= 12;
			TD = 1;
		}
		switch(time.getDayOfWeek())
		{
		case 7: // '\007'
			TD |= 0;
			break;

		case 1: // '\001'
			TD |= 2;
			break;

		case 2: // '\002'
			TD |= 4;
			break;

		case 3: // '\003'
			TD |= 6;
			break;

		case 4: // '\004'
			TD |= 8;
			break;

		case 5: // '\005'
			TD |= 0xa;
			break;

		case 6: // '\006'
			TD |= 0xc;
			break;
		}
		int T = hours * 60 * 60 + time.getMinuteOfHour() * 60 + time.getSecondOfMinute();
		packet.addByte((byte)(TD & 0xff));
		packet.addByte((byte)(T >> 8 & 0xff));
		packet.addByte((byte)(T & 0xff));
		packet.addByte((byte)((int)((double)time.getMillisOfSecond() * 0.25600000000000001D) & 0xff));
		return packet;
	}

	private LocalDateTime time;
}
