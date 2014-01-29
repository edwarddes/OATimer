package com.desparddesign.orienteering.timer.PTB605.commands;

import com.desparddesign.orienteering.timer.PTB605.PTB605CommandPacket;

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
		packet.addByte((byte)'E');
		
		if(blocked)
			packet.addByte((byte)'Y');
		else
			packet.addByte((byte)'N');
		
		return packet;
	}
}
