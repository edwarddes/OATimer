package com.desparddesign.orienteering.codechecking.sportident.commands;

import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;


public class GetTime extends SICommand
{

	public GetTime()
	{
	}

	public SICommandPacket rawPacket()
	{
		SICommandPacket packet = new SICommandPacket();
		packet.setCommand(0xF7);
		packet.setLength(0);
		return packet;
	}
}
