package com.desparddesign.orienteering.codechecking.sportident.commands;

import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;

public class GetSystemValue extends SICommand
{

	public GetSystemValue(int a, int l)
	{
		adr = a;
		length = l;
	}

	public SICommandPacket rawPacket()
	{
		SICommandPacket packet = new SICommandPacket();
		packet.setCommand(0x83);
		packet.setLength(2);
		packet.addByte((byte)(adr & 0xff));
		packet.addByte((byte)(length & 0xff));
		return packet;
	}

	int adr;
	int length;
}
