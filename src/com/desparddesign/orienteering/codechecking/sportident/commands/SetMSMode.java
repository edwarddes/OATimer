package com.desparddesign.orienteering.codechecking.sportident.commands;

import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;

public class SetMSMode extends SICommand
{
	public enum MODE
	{
		DIRECT,
		REMOTE
	}

	public SetMSMode(MODE m)
	{
		if(m == MODE.DIRECT)
			mode = 0x4D;
		else
			mode = 0x53;
	}
	
	public SetMSMode(SICommandPacket basePacket)
	{
		byte packetData[] = basePacket.data();
		mode = packetData[0];
	}

	public SICommandPacket rawPacket()
	{
		SICommandPacket packet = new SICommandPacket();
		packet.setCommand(0xF0);
		packet.setLength(1);
		packet.addByte((byte)(mode & 0xff));
		return packet;
	}
	
	public String toString()
	{
		return "SetMSMode: " + ((mode == 0x4D) ? "DIRECT" : "REMOTE");
	}

	private int mode;
}
