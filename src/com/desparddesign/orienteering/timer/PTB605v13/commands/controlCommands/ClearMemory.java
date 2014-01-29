package com.desparddesign.orienteering.timer.PTB605v13.commands.controlCommands;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;

public class ClearMemory 
{
	public ClearMemory()
	{		
	}
	
	public PTB605CommandPacket rawPacket()
	{
		PTB605CommandPacket packet = new PTB605CommandPacket();
		packet.addByte((byte)'C');
		packet.addByte((byte)'C');
		
		return packet;
	}
}
