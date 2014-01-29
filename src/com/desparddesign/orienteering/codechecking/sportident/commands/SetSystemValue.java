package com.desparddesign.orienteering.codechecking.sportident.commands;

import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;

public class SetSystemValue extends SICommand
{

	public SetSystemValue()
	{
	}

	public SICommandPacket rawPacket()
	{
		SICommandPacket packet = new SICommandPacket();
		packet.setCommand(0x82);
		packet.setLength(2);
		packet.addByte((byte)0x0F);
		packet.addByte((byte)0xB5);
		return packet;
	}
}
