package com.desparddesign.orienteering.timer.PTB605v13.commands.queryCommands;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;

public class GetMemoryStatus 
{
	public GetMemoryStatus()
	{		
	}
	
	public PTB605CommandPacket rawPacket()
	{
		PTB605CommandPacket packet = new PTB605CommandPacket();
		packet.addByte((byte)'Q');
		packet.addByte((byte)'M');
		
		return packet;
	}
}
