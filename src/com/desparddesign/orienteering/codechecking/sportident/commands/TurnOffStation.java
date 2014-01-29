package com.desparddesign.orienteering.codechecking.sportident.commands;

import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;

public class TurnOffStation extends SICommand
{

	public TurnOffStation()
	{
	}

	public SICommandPacket rawPacket()
	{
		SICommandPacket packet = new SICommandPacket();
		packet.setCommand(0xF8);
		packet.setLength(0);
		return packet;
	}
}
