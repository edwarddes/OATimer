package com.desparddesign.orienteering.timer.PTB605v13.commands.controlCommands;


import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;
import com.desparddesign.orienteering.timer.PTB605v13.commands.PTB605Command;

public class ClearMemory extends PTB605Command
{
	public ClearMemory()
	{		
	}
	
	public ClearMemory(PTB605CommandPacket packet)
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
