package com.desparddesign.orienteering.codechecking.sportident.commands;

import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;

public class TurnOffStationResponse extends SICommand
{

	public TurnOffStationResponse(SICommandPacket basePacket)
	{
		byte packetData[] = basePacket.data();
		control = packetData[0] << 8 | packetData[1];
	}

	public String toString()
	{
		return (new StringBuilder("Station: ")).append(control).append(" Turned off").toString();
	}

	public int control;
}
