package com.desparddesign.orienteering.codechecking.sportident.commands;

import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;


public class SetBaudRate extends SICommand
{
	enum SPEED
	{
		SLOW,
		FAST
	}


	public SetBaudRate(SPEED m)
	{
		if(m == SPEED.SLOW)
			rate = 0;
		else
			rate = 1;
	}

	public SICommandPacket rawPacket()
	{
		SICommandPacket packet = new SICommandPacket();
		packet.setCommand(0xFE);
		packet.setLength(1);
		packet.addByte((byte)(rate & 0xff));
		return packet;
	}

	private int rate;
}
