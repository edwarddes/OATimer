package com.desparddesign.orienteering.codechecking.sportident.commands;

import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;
import com.desparddesign.orienteering.codechecking.sportident.SITimeFormatter;

import org.joda.time.LocalDateTime;


public class GetSetTimeResponse extends SICommand
{

	public GetSetTimeResponse(SICommandPacket basePacket)
	{
		byte packetData[] = basePacket.data();
		control = packetData[0] << 8 | packetData[1];
		time = SITimeFormatter.format24HourDateTime(packetData[2] & 0xff, packetData[3] & 0xff, packetData[4] & 0xff, packetData[5] & 0xff, packetData[6] & 0xff, packetData[7] & 0xff, packetData[8] & 0xff);
	}

	public String toString()
	{
		return (new StringBuilder("Station: ")).append(control).append(" Time: ").append(time).toString();
	}

	public int control;
	public LocalDateTime time;
}
