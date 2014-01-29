package com.desparddesign.orienteering.codechecking.sportident.commands;

import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;


public class GetMSMode extends SICommand
{

	public GetMSMode()
	{
	}

	public SICommandPacket rawPacket()
	{
		SICommandPacket packet = new SICommandPacket();
		packet.setCommand(0xF1);
		packet.setLength(0);
		return packet;
	}
}
