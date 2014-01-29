package com.desparddesign.orienteering.codechecking.sportident.commands;

import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;

public class GetSystemValueResponse extends SICommand
{

	public GetSystemValueResponse(SICommandPacket basePacket)
	{
		byte packetData[] = basePacket.data();
		control = packetData[0] << 8 | packetData[1];
	}

	public String toString()
	{
		return (new StringBuilder("Station: ")).append(control).toString();
	}

	public int control;
}
