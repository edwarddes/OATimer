package com.desparddesign.orienteering.codechecking.sportident.commands;

import org.joda.time.LocalDateTime;

import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;

public class GetTimeResponse extends SICommand 
{
	public GetTimeResponse(SICommandPacket basePacket)
	{
		byte packetData[] = basePacket.data();
		
		control = packetData[0] << 8 | packetData[1];
		
		int year = packetData[2]+2000;
		int month = packetData[3];
		int day = packetData[4];
		//int dayOfWeek = (packetData[5] & 0x0E) >> 1;//only bits 1-3
		int ampm = packetData[5] & 0x1;
		int T = (packetData[6] << 8 | (packetData[7] & 0xff))&0x0000ffff;
		int TSS = 0x00 <<8 | (packetData[8] & 0xff);
		
		int hours = T/(60*60);
		if(ampm == 1)
			hours+=12;
		int min = (T-(hours*60*60))/60;
		int seconds = (int)(T-(hours*60*60)-(min*60));
		int millis = (int)(TSS*3.90625);
		time = new LocalDateTime(year, month, day, hours, min, seconds, millis);
	}

	public String toString()
	{
		return (new StringBuilder("Station: ")).append(control).append(" time: ").append(time.toString()).toString();
	}
	
	int control;
	LocalDateTime time;
}
