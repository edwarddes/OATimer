package com.desparddesign.orienteering.codechecking.sportident.commands;

import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;

public class EraseBackupData extends SICommand
{

	public EraseBackupData()
	{
	}

	public SICommandPacket rawPacket()
	{
		SICommandPacket packet = new SICommandPacket();
		packet.setCommand(0xF5);
		packet.setLength(0);
		return packet;
	}
}
