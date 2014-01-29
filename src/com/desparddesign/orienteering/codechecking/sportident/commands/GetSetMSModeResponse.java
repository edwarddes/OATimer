package com.desparddesign.orienteering.codechecking.sportident.commands;

import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;

public class GetSetMSModeResponse extends SICommand
{

	public GetSetMSModeResponse(SICommandPacket basePacket)
	{
		byte packetData[] = basePacket.data();
		control = packetData[0] << 8 | packetData[1];
		mode = packetData[2];
	}

	public String toString()
	{
		if(mode == 0x4D)
			return (new StringBuilder("Station: ")).append(control).append(" Mode: direct").toString();
		else
			return (new StringBuilder("Station: ")).append(control).append(" Mode: remote").toString();
	}

	public int control;
	public int mode;
}
