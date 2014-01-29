package com.desparddesign.orienteering.timer.PTB605v13.commands.parameterCommands;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;

public class BlockHighInputs 
{
	boolean blocked;
	
	public BlockHighInputs(boolean b)
	{		
		blocked = b;
	}
	
	public PTB605CommandPacket rawPacket()
	{
		PTB605CommandPacket packet = new PTB605CommandPacket();
		packet.addByte((byte)'P');
		
		if(blocked)
			packet.addByte((byte)'e');
		else
			packet.addByte((byte)'E');
		
		return packet;
	}
}
