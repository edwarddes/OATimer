package com.desparddesign.orienteering.codechecking.sportident.commands;

import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;

public class SI5ReadBlockRequest extends SICommand
{
	public SI5ReadBlockRequest()
	{
		
	}
	
	public SICommandPacket rawPacket()
	{
		SICommandPacket packet = new SICommandPacket();
		packet.setCommand(0xB1);
		packet.setLength(0);
		return packet;
	}
}
