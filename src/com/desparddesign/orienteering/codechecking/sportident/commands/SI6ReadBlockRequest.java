package com.desparddesign.orienteering.codechecking.sportident.commands;

import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;

public class SI6ReadBlockRequest extends SICommand
{
	private int block;
	public SI6ReadBlockRequest(int BN)
	{
		block = BN;
	}
	
	public SICommandPacket rawPacket()
	{
		SICommandPacket packet = new SICommandPacket();
		packet.setCommand(0xE1);
		packet.setLength(1);
		packet.addByte((byte) (block & 0xff));
		return packet;
	}
}
