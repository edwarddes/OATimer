package com.desparddesign.orienteering.codechecking.sportident.commands;

import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;

public class SetSystemValue extends SICommand
{

	public SetSystemValue(int a,byte[] d)
	{
		adr = (byte)a;
		data = d;
	}

	public SICommandPacket rawPacket()
	{
		SICommandPacket packet = new SICommandPacket();
		packet.setCommand(0x82);
		packet.setLength(data.length+1);
		packet.addByte(adr);
		for(int i=0;i<data.length;i++)
			packet.addByte(data[i]);
		return packet;
	}
	byte adr;
	byte[] data;
}
