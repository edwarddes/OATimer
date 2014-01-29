package com.desparddesign.orienteering.codechecking.sportident.commands;

import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;


public class SetBaudRateResponse extends SICommand
{

	public SetBaudRateResponse(SICommandPacket basePacket)
	{
		byte packetData[] = basePacket.data();
		control = packetData[0] << 8 | packetData[1];
		rate = packetData[2];
	}

	public String toString()
	{
		if(rate == 0)
			return (new StringBuilder("Station: ")).append(control).append(" Speed: 4800").toString();
		else
			return (new StringBuilder("Station: ")).append(control).append(" Speed: 38400").toString();
	}

	public int control;
	public int rate;
}
